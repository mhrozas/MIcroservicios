package com.prueba.envio.Model.DTO;
import java.util.Date;

import lombok.Data;
@Data
public class PedidoDTO {
    private Integer id;
    private Date fecha;
    private String estado;
    private Double total;


}
