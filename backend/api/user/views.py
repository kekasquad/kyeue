from rest_framework.generics import RetrieveUpdateDestroyAPIView
from rest_framework.permissions import SAFE_METHODS

from core.models import User
from . import serializers


class UserRetrieveUpdateDestroyAPIView(RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return serializers.UserRetrieveSerializer
        return serializers.UserUpdateSerializer
