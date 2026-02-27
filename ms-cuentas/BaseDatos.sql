-- ============================================================
-- CREAR TABLAS
-- ============================================================

CREATE TABLE IF NOT EXISTS clientes_info (
    id BIGSERIAL PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_clientes_info_cliente_id UNIQUE (cliente_id)
    );

CREATE INDEX IF NOT EXISTS idx_clientes_info_cliente_id ON clientes_info (cliente_id);


CREATE TABLE IF NOT EXISTS cuentas
(
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    saldo_disponible NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL,

    CONSTRAINT uq_cuentas_numero_cuenta UNIQUE (numero_cuenta)
);

CREATE INDEX IF NOT EXISTS idx_cuentas_numero_cuenta ON cuentas (numero_cuenta);
CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id ON cuentas (cliente_id);


CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT NOW(),
    tipo_movimiento VARCHAR(2),
    valor NUMERIC(15, 2) NOT NULL,
    saldo NUMERIC(15, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL,

    CONSTRAINT fk_movimientos_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuentas (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_id ON movimientos (cuenta_id);


-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================
INSERT INTO clientes_info (cliente_id, nombre, identificacion, estado)
VALUES
    ('CLI001', 'Jose Lema',            '12345678', TRUE),
    ('CLI002', 'Marianela Montalvo',   '87654321', TRUE),
    ('CLI003', 'Juan Osorio',          '11111111', FALSE)
    ON CONFLICT DO NOTHING;

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES ('478758', 'AHORROS', 2000.00, 2000.00, TRUE, 1),
       ('225487', 'CORRIENTE', 100.00, 100.00, TRUE, 2),
       ('495878', 'AHORROS', 0.00, 0.00, TRUE, 3),
       ('496825', 'AHORROS', 540.00, 540.00, TRUE, 2),
       ('585545', 'CORRIENTE', 1000.00, 1000.00, TRUE, 1) ON CONFLICT DO NOTHING;

INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo, cuenta_id)
VALUES (NOW(), 'DEBITO', -575.00, 1425.00, 1),
       (NOW(), 'CREDITO', 600.00, 700.00, 2),
       (NOW(), 'DEBITO', -100.00, 0.00, 3),
       (NOW(), 'CREDITO', 150.00, 690.00, 4),
       (NOW(), 'DEBITO', -540.00, 460.00, 5) ON CONFLICT DO NOTHING;
