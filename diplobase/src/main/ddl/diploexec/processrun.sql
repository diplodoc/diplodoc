-- Table: diploexec.processrun

-- DROP TABLE diploexec.processrun;

CREATE TABLE diploexec.processrun
(
  id bigserial NOT NULL,
  process_id bigint,
  starttime character varying(80),
  endtime character varying(80),
  exitstatus character varying(80),
  CONSTRAINT processrun_pk PRIMARY KEY (id),
  CONSTRAINT process_fk FOREIGN KEY (process_id)
      REFERENCES diploexec.process (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diploexec.processrun
  OWNER TO postgres;
