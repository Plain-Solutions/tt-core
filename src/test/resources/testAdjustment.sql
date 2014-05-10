INSERT INTO departments(name, tag, message) VALUES('Biology House', 'bf', 'Crucial Information!');
INSERT INTO departments(name, tag, message) VALUES('Geography House', 'gf', '');
INSERT INTO groups(department_id, name) VALUES('1','111');
INSERT INTO groups(department_id, name) VALUES('1','String name');
INSERT INTO subjects(name) VALUES('Calculus');
INSERT INTO teachers(name) VALUES('Sakhno L.V.');
INSERT INTO locations(building, room) VALUES('B1', 'Default');
INSERT INTO datetimes(week_id, sequence, day_id) VALUES(1,1,1);
INSERT INTO subgroups(group_id, name) VALUES(1, '1st');
INSERT INTO lessons(group_id, datetime_id, activity_id, subject_id, subgroup_id,
teacher_id, location_id, timestamp) VALUES(1,1,1,1,1,1,1,12345);

