from django.urls import re_path

from . import views

urlpatterns = [
    re_path(
        r'^$', views.QueueListCreateAPIView.as_view(),
        name='api_queue_list_create'
    ),
]
