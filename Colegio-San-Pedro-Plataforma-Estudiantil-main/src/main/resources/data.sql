INSERT IGNORE INTO usuarios (id_usuario, username, email, password, rol)
VALUES
  (1000, 'a_sgarcia', 'a_sgarcia@sanpedro.edu.pe', '1234', 'alumno'),
  (1001, 'doc_jose', 'doc_jose@sanpedro.edu.pe', '1234', 'docente'),
  (1002, 'a_jperez', 'a_jperez@sanpedro.edu.pe', '1234', 'alumno'),
  (1003, 'a_mlopez', 'a_mlopez@sanpedro.edu.pe', '1234', 'alumno'),
  (1004, 'a_rramos', 'a_rramos@sanpedro.edu.pe', '1234', 'alumno'),
  (1005, 'doc_carmen', 'doc_carmen@sanpedro.edu.pe', '1234', 'docente');

INSERT IGNORE INTO docentes (id_docente, usuario_id, dni, nombres, apellidos, especialidad, telefono)
VALUES
  (1000, 1001, '12345678', 'José', 'Pérez', 'Matemática', '987654321'),
  (1001, 1005, '12345679', 'Carmen', 'López', 'Comunicación', '987654322');

INSERT IGNORE INTO materias (id_materia, nombre, area)
VALUES
  (1000, 'Matemáticas', 'Ciencias'),
  (1001, 'Comunicación', 'Comunicación');

INSERT IGNORE INTO aulas (id_aula, nivel, grado, seccion, tutor_id, periodo, capacidad)
VALUES
  (1000, 'Primaria', '1ro', 'A', 1000, '2026', 30);

INSERT IGNORE INTO alumnos (id_alumno, usuario_id, dni, codigo_estudiante, nombres, apellidos, fecha_nacimiento, telefono_apoderado)
VALUES
  (1000, 1000, '87654321', 'A1000', 'Sofía', 'García', '2016-05-12', '987000000'),
  (1001, 1002, '87654322', 'A1001', 'Juan', 'Pérez', '2016-06-15', '987000001'),
  (1002, 1003, '87654323', 'A1002', 'María', 'López', '2016-07-20', '987000002'),
  (1003, 1004, '87654324', 'A1003', 'Roberto', 'Ramos', '2016-08-10', '987000003');

INSERT IGNORE INTO matriculas (id_matricula, alumno_id, aula_id, fecha_matricula)
VALUES
  (1000, 1000, 1000, '2026-03-01'),
  (1001, 1001, 1000, '2026-03-01'),
  (1002, 1002, 1000, '2026-03-01'),
  (1003, 1003, 1000, '2026-03-01');

INSERT IGNORE INTO cursos_programados (id_curso_prog, materia_id, docente_id, aula_id)
VALUES
  (1000, 1000, 1000, 1000),
  (1001, 1001, 1001, 1000);

INSERT IGNORE INTO detalle_matriculas (id_detalle, matricula_id, curso_prog_id, promedio_final, letra_final)
VALUES
  (1000, 1000, 1000, NULL, NULL),
  (1001, 1000, 1001, NULL, NULL),
  (1002, 1001, 1000, NULL, NULL),
  (1003, 1001, 1001, NULL, NULL),
  (1004, 1002, 1000, NULL, NULL),
  (1005, 1002, 1001, NULL, NULL),
  (1006, 1003, 1000, NULL, NULL),
  (1007, 1003, 1001, NULL, NULL);

INSERT IGNORE INTO horarios (id_horario, curso_prog_id, dia_semana, hora_inicio, hora_fin)
VALUES
  (1000, 1000, 'Lunes', '08:00', '08:45'),
  (1001, 1000, 'Miércoles', '08:00', '08:45'),
  (1002, 1001, 'Martes', '09:00', '09:45'),
  (1003, 1001, 'Jueves', '09:00', '09:45');
