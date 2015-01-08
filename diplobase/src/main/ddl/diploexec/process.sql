-- Table: diploexec.process

-- DROP TABLE diploexec.process;

CREATE TABLE diploexec.process
(
  id serial NOT NULL,
  definition text,
  name character varying(80),
  lastupdate character varying(80),
  CONSTRAINT process_pk PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE diploexec.process
OWNER TO postgres;
