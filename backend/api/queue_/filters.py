import django_filters

from queue_module.models import Queue


class QueueFilter(django_filters.FilterSet):

    class Meta:
        model = Queue
        fields = ('name', 'creatorId', 'creatorUsername')

    name = django_filters.CharFilter(
        field_name='name', lookup_expr='istartswith'
    )
    creatorId = django_filters.UUIDFilter(
        field_name='creator__id', lookup_expr='exact'
    )
    creatorUsername = django_filters.CharFilter(
        field_name='creator__username', lookup_expr='istartswith'
    )
