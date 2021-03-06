from asgiref.sync import sync_to_async
from channels.routing import URLRouter
from django.test import TestCase
from channels.testing import WebsocketCommunicator
from django.urls import re_path, reverse
from factory.fuzzy import FuzzyText

from api.tests import AuthMixin
from core.factories import UserFactory
from queue_module.factories import QueueFactory
from .consumers import QueueConsumer, CommonNotificationsConsumer


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

    async def test_skip_turn_notification(self):
        users = await sync_to_async(UserFactory.create_batch)(2)
        queue = await sync_to_async(QueueFactory)(
            creator=self.user, members=[str(u.id) for u in users]
        )

        application = URLRouter([
            re_path(r'^ws/queue/(?P<queue_id>[^/]+)/?$', QueueConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/queue/{str(queue.id)}')
        connected, _ = await communicator.connect()

        await sync_to_async(self.client.put)(
            reverse('api_queue_skip_turn_api_view', kwargs={'pk': str(queue.id)}),
            data={'userId': str(users[1].id)},
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )

        message = await communicator.receive_json_from()
        self.assertEqual(message, {
            'type': 'member_operation',
            'text': {
                'skip_turn': str(users[1].id)
            }
        })

    async def test_queue_create_delete_notifications(self):
        application = URLRouter([
            re_path(r'^ws/notifications/?$', CommonNotificationsConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/notifications')
        connected, _ = await communicator.connect()

        response = await sync_to_async(self.client.post)(
            reverse('api_queue_list_create_api_view'),
            data={'name': FuzzyText().fuzz()},
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )
        response = response.json()

        message = await communicator.receive_json_from()
        self.assertEqual(message, {
            'type': 'queue_operation',
            'text': {
                'create_queue': response['id']
            }
        })

        await sync_to_async(self.client.delete)(
            reverse(
                'api_queue_retrieve_update_destroy_api_view',
                kwargs={'pk': response['id']}
            ),
            HTTP_AUTHORIZATION=self.access_header
        )

        message = await communicator.receive_json_from()
        self.assertEqual(message, {
            'type': 'queue_operation',
            'text': {
                'delete_queue': response['id']
            }
        })
