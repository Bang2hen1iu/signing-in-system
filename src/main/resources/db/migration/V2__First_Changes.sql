CREATE TABLE sign_in_info_v2 (
  id                  BIGINT,
  student_id          BIGINT,
  oper_date           DATE,

  PRIMARY KEY (id),
  FOREIGN KEY (student_id) REFERENCES student(id)
) WITHOUT OIDS;

CREATE TABLE sign_in_info_record (
  sign_in_info_id          BIGINT,
  start_time          TIMESTAMP,
  end_time            TIMESTAMP,

  PRIMARY KEY (sign_in_info_id,start_time),
  FOREIGN KEY (sign_in_info_id) REFERENCES sign_in_info_v2(id)
) WITHOUT OIDS;

CREATE TABLE fingerprint (
  student_id          BIGINT,
  token               TEXT,

  PRIMARY KEY (student_id,token),
  FOREIGN KEY (student_id) REFERENCES student(id)
) WITHOUT OIDS;

ALTER TABLE sign_in_token DROP admin_id;
DROP TABLE sys_admin;

DROP TABLE statistics;

ALTER TABLE absence ADD sign_in_info_id BIGINT;
ALTER TABLE absence ADD id BIGINT;
ALTER TABLE absence DROP CONSTRAINT absence_pkey;
ALTER TABLE absence DROP student_id;
ALTER TABLE absence DROP oper_date;
ALTER TABLE absence ADD PRIMARY KEY (id);
ALTER TABLE absence ADD FOREIGN KEY (sign_in_info_id) REFERENCES sign_in_info_v2(id);

ALTER TABLE course_per_week ADD course_id BIGINT;
ALTER TABLE course_per_week DROP CONSTRAINT course_per_week_pkey;
ALTER TABLE course_per_week ADD PRIMARY KEY (course_id,weekday);

ALTER TABLE course_per_week DROP student_id;
ALTER TABLE course_per_week DROP course_name;

ALTER TABLE course ADD id BIGINT;
ALTER TABLE course DROP CONSTRAINT course_pkey;
ALTER TABLE course ADD PRIMARY KEY(id);

ALTER TABLE course_per_week ADD FOREIGN KEY (course_id) REFERENCES course(id);
