-- Table: diplouser.interest

-- DROP TABLE diplouser.interest;

CREATE TABLE diplouser.interest
(
  id serial NOT NULL,
  tag character varying(80),
  CONSTRAINT interest_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplouser.interest
  OWNER TO postgres;
