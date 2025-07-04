CREATE TABLE kubunchi (
  kubun_cd VARCHAR(4) NOT NULL,
  kubunchi_cd VARCHAR(4),
  kubun_name VARCHAR(30),
  kubunchi_name VARCHAR(30),
  version SMALLINT,
  deleted_flag SMALLINT DEFAULT 0,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  UNIQUE KEY constraint_1 (kubun_cd, kubunchi_cd)
);