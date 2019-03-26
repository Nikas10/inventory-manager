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