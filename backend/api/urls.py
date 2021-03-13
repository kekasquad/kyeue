from django.urls import re_path, include

urlpatterns = [
    re_path(r'^queue/', include('api.queue_.urls')),
    re_path(r'^auth/', include('api.auth.urls')),
    re_path(r'^user/', include('api.user.urls')),
]
