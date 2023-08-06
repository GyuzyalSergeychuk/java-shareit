DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS item CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description VARCHAR(264) NOT NULL,
    requestor_id BIGINT REFERENCES users (id),
    created_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS item (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    item_name VARCHAR(64) NOT NULL,
    description VARCHAR(200) NOT NULL,
    is_available BOOLEAN DEFAULT false NOT NULL,
    user_id BIGINT REFERENCES users (id),
    request_id BIGINT REFERENCES requests (id),
    last_booking_id BIGINT,
    next_booking_id BIGINT
);

CREATE TABLE IF NOT EXISTS user_items (
    owner_id BIGINT REFERENCES users (id),
    items BIGINT REFERENCES item (id)
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    item_id BIGINT REFERENCES item (id),
    booker_id BIGINT REFERENCES users (id),
    status VARCHAR(350) DEFAULT false NOT NULL,
    item_owner_id BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR(350) NOT NULL,
    author_id BIGINT REFERENCES users (id),
    created TIMESTAMP WITH TIME ZONE,
    item_id BIGINT REFERENCES item (id)
);





