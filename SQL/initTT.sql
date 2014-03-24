CREATE TABLE IF NOT EXISTS departments(
  id TINYINT NOT NULL auto_increment,
  name VARCHAR(100),
  tag CHAR(10),
  PRIMARY KEY (ID)
);
CREATE TABLE IF NOT EXISTS groups(
  id TINYINT NOT NULL auto_increment,
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
  FOREIGN KEY (day_id) REFERENCES day(id)
);

CREATE TABLE IF NOT EXISTS lesson_records(
  group_id TINYINT NOT NULL auto_increment,
  datetime_id MEDIUMINT,
  subject_id MEDIUMINT,
  FOREIGN KEY (group_id) REFERENCES groups(id),
  FOREIGN KEY (datetime_id) REFERENCES lessons_datetimes(id),
  FOREIGN KEY (subject_id) REFERENCES subjects(id)
);