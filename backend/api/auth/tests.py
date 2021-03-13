from datetime import timedelta
from unittest import mock

from django.test import TestCase
from django.urls import reverse
from factory import fuzzy
from rest_framework import status
from rest_framework_simplejwt.utils import aware_utcnow

import settings
from core.factories import UserFactory
from core.models import User


class AuthAPITestCases(TestCase):

    @staticmethod
    def _get_auth_header(token):
        return f'Bearer {token}'

    def test_forbidden_request(self):
        response = self.client.get(reverse('api_queue_list_create_api_view'))
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

    def test_success_signup(self):
        fuzzy_text = fuzzy.FuzzyText()
        data = {
            'username': fuzzy_text.fuzz(),
            'password': fuzzy_text.fuzz(),
            'firstName': fuzzy_text.fuzz(),
            'lastName': fuzzy_text.fuzz(),
        }

        response = self.client.post(reverse('api_auth_signup_api_view'), data=data)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)

        self.assertEqual(User.objects.count(), 1)

        user = User.objects.get(
            username=data['username'],
            first_name=data['firstName'] if 'firstName' in data else None,
            last_name=data['lastName'] if 'lastName' in data else None
        )
        self.assertTrue(user.check_password(data['password']))

    def test_failed_signup(self):
        fuzzy_text = fuzzy.FuzzyText()
        dataset = (
            {'login': fuzzy_text.fuzz()},
            {'password': fuzzy_text.fuzz()},
            {'login': fuzzy_text.fuzz(), 'password': ''},
            {'login': '', 'password': fuzzy_text.fuzz()},
        )
        for data in dataset:
            response = self.client.post(reverse('api_auth_signup_api_view'), data=data)
            self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
            self.assertEqual(User.objects.count(), 0)

    def test_success_auth(self):
        user = UserFactory()
        password = fuzzy.FuzzyText().fuzz()
        user.set_password(password)
        user.save()

        jwt_response = self.client.post(reverse('api_auth_login_api_view'), data={
            'username': user.username,
            'password': password
        })
        self.assertEqual(jwt_response.status_code, status.HTTP_200_OK)

        jwt = jwt_response.json()
        self.assertIn('access', jwt)
        self.assertIn('refresh', jwt)

        res = self.client.get(
            reverse('api_queue_list_create_api_view'),
            HTTP_AUTHORIZATION=self._get_auth_header(jwt["access"])
        )
        self.assertEqual(res.status_code, status.HTTP_200_OK)

    def test_failed_auth(self):
        user = UserFactory()
        user.set_password(fuzzy.FuzzyText().fuzz())
        user.save()

        response = self.client.post(reverse('api_auth_login_api_view'), data={
            'username': user.username,
            'password': fuzzy.FuzzyText().fuzz()
        })
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

    def test_logout(self):
        user = UserFactory()
        password = fuzzy.FuzzyText().fuzz()
        user.set_password(password)
        user.save()

        jwt = self.client.post(reverse('api_auth_login_api_view'), data={
            'username': user.username,
            'password': password
        }).json()

        response = self.client.post(
            reverse('api_auth_logout_api_view'),
            HTTP_AUTHORIZATION=self._get_auth_header(jwt['access'])
        )
        self.assertEqual(response.status_code, status.HTTP_205_RESET_CONTENT)

        response = self.client.post(
            reverse('api_auth_login_refresh_api_view'),
            HTTP_AUTHORIZATION=self._get_auth_header(jwt['refresh'])
        )
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)

        mock_ = mock.Mock()
        mock_.return_value = aware_utcnow() +\
            settings.SIMPLE_JWT['ACCESS_TOKEN_LIFETIME'] +\
            timedelta(seconds=1)

        with mock.patch('rest_framework_simplejwt.tokens.aware_utcnow', mock_):
            response = self.client.get(reverse('api_queue_list_create_api_view'))
        self.assertEquals(response.status_code, status.HTTP_401_UNAUTHORIZED)
