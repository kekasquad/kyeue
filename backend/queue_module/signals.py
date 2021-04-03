from django.dispatch import Signal

push_member_signal = Signal(providing_args=['queue_id', 'user_id'])
pop_member_signal = Signal(providing_args=['queue_id', 'user_id'])
move_member_to_the_end_signal = Signal(providing_args=['queue_id', 'user_id'])
