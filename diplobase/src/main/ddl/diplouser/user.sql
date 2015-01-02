-- Table: diplouser."user"

-- DROP TABLE diplouser."user";

CREATE TABLE diplouser."user"
(
  id serial NOT NULL,
  name character varying(80),
  CONSTRAINT user_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplouser."user"
  OWNER TO postgres;
