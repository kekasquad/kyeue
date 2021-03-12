from django.urls import re_path

from . import views

urlpatterns = [
    re_path(
        r'^$', views.QueueListCreateAPIView.as_view(),
        name='api_queue_list_create_api_view'
    ),
    re_path(
        r'^(?P<pk>[^/]+)/?$', views.QueueRetrieveUpdateDestroyAPIView.as_view(),
        name='api_queue_retrieve_update_destroy_api_view'
    ),
    re_path(
        r'^(?P<pk>[^/]+)/add/?$', views.QueueAddMemberAPIView.as_view(),
        name='api_queue_add_member_api_view'
    ),
    re_path(
        r'^(?P<pk>[^/]+)/remove/?$', views.QueueRemoveMemberAPIView.as_view(),
        name='api_queue_remove_member_api_view'
    ),
    re_path(
        r'^(?P<pk>[^/]+)/move-to-end/?$', views.QueueMoveMemberToEndAPIView.as_view(),
        name='api_queue_move_member_to_end_api_view'
    )
]
