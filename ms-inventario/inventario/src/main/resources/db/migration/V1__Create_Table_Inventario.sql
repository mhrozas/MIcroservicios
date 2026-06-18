CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ropa_id INT NOT NULL,
    cantidad INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    detalle VARCHAR(255) NOT NULL UNIQUE, 
    fecha_creacion DATETIME NOT NULL 
);