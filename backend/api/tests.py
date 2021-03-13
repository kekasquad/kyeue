from django.urls import reverse
from factory import fuzzy

from core.factories import UserFactory


class AuthMixin:
    def setUp(self) -> None:
        self.user = UserFactory()
        self.user_password = fuzzy.FuzzyText().fuzz()
        self.user.set_password(self.user_password)
        self.user.save()

        jwt = self.client.post(
            reverse('api_auth_login_api_view'),
            data={
                'username': self.user.username,
                'password': self.user_password
            }
        ).json()
        self.access_header = f"Bearer {jwt['access']}"
