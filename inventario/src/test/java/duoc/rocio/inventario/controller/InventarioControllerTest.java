package duoc.rocio.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import duoc.rocio.inventario.dto.InventarioResumenDTO;
import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.service.InventarioService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioController.class)
@ActiveProfiles("test")
public class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void obtenerInventarios_ListaConDatos() throws Exception {
        InventarioResumenDTO inv1 = new InventarioResumenDTO(1L, "Sede Centro", "Inventario principal");
        InventarioResumenDTO inv2 = new InventarioResumenDTO(2L, "Sede Norte", "Inventario secundario");

        Mockito.when(inventarioService.getInventarios()).thenReturn(Arrays.asList(inv1, inv2));

        mockMvc.perform(get("/api/ecomarket/v1/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreInventario", is("Sede Centro")))
                .andExpect(jsonPath("$[1].descripcionInventario", is("Inventario secundario")));
    }

    @Test
    void obtenerInventarios_ListaVacia() throws Exception {
        Mockito.when(inventarioService.getInventarios()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/ecomarket/v1/inventarios"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No existen inventarios registrados en el sistema"));
    }

    @Test
    void obtenerInventarioPorId_Existe() throws Exception {
        Inventario inventarioBuscado = new Inventario(1L, "Sede Sur", "Inventario zona sur", new ArrayList<>());

        Mockito.when(inventarioService.getInvById(1L)).thenReturn(Optional.of(inventarioBuscado));

        mockMvc.perform(get("/api/ecomarket/v1/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInventario").value(1L))
                .andExpect(jsonPath("$.nombreInv").value("Sede Sur"))
                .andExpect(jsonPath("$.descripcionInv").value("Inventario zona sur"));
    }

    @Test
    void obtenerInventarioPorId_NoExiste() throws Exception {
        Mockito.when(inventarioService.getInvById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/inventarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Inventario no encontrado"));
    }

    @Test
    void guardarInventario_Exito() throws Exception {
        Inventario inventarioNuevo = new Inventario(null, "Sede Este", "Inventario zona este", new ArrayList<>());
        Inventario inventarioGuardado = new Inventario(5L, "Sede Este", "Inventario zona este", new ArrayList<>());

        Mockito.when(inventarioService.guardarInv(any(Inventario.class))).thenReturn(inventarioGuardado);

        mockMvc.perform(post("/api/ecomarket/v1/inventarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioNuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInventario").value(5L))
                .andExpect(jsonPath("$.nombreInv").value("Sede Este"));
    }

    @Test
    void eliminarInventario_Exito() throws Exception {
        Mockito.when(inventarioService.eliminarInv(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/ecomarket/v1/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Inventario eliminado correctamente"));
    }
}