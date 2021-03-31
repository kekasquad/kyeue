from django.contrib.auth import authenticate
from rest_framework import status
from rest_framework.authtoken.models import Token
from rest_framework.generics import CreateAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView

from core.models import User
from .serializers import TokenRetrieveSerializer
from ..user import serializers as user_serializers
from . import serializers


class SignUpView(CreateAPIView):
    queryset = User.objects.all()
    serializer_class = user_serializers.UserCreateSerializer


class LogInView(CreateAPIView):
    queryset = Token.objects.all()
    serializer_class = serializers.TokenCreateSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        user = authenticate(
            username=serializer.validated_data.get('username'),
            password=serializer.validated_data.get('password')
        )
        if not user:
            return Response({'error': 'Invalid Credentials'},
                            status=status.HTTP_401_UNAUTHORIZED)

        token, _ = Token.objects.get_or_create(user=user)
        token_serializer = TokenRetrieveSerializer(instance=token)
        headers = self.get_success_headers(token_serializer.data)

        return Response(token_serializer.data, status=status.HTTP_200_OK, headers=headers)


class LogoutView(APIView):
    permission_classes = (IsAuthenticated,)

    def post(self, request):
        try:
            Token.objects.get(user_id=request.user.id).delete()
        except Exception:
            pass

        return Response(status=status.HTTP_205_RESET_CONTENT)
