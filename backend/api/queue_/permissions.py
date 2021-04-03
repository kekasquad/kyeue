from rest_framework.permissions import BasePermission, SAFE_METHODS


class QueueRetrieveUpdateDestroyAPIPermission(BasePermission):
    def has_permission(self, request, view):
        if request.method in SAFE_METHODS:
            return True
        else:
            return view.get_object().creator == request.user


class BaseQueueMemberOperationAPIPermission(BasePermission):
    def has_permission(self, request, view):
        return str(request.user.id) in (
            request.data.get('userId', None), str(view.get_object().creator.id)
        )
