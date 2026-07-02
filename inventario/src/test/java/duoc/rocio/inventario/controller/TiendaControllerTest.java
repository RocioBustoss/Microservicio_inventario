package duoc.rocio.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import duoc.rocio.inventario.model.Tienda;
import duoc.rocio.inventario.service.TiendaService;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TiendaController.class)
@ActiveProfiles("test")
public class TiendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TiendaService tiendaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void obtenerTiendas_ListaConDatos() throws Exception {
        Tienda t1 = new Tienda(1L, "Concepcion", "Desc 1", "8:00-18:00", "Pol 1");
        Tienda t2 = new Tienda(2L, "Talcahuano", "Desc 2", "8:00-18:00", "Pol 2");

        Mockito.when(tiendaService.getTiendas()).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/api/ecomarket/v1/tiendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombreTie", is("Concepcion")))
                .andExpect(jsonPath("$[1].nombreTie", is("Talcahuano")));
    }

    @Test
    void obtenerTiendas_ListaVacia() throws Exception {
        Mockito.when(tiendaService.getTiendas()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/ecomarket/v1/tiendas"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No existen inventarios registrados en el sistema"));
    }

    @Test
    void obtenerTiendaPorId_Existe() throws Exception {
        Tienda t1 = new Tienda(1L, "Concepcion", "Desc 1", "8:00-18:00", "Pol 1");

        Mockito.when(tiendaService.getTieById(1L)).thenReturn(Optional.of(t1));

        mockMvc.perform(get("/api/ecomarket/v1/tiendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTienda").value(1L))
                .andExpect(jsonPath("$.nombreTie").value("Concepcion"));
    }

    @Test
    void obtenerTiendaPorId_NoExiste() throws Exception {
        Mockito.when(tiendaService.getTieById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/tiendas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tienda no encontrada"));
    }

    @Test
    void guardarTienda_Exito() throws Exception {
        Tienda nueva = new Tienda(null, "Chiguayante", "Desc", "8:00-18:00", "Pol");
        Tienda guardada = new Tienda(5L, "Chiguayante", "Desc", "8:00-18:00", "Pol");

        Mockito.when(tiendaService.guardarTie(any(Tienda.class))).thenReturn(guardada);

        mockMvc.perform(post("/api/ecomarket/v1/tiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(5L))
                .andExpect(jsonPath("$.nombreTie").value("Chiguayante"));
    }

    @Test
    void eliminarTienda_Exito() throws Exception {
        Mockito.when(tiendaService.eliminarTie(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/ecomarket/v1/tiendas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tienda eliminada correctamente"));
    }
}