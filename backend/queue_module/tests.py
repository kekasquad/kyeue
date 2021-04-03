""" Queue tests
"""
from django.test import TestCase

from core.models import User
from core.factories import UserFactory
from .models import Queue


class QueueTestCases(TestCase):
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
