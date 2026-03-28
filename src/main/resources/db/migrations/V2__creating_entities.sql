CREATE TABLE IF NOT EXISTS public.users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255),
    telegram_id INTEGER,
    created_at TIMESTAMP,
    role VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS public.refresh_token (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL,
    token VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS public.email_token (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255),
    token VARCHAR(255),
    expiry_date TIMESTAMP
);