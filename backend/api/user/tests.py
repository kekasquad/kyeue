from django.test import TestCase
from django.urls import reverse
from factory import fuzzy
from rest_framework import status

from core.models import User
from ..tests import AuthMixin


class UserAPITestCases(AuthMixin, TestCase):

    @staticmethod
    def _get_url(user_id):
        return reverse(
            'api_user_retrieve_update_destroy_api_view',
            kwargs={'pk': str(user_id)}
        )

    def test_delete_user(self):

        response = self.client.delete(
            self._get_url(self.user.id),
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
        self.assertEqual(User.objects.count(), 0)

    def test_retrieve_user(self):
        response = self.client.get(
            self._get_url(self.user.id),
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        response = response.json()

        self.assertEqual(response['id'], str(self.user.id))
        self.assertEqual(response['username'], self.user.username)
        self.assertEqual(response['firstName'], self.user.first_name)
        self.assertEqual(response['lastName'], self.user.last_name)

    def test_update_user_successful(self):
        fuzzy_text = fuzzy.FuzzyText()
        update_data = {
            'username': fuzzy_text.fuzz(),
            'password': fuzzy_text.fuzz(),
            'firstName': fuzzy_text.fuzz(),
            'lastName': fuzzy_text.fuzz(),
        }
        response = self.client.patch(
            self._get_url(self.user.id),
            data=update_data,
            content_type='application/json',
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        response = response.json()

        self.assertEqual(response['firstName'], update_data['firstName'])
        self.assertEqual(response['lastName'], update_data['lastName'])

        self.user.refresh_from_db()
        self.assertTrue(self.user.check_password(update_data['password']))
        self.assertEqual(self.user.first_name, update_data['firstName'])
        self.assertEqual(self.user.last_name, update_data['lastName'])
