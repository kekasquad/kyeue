from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer
from django.db.models.signals import post_save
from django.dispatch import receiver

from queue_module import signals as queue_signals
from queue_module.models import Queue


def send_message(message, message_type, group_id):
    channel_layer = get_channel_layer()
    if channel_layer is not None:
        async_to_sync(channel_layer.group_send)(
            group_id,
            {
                'type': message_type,
                'text': message
            }
        )


@receiver(queue_signals.push_member_signal)
def push_member_receiver(instance, **kwargs):
    send_message({
        'push_member': kwargs.get('user_id')
    }, 'member_operation', str(instance.id))


@receiver(queue_signals.pop_member_signal)
def pop_member_reciever(instance, **kwargs):
    send_message({
        'pop_member': kwargs.get('user_id')
    }, 'member_operation', str(instance.id))


@receiver(queue_signals.move_member_to_the_end_signal)
def move_member_to_the_end_reciever(instance, **kwargs):
    send_message({
        'move_member_to_the_end': kwargs.get('user_id')
    }, 'member_operation', str(instance.id))


@receiver(post_save, sender=Queue)
def create_queue_receiver(created, instance, **_):
    if created:
        send_message({
            'create_queue': str(instance.id)
        }, 'queue_operation', 'common_notifications')
