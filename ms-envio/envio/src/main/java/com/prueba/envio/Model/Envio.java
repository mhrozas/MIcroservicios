package com.prueba.envio.Model;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Entity 
@Table(name = "envio")
public class Envio {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer pedidoId;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Date fechaEntrega;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column
    private String trackingCode;


}
