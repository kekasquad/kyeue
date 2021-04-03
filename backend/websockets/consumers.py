from asgiref.sync import async_to_sync
from channels.generic.websocket import JsonWebsocketConsumer


class QueueConsumer(JsonWebsocketConsumer):

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

    def disconnect(self, close_code):
        pass
