from asgiref.sync import sync_to_async
from channels.routing import URLRouter
from django.test import TestCase
from channels.testing import WebsocketCommunicator, HttpCommunicator
from django.urls import re_path

from api.tests import AuthMixin
from queue_module.factories import QueueFactory
from .consumers import QueueConsumer


class MyTests(AuthMixin, TestCase):
    async def test_my_consumer(self):
        queue = await sync_to_async(QueueFactory)(creator=self.user)

        application = URLRouter([
            re_path(r'^ws/queue/(?P<queue_id>[^/]+)/?$', QueueConsumer.as_asgi()),
        ])

        communicator = WebsocketCommunicator(application, f'/ws/queue/{str(queue.id)}')
        connected, _ = await communicator.connect()

        await communicator.disconnect()
