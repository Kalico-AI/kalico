CREATE SEQUENCE IF NOT EXISTS pk_sequence_recipe
    start 1
  increment 1;


CREATE TABLE IF NOT EXISTS public.recipe (
    id BIGSERIAL NOT NULL constraint recipe_id_fk primary key,
    content_id varchar(256),
    slug varchar(256),
    canonical_url varchar(256),
    title varchar not null default 'Untitled',
    description varchar(256),
    summary varchar,
    thumbnail varchar(256),
    num_ingredients bigint,
    num_steps bigint,
    cooking_time_minutes bigint,
    ingredients varchar,
    instructions varchar,
    processed boolean not null default false,
    failed boolean not null default false,
    reason_failed varchar(255),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(slug),
    UNIQUE(content_id),
    UNIQUE(canonical_url)
    );
ALTER TABLE public.recipe OWNER TO root;