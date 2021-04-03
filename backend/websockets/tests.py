from asgiref.sync import sync_to_async
from channels.routing import URLRouter
from django.test import TestCase
from channels.testing import WebsocketCommunicator
from django.urls import re_path, reverse
from factory.fuzzy import FuzzyText

from api.tests import AuthMixin
from queue_module.factories import QueueFactory
from .consumers import QueueConsumer


class WebsocketTestCases(AuthMixin, TestCase):
    async def test_queue_operation_notifications(self):
        queue = await sync_to_async(QueueFactory)(creator=self.user)

        application = URLRouter([
            re_path(r'^ws/queue/(?P<queue_id>[^/]+)/?$', QueueConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/queue/{str(queue.id)}')
        connected, _ = await communicator.connect()

        for url_name, operation in (
            ('api_queue_add_member_api_view', 'push_member'),
            ('api_queue_move_member_to_end_api_view', 'move_member_to_the_end'),
            ('api_queue_remove_member_api_view', 'pop_member')
        ):
            await sync_to_async(self.client.put)(
                reverse(url_name, kwargs={'pk': str(queue.id)}),
                data={'userId': str(self.user.id)},
                content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )

            message = await communicator.receive_json_from()
            self.assertEqual(message, {
                'type': 'member_operation',
                'text': {
                    operation: str(self.user.id)
                }
            })

        await communicator.disconnect()

    async def test_queue_creation_notification(self):
        application = URLRouter([
            re_path(r'^ws/notifications/?$', QueueConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/notifications')
        connected, _ = await communicator.connect()

        response = await sync_to_async(self.client.post)(
            reverse('api_queue_list_create_api_view'),
            data={'name': FuzzyText().fuzz()},
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )

        message = await communicator.receive_json_from()
        self.assertEqual(message, {
            'type': 'member_operation',
            'text': {
                'queue_created': response.json()['id']
            }
        })
