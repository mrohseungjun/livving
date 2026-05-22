create table if not exists public.user_contact_settings (
    user_id uuid primary key references public.profiles(id) on delete cascade,
    phone_number text,
    phone_call_enabled boolean not null default false,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    constraint user_contact_settings_phone_number_check check (
        phone_number is null or length(trim(phone_number)) between 8 and 30
    ),
    constraint user_contact_settings_enabled_requires_phone_check check (
        phone_call_enabled = false or nullif(trim(coalesce(phone_number, '')), '') is not null
    )
);

alter table public.user_contact_settings enable row level security;

create policy user_contact_settings_select_related
    on public.user_contact_settings
    for select
    to authenticated
    using (
        (select auth.uid()) is not null
        and (
            (select auth.uid()) = user_id
            or exists (
                select 1
                from public.guardian_relations gr
                where gr.status = 'accepted'
                  and (
                    (gr.user_id = user_contact_settings.user_id and gr.guardian_user_id = (select auth.uid()))
                    or (gr.guardian_user_id = user_contact_settings.user_id and gr.user_id = (select auth.uid()))
                  )
            )
        )
    );

create policy user_contact_settings_insert_own
    on public.user_contact_settings
    for insert
    to authenticated
    with check ((select auth.uid()) = user_id);

create policy user_contact_settings_update_own
    on public.user_contact_settings
    for update
    to authenticated
    using ((select auth.uid()) = user_id)
    with check ((select auth.uid()) = user_id);

create policy user_contact_settings_delete_own
    on public.user_contact_settings
    for delete
    to authenticated
    using ((select auth.uid()) = user_id);

grant select, insert, update, delete
    on public.user_contact_settings
    to authenticated;
