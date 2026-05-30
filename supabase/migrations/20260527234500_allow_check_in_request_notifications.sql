alter table public.notification_events
    drop constraint if exists notification_events_type_check;

alter table public.notification_events
    add constraint notification_events_type_check check (
        event_type in (
            'missed_check_in',
            'check_in_request',
            'guardian_request',
            'relation_accepted',
            'test_push'
        )
    );
