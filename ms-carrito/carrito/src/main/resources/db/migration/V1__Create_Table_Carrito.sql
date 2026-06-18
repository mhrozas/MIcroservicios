
CREATE TABLE carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_articulos INT NOT NULL,
    total DOUBLE NOT NULL,
    usuario_id INT NOT NULL
);

CREATE TABLE carrito_ropa_ids (
    carrito_id INT NOT NULL,
    ropa_id INT NOT NULL,
    CONSTRAINT fk_carrito_ropa FOREIGN KEY (carrito_id) REFERENCES carrito(id) ON DELETE CASCADE
);