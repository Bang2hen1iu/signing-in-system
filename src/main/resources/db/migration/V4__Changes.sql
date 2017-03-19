CREATE TABLE semester (
  id             BIGINT   NOT NULL,
  start_date     DATE     NOT NULL
) WITHOUT OIDS;

ALTER TABLE semester
  ADD PRIMARY KEY (id);

ALTER TABLE course
  ADD semester_id BIGINT;
ALTER TABLE course
  ADD FOREIGN KEY (semester_id) REFERENCES semester (id);
