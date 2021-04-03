from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer
from django.dispatch import receiver

from queue_module import signals as queue_signals


def send_message(message, message_type, queue_id):
    channel_layer = get_channel_layer()
    if channel_layer is not None:
        async_to_sync(channel_layer.group_send)(
            queue_id,
            {
                'type': message_type,
                'text': message
            }
        )


@receiver(queue_signals.push_member_signal)
def push_member_receiver(**kwargs):
    send_message({
        'push_member': kwargs.get('user_id')
    }, 'member_operation', kwargs.get('queue_id'))


@receiver(queue_signals.pop_member_signal)
def pop_member_reciever(**kwargs):
    send_message({
        'pop_member': kwargs.get('user_id')
    }, 'member_operation', kwargs.get('queue_id'))


@receiver(queue_signals.move_member_to_the_end_signal)
def move_member_to_the_end_reciever(**kwargs):
    send_message({
        'move_member_to_the_end': kwargs.get('user_id')
    }, 'member_operation', kwargs.get('queue_id'))
