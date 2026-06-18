package com.prueba.inventario.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Entity 
@Table(name = "inventario")
public class Inventario {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer ropaId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String detalle;

    @Column(nullable = false)
    private Date fechaCreacion;

}
