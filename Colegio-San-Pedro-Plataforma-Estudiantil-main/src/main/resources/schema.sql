CREATE TABLE IF NOT EXISTS horarios (
  id_horario INT AUTO_INCREMENT PRIMARY KEY,
  curso_prog_id INT NOT NULL,
  dia_semana VARCHAR(20) NOT NULL,
  hora_inicio VARCHAR(10) NOT NULL,
  hora_fin VARCHAR(10) NOT NULL,
  estado VARCHAR(20) DEFAULT 'Activo',
  CONSTRAINT fk_horario_curso_programado FOREIGN KEY (curso_prog_id)
    REFERENCES cursos_programados(id_curso_prog)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
