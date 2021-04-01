from rest_framework.permissions import BasePermission, SAFE_METHODS


class UserRetrieveUpdateDestroyAPIPermission(BasePermission):
    def has_permission(self, request, view):
        if request.method in SAFE_METHODS:
            return True
        else:
            return view.get_object().id == request.user.id
