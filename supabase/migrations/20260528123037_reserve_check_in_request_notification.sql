create or replace function public.reserve_check_in_request_notification(
    p_guardian_user_id uuid,
    p_target_user_id uuid,
    p_title text,
    p_body text
)
returns table (
    event_id uuid,
    guardian_relation_id bigint,
    already_checked_in boolean,
    throttled boolean,
    retry_after_seconds integer
)
language plpgsql
security invoker
set search_path = public
as $$
declare
    v_relation_id bigint;
    v_recent_created_at timestamptz;
    v_event_id uuid;
    v_now timestamptz := now();
begin
    perform pg_advisory_xact_lock(
        hashtextextended(
            p_guardian_user_id::text || ':' || p_target_user_id::text || ':check_in_request',
            0
        )
    );

    select gr.id
      into v_relation_id
      from public.guardian_relations gr
     where gr.user_id = p_target_user_id
       and gr.guardian_user_id = p_guardian_user_id
       and gr.status = 'accepted'
     limit 1;

    if v_relation_id is null then
        return;
    end if;

    if exists (
        select 1
          from public.check_ins ci
         where ci.user_id = p_target_user_id
           and ci.check_in_date = ((v_now at time zone 'Asia/Seoul')::date)
    ) then
        return query
        select null::uuid, v_relation_id, true, false, null::integer;
        return;
    end if;

    select ne.created_at
      into v_recent_created_at
      from public.notification_events ne
     where ne.recipient_user_id = p_target_user_id
       and ne.actor_user_id = p_guardian_user_id
       and ne.event_type = 'check_in_request'
       and ne.created_at >= v_now - interval '60 seconds'
     order by ne.created_at desc
     limit 1;

    if v_recent_created_at is not null then
        return query
        select
            null::uuid,
            v_relation_id,
            false,
            true,
            greatest(1, 60 - floor(extract(epoch from (v_now - v_recent_created_at)))::integer);
        return;
    end if;

    insert into public.notification_events (
        recipient_user_id,
        actor_user_id,
        event_type,
        title,
        body,
        related_user_id,
        guardian_relation_id,
        status
    )
    values (
        p_target_user_id,
        p_guardian_user_id,
        'check_in_request',
        p_title,
        p_body,
        p_guardian_user_id,
        v_relation_id,
        'pending'
    )
    returning id into v_event_id;

    return query
    select v_event_id, v_relation_id, false, false, null::integer;
end;
$$;

revoke all on function public.reserve_check_in_request_notification(uuid, uuid, text, text) from public;
grant execute on function public.reserve_check_in_request_notification(uuid, uuid, text, text) to service_role;
