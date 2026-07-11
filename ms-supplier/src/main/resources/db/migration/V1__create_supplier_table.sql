CREATE TABLE supplier (
    id          UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    rut         VARCHAR(20)  NOT NULL UNIQUE,
    contact_name VARCHAR(150),
    phone       VARCHAR(30),
    email       VARCHAR(150),
    address     VARCHAR(255),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);
