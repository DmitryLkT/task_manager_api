DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
                    CHECK (name IN ('USER', 'ADMIN'))
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(250) NOT NULL UNIQUE
                  CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    password_hash VARCHAR(255) NOT NULL UNIQUE,
    role_id INT NOT NULL REFERENCES roles(id)
);

CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    title VARCHAR(75) UNIQUE NOT NULL,
    description VARCHAR(500) NOT NULL,
    createdAt TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    owner INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_projects_user_id ON projects(owner);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(75) DEFAULT 'New Task',
    description VARCHAR(350),
    status VARCHAR(20) DEFAULT 'TODO'
        CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    project_id INT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tasks_project ON tasks(project_id);
CREATE INDEX idx_tasks_assignedTo ON tasks(user_id);