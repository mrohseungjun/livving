drop function if exists public.process_missed_check_ins();

create unique index if not exists notification_events_relation_accepted_unique
    on public.notification_events(guardian_relation_id, event_type)
    where event_type = 'relation_accepted' and guardian_relation_id is not null;

drop policy if exists notification_events_insert_relation_accepted_actor on public.notification_events;
create policy notification_events_insert_relation_accepted_actor
    on public.notification_events
    for insert
    to authenticated
    with check (
        event_type = 'relation_accepted'
        and actor_user_id = (select auth.uid())
        and related_user_id = (select auth.uid())
        and exists (
            select 1
            from public.guardian_relations gr
            where gr.id = guardian_relation_id
              and gr.user_id = recipient_user_id
              and gr.guardian_user_id = (select auth.uid())
              and gr.status = 'accepted'
        )
    );

grant insert on public.notification_events to authenticated;

create or replace function public.process_missed_check_ins()
returns table (
    event_id uuid,
    recipient_user_id uuid,
    related_user_id uuid,
    push_token_id bigint,
    token text,
    platform text,
    event_type text,
    title text,
    body text
)
language sql
security definer
set search_path = public
as $$
    with due_users as (
        select
            us.user_id,
            us.deadline_time,
            us.delay_minutes,
            ((now() at time zone 'Asia/Seoul')::date) as check_date
        from public.user_settings us
        where us.push_enabled = true
          and us.missed_push_enabled = true
          and (now() at time zone 'Asia/Seoul') >= (
              ((now() at time zone 'Asia/Seoul')::date + us.deadline_time)
              + make_interval(mins => us.delay_minutes)
          )
          and not exists (
              select 1
              from public.check_ins ci
              where ci.user_id = us.user_id
                and ci.check_in_date = ((now() at time zone 'Asia/Seoul')::date)
          )
    ),
    inserted as (
        insert into public.notification_events (
            recipient_user_id,
            actor_user_id,
            event_type,
            title,
            body,
            related_user_id,
            guardian_relation_id,
            check_in_date
        )
        select
            gr.guardian_user_id,
            du.user_id,
            'missed_check_in',
            p.nickname || '님 안부 미확인',
            du.deadline_time::text || '까지 안부 확인이 없었어요.',
            du.user_id,
            gr.id,
            du.check_date
        from due_users du
        join public.guardian_relations gr
          on gr.user_id = du.user_id
         and gr.status = 'accepted'
         and gr.guardian_user_id is not null
        join public.profiles p
          on p.id = du.user_id
        join public.user_settings guardian_settings
          on guardian_settings.user_id = gr.guardian_user_id
         and guardian_settings.push_enabled = true
         and guardian_settings.missed_push_enabled = true
        on conflict do nothing
        returning id
    )
    select
        ne.id,
        ne.recipient_user_id,
        ne.related_user_id,
        pt.id,
        pt.token,
        pt.platform,
        ne.event_type,
        ne.title,
        ne.body
    from public.notification_events ne
    join public.push_tokens pt
      on pt.user_id = ne.recipient_user_id
     and pt.enabled = true
    join public.user_settings recipient_settings
      on recipient_settings.user_id = ne.recipient_user_id
     and recipient_settings.push_enabled = true
    where ne.status in ('pending', 'failed')
      and (
          (
              ne.event_type = 'missed_check_in'
              and ne.check_in_date = ((now() at time zone 'Asia/Seoul')::date)
              and recipient_settings.missed_push_enabled = true
          )
          or (
              ne.event_type in ('guardian_request', 'relation_accepted')
              and recipient_settings.relation_push_enabled = true
          )
      );
$$;

revoke all on function public.process_missed_check_ins() from public;
