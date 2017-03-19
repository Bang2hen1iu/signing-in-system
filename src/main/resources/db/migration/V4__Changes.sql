CREATE TABLE semester (
  id                                 BIGINT,
  start_date                          DATE,
) WITHOUT OIDS;

ALTER TABLE course
  ADD semester_id BIGINT;
  ADD FOREIGN KEY (semester_id) REFERENCES semester (id);
