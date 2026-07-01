package duoc.rocio.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import duoc.rocio.inventario.model.Inventario;
import duoc.rocio.inventario.model.ProductoInventario;
import duoc.rocio.inventario.service.ProductoInventarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoInventarioController.class)
@ActiveProfiles("test")
public class ProductoInventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoInventarioService productoInventarioService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void agregarProducto_Exito() throws Exception {
        ProductoInventario p = new ProductoInventario(null, "Quix", "QX-001", LocalDate.now(), 50, null);
        Mockito.when(productoInventarioService.agregarProd(eq(1L), any(ProductoInventario.class))).thenReturn(0);

        mockMvc.perform(post("/api/ecomarket/v1/productos/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Producto agregado al inventario correctamente"));
    }

    @Test
    void agregarProducto_InventarioNoEncontrado() throws Exception {
        ProductoInventario p = new ProductoInventario(null, "Quix", "QX-001", LocalDate.now(), 50, null);
        Mockito.when(productoInventarioService.agregarProd(eq(1L), any(ProductoInventario.class))).thenReturn(1);

        mockMvc.perform(post("/api/ecomarket/v1/productos/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Inventario no encontrado"));
    }

    @Test
    void agregarProducto_Conflicto() throws Exception {
        ProductoInventario p = new ProductoInventario(null, "Quix", "QX-001", LocalDate.now(), 50, null);
        Mockito.when(productoInventarioService.agregarProd(eq(1L), any(ProductoInventario.class))).thenReturn(2);

        mockMvc.perform(post("/api/ecomarket/v1/productos/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isConflict())
                .andExpect(content().string("El producto ya existe en este inventario"));
    }

    @Test
    void obtenerProductosPorInventario_Exito() throws Exception {
        ProductoInventario p1 = new ProductoInventario(1L, "Jabon", "JB-01", LocalDate.now(), 10, new Inventario());
        ProductoInventario p2 = new ProductoInventario(2L, "Shampoo", "SH-01", LocalDate.now(), 20, new Inventario());

        Mockito.when(productoInventarioService.getProductosByInv(1L)).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Jabon")))
                .andExpect(jsonPath("$[1].stock", is(20)));
    }

    @Test
    void obtenerProductoDeInventario_Existe() throws Exception {
        ProductoInventario p1 = new ProductoInventario(1L, "Jabon", "JB-01", LocalDate.now(), 10, null);
        Mockito.when(productoInventarioService.getProductoByInvAndId(1L, 1L)).thenReturn(Optional.of(p1));

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProd", is("Jabon")));
    }

    @Test
    void obtenerProductoDeInventario_NoExiste() throws Exception {
        Mockito.when(productoInventarioService.getProductoByInvAndId(1L, 1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/producto/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado en el inventario especificado"));
    }

    @Test
    void buscarPorNombre_Exito() throws Exception {
        ProductoInventario p1 = new ProductoInventario(1L, "Quix Limón", "QX-02", LocalDate.now(), 15, null);
        Mockito.when(productoInventarioService.findByNombre(1L, "Quix")).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/buscarNombre").param("nombre", "Quix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProd", is("Quix Limón")));
    }

    @Test
    void buscarPorNombre_NoContent() throws Exception {
        Mockito.when(productoInventarioService.findByNombre(1L, "Fantasma")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/buscarNombre").param("nombre", "Fantasma"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No existen productos con el nombre solicitado en este inventario"));
    }

    @Test
    void filtrarStockBajo_Exito() throws Exception {
        ProductoInventario p1 = new ProductoInventario(1L, "Cloro", "CL-01", LocalDate.now(), 2, new Inventario());
        Mockito.when(productoInventarioService.buscarStockBajo(1L, 5)).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/stock-bajo/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Cloro")))
                .andExpect(jsonPath("$[0].stock", is(2)));
    }

    @Test
    void existeProductoPorId_True() throws Exception {
        Mockito.when(productoInventarioService.getProdById(1L)).thenReturn(Optional.of(new ProductoInventario()));

        mockMvc.perform(get("/api/ecomarket/v1/productos/1/conexion"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void existeProductoPorId_False() throws Exception {
        Mockito.when(productoInventarioService.getProdById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/productos/99/conexion"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void obtenerProductoPorId_Existe() throws Exception {
        ProductoInventario p = new ProductoInventario(1L, "Jabon Liquido", "JB-02", LocalDate.now(), 40, null);
        Mockito.when(productoInventarioService.getProdById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/ecomarket/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProd", is("Jabon Liquido")));
    }

    @Test
    void obtenerProductoPorId_NoExiste() throws Exception {
        Mockito.when(productoInventarioService.getProdById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/productos/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado"));
    }

    @Test
    void consultarStock_Existe() throws Exception {
        Mockito.when(productoInventarioService.consultarStock(1L, 1L)).thenReturn(Optional.of(25));

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/producto/1/stock"))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));
    }

    @Test
    void consultarStock_NoExiste() throws Exception {
        Mockito.when(productoInventarioService.consultarStock(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/ecomarket/v1/productos/inventario/1/producto/1/stock"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado"));
    }

    @Test
    void actualizarStock_Exito() throws Exception {
        Mockito.when(productoInventarioService.actualizarStock(1L, 1L, 20)).thenReturn(0);

        mockMvc.perform(put("/api/ecomarket/v1/productos/inventario/1/producto/1/stock")
                        .param("cantidad", "20"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock actualizado correctamente"));
    }

    @Test
    void actualizarStock_NoEncontrado() throws Exception {
        Mockito.when(productoInventarioService.actualizarStock(1L, 1L, 20)).thenReturn(1);

        mockMvc.perform(put("/api/ecomarket/v1/productos/inventario/1/producto/1/stock")
                        .param("cantidad", "20"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarStock_ErrorCantidadMenorQueCero() throws Exception {
        Mockito.when(productoInventarioService.actualizarStock(1L, 1L, -5)).thenReturn(2);

        mockMvc.perform(put("/api/ecomarket/v1/productos/inventario/1/producto/1/stock")
                        .param("cantidad", "-5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El stock a sumar no puede ser menor que cero"));
    }

    @Test
    void actualizarStock_ErrorCantidadIngresadaConflicto() throws Exception {
        Mockito.when(productoInventarioService.actualizarStock(1L, 1L, 0)).thenReturn(3);

        mockMvc.perform(put("/api/ecomarket/v1/productos/inventario/1/producto/1/stock")
                        .param("cantidad", "0"))
                .andExpect(status().isConflict())
                .andExpect(content().string("La cantidad ingresada"));
    }

    @Test
    void eliminarProductoSistema_Exito() throws Exception {
        Mockito.when(productoInventarioService.eliminarProducto(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/ecomarket/v1/productos/inventario/1/producto/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Producto eliminado correctamente"));
    }

    @Test
    void eliminarProductoSistema_NoEncontrado() throws Exception {
        Mockito.when(productoInventarioService.eliminarProducto(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/ecomarket/v1/productos/inventario/1/producto/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Producto no encontrado"));
    }
}