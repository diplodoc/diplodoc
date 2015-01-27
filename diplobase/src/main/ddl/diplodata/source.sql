-- Table: diplodata.source

-- DROP TABLE diplodata.source;

CREATE TABLE diplodata.source
(
  id bigserial NOT NULL,
  name character varying(80),
  newpostsfindermodule character varying(80),
  CONSTRAINT source_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplodata.source
  OWNER TO postgres;
