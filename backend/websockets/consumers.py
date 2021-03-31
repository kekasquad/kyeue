import json
from channels.generic.websocket import WebsocketConsumer
from django.dispatch import receiver

from queue_module import signals as queue_signals
import logging


class QueueConsumer(WebsocketConsumer):

    queue_id = None

    @receiver(queue_signals.push_member_signal)
    def push_member_reciever(self, _, **kwargs):
        if kwargs.get('queue_id') != self.queue_id:
            return
        self.send(text_data=json.dumps({
            'push_member': {
                'user_id': kwargs.get('user_id')
            }
        }))

    @receiver(queue_signals.pop_member_signal)
    def pop_member_reciever(self, _, **kwargs):
        if kwargs.get('queue_id') != self.queue_id:
            return
        self.send(text_data=json.dumps({
            'pop_member': {
                'user_id': kwargs.get('user_id')
            }
        }))

    @receiver(queue_signals.move_member_to_the_end_signal)
    def move_member_to_the_end_reciever(self, _, **kwargs):
        if kwargs.get('queue_id') != self.queue_id:
            return
        self.send(text_data=json.dumps({
            'move_member_to_the_end': {
                'user_id': kwargs.get('user_id')
            }
        }))

    def connect(self):
        self.queue_id = self.scope['url_route']['kwargs']['queue_id']
        self.accept()

    def disconnect(self, close_code):
        pass
