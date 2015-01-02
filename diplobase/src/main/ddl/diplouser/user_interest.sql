-- Table: diplouser.user_interest

-- DROP TABLE diplouser.user_interest;

CREATE TABLE diplouser.user_interest
(
  id serial NOT NULL,
  user_id integer,
  interest_id integer,
  CONSTRAINT user_interest_pk PRIMARY KEY (id),
  CONSTRAINT interest_fk FOREIGN KEY (interest_id)
      REFERENCES diplouser.interest (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_fk FOREIGN KEY (user_id)
      REFERENCES diplouser."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplouser.user_interest
  OWNER TO postgres;
