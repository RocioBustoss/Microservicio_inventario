package duoc.rocio.inventario.model;

import java.time.LocalDateTime;

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
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @NotBlank
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipoMovimiento;

    @Min(1)
    @Column(name = "cantidad_movimiento", nullable = false)
    private int cantidad;

    @NotNull
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fecha;

    @NotBlank
    @Column(name = "motivo_movimiento", nullable = false)
    private String motivo;

    @Min(0)
    @Column(name = "stock_anterior", nullable = false)
    private int stockAnterior;

    @Min(0)
    @Column(name = "stock_posterior", nullable = false)
    private int stockPosterior;

    @NotNull
    @Column(name = "id_responsable", nullable = false)
    private Long idResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "inventario"})
    private ProductoInventario producto;
}