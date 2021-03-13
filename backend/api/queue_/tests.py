import uuid

from django.test import TestCase
from django.urls import reverse
from factory import fuzzy
from rest_framework import status

from factories import QueueFactory
from queue_module.models import Queue
from ..tests import AuthMixin


class QueueAPITestCases(AuthMixin, TestCase):

    def test_create_queue(self):
        name = 'abc'
        response = self.client.post(
            reverse('api_queue_list_create_api_view'),
            data={'name': name},
            HTTP_AUTHORIZATION=self.access_header
        )

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Queue.objects.count(), 1)
        Queue.objects.get(name=name)

    def test_get_queue_list(self):
        count = 3
        for i in range(count):
            self.client.post(
                reverse('api_queue_list_create_api_view'),
                data={'name': str(i)},
                HTTP_AUTHORIZATION=self.access_header
            )

        response = self.client.get(
            reverse('api_queue_list_create_api_view'),
            HTTP_AUTHORIZATION=self.access_header
        )

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        queue_list = response.json()
        self.assertEqual(len(queue_list), count)
        for ind, queue in enumerate(queue_list):
            self.assertEqual(queue['name'], str(ind))

    def test_get_queue_details(self):
        queue = QueueFactory()

        response = self.client.get(reverse(
            'api_queue_retrieve_update_destroy_api_view',
            kwargs={'pk': str(queue.id)}
        ), HTTP_AUTHORIZATION=self.access_header)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        response = response.json()
        print(response)
        for key in ('name', 'members'):
            self.assertEqual(response[key], getattr(queue, key))

        self.assertEqual(response['id'], str(queue.id))
        self.assertEqual(response['isPrivate'], queue.is_private)

    def test_member_operations(self):
        queue = QueueFactory()
        count = 3

        members = [str(uuid.uuid1()) for _ in range(count)]
        for member in members:
            res = self.client.put(
                reverse(
                    'api_queue_add_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ),
                data={'userId': member}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)

        queue.refresh_from_db()
        self.assertEqual(queue.members, members[::-1])
        for member in members:
            res = self.client.put(
                reverse(
                    'api_queue_move_member_to_end_api_view',
                    kwargs={'pk': str(queue.id)}
                ), data={'userId': member}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            queue.refresh_from_db()
            self.assertEqual(queue.members[0], member)

        queue.refresh_from_db()
        self.assertEqual(queue.members, members[::-1])

        for i in range(count):
            member = members[i]
            res = self.client.put(
                reverse(
                    'api_queue_remove_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ), data={'userId': member}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            queue.refresh_from_db()
            self.assertEqual(len(queue.members), count - i - 1)

    def test_delete_queue(self):
        queue = QueueFactory()

        res = self.client.delete(
            reverse(
                'api_queue_retrieve_update_destroy_api_view',
                kwargs={'pk': str(queue.id)}
            ),
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(res.status_code, status.HTTP_204_NO_CONTENT)
        self.assertEqual(Queue.objects.count(), 0)

    def test_update_queue(self):
        queue = QueueFactory()
        new_name = fuzzy.FuzzyText().fuzz()

        res = self.client.patch(
            reverse(
                'api_queue_retrieve_update_destroy_api_view',
                kwargs={'pk': str(queue.id)}
            ), data={'name': new_name}, content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(res.status_code, status.HTTP_200_OK)

        queue.refresh_from_db()
        self.assertEqual(queue.name, new_name)
        self.assertEqual(res.json()['name'], new_name)
