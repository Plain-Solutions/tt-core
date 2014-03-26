CREATE TABLE IF NOT EXISTS departments(
  ID TINYINT auto_increment NOT NULL,
  name VARCHAR(100),
  tag CHAR(10),
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS groups(
  id MEDIUMINT auto_increment NOT NULL,
  department_id TINYINT,
  name CHAR(30),
  PRIMARY KEY (id),
  FOREIGN KEY (department_id) REFERENCES departments(id)
);
CREATE TABLE IF NOT EXISTS week_states(
  id TINYINT NOT NULL auto_increment,
  state CHAR(4),
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS day(
  id TINYINT NOT NULL auto_increment,
  name CHAR(3),
  PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS subjects(
  id MEDIUMINT NOT NULL auto_increment,
  info LONGVARCHAR(500),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lessons_datetimes(
  id MEDIUMINT NOT NULL auto_increment,
  week_id TINYINT,
  sequence TINYINT,
  day_id TINYINT,
  PRIMARY KEY (id),
  FOREIGN KEY (week_id) REFERENCES week_states(id),
  FOREIGN KEY (day_id) REFERENCES day(id),
  CONSTRAINT dateinfo UNIQUE (week_id, sequence, day_id)
);

CREATE TABLE IF NOT EXISTS lesson_records(
  group_id TINYINT NOT NULL auto_increment,
  datetime_id MEDIUMINT,
  subject_id MEDIUMINT,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (datetime_id) REFERENCES lessons_datetimes(id),
  FOREIGN KEY (subject_id) REFERENCES subjects(id)
);


INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'even') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'even'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'odd') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'odd'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'both') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'both'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'even') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'even'
  ) LIMIT 1;
INSERT INTO week_states (state)
  SELECT * FROM (SELECT 'odd') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'odd'
  ) LIMIT 1;


INSERT INTO day (name)
  SELECT * FROM (SELECT 'mon') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'mon'
  ) LIMIT 1;
INSERT INTO day (name)
  SELECT * FROM (SELECT 'tue') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'tue'
  ) LIMIT 1;
INSERT INTO day (name)
  SELECT * FROM (SELECT 'wed') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'wed'
  ) LIMIT 1;
INSERT INTO day (name)
  SELECT * FROM (SELECT 'thu') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'thu'
  ) LIMIT 1;
INSERT INTO day (name)
  SELECT * FROM (SELECT 'fri') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'fri'
  ) LIMIT 1;
INSERT INTO day (name)
  SELECT * FROM (SELECT 'sat') AS tmp
  WHERE NOT EXISTS (
      SELECT name FROM day WHERE name = 'sat'
  ) LIMIT 1;
