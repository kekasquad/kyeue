from django.urls import re_path
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
)

from . import views

urlpatterns = [
    re_path(
        r'^signup/?$', views.SignUpView.as_view(),
        name='api_auth_signup_api_view'
    ),
    re_path(
        r'^login/?$', TokenObtainPairView.as_view(),
        name='api_auth_login_api_view'
    ),
    re_path(
        r'^login/refresh/?$', TokenRefreshView.as_view(),
        name='api_auth_login_refresh_api_view'
    ),
    re_path(
        r'^logout/?$', views.LogoutView.as_view(),
        name='api_auth_logout_api_view'
    )
]
