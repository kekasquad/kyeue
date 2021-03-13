from rest_framework.generics import CreateAPIView

from core.models import User
from ..user import serializers as user_serializers


class SignUpView(CreateAPIView):
    queryset = User.objects.all()
    serializer_class = user_serializers.UserCreateSerializer
