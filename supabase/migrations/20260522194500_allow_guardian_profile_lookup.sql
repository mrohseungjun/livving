drop policy if exists profiles_select_accepted_guardian on public.profiles;
create policy profiles_select_accepted_guardian
    on public.profiles
    for select
    using (
        exists (
            select 1
            from public.guardian_relations gr
            where gr.user_id = profiles.id
              and gr.guardian_user_id = auth.uid()
              and gr.status = 'accepted'
        )
    );
