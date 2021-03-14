from django.db import IntegrityError
from rest_framework import serializers
from rest_framework.exceptions import ValidationError

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
        write_only=True
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

    def create(self, validated_data):
        try:
            user = User.objects.create_user(**validated_data)
            return user
        except IntegrityError:
            raise ValidationError({'username': ['User with this username already exists']})


class UserUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        read_only_fields = ('id', 'username')
        fields = read_only_fields + ('password', 'firstName', 'lastName')

    password = serializers.CharField(
        required=False,
        max_length=128,
        write_only=True
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

    def update(self, instance, validated_data):
        instance = super().update(instance, validated_data)
        if 'password' in validated_data:
            instance.set_password(validated_data['password'])
            instance.save()

        return instance


class UserRetrieveSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        read_only_fields = ('id', 'username', 'firstName', 'lastName')
        fields = read_only_fields

    firstName = serializers.CharField(
        required=False,
        source='first_name'
    )
    lastName = serializers.CharField(
        required=False,
        source='last_name'
    )
