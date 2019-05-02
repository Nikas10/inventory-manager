CREATE TABLE oauth_client_details
(
    client_id               text COLLATE pg_catalog."default" NOT NULL,
    resource_ids            text COLLATE pg_catalog."default",
    client_secret           text COLLATE pg_catalog."default",
    scope                   text COLLATE pg_catalog."default",
    authorized_grant_types  text COLLATE pg_catalog."default",
    web_server_redirect_uri text COLLATE pg_catalog."default",
    authorities             text COLLATE pg_catalog."default",
    access_token_validity   bigint,
    refresh_token_validity  bigint,
    additional_information  text COLLATE pg_catalog."default",
    autoapprove             text COLLATE pg_catalog."default",
    CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;