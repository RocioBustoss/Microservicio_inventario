package duoc.rocio.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRestockDTO {

    @Min(1)
    private int cantidadSolicitada;

    @NotNull
    private Long idSolicitante;
}