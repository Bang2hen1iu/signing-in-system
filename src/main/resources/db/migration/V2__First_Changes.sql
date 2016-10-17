CREATE TABLE sign_in_info_v2 (
  id                  BIGINT,
  student_id          BIGINT,
  oper_date           DATE,

  PRIMARY KEY (id),
  FOREIGN KEY (student_id) REFERENCES student (id)
) WITHOUT OIDS;

CREATE TABLE sign_in_info_record (
  sign_in_id          BIGINT,
  start_time          TIMESTAMP,
  end_time            TIMESTAMP,

  PRIMARY KEY (sign_in_id,start_time),
  FOREIGN KEY (sign_in_id) REFERENCES sign_in_info_v2(id)
) WITHOUT OIDS;

ALTER TABLE student ADD fingerprint TEXT;