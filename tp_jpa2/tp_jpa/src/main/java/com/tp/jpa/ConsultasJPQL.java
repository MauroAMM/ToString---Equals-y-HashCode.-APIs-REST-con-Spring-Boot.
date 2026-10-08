package com.tp.jpa;

import java.time.LocalDate;
import java.util.List;

import com.tp.jpa.entities.Articulo;
import com.tp.jpa.entities.Cliente;
import com.tp.jpa.entities.FacturaVenta;
import com.tp.jpa.entities.FacturaVentaDetalle;
import com.tp.jpa.entities.Marca;
import com.tp.jpa.entities.PuntoVenta;

import jakarta.persistence.EntityManager;

public class ConsultasJPQL {

    // --- NIVEL 1: Consultas Básicas y Proyecciones ---

    // 1. Obtener la lista completa de todas las facturas
    public static void ej01_todasLasFacturas(EntityManager em) {
        String jpql = "SELECT f FROM FacturaVenta f";
        List<FacturaVenta> facturas = em.createQuery(jpql, FacturaVenta.class).getResultList();
        System.out.println("--- EJ 1: Total Facturas: " + facturas.size());
    }

    // 2. Proyección de número, fecha e importe
    public static void ej02_proyeccionFacturas(EntityManager em) {
        String jpql = "SELECT f.numero, f.fechaEmision, f.importeTotal FROM FacturaVenta f";
        List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();
        for (Object[] fila : resultados) {
            System.out.println("Nro: " + fila[0] + " | Fecha: " + fila[1] + " | Total: " + fila[2]);
        }
    }

    // 3. Filtrado por igual en Rubro (ej. "Tecnología")
    public static void ej03_articulosPorRubro(EntityManager em, String rubro) {
        String jpql = "SELECT a FROM Articulo a WHERE a.rubro.denominacion = :rubroNombre";
        List<Articulo> articulos = em.createQuery(jpql, Articulo.class)
                .setParameter("rubroNombre", rubro)
                .getResultList();
        articulos.forEach(a -> System.out.println("Articulo: " + a.getDenominacion()));
    }

    // 4. Filtrado por Rango de Fechas (BETWEEN)
    public static void ej04_facturasPorRangoFechas(EntityManager em, LocalDate desde, LocalDate hasta) {
        java.util.Date desdeDate = java.sql.Date.valueOf(desde);
        java.util.Date hastaDate = java.sql.Date.valueOf(hasta);
        
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.fechaEmision BETWEEN :desde AND :hasta";
        List<FacturaVenta> facturas = em.createQuery(jpql, FacturaVenta.class)
                .setParameter("desde", desdeDate)
                .setParameter("hasta", hastaDate)
                .getResultList();
        System.out.println("Facturas en rango: " + facturas.size());
    }

    // --- NIVEL 2: Condicionales, Texto y Agregaciones ---

