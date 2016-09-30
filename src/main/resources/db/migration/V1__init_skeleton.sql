CREATE TABLE student (
  id     TEXT NOT NULL,
  name   TEXT NOT NULL,

  PRIMARY KEY (id)
) WITHOUT OIDS;

CREATE TABLE sign_in_info (
  student_id      TEXT NOT NULL,
  oper_date       DATE,
  start_morning   TIMESTAMP,
  end_morning     TIMESTAMP,
  start_afternoon TIMESTAMP,
  end_afternoon   TIMESTAMP,
  start_night     TIMESTAMP,
  end_night       TIMESTAMP,

  PRIMARY KEY (student_id, oper_date),
  FOREIGN KEY (student_id) REFERENCES student(id)
) WITHOUT OIDS;

CREATE TABLE absence (
  student_id     TEXT NOT NULL,
  oper_date      DATE NOT NULL,
  start_absence  TIMESTAMP NOT NULL,
  end_absence    TIMESTAMP NOT NULL,
  absence_reason TEXT NOT NULL,

  PRIMARY KEY (student_id, oper_date),
  FOREIGN KEY (student_id, oper_date) REFERENCES sign_in_info(student_id, oper_date)
) WITHOUT OIDS;

CREATE TABLE course (
  student_id    TEXT NOT NULL,
  course_name   TEXT NOT NULL,
  start_week    INTEGER NOT NULL,
  end_week      INTEGER NOT NULL,
  weekday       INTEGER NOT NULL,
  start_section INTEGER NOT NULL,
  end_section   INTEGER NOT NULL,

  PRIMARY KEY (student_id, course_name),
  FOREIGN KEY (student_id) REFERENCES student(id)
) WITHOUT OIDS;

CREATE TABLE duty_student (
  student_id TEXT NOT NULL,
  oper_date  DATE,

  PRIMARY KEY (student_id, oper_date),
  FOREIGN KEY (student_id) REFERENCES student(id)
) WITHOUT OIDS;

CREATE TABLE sys_admin (
  id              TEXT NOT NULL,
  password_salted TEXT NOT NULL,

  PRIMARY KEY (id)
) WITHOUT OIDS;
