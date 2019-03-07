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


CREATE TABLE oauth_client_details

(

client_id text COLLATE pg_catalog."default" NOT NULL,

resource_ids text COLLATE pg_catalog."default",

client_secret text COLLATE pg_catalog."default",

scope text COLLATE pg_catalog."default",

authorized_grant_types text COLLATE pg_catalog."default",

web_server_redirect_uri text COLLATE pg_catalog."default",

authorities text COLLATE pg_catalog."default",

access_token_validity bigint,

refresh_token_validity bigint,

additional_information text COLLATE pg_catalog."default",

autoapprove text COLLATE pg_catalog."default",

CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id)

)

WITH (

OIDS = FALSE

)

TABLESPACE pg_default;

CREATE TABLE oauth_code

(

code text COLLATE pg_catalog."default",

authentication bytea

)

WITH (

OIDS = FALSE

)

TABLESPACE pg_default;

CREATE TABLE oauth_refresh_token

(

token_id text COLLATE pg_catalog."default",

token bytea,

authentication bytea

)

WITH (

OIDS = FALSE

)

TABLESPACE pg_default;

CREATE TABLE account

(

uid uuid NOT NULL,

login text COLLATE pg_catalog."default",

pass text COLLATE pg_catalog."default",

admin boolean,

CONSTRAINT account_pkey PRIMARY KEY (uid)

)

WITH (

OIDS = FALSE

)

TABLESPACE pg_default;


INSERT INTO oauth_client_details VALUES ('web',NULL,'$2a$10$GHRBb68j3ZEhrehTuYXiLeShSWxSPq2KggDfeda5DJR0elQAWAzAy','read,write,trust','password,authorization_code,refresh_token,implicit','','USER,ADMIN',NULL,NULL,NULL,NULL);