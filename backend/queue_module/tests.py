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
