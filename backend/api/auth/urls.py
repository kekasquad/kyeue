from django.urls import re_path

from . import views

urlpatterns = [
    re_path(
        r'^signup/?$', views.SignUpView.as_view(),
        name='api_auth_signup_api_view'
    ),
]
