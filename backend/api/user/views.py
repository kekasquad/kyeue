from rest_framework.generics import RetrieveUpdateDestroyAPIView
from rest_framework.permissions import SAFE_METHODS, IsAuthenticated

from core.models import User
from . import serializers
from .permissions import UserRetrieveUpdateDestroyAPIPermission


class UserRetrieveUpdateDestroyAPIView(RetrieveUpdateDestroyAPIView):
    permission_classes = (IsAuthenticated, UserRetrieveUpdateDestroyAPIPermission)

    queryset = User.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.UserRetrieveSerializer
        return serializers.UserUpdateSerializer
