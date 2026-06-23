
CREATE TABLE direcciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    calle VARCHAR(255) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL
);


CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono INT NOT NULL,               
    pedido_id INT NOT NULL,              
    direccion_id INT NULL,               
    CONSTRAINT fk_usuario_direccion FOREIGN KEY (direccion_id) REFERENCES direcciones(id) ON DELETE SET NULL,
    CONSTRAINT uq_direccion UNIQUE (direccion_id)
);