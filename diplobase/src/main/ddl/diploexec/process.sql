-- Table: diploexec.process

-- DROP TABLE diploexec.process;

CREATE TABLE diploexec.process
(
  id bigserial NOT NULL,
  definition text,
  name character varying(80),
  lastupdate timestamp without time zone,
  active boolean,
  CONSTRAINT process_pk PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE diploexec.process
OWNER TO postgres;
