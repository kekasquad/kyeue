""" Queue model
"""
import uuid

from django.db import models
from core.models import User


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
    creator = models.ForeignKey(
        User,
        on_delete=models.CASCADE
    )
    members = models.JSONField(
        verbose_name='Queue members list',
        blank=False,
        default=list
    )

    def push_member(self, user_id: str) -> None:
        if user_id not in self.members:
            User.objects.get(pk=user_id)
            self.members.insert(0, user_id)

    def pop_member(self, user_id: str) -> None:
        if user_id in self.members:
            User.objects.get(pk=user_id)
            self.members.remove(user_id)

    def move_member_to_the_end(self, user_id) -> None:
        self.pop_member(user_id)
        self.push_member(user_id)
