CREATE TABLE employee (
  id BIGINT PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255),
  department VARCHAR(100),
  salary DECIMAL(15,2),
  ssn VARCHAR(20)
);

