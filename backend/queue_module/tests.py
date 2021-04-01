""" Queue tests
"""
from django.test import TestCase

from core.models import User
from .models import Queue


class QueueTestCases(TestCase):
    """ Queue tests
    """

    def test_queue_members_operations(self):
        user = User()
        user.save()
        queue = Queue(name='abc', creator=user)
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, [])

        queue.push_member('1')
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, ['1'])

        queue.pop_member('2')
        queue.save()
        queue.refresh_from_db()
        self.assertEqual(queue.members, ['1'])

        queue.pop_member('1')
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
