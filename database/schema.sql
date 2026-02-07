-- Legal Case Management System - Database Schema
-- PostgreSQL Database Initialization Script

-- Create database (run as postgres superuser)
-- CREATE DATABASE legal_cms_db;

-- Connect to database
-- \c legal_cms_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'ADVOCATE', 'CLIENT')),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Cases table
CREATE TABLE IF NOT EXISTS cases (
    id BIGSERIAL PRIMARY KEY,
    case_title VARCHAR(500) NOT NULL,
    case_type VARCHAR(100) NOT NULL,
    case_number VARCHAR(100) NOT NULL UNIQUE,
    cnr_number VARCHAR(100) UNIQUE,
    court_name VARCHAR(255) NOT NULL,
    next_hearing_date DATE,
    case_stage VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'DISPOSED')),
    assigned_advocate_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Parties table
CREATE TABLE IF NOT EXISTS parties (
    id BIGSERIAL PRIMARY KEY,
    case_id BIGINT NOT NULL REFERENCES cases(id) ON DELETE CASCADE,
    petitioner_name VARCHAR(500) NOT NULL,
    respondent_name VARCHAR(500) NOT NULL
);

-- Hearings table
CREATE TABLE IF NOT EXISTS hearings (
    id BIGSERIAL PRIMARY KEY,
    case_id BIGINT NOT NULL REFERENCES cases(id) ON DELETE CASCADE,
    hearing_date DATE NOT NULL,
    stage VARCHAR(255),
    remarks TEXT
);

-- Documents table
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    case_id BIGINT NOT NULL REFERENCES cases(id) ON DELETE CASCADE,
    file_name VARCHAR(500) NOT NULL,
    file_url VARCHAR(1000) NOT NULL,
    document_type VARCHAR(50) NOT NULL CHECK (document_type IN ('ORDER', 'PETITION', 'JUDGMENT')),
    uploaded_by_id BIGINT NOT NULL REFERENCES users(id),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_cases_case_number ON cases(case_number);
CREATE INDEX idx_cases_cnr_number ON cases(cnr_number);
CREATE INDEX idx_cases_assigned_advocate ON cases(assigned_advocate_id);
CREATE INDEX idx_cases_next_hearing_date ON cases(next_hearing_date);
CREATE INDEX idx_parties_case_id ON parties(case_id);
CREATE INDEX idx_hearings_case_id ON hearings(case_id);
CREATE INDEX idx_documents_case_id ON documents(case_id);
CREATE INDEX idx_users_email ON users(email);

-- Insert default admin user
-- Password: admin123 (BCrypt hash)
INSERT INTO users (full_name, email, password, role, is_active, created_at, updated_at)
VALUES (
    'System Admin',
    'admin@legalcms.com',
    '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6',
    'ADMIN',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Sample advocate user
-- Password: advocate123
INSERT INTO users (full_name, email, password, role, is_active, created_at, updated_at)
VALUES (
    'Rajesh Kumar',
    'rajesh@legalcms.com',
    '$2a$10$8K1p/h/lFfJtN.HCEhYXVeT9f4cVEF1lYHkYIJZ4gZMHWKQJWzZQi',
    'ADVOCATE',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Sample client user
-- Password: client123
INSERT INTO users (full_name, email, password, role, is_active, created_at, updated_at)
VALUES (
    'Priya Sharma',
    'priya@example.com',
    '$2a$10$rMfMqKCXHtDc5v5zTl.5m.uWF4lQKZN3tGG5LQ6Bh4gR4MVqEDEuC',
    'CLIENT',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (email) DO NOTHING;

-- Grant permissions (if needed)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_user;
