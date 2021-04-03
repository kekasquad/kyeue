from asgiref.sync import async_to_sync
from channels.generic.websocket import JsonWebsocketConsumer


class QueueConsumer(JsonWebsocketConsumer):
    """ Consumer for a WS connection to particular queue
    """
    queue_id = None

    def connect(self):
        self.queue_id = self.scope['url_route']['kwargs']['queue_id']

        async_to_sync(self.channel_layer.group_add)(
            self.queue_id,
            self.channel_name
        )
        self.accept()

    def member_operation(self, message):
        self.send_json(message)


class CommonNotificationsConsumer(JsonWebsocketConsumer):
    """ Consumer for common WS notifications
    """
    def connect(self):
        async_to_sync(self.channel_layer.group_add)(
            'common_notifications',
            self.channel_name
        )
        self.accept()

    def queue_operation(self, message):
        self.send_json(message)
