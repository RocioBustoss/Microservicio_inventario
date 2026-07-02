package duoc.rocio.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tienda")
public class Tienda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idTienda;
    
    @NotBlank
    @Column(name="nombre_tienda", nullable = false)
    public String nombreTie;
    
    @NotBlank
    @Column(name="descripcion_tienda", nullable = false)
    public String descripcionTie;

    @NotBlank
    @Column(name="horario_tienda", nullable = false)
    public String horarioTie;

    @NotBlank
    @Column(name="politicas", nullable = false)
    public String politicas;
}

