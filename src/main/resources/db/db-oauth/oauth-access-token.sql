CREATE TABLE public.oauth_access_token
(
    token_id text COLLATE pg_catalog."default",
    token bytea,
    authentication_id text COLLATE pg_catalog."default" NOT NULL,
    user_name text COLLATE pg_catalog."default",
    client_id text COLLATE pg_catalog."default",
    authentication bytea,
    refresh_token text COLLATE pg_catalog."default",
    CONSTRAINT oauth_access_token_pkey PRIMARY KEY (authentication_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;