from django.test import TestCase
from django.urls import reverse
from rest_framework import status

from queue_module.models import Queue


class QueueAPITestCases(TestCase):

    def test_create_queue(self):
        name = 'abc'
        response = self.client.post(
            reverse('api_queue_list_create'),
            data={'name': name}
        )

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Queue.objects.count(), 1)
        Queue.objects.get(name=name)

    def test_get_queue_list(self):
        count = 3
        for i in range(count):
            self.client.post(
                reverse('api_queue_list_create'),
                data={'name': str(i)}
            )

        response = self.client.get(reverse('api_queue_list_create'))

        self.assertEqual(response.status_code, status.HTTP_200_OK)
        queue_list = response.json()
        self.assertEqual(len(queue_list), count)
        for ind, queue in enumerate(queue_list):
            self.assertEqual(queue['name'], str(ind))