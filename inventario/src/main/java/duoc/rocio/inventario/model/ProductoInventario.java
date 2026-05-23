package duoc.rocio.inventario.model;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto_inventario")
public class ProductoInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;
    
    @NotBlank
    @Column(name="nombre_producto_inventario", nullable = false)
    private String nombreProInv;


    @NotBlank
    @Column(name="codigo_sku", nullable = false)
    private String codigoSku;

    @NotNull
    @Column(name = "fecha_caducidad_producto", nullable = false)
    private LocalDate fechaCaducidad;

    @Min(0)
    @Column(name = "stock_actual_producto", nullable = false)
    private int stockActual;
    
    @NotBlank
    @Column(name = "estado_producto", nullable = false)
    private String estadoProd;


}
