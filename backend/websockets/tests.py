from asgiref.sync import sync_to_async
from channels.routing import URLRouter
from django.test import TestCase
from channels.testing import WebsocketCommunicator
from django.urls import re_path, reverse

from api.tests import AuthMixin
from queue_module.factories import QueueFactory
from .consumers import QueueConsumer


class WebsocketTestCases(AuthMixin, TestCase):
    async def test_my_consumer(self):
        queue = await sync_to_async(QueueFactory)(creator=self.user)

        application = URLRouter([
            re_path(r'^ws/queue/(?P<queue_id>[^/]+)/?$', QueueConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/queue/{str(queue.id)}')
        connected, _ = await communicator.connect()

        await sync_to_async(self.client.put)(
            reverse('api_queue_add_member_api_view', kwargs={'pk': str(queue.id)}),
            data={'userId': str(self.user.id)},
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )

        message = await communicator.receive_json_from()
        self.assertEqual(message, {
            'type': 'member_operation',
            'text': {
                'push_member': str(self.user.id)
            }
        })

        await communicator.disconnect()
