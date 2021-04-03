from django.urls import re_path

from . import consumers

websocket_urlpatterns = [
    re_path(r'^ws/notifications/?$', consumers.CommonNotificationsConsumer.as_asgi()),
    re_path(r'^ws/queue/(?P<queue_id>[^/]+)/?$', consumers.QueueConsumer.as_asgi()),
]