    // 5. AND, OR, IS NULL
    public static void ej05_facturasEmitidasValidas(EntityManager em) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.estado = 'EMITIDA' AND f.importeTotal > 10000 AND f.fechaAnulacion IS NULL";
        List<FacturaVenta> res = em.createQuery(jpql, FacturaVenta.class).getResultList();
        System.out.println("Facturas emitidas mayores a 10k: " + res.size());
    }

    // 6. Búsqueda por Texto (LIKE y LOWER)
    public static void ej06_buscarClientes(EntityManager em, String textoParcial) {
        String jpql = "SELECT c FROM Cliente c WHERE LOWER(c.denominacion) LIKE :texto OR c.cuitCuil LIKE '20-%'";
        List<Cliente> clientes = em.createQuery(jpql, Cliente.class)
                .setParameter("texto", "%" + textoParcial.toLowerCase() + "%")
                .getResultList();
        clientes.forEach(c -> System.out.println("Cliente: " + c.getDenominacion()));
    }

    // 7. DISTINCT y ORDER BY
    public static void ej07_estadosSinDuplicados(EntityManager em) {
        String jpql = "SELECT DISTINCT f.estado FROM FacturaVenta f ORDER BY f.estado ASC";
        List<String> estados = em.createQuery(jpql, String.class).getResultList();
        System.out.println("Estados disponibles: " + estados);
    }

    // 8. Agregación (COUNT, SUM, AVG)
    public static void ej08_estadisticasFacturas(EntityManager em) {
        String jpql = "SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal) FROM FacturaVenta f";
        Object[] res = em.createQuery(jpql, Object[].class).getSingleResult();
        System.out.println("Total: " + res[0] + " | Suma: " + res[1] + " | Promedio: " + res[2]);
    }

    // 9. Operador IN
    public static void ej09_puntosVentaIn(EntityManager em, List<Integer> numeros) {
        String jpql = "SELECT pv FROM PuntoVenta pv WHERE pv.numero IN :listaNumeros";
        List<PuntoVenta> pvs = em.createQuery(jpql, PuntoVenta.class)
                .setParameter("listaNumeros", numeros)
                .getResultList();
        pvs.forEach(pv -> System.out.println("Punto de venta: " + pv.getNumero()));
    }

    // --- NIVEL 3: Navegación, JOINs y Subconsultas ---

    // 10. Navegación Implícita (Path Expressions)
    public static void ej10_facturasPorUsuario(EntityManager em, String username) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.usuarioCarga.usuario = :username";
        List<FacturaVenta> res = em.createQuery(jpql, FacturaVenta.class)
                .setParameter("username", username)
                .getResultList();
        System.out.println("Facturas del usuario: " + res.size());
    }

    // 11. INNER JOIN Explícito
    public static void ej11_detallesPorPuntoVenta(EntityManager em, Integer nroPuntoVenta) {
        String jpql = "SELECT d FROM FacturaVentaDetalle d JOIN d.facturaVenta f WHERE f.puntoVenta.numero = :pv";
        List<FacturaVentaDetalle> detalles = em.createQuery(jpql, FacturaVentaDetalle.class)
                .setParameter("pv", nroPuntoVenta)
                .getResultList();
        System.out.println("Detalles encontrados: " + detalles.size());
    }

    // 12. LEFT JOIN (Incluir sin marca)
    public static void ej12_articulosYMarcas(EntityManager em) {
        String jpql = "SELECT a.denominacion, m.denominacion FROM Articulo a LEFT JOIN a.marca m";
        List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();
        for (Object[] fila : resultados) {
            System.out.println("Artículo: " + fila[0] + " | Marca: " + (fila[1] != null ? fila[1] : "SIN MARCA"));
        }
    }

    // 13. JOINs Combinados
    public static void ej13_facturasPorMarcaArticulo(EntityManager em, String nombreMarca) {
        String jpql = "SELECT DISTINCT f FROM FacturaVenta f JOIN f.detalles d JOIN d.listaPrecioArticulo lpa JOIN lpa.articulo a JOIN a.marca m WHERE m.denominacion = :marca";
        List<FacturaVenta> res = em.createQuery(jpql, FacturaVenta.class)
                .setParameter("marca", nombreMarca)
                .getResultList();
        System.out.println("Facturas con marca " + nombreMarca + ": " + res.size());
    }

    // 14. Subconsulta en WHERE (Mayor al promedio)
    public static void ej14_facturasSobreElPromedio(EntityManager em) {
        String jpql = "SELECT f FROM FacturaVenta f WHERE f.importeTotal > (SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)";
        List<FacturaVenta> res = em.createQuery(jpql, FacturaVenta.class).getResultList();
        System.out.println("Facturas por encima del promedio: " + res.size());
    }

    // --- NIVEL 4: GROUP BY y HAVING ---

    // 15. GROUP BY básico
    public static void ej15_resumenPorPuntoVenta(EntityManager em) {
        String jpql = "SELECT pv.descripcion, COUNT(f), SUM(f.importeTotal) FROM FacturaVenta f JOIN f.puntoVenta pv GROUP BY pv.descripcion";
        List<Object[]> res = em.createQuery(jpql, Object[].class).getResultList();
        for (Object[] fila : res) {
            System.out.println("PV: " + fila[0] + " | Cant: " + fila[1] + " | Suma: " + fila[2]);
        }
    }

    // 16. GROUP BY con HAVING (> 5 facturas)
    public static void ej16_usuariosConMasDe5Facturas(EntityManager em) {
        String jpql = "SELECT u.usuario FROM FacturaVenta f JOIN f.usuarioCarga u GROUP BY u.usuario HAVING COUNT(f) > 5";
        List<String> usuarios = em.createQuery(jpql, String.class).getResultList();
        System.out.println("Usuarios con > 5 facturas: " + usuarios);
    }

    // 17. Agrupamiento por Entidad Relacionada (Marcas)
    public static void ej17_ventasPorMarca(EntityManager em) {
        String jpql = "SELECT m.denominacion, SUM(d.cantidad), SUM(d.importeSubtotal) " +
                      "FROM FacturaVentaDetalle d JOIN d.listaPrecioArticulo lpa JOIN lpa.articulo a JOIN a.marca m GROUP BY m.denominacion";
        List<Object[]> res = em.createQuery(jpql, Object[].class).getResultList();
        for (Object[] fila : res) {
            System.out.println("Marca: " + fila[0] + " | Unidades: " + fila[1] + " | Subtotal: " + fila[2]);
        }
    }

    // --- NIVEL 5: EXISTS, NOT EXISTS y CASE WHEN ---

    // 18. EXISTS (Marcas con artículos vendidos)
    public static void ej18_marcasVendidas(EntityManager em) {
        String jpql = "SELECT m FROM Marca m WHERE EXISTS (" +
                      "SELECT d FROM FacturaVentaDetalle d JOIN d.listaPrecioArticulo lpa JOIN lpa.articulo a WHERE a.marca = m)";
        List<Marca> marcas = em.createQuery(jpql, Marca.class).getResultList();
        marcas.forEach(m -> System.out.println("Marca vendida: " + m.getDenominacion()));
    }

    // 19. NOT EXISTS (Artículos nunca facturados)
    public static void ej19_articulosSinVender(EntityManager em) {
        String jpql = "SELECT a FROM Articulo a WHERE NOT EXISTS (" +
                      "SELECT d FROM FacturaVentaDetalle d JOIN d.listaPrecioArticulo lpa WHERE lpa.articulo = a)";
        List<Articulo> articulos = em.createQuery(jpql, Articulo.class).getResultList();
        articulos.forEach(a -> System.out.println("Sin vender: " + a.getDenominacion()));
    }

    // 20. Proyección Condicional (CASE WHEN)
    public static void ej20_clasificacionFacturas(EntityManager em) {
        String jpql = "SELECT f.numero, f.importeTotal, " +
                      "CASE WHEN f.importeTotal > 50000 THEN 'ALTO VALOR' " +
                      "     WHEN f.importeTotal BETWEEN 10000 AND 50000 THEN 'MEDIO VALOR' " +
                      "     ELSE 'BAJO VALOR' END " +
                      "FROM FacturaVenta f ORDER BY f.importeTotal DESC";
        List<Object[]> res = em.createQuery(jpql, Object[].class).getResultList();
        for (Object[] fila : res) {
            System.out.println("Nro: " + fila[0] + " | Total: $" + fila[1] + " | Categoría: " + fila[2]);
        }
    }
}