package duoc.rocio.inventario.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "restock")
public class Restock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRestock;

    @NotNull
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Min(1)
    @Column(name = "cantidad_solicitada", nullable = false)
    private int cantidadSolicitada;

    @NotBlank
    @Column(name = "estado_restock", nullable = false)
    private String estado;

    @NotNull
    @Column(name = "id_solicitante", nullable = false)
    private Long idSolicitante;

    @Column(name = "id_aprobador")
    private Long idAprobador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "inventario"})
    private ProductoInventario producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Proveedor proveedor;
}