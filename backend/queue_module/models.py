""" Queue model
"""
import uuid

from django.db import models

from . import signals


class Queue(models.Model):
    """ Queue model
    """

    id = models.UUIDField(
        primary_key=True,
        editable=False,
        default=uuid.uuid1
    )
    name = models.CharField(
        verbose_name='Queue name',
        blank=False,
        max_length=255,
        db_index=True
    )
    is_private = models.BooleanField(
        verbose_name='Privacy flag',
        default=False
    )
    members = models.JSONField(
        verbose_name='Queue members list',
        blank=False,
        default=list
    )

    def push_member(self, user_id: str) -> None:
        if user_id not in self.members:
            self.members.insert(0, user_id)
            signals.push_member_signal.send(
                sender=self.__class__, queue_id=str(self.id), user_id=user_id
            )

    def pop_member(self, user_id: str) -> None:
        if user_id in self.members:
            self.members.remove(user_id)
            signals.pop_member_signal.send(
                sender=self.__class__, queue_id=str(self.id), user_id=user_id
            )

    def move_member_to_the_end(self, user_id) -> None:
        if user_id in self.members:
            self.members.remove(user_id)
            self.members.insert(0, user_id)
            signals.move_member_to_the_end_signal.send(
                sender=self.__class__, queue_id=str(self.id), user_id=user_id
            )
