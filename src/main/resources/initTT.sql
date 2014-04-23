CREATE TABLE IF NOT EXISTS department_messages(
  id TINYINT auto_increment NOT NULL,
  message LONGVARCHAR(5000),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS departments(
  id TINYINT auto_increment NOT NULL,
  name VARCHAR(100),
  tag CHAR(10),
  message_id TINYINT,
  PRIMARY KEY (id),
  FOREIGN KEY (message_id) REFERENCES department_messages(id)
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

CREATE TABLE IF NOT EXISTS days(
  id TINYINT NOT NULL auto_increment,
  name CHAR(3),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS subjects(
  id MEDIUMINT NOT NULL auto_increment,
  name VARCHAR(400),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS teachers(
  id MEDIUMINT NOT NULL auto_increment,
  name VARCHAR(80),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS locations(
  id MEDIUMINT NOT NULL auto_increment,
  building VARCHAR(20),
  room VARCHAR(30),
  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS datetimes(
  id MEDIUMINT NOT NULL auto_increment,
  week_id TINYINT,
  sequence TINYINT,
  day_id TINYINT,
  PRIMARY KEY (id),
  FOREIGN KEY (week_id) REFERENCES week_states(id),
  FOREIGN KEY (day_id) REFERENCES days(id),
  CONSTRAINT dateinfo UNIQUE (week_id, sequence, day_id)
);

CREATE TABLE IF NOT EXISTS subgroups(
  id MEDIUMINT NOT NULL auto_increment,
  group_id MEDIUMINT,
  name VARCHAR(40),
  PRIMARY KEY (id),
  FOREIGN KEY (group_id) REFERENCES groups(id)
);


CREATE TABLE IF NOT EXISTS lessons(
  group_id MEDIUMINT,
  subgroup_id MEDIUMINT,
  datetime_id MEDIUMINT,
  subject_id MEDIUMINT,
  teacher_id MEDIUMINT,
  location_id MEDIUMINT,
  timestamp BIGINT,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (datetime_id) REFERENCES datetimes(id),
  FOREIGN KEY (subject_id) REFERENCES subjects(id),
  FOREIGN KEY (teacher_id) REFERENCES teachers(id),
  FOREIGN KEY (location_id) REFERENCES locations(id),
  FOREIGN KEY (subgroup_id) REFERENCES subgroups(id)
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
  SELECT * FROM (SELECT 'all') AS tmp
  WHERE NOT EXISTS (
      SELECT state FROM week_states WHERE state = 'all'
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
