from django.apps import AppConfig


class QueueModuleConfig(AppConfig):
    name = 'queue_module'

    def ready(self):
        import queue_module.signals
