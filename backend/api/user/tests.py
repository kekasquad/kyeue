from django.test import TestCase
from django.urls import reverse
from rest_framework import status

from core.factories import UserFactory
from ..tests import AuthMixin


class UserAPITestCases(AuthMixin, TestCase):

    @staticmethod
    def _get_url(user_id):
        return reverse(
            'api_user_retrieve_update_destroy_api_view',
            kwargs={'pk': str(user_id)}
        )

    def test_delete_user(self):
        user = UserFactory()

        response = self.client.delete(
            self._get_url(user.id),
            HTTP_AUTHORIZATION=self.access_header
        )
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)
