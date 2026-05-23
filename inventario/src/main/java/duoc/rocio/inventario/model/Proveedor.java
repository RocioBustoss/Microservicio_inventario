package duoc.rocio.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedor")
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idProveedor;

    @NotBlank
    @Column(name="nombre", nullable = false)
    public String nombre;
    
    @NotBlank
    @Column(name="rut", nullable = false)
    public String rut;
    
    @NotBlank
    @Email
    @Column(name="correo", nullable = false)
    public String correo;

    @NotBlank
    @Column(name="telefono", nullable = false)
    public String telefono;
}

