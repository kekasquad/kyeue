from rest_framework import serializers

from queue_module.models import Queue


class QueueRetrieveSerializer(serializers.ModelSerializer):
    class Meta:
        model = Queue
        read_only_fields = ('id', 'name', 'isPrivate', 'members')
        fields = read_only_fields

    isPrivate = serializers.BooleanField(read_only=True, source='is_private')


class QueueCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Queue
        fields = ('name',)
