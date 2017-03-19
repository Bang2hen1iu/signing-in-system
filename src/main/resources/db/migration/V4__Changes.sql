CREATE TABLE semester (
  id             BIGINT   NOT NULL,
  start_date     DATE     NOT NULL
) WITHOUT OIDS;

ALTER TABLE course
  ADD semester_id BIGINT;
  ADD FOREIGN KEY (semester_id) REFERENCES semester (id);
