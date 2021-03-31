from channels.auth import AuthMiddlewareStack
from channels.routing import ProtocolTypeRouter, URLRouter
from . import urls

application = ProtocolTypeRouter({
    'websocket': AuthMiddlewareStack(
        URLRouter(urls.websocket_urlpatterns)
    ),
})
