-- Table: diplouser.user_action

-- DROP TABLE diplouser.user_action;

CREATE TABLE diplouser.user_action
(
  id serial NOT NULL,
  action_type_id integer,
  user_id integer,
  post_id integer,
  CONSTRAINT user_action_pk PRIMARY KEY (id),
  CONSTRAINT action_type_fk FOREIGN KEY (action_type_id)
      REFERENCES diplouser.action_type (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT post_fk FOREIGN KEY (post_id)
      REFERENCES diplodata.post (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_fk FOREIGN KEY (user_id)
      REFERENCES diplouser."user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE diplouser.user_action
  OWNER TO postgres;
