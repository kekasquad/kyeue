from rest_framework import serializers

from core.models import User
from queue_module.models import Queue
from user.serializers import UserRetrieveSerializer


class QueueRetrieveSerializer(serializers.ModelSerializer):
    class Meta:
        model = Queue
        read_only_fields = ('id', 'name', 'isPrivate', 'members')
        fields = read_only_fields

    isPrivate = serializers.BooleanField(read_only=True, source='is_private')
    members = serializers.SerializerMethodField(read_only=True)

    def get_members(self, obj):
        members = []
        for member_id in obj.members:
            members.append(
                UserRetrieveSerializer(User.objects.get(pk=member_id)).data
            )
        return members


class QueueCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Queue
        read_only_fields = ('id',)
        fields = read_only_fields + ('name',)


class QueueUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Queue
        read_only_fields = ('id',)
        fields = read_only_fields + ('name',)


class QueueMemberSerializer(serializers.Serializer):
    userId = serializers.UUIDField(required=True)

