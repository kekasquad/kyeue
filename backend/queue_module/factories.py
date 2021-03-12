import factory
from factory import fuzzy

from queue_module.models import Queue


class QueueFactory(factory.django.DjangoModelFactory):
    class Meta:
        model = Queue

    name = fuzzy.FuzzyText().fuzz()
