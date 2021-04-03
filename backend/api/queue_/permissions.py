from django.core.exceptions import ObjectDoesNotExist
from rest_framework.permissions import BasePermission, SAFE_METHODS

from core.models import User


class QueueRetrieveUpdateDestroyAPIPermission(BasePermission):
    def has_permission(self, request, view):
        if request.method in SAFE_METHODS:
            return True
        else:
            return view.get_object().creator == request.user


class BaseQueueMemberOperationAPIPermission(BasePermission):
    def has_permission(self, request, view):
        if str(request.user.id) == request.data.get('userId', None):
            return not request.user.is_teacher

        try:
            return request.user.id == view.get_object().creator.id and \
                not User.objects.get(pk=request.data.get('userId', None)).is_teacher
        except ObjectDoesNotExist:
            return True
