from django.urls import re_path, include

urlpatterns = [
    re_path(r'^queue/', include('api.queue_.urls')),
]
