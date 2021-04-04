import uuid

from django.test import TestCase
from django.urls import reverse
from factory import fuzzy
from rest_framework import status

from backend.queue_module.factories import QueueFactory
from core.factories import UserFactory
from queue_module.models import Queue
from ..tests import AuthMixin


class QueueAPITestCases(AuthMixin, TestCase):

    @staticmethod
    def _get_auth_header(token):
        return f'Token {token}'

    def test_create_queue(self):
        name = 'abc'
        response = self.client.post(
            reverse('api_queue_list_create_api_view'),
            data={'name': name},
            HTTP_AUTHORIZATION=self.access_header
        )

        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(response.json()['creator']['id'], str(self.user.id))
        self.assertEqual(self.user, Queue.objects.first().creator)
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
        queue = QueueFactory(creator=self.user)

        response = self.client.get(reverse(
            'api_queue_retrieve_update_destroy_api_view',
            kwargs={'pk': str(queue.id)}
        ), HTTP_AUTHORIZATION=self.access_header)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

        response = response.json()
        for key in ('name', 'members'):
            self.assertEqual(response[key], getattr(queue, key))

        self.assertEqual(response['id'], str(queue.id))
        self.assertEqual(response['isPrivate'], queue.is_private)
        self.assertEqual(response['creator']['id'], str(self.user.id))

    def test_member_operations(self):
        queue = QueueFactory(creator=self.user)
        count = 3
        users = UserFactory.create_batch(count)

        for user in users:
            res = self.client.put(
                reverse(
                    'api_queue_add_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ),
                data={'userId': str(user.id)}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)

        queue.refresh_from_db()
        self.assertEqual(queue.members, list(map(lambda x: str(x.id), users[::-1])))
        for user in users:
            res = self.client.put(
                reverse(
                    'api_queue_move_member_to_end_api_view',
                    kwargs={'pk': str(queue.id)}
                ), data={'userId': str(user.id)}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            queue.refresh_from_db()
            self.assertEqual(queue.members[0], str(user.id))

        queue.refresh_from_db()
        self.assertEqual(queue.members, list(map(lambda x: str(x.id), users[::-1])))

        queue.refresh_from_db()
        self.assertEqual(queue.members, list(map(lambda x: str(x.id), users[::-1])))

        for i in range(count):
            user = users[i]
            res = self.client.put(
                reverse(
                    'api_queue_remove_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ), data={'userId': str(user.id)}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            queue.refresh_from_db()
            self.assertEqual(len(queue.members), count - i - 1)

    def test_delete_queue(self):
        queue = QueueFactory(creator=self.user)

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
        queue = QueueFactory(creator=self.user)
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

    def test_check_permission(self):
        queue = QueueFactory(creator=self.user)
        count = 3

        members = [UserFactory() for _ in range(count)]
        for member in members:
            res = self.client.put(
                reverse(
                    'api_queue_add_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ),
                data={'userId': member.id}, content_type='application/json',
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)

        queue.refresh_from_db()
        self.assertEqual(len(queue.members), count)

        for i in range(count):
            member = members[i]
            member_request = members[(i + 1) % len(members)]
            password = fuzzy.FuzzyText().fuzz()
            member_request.set_password(password)
            member_request.save()

            key = self.client.post(reverse('api_auth_login_api_view'), data={
                'username': member_request.username,
                'password': password
            }).json()['key']

            res = self.client.put(
                reverse(
                    'api_queue_remove_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ), data={'userId': member.id}, content_type='application/json',
                HTTP_AUTHORIZATION=self._get_auth_header(key)
            )
            self.assertEqual(res.status_code, status.HTTP_403_FORBIDDEN)
            queue.refresh_from_db()
            self.assertEqual(len(queue.members), count)

    def test_permission_for_user_himself(self):
        queue = QueueFactory(creator=self.user)
        count = 3

        members = [UserFactory() for _ in range(count)]

        for member in members:
            password = fuzzy.FuzzyText().fuzz()
            member.set_password(password)
            member.save()

            token = self.client.post(reverse('api_auth_login_api_view'), data={
                'username': member.username,
                'password': password
            }).json()['key']

            res = self.client.put(
                reverse(
                    'api_queue_add_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ),
                data={'userId': member.id}, content_type='application/json',
                HTTP_AUTHORIZATION=self._get_auth_header(token)
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)

        queue.refresh_from_db()
        self.assertEqual(len(queue.members), count)

    def test_is_teacher_permissions(self):
        user = UserFactory(is_teacher=True)
        password = fuzzy.FuzzyText().fuzz()
        user.set_password(password)
        user.save()
        key = self.client.post(reverse('api_auth_login_api_view'), data={
            'username': user.username,
            'password': password
        }).json()['key']

        queue = QueueFactory(creator=self.user)

        for token in (self._get_auth_header(key), self.access_header):
            res = self.client.put(
                reverse(
                    'api_queue_add_member_api_view',
                    kwargs={'pk': str(queue.id)}
                ),
                data={'userId': str(user.id)}, content_type='application/json',
                HTTP_AUTHORIZATION=token
            )
            self.assertEqual(res.status_code, status.HTTP_403_FORBIDDEN)

    def test_pagination(self):
        count = 50
        QueueFactory.create_batch(count, creator=self.user)
        for limit in range(1, count + 10):
            res = self.client.get(
                reverse('api_queue_list_create_api_view'),
                data={'limit': limit},
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            self.assertEqual(len(res.json()['results']), min(limit, count))

        for offset in range(count+1):
            res = self.client.get(
                reverse('api_queue_list_create_api_view'),
                data={'limit': count, 'offset': offset},
                HTTP_AUTHORIZATION=self.access_header
            )
            self.assertEqual(res.status_code, status.HTTP_200_OK)
            self.assertEqual(len(res.json()['results']), count - offset)
