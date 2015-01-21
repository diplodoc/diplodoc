-- Table: diploexec.processrunparameter

-- DROP TABLE diploexec.processrunparameter;

CREATE TABLE diploexec.processrunparameter
(
  id bigserial NOT NULL,
  key character varying(80),
  type character varying(80),
  value text,
  processrun_id bigint,
  CONSTRAINT processrunparameter_pk PRIMARY KEY (id),
  CONSTRAINT processrun_fk FOREIGN KEY (processrun_id)
      REFERENCES diploexec.processrun (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diploexec.processrunparameter
  OWNER TO postgres;
