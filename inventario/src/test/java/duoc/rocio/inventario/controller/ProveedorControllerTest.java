package duoc.rocio.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import duoc.rocio.inventario.model.Proveedor;
import duoc.rocio.inventario.service.ProveedorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProveedorController.class)
@ActiveProfiles("test")
public class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProveedorService proveedorService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void obtenerProveedores_ListaConDatos() throws Exception {
        Proveedor p1 = new Proveedor(1L, "Proveedor A", "11111111-1", "contacto@a.cl", "987654321");
        Proveedor p2 = new Proveedor(2L, "Proveedor B", "22222222-2", "contacto@b.cl", "912345678");

        Mockito.when(proveedorService.getProveedores()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/ecomarket/v1/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Proveedor A")))
                .andExpect(jsonPath("$[1].rut", is("22222222-2")));
    }

    @Test
    void obtenerProveedores_ListaVacia() throws Exception {
        Mockito.when(proveedorService.getProveedores()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/ecomarket/v1/proveedores"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No existen proveedores registrados"));
    }

    @Test
    void obtenerProveedorPorId_Existe() throws Exception {
        Proveedor p = new Proveedor(1L, "Proveedor A", "11111111-1", "contacto@a.cl", "987654321");

        Mockito.when(proveedorService.getProvById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/ecomarket/v1/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProveedor").value(1L))
                .andExpect(jsonPath("$.nombre").value("Proveedor A"));
    }

    @Test
    void obtenerProveedorPorId_NoExiste() throws Exception {
        Mockito.when(proveedorService.getProvById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/proveedores/99"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Proveedor no encontrado"));
    }

    @Test
    void guardarProveedor_Exito() throws Exception {
        Proveedor nuevo = new Proveedor(null, "Proveedor Nuevo", "33333333-3", "nuevo@prov.cl", "999888777");

        Mockito.when(proveedorService.guardarProv(any(Proveedor.class))).thenReturn(0);

        mockMvc.perform(post("/api/ecomarket/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Proveedor registrado correctamente"));
    }

    @Test
    void guardarProveedor_Conflicto() throws Exception {
        Proveedor nuevo = new Proveedor(null, "Proveedor Existente", "11111111-1", "existe@prov.cl", "999888777");

        Mockito.when(proveedorService.guardarProv(any(Proveedor.class))).thenReturn(1);

        mockMvc.perform(post("/api/ecomarket/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isConflict())
                .andExpect(content().string("El proveedor ya se encuentra registrado"));
    }

    @Test
    void actualizarProveedor_Exito() throws Exception {
        Proveedor actualizado = new Proveedor(null, "Proveedor Editado", "11111111-1", "edit@prov.cl", "111222333");

        Mockito.when(proveedorService.actualizarProveedor(eq(1L), any(Proveedor.class))).thenReturn(true);

        mockMvc.perform(put("/api/ecomarket/v1/proveedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(content().string("Proveedor actualizado correctamente"));
    }

    @Test
    void actualizarProveedor_NoEncontrado() throws Exception {
        Proveedor actualizado = new Proveedor(null, "Proveedor Editado", "11111111-1", "edit@prov.cl", "111222333");

        Mockito.when(proveedorService.actualizarProveedor(eq(99L), any(Proveedor.class))).thenReturn(false);

        mockMvc.perform(put("/api/ecomarket/v1/proveedores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Proveedor no encontrado"));
    }

    @Test
    void eliminarProveedor_Exito() throws Exception {
        Mockito.when(proveedorService.eliminarProv(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/ecomarket/v1/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Proveedor eliminado correctamente"));
    }

    @Test
    void eliminarProveedor_NoEncontrado() throws Exception {
        Mockito.when(proveedorService.eliminarProv(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/ecomarket/v1/proveedores/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Proveedor no encontrado"));
    }
}