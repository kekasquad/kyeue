""" Queue model
"""
import uuid

from django.db import models
from core.models import User

from .mixins import TimeStampedModel
from . import signals


class Queue(TimeStampedModel):
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
    creator = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )
    members = models.JSONField(
        verbose_name='Queue members list',
        blank=False,
        default=list
    )

    def _send_signal(self, signal, user_id: str) -> None:
        signal.send(sender=self.__class__, instance=self, user_id=user_id)

    def push_member(self, user_id: str) -> None:
        if user_id not in self.members:
            User.objects.get(pk=user_id)
            self.members.insert(0, user_id)
            self._send_signal(signals.push_member_signal, user_id)

    def pop_member(self, user_id: str) -> None:
        if user_id in self.members:
            User.objects.get(pk=user_id)
            self.members.remove(user_id)
            self._send_signal(signals.pop_member_signal, user_id)

    def move_member_to_the_end(self, user_id: str) -> None:
        if user_id in self.members:
            User.objects.get(pk=user_id)
            self.members.remove(user_id)
            self.members.insert(0, user_id)
            self._send_signal(signals.move_member_to_the_end_signal, user_id)

    def skip_turn(self, user_id: str) -> None:
        if user_id in self.members and self.members[0] != user_id:
            User.objects.get(pk=user_id)
            user_index = self.members.index(user_id)
            self.members[user_index], self.members[user_index-1] = \
                self.members[user_index-1], self.members[user_index]
            self._send_signal(signals.skip_turn_signal, user_id)
