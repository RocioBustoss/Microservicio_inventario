package duoc.rocio.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prod_restock")
public class ProdRestock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProdRestock;

    @NotBlank
    @Column(name = "nombre_producto", nullable = false)
    private String nombre;

    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;
}