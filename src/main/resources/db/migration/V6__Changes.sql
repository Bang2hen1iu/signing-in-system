ALTER TABLE week_plan
  ADD PRIMARY KEY (id);
ALTER TABLE plan_record
  ADD PRIMARY KEY (id);
ALTER TABLE plan_record
  ADD FOREIGN KEY (plan_id) REFERENCES week_plan (id);
