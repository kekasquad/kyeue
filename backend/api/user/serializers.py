from rest_framework import serializers

from core.models import User


class UserCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        read_only_fields = ('id',)
        fields = read_only_fields + ('username', 'password', 'firstName', 'lastName')

    username = serializers.CharField(
        required=True,
        max_length=60
    )
    password = serializers.CharField(
        required=True,
        max_length=128,
    )

    firstName = serializers.CharField(
        max_length=60,
        required=False,
        source='first_name'
    )
    lastName = serializers.CharField(
        max_length=60,
        required=False,
        source='last_name'
    )


class UserUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        read_only_fields = ('id', 'username')
        fields = read_only_fields + ('password', 'firstName', 'lastName')

    password = serializers.CharField(
        required=False,
        max_length=128,
    )
    firstName = serializers.CharField(
        max_length=60,
        required=False,
        source='first_name'
    )
    lastName = serializers.CharField(
        max_length=60,
        required=False,
        source='last_name'
    )


class UserRetrieveSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        read_only_fields = ('id', 'username', 'password', 'firstName', 'lastName')
        fields = read_only_fields

    firstName = serializers.CharField(
        required=False,
        source='first_name'
    )
    lastName = serializers.CharField(
        required=False,
        source='last_name'
    )