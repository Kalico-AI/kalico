CREATE SEQUENCE IF NOT EXISTS pk_email_tracking
    start 1
  increment 1;

CREATE TABLE IF NOT EXISTS public.email_tracking (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    email varchar(255),
    num_opens bigint NOT NULL DEFAULT 0,
    campaign_id varchar(255),
    ip_address varchar(255),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(email, campaign_id)
);
ALTER TABLE public.email_tracking OWNER TO root;
comment on table public.email_tracking is 'Email tracking data';
