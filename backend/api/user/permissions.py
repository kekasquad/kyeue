from rest_framework.permissions import BasePermission, SAFE_METHODS


class UserRetrieveUpdateDestroyAPIPermission(BasePermission):
    def has_permission(self, request, view):
        if request.method in SAFE_METHODS:
            return True
        else:
            return request.data['userId'] == request.user.id
