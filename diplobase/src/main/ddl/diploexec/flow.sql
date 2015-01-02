-- Table: diploexec.flow

-- DROP TABLE diploexec.flow;

CREATE TABLE diploexec.flow
(
  id serial NOT NULL,
  definition text,
  name character varying(80),
  CONSTRAINT flow_pk PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE diploexec.flow
OWNER TO postgres;
