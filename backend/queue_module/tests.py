""" Queue tests
"""
from django.test import TestCase

from .models import Queue


class QueueTestCases(TestCase):
    """ Queue tests
    """

    def test_queue_members_operations(self):
        queue = Queue(name='abc')
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
