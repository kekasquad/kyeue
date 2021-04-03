from django.apps import AppConfig


class WebsocketsModuleConfig(AppConfig):
    name = 'websockets'

    def ready(self):
        import websockets.receivers
