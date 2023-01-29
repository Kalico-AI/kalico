ALTER TABLE public.video_content DROP CONSTRAINT blog_post_id_fk;

ALTER TABLE public.video_content
    ADD CONSTRAINT blog_post_id_fk FOREIGN KEY (blog_post_id) REFERENCES public.blog_post (id);