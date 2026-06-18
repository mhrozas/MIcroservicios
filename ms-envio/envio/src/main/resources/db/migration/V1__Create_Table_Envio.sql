CREATE TABLE envio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    fecha_entrega DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    tracking_code VARCHAR(100) NULL 
);