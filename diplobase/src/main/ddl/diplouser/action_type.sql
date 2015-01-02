-- Table: diplouser.action_type

-- DROP TABLE diplouser.action_type;

CREATE TABLE diplouser.action_type
(
  id serial NOT NULL,
  name character varying(80),
  CONSTRAINT action_type_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplouser.action_type
  OWNER TO postgres;
