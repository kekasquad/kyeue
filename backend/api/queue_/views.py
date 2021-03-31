from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView, UpdateAPIView
from rest_framework.permissions import SAFE_METHODS, IsAuthenticated
from rest_framework.response import Response

from queue_module.models import Queue
from . import serializers


class QueueListCreateAPIView(ListCreateAPIView):
    permission_classes = (IsAuthenticated,)

    queryset = Queue.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.QueueRetrieveSerializer
        return serializers.QueueCreateSerializer


class QueueRetrieveUpdateDestroyAPIView(RetrieveUpdateDestroyAPIView):
    permission_classes = (IsAuthenticated,)

    queryset = Queue.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.QueueRetrieveSerializer
        return serializers.QueueUpdateSerializer


class BaseQueueMemberOperationAPIView(UpdateAPIView):
    permission_classes = (IsAuthenticated,)

    queryset = Queue.objects.all()
    serializer_class = serializers.QueueMemberSerializer

    method_name = None

    def update(self, request, *args, **kwargs):
        instance = self.get_object()
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid()

        getattr(instance, self.method_name)(str(serializer.validated_data['userId']))
        instance.save()

        response_serializer = serializers.QueueRetrieveSerializer(instance=instance)
        return Response(response_serializer.data)


class QueueAddMemberAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'push_member'


class QueueRemoveMemberAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'pop_member'


class QueueMoveMemberToEndAPIView(BaseQueueMemberOperationAPIView):
    method_name = 'move_member_to_the_end'

