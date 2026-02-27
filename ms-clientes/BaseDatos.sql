-- ============================================================
-- CREAR TABLAS
-- ============================================================

CREATE TABLE IF NOT EXISTS clientes
(
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo_documento VARCHAR(2) NOT NULL,
    genero VARCHAR(1) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(20) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    cliente_id VARCHAR(50) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_clientes_cliente_id UNIQUE (cliente_id),
    CONSTRAINT uq_clientes_identificacion UNIQUE (identificacion)
);

CREATE INDEX IF NOT EXISTS idx_clientes_cliente_id ON clientes (cliente_id);
CREATE INDEX IF NOT EXISTS idx_clientes_identificacion ON clientes (identificacion);

-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================

INSERT INTO clientes (nombre, tipo_documento, genero, edad, identificacion, direccion, telefono, cliente_id, contrasena, estado)
VALUES ('Jose Lema', 'CC', 'M', 20, '12345678', 'Otavalo sn y principal', '098254785', 'CLI001', '1234', TRUE),
       ('Marianela Montalvo', 'CC', 'F', 31,'87654321', 'Amazonas y NNUU', '097548965', 'CLI002', '5678', TRUE),
       ('Juan Osorio', 'PS', 'M',40, 28, '13 junio y Equinoccial', '098874587', 'CLI003', '1245', FALSE)
ON CONFLICT DO NOTHING;
