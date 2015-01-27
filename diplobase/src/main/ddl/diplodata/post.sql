-- Table: diplodata.post

-- DROP TABLE diplodata.post;

CREATE TABLE diplodata.post
(
  id bigserial NOT NULL,
  url character varying(1000),
  html text,
  title character varying(200),
  meaningtext text,
  source_id biginteger,
  CONSTRAINT page_pk PRIMARY KEY (id),
  CONSTRAINT source_fk FOREIGN KEY (source_id)
  REFERENCES diplodata.source (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE diplodata.post
OWNER TO postgres;
