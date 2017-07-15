CREATE TABLE week_plan (
  id             BIGINT   NOT NULL,
  week_name      TEXT     NOT NULL,
  create_at      DATE     NOT NULL
) WITHOUT OIDS;

CREATE TABLE plan_record (
  id             BIGINT   NOT NULL,
  plan_id        BIGINT   NOT NULL,
  student_id     BIGINT   NOT NULL,
  plan           TEXT     NOT NULL,
  achievement    TEXT     DEFAULT '',
  tutor_feedback TEXT     DEFAULT ''
) WITHOUT OIDS;

