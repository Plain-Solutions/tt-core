CREATE TABLE IF NOT EXISTS departments(
  id TINYINT auto_increment NOT NULL,
  name VARCHAR(100),
  tag CHAR(6),
  message LONGVARCHAR(5000),
  PRIMARY KEY (id),
);

CREATE TABLE IF NOT EXISTS groups(
  id SMALLINT auto_increment NOT NULL,
  department_id TINYINT,
  name CHAR(30),
  PRIMARY KEY (id),
  FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS week_states(
  id TINYINT NOT NULL auto_increment,
  state CHAR(5),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS days(
  id TINYINT NOT NULL auto_increment,
  name CHAR(3),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS subjects(
  id SMALLINT NOT NULL auto_increment,
  name VARCHAR(400),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS teachers(
  id SMALLINT NOT NULL auto_increment,
  name VARCHAR(80),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS locations(
  id MEDIUMINT NOT NULL auto_increment,
  building VARCHAR(50),
  room VARCHAR(50),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS datetimes(
  id SMALLINT NOT NULL auto_increment,
  week_id TINYINT,
  sequence TINYINT,
  day_id TINYINT,
  PRIMARY KEY (id),
  FOREIGN KEY (week_id) REFERENCES week_states(id),
  FOREIGN KEY (day_id) REFERENCES days(id),
  CONSTRAINT dateinfo UNIQUE (week_id, sequence, day_id)
);

CREATE TABLE IF NOT EXISTS subgroups(
  id SMALLINT NOT NULL auto_increment,
  group_id SMALLINT,
  name VARCHAR(60),
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE IF NOT EXISTS activities(
  id TINYINT NOT NULL auto_increment,
  type char(10),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lessons(
  group_id SMALLINT,
  datetime_id SMALLINT,
  activity_id TINYINT,
  subject_id SMALLINT,
  subgroup_id SMALLINT,
  teacher_id SMALLINT,
  location_id SMALLINT,
  timestamp BIGINT,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (datetime_id) REFERENCES datetimes(id),
  FOREIGN KEY (subject_id) REFERENCES subjects(id),
  FOREIGN KEY (teacher_id) REFERENCES teachers(id),
  FOREIGN KEY (location_id) REFERENCES locations(id),
  FOREIGN KEY (subgroup_id) REFERENCES subgroups(id),
  FOREIGN KEY (activity_id) REFERENCES activities(id),
);

CREATE INDEX IDXNAME ON DEPARTMENTS(TAG);

INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'nom') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'nom'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'denom') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'denom'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'full') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'full'
  ) LIMIT 1;

INSERT INTO days (name)
  SELECT * FROM (SELECT 'mon') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'mon'
  ) LIMIT 1;
INSERT INTO days (name)
  SELECT * FROM (SELECT 'tue') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'tue'
  ) LIMIT 1;
INSERT INTO days (name)
  SELECT * FROM (SELECT 'wed') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'wed'
  ) LIMIT 1;
INSERT INTO days (name)
  SELECT * FROM (SELECT 'thu') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'thu'
  ) LIMIT 1;
INSERT INTO days (name)
  SELECT * FROM (SELECT 'fri') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'fri'
  ) LIMIT 1;
INSERT INTO days (name)
  SELECT * FROM (SELECT 'sat') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM days WHERE name = 'sat'
  ) LIMIT 1;
INSERT INTO activities (type)
  SELECT * FROM (SELECT 'lecture') AS tmp
  WHERE NOT EXISTS (
      SELECT type FROM activities WHERE type = 'lecture'
  ) LIMIT 1;
INSERT INTO activities (type)
  SELECT * FROM (SELECT 'practice') AS tmp
  WHERE NOT EXISTS (
      SELECT type FROM activities WHERE type = 'practice'
  ) LIMIT 1;
INSERT INTO activities (type)
  SELECT * FROM (SELECT 'laboratory') AS tmp
  WHERE NOT EXISTS (
      SELECT type FROM activities WHERE type = 'laboratory'
  ) LIMIT 1;
