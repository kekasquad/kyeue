from django.contrib.auth import authenticate
from rest_framework import serializers, status
from rest_framework.authtoken.models import Token
from rest_framework.response import Response

from user.serializers import UserRetrieveSerializer


class TokenCreateSerializer(serializers.Serializer):
    username = serializers.CharField(
        required=True, max_length=60
    )
    password = serializers.CharField(
        required=True, max_length=60
    )


class TokenRetrieveSerializer(serializers.ModelSerializer):
    class Meta:
        model = Token
        read_only_fields = ('key', 'user', 'created')
        fields = read_only_fields

    user = UserRetrieveSerializer()
