alter table public.guardian_relations
    drop constraint if exists guardian_relations_invite_code_key;

create unique index if not exists guardian_relations_user_guardian_key
    on public.guardian_relations(user_id, guardian_user_id);

drop policy if exists guardian_relations_guardian_accept_insert on public.guardian_relations;
create policy guardian_relations_guardian_accept_insert
    on public.guardian_relations
    for insert
    with check (
        auth.uid() = guardian_user_id
        and status = 'accepted'
        and exists (
            select 1
            from public.invite_links il
            where il.id = invite_link_id
              and il.invite_code = guardian_relations.invite_code
              and il.owner_user_id = guardian_relations.user_id
              and il.status = 'active'
        )
    );

drop policy if exists profiles_select_active_invite_owner on public.profiles;
create policy profiles_select_active_invite_owner
    on public.profiles
    for select
    using (
        exists (
            select 1
            from public.invite_links il
            where il.owner_user_id = profiles.id
              and il.status = 'active'
        )
    );
