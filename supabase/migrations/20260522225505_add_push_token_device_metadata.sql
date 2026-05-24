alter table public.push_tokens
    add column if not exists device_id text,
    add column if not exists last_seen_at timestamptz not null default now();
