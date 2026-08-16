-- Flyway migration naming convention: V<version>__<description>.sql
-- Never edit a migration once it has run against any shared environment (DEV/UAT/PROD) —
-- write a new V<n+1> migration instead. Flyway checksums existing files and will fail
-- the build if a previously-applied file changes.

CREATE TABLE customer (
    id          VARCHAR(36)  PRIMARY KEY,
    full_name   VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_customer_email ON customer (email);
