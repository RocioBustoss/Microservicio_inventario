package duoc.rocio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioResumenDTO {
    private Long idInventario;
    private String nombreInventario;
    private String descripcionInventario;
}
