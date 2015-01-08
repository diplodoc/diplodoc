-- Table: diploexec.module

-- DROP TABLE diploexec.module;

CREATE TABLE diploexec.module
(
  id serial NOT NULL,
  definition text,
  name character varying(80),
  lastupdate character varying(80),
  CONSTRAINT module_pk PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE diploexec.module
OWNER TO postgres;
