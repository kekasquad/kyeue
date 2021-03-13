import factory
from factory import fuzzy

from core.models import User


class UserFactory(factory.django.DjangoModelFactory):
    class Meta:
        model = User

    username = fuzzy.FuzzyText()
    is_active = True

    first_name = fuzzy.FuzzyText()
    last_name = fuzzy.FuzzyText()
