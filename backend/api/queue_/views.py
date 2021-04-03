from django.core.exceptions import ObjectDoesNotExist
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView, UpdateAPIView
from rest_framework.permissions import SAFE_METHODS, IsAuthenticated
from rest_framework.response import Response
from rest_framework.serializers import ValidationError

from queue_module.models import Queue
from . import serializers
from .permissions import QueueRetrieveUpdateDestroyAPIPermission, BaseQueueMemberOperationAPIPermission


class QueueListCreateAPIView(ListCreateAPIView):
    permission_classes = (IsAuthenticated,)

    queryset = Queue.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.QueueRetrieveSerializer
        return serializers.QueueCreateSerializer

    def perform_create(self, serializer):
        serializer.save(creator=self.request.user)


class QueueRetrieveUpdateDestroyAPIView(RetrieveUpdateDestroyAPIView):
    permission_classes = (IsAuthenticated, QueueRetrieveUpdateDestroyAPIPermission)

    queryset = Queue.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.QueueRetrieveSerializer
        return serializers.QueueUpdateSerializer


class BaseQueueMemberOperationAPIView(UpdateAPIView):
    permission_classes = (IsAuthenticated, BaseQueueMemberOperationAPIPermission)

    queryset = Queue.objects.all()
    serializer_class = serializers.QueueMemberSerializer

    method_name = None

    def update(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid()

        try:
            getattr(instance, self.method_name)(str(serializer.validated_data['userId']))
            instance.save()
            response_serializer = serializers.QueueRetrieveSerializer(instance=instance)
            return Response(response_serializer.data)
        except ObjectDoesNotExist:
            raise ValidationError({'userId': ['User with provided id does not exist']})


class QueueAddMemberAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'push_member'


class QueueRemoveMemberAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'pop_member'


class QueueMoveMemberToEndAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'move_member_to_the_end'
