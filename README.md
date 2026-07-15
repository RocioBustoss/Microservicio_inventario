# Microservicio de Inventario - EcoMarket SPA

## Descripción del proyecto
El microservicio de Inventario administra el stock de productos, proveedores, tiendas y procesos de reposición. Permite mantener actualizada la disponibilidad de productos y entregar información al resto de los microservicios mediante APIs REST.

## Integrantes
- Dairys Bernal
- Rocio Bustos
- Amaru Burdiles

## Microservicios implementados
- API Gateway
- Usuarios
- Inventario
- Catálogo
- Cupones
- Pedidos
- Ventas
- Envíos
- Reportes y Soporte

## Rutas principales
Inventario
GET / POST
/api/ecomarket/v1/inventarios

Productos
GET / POST
/api/ecomarket/v1/productos

Proveedores
GET / POST
/api/ecomarket/v1/proveedores

Restock
GET / POST
/api/ecomarket/v1/restocks

Tiendas
GET / POST
/api/ecomarket/v1/tiendas


## Documentación Swagger
Swagger UI
http://localhost:8081/doc/swagger-ui.html


OpenAPI
http://localhost:8081/v3/api-docs


## Ejecución local
1. Clonar el repositorio.
2. Configurar la base de datos MySQL correspondiente.
3. Ejecutar el proyecto con Spring Boot.
4. Verificar que el servicio esté disponible en el puerto **8081**.
5. Acceder a Swagger para probar los endpoints.


## Ejecución mediante API Gateway
http://localhost:8080/api/ecomarket/v1/inventarios

