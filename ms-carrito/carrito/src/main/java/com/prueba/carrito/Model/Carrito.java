package com.prueba.carrito.Model;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Entity 
@Table(name = "carrito")
public class Carrito {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int  total_articulos;
    
     @Column(nullable = false)
    private Double total;


    @ElementCollection
    @CollectionTable(name = "carrito_ropa_ids", joinColumns = @JoinColumn(name = "carrito_id"))
    @Column(name = "ropa_id")
    private List<Integer> ropaIds;

    @Column(nullable = false)
    private Integer usuarioId; 

}
