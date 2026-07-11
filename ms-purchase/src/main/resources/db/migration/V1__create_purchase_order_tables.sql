CREATE TABLE purchase_order (
    id            UUID         DEFAULT gen_random_uuid() PRIMARY KEY,
    supplier_id   UUID         NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    confirmed_at  TIMESTAMP
);

CREATE TABLE purchase_order_item (
    id                 UUID          DEFAULT gen_random_uuid() PRIMARY KEY,
    purchase_order_id  UUID          NOT NULL REFERENCES purchase_order(id),
    product_id         UUID          NOT NULL,
    quantity           INTEGER       NOT NULL,
    unit_cost          NUMERIC(12,2) NOT NULL
);
