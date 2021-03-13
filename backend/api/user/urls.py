from django.urls import re_path

from . import views

urlpatterns = [
    re_path(
        r'^(?P<pk>[^/]+)/?$', views.UserRetrieveUpdateDestroyAPIView.as_view(),
        name='api_user_retrieve_update_destroy_api_view'
    ),
]

