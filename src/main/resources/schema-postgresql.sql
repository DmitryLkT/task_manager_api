DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(250) NOT NULL UNIQUE
                  CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    password_hash VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
                    CHECK (name IN ('ROLE_USER', 'ROLE_ADMIN'))
);

CREATE TABLE user_roles (
    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX idx_user_roles_role_id ON user_roles(role_id);

CREATE TABLE projects (
    id SERIAL PRIMARY KEY,
    title VARCHAR(75) DEFAULT 'New Project',
    description VARCHAR(350) NOT NULL,
    createdAt TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    owner INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_projects_user_id ON projects(owner);

CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(75) DEFAULT 'New Task',
    description VARCHAR(350) NOT NULL,
    status VARCHAR(20) NOT NULL
        CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    project INT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    assignedTo INT REFERENCES users(id) ON DELETE SET NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tasks_project ON tasks(project);
CREATE INDEX idx_tasks_assignedTo ON tasks(assignedTo);