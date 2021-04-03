""" Queue tests
"""
from django.dispatch import receiver
from django.test import TestCase
from django.urls import reverse

from core.models import User
from core.factories import UserFactory
from api.tests import AuthMixin
from .factories import QueueFactory
from .models import Queue
from . import signals


class QueueTestCases(AuthMixin, TestCase):
    """ Queue tests
    """

    def test_queue_members_operations(self):
        creator = UserFactory()
        users = UserFactory.create_batch(2)
        queue = Queue(name='abc', creator=creator)
        queue.save()
        self.assertEqual(queue.members, [])

        queue.push_member(str(users[0].id))
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, [str(users[0].id)])

        queue.pop_member(str(users[1].id))
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, [str(users[0].id)])

        queue.pop_member(str(users[0].id))
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, [])

    def test_cascade_deletion(self):
        user = User()
        user.save()
        queue = Queue(name='abc', creator=user)
        queue.save()
        self.assertEqual(Queue.objects.count(), 1)
        self.assertEqual(User.objects.count(), 1)
        queue.delete()
        self.assertEqual(Queue.objects.count(), 0)
        self.assertEqual(User.objects.count(), 1)

        queue = Queue(name='abcd', creator=user)
        queue.save()
        self.assertEqual(Queue.objects.count(), 1)
        self.assertEqual(User.objects.count(), 1)
        user.delete()
        self.assertEqual(Queue.objects.count(), 0)
        self.assertEqual(User.objects.count(), 0)

    def test_signals(self):
        queue = QueueFactory(creator=self.user)
        self.signal_received = False

        @receiver(signals.push_member_signal)
        def signal_handler(sender, **_):
            self.assertEqual(sender, Queue)
            self.signal_received = True

        self.client.put(
            reverse('api_queue_add_member_api_view', kwargs={'pk': str(queue.id)}),
            data={'userId': str(self.user.id)},
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertTrue(self.signal_received)
