from rest_framework.generics import ListCreateAPIView
from rest_framework.permissions import SAFE_METHODS

from queue_module.models import Queue
from . import serializers


class QueueListCreateAPIView(ListCreateAPIView):

    queryset = Queue.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.QueueRetrieveSerializer
        return serializers.QueueCreateSerializer
