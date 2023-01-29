CREATE SCHEMA IF NOT EXISTS public;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SEQUENCE IF NOT EXISTS pk_sequence_blog_post
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_ingredient
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_recipe_step
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_sampled_image
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_video_content
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_authorized_user
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_user
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_submission
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_bookmark
    start 1
  increment 1;

CREATE SEQUENCE IF NOT EXISTS pk_sequence_cookie_jar
    start 1
  increment 1;


CREATE TABLE IF NOT EXISTS public.authorized_user (
    id BIGSERIAL NOT NULL,
    full_name varchar(255),
    email varchar(255),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.authorized_user OWNER TO root;
comment on table public.authorized_user is 'Pre-authorized users';


CREATE TABLE IF NOT EXISTS public.user (
  firebase_id varchar(255) NOT NULL,
  full_name varchar(255),
  first_name varchar(128),
  last_name varchar(128),
  email varchar(255),
  roles varchar(255)[],
  updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
UNIQUE(firebase_id)
);
ALTER TABLE public.user OWNER TO root;
comment on table public.user is 'Basic user information';

CREATE TABLE IF NOT EXISTS public.blog_post (
    id BIGSERIAL NOT NULL constraint blog_post_id_fk primary key,
    title varchar(255),
    slug varchar(255),
    tags varchar(255),
    summary varchar(255),
    author varchar(255),
    prep_time varchar(255),
    primary_thumbnail varchar(255),
    cuisine varchar(255),
    body varchar,
    is_featured boolean,
    published boolean,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_on timestamptz,
    UNIQUE(slug)
);
ALTER TABLE public.blog_post OWNER TO root;
comment on table public.blog_post is 'Blog post';

create unique index blog_post_slug_idx
  on blog_post (slug);

CREATE TABLE IF NOT EXISTS public.recipe_step (
  id BIGSERIAL NOT NULL,
  blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
  image_url varchar(255) not null,
  description varchar not null,
  step_number integer not null,
  updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.recipe_step OWNER TO root;
comment on table public.recipe_step is 'Blog post image url';

CREATE TABLE IF NOT EXISTS public.sampled_image (
  id BIGSERIAL NOT NULL,
  blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
  image_key varchar(255),
  updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.sampled_image OWNER TO root;
comment on table public.sampled_image is 'Image sampled from video frames';

CREATE TABLE IF NOT EXISTS public.video_content (
    id BIGSERIAL NOT NULL,
    blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
    url varchar not null,
    video_id varchar(255) not null,
    title varchar,
    transcript varchar,
    raw_transcript varchar,
    permalink varchar,
    on_screen_text varchar,
    description varchar,
    creator_avatar varchar,
    creator_handle varchar,
    share_count bigint NOT NULL,
    like_count bigint NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(video_id)
  );
ALTER TABLE public.video_content OWNER TO root;
comment on table public.video_content is 'Blog post video metadata';


CREATE TABLE IF NOT EXISTS public.ingredient (
  id BIGSERIAL NOT NULL,
  blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
  description varchar(255) not null,
  amount varchar(255) not null,
  units varchar(255) not null,
  sort_order integer not null,
  updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
  );
ALTER TABLE public.ingredient OWNER TO root;
comment on table public.ingredient is 'Recipe ingredient';

CREATE TABLE IF NOT EXISTS public.submission (
    id BIGSERIAL NOT NULL,
    user_id varchar not null constraint user_id_fk references public.user (firebase_id),
    blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, blog_post_id)
);
ALTER TABLE public.submission OWNER TO root;
comment on table public.submission is 'User submissions';

CREATE TABLE IF NOT EXISTS public.bookmark (
    id BIGSERIAL NOT NULL,
    user_id varchar not null constraint user_id_fk references public.user (firebase_id),
    blog_post_id bigint not null constraint blog_post_id_fk references public.blog_post (id),
    updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, blog_post_id)
    );
ALTER TABLE public.bookmark OWNER TO root;
comment on table public.bookmark is 'User bookmarks';

CREATE TABLE IF NOT EXISTS public.cookie_jar (
     id BIGSERIAL PRIMARY KEY NOT NULL,
     c_key varchar,
     c_path varchar,
     c_domain varchar,
     c_name varchar,
     c_value varchar,
     c_username varchar,
     c_host_only boolean,
     c_http_only boolean,
     c_persistent boolean,
     c_secure boolean,
     c_expires_at bigint,
     updated_dt timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
     created_dt timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE public.cookie_jar OWNER TO root;
comment on table public.cookie_jar is 'Persists request cookies';

-- Create table indices
CREATE INDEX IF NOT EXISTS cookie_jar_idx
    ON cookie_jar (c_key, c_username);
