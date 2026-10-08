package com.tp.jpa;


import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import com.tp.jpa.dto.FacturaReporteDTO;
import com.tp.jpa.entities.*;
import com.tp.jpa.service.FacturaReporteService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            cargarDatosPrueba(em);
            em.getTransaction().commit();

            System.out.println("\n=============================================");
            System.out.println("       EJECUTANDO CONSULTAS JPQL            ");
            System.out.println("=============================================\n");

            // --- EJECUCIÓN DE TUS CONSULTAS JPQL EXISTENTES ---
            // ConsultasJPQL.ej01_todasLasFacturas(em);
            // ... (puedes descomentar tus métodos a medida que los tengas implementados)

            // --- PRUEBA PARTE 2: CONSULTA DTO Y EXPORTACIÓN A PDF / EXCEL ---
            System.out.println("\n--- PARTE 2: GENERACIÓN DE REPORTE DTO Y ARCHIVOS ---");
            FacturaReporteService reporteService = new FacturaReporteService(em);
            List<FacturaReporteDTO> reporte = reporteService.obtenerReporteFacturas();

            for (FacturaReporteDTO dto : reporte) {
                System.out.println("Factura N°: " + dto.getNumeroFactura() +
                        " | Cliente: " + dto.getClienteDenominacion() +
                        " | Cond. IVA: " + dto.getCondicionIva() +
                        " | Total: $" + dto.getImporteTotal() +
                        " | Cant. Ítems: " + dto.getCantidadItems());
            }

            // Exportación a disco
            reporteService.exportarPDF(reporte, "Reporte_Facturas.pdf");
            reporteService.exportarExcelTSV(reporte, "Reporte_Facturas.xls");
            System.out.println("\n>>> Archivos 'Reporte_Facturas.pdf' y 'Reporte_Facturas.xls' generados exitosamente.");

            System.out.println("\n=============================================\n");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    private static void cargarDatosPrueba(EntityManager em) {
        Date ahora = new Date();

        // 1. Usuarios
        Usuario uAdmin = Usuario.builder()
                .username("admin")
                .password("1234")
                .nombre("Carlos")
                .apellido("Pérez")
                .email("admin@empresa.com")
                .activo(true)
                .build();
        uAdmin.setFechaAlta(ahora);
        uAdmin.setUsuarioAlta("system");
        em.persist(uAdmin);

        Usuario uVendedor = Usuario.builder()
                .username("vendedor1")
                .password("1234")
                .nombre("Ana")
                .apellido("Gómez")
                .email("vendedor@empresa.com")
                .activo(true)
                .build();
        uVendedor.setFechaAlta(ahora);
        uVendedor.setUsuarioAlta("system");
        em.persist(uVendedor);

        // 2. Puntos de Venta
        PuntoVenta pv1 = PuntoVenta.builder()
                .numero(1)
                .descripcion("Sucursal Central")
                .build();
        em.persist(pv1);

        PuntoVenta pv2 = PuntoVenta.builder()
                .numero(2)
                .descripcion("Sucursal Norte")
                .build();
        em.persist(pv2);

        // 3. Rubros
        Rubro rTecno = Rubro.builder().denominacion("Tecnología").build();
        rTecno.setFechaAlta(ahora);
        rTecno.setUsuarioAlta(uAdmin.getUsername());
        em.persist(rTecno);

        Rubro rHogar = Rubro.builder().denominacion("Hogar").build();
        rHogar.setFechaAlta(ahora);
        rHogar.setUsuarioAlta(uAdmin.getUsername());
        em.persist(rHogar);

        // 4. Marcas
        Marca mLogitech = Marca.builder().denominacion("Logitech").build();
        mLogitech.setFechaAlta(ahora);
        mLogitech.setUsuarioAlta(uAdmin.getUsername());
        em.persist(mLogitech);

        Marca mSamsung = Marca.builder().denominacion("Samsung").build();
        mSamsung.setFechaAlta(ahora);
        mSamsung.setUsuarioAlta(uAdmin.getUsername());
        em.persist(mSamsung);

        Marca mSony = Marca.builder().denominacion("Sony").build();
        mSony.setFechaAlta(ahora);
        mSony.setUsuarioAlta(uAdmin.getUsername());
        em.persist(mSony);

        // 5. Contacto, Domicilio, Condición IVA, Moneda y Cliente
        Contacto con = Contacto.builder()
                .email("contacto@cliente.com")
                .telefono("011-4444-5555")
                .build();

        Domicilio dom = Domicilio.builder()
                .calle("Calle Falsa")
                .numero("123")
                .localidad("Mendoza")
                .provincia("Mendoza")
                .build();

        CondicionIva condIva = CondicionIva.builder()
                .codigo("RI")
                .denominacion("Responsable Inscripto")
                .porcentaje(21.0)
                .build();
        em.persist(condIva);

        Cliente cli = Cliente.builder()
                .denominacion("Empresa Cliente S.A.")
                .cuit("20-30405060-8")
                .contacto(con)
                .domicilio(dom)
                .condicionIva(condIva)
                .build();
        cli.setFechaAlta(ahora);
        cli.setUsuarioAlta(uAdmin.getUsername());
        em.persist(cli);

        TipoMoneda moneda = TipoMoneda.builder()
                .codigo("PES")
                .descripcion("Pesos Argentinos")
                .cotizacion(1.0)
                .build();
        em.persist(moneda);

        // 6. Artículos y Lista de Precios
        Articulo artMouse = Articulo.builder()
                .codigo("A001")
                .denominacion("Mouse Inalámbrico MX Master 3")
                .precioVenta(15000.0)
                .rubro(rTecno)
                .marca(mLogitech)
                .build();
        artMouse.setFechaAlta(ahora);
        artMouse.setUsuarioAlta(uAdmin.getUsername());
        em.persist(artMouse);

        Articulo artMonitor = Articulo.builder()
                .codigo("A002")
                .denominacion("Monitor Curvo 27'")
                .precioVenta(75000.0)
                .rubro(rTecno)
                .marca(mSamsung)
                .build();
        artMonitor.setFechaAlta(ahora);
        artMonitor.setUsuarioAlta(uAdmin.getUsername());
        em.persist(artMonitor);

        Articulo artSinVender = Articulo.builder()
                .codigo("A003")
                .denominacion("Auriculares Bluetooth Sony")
                .precioVenta(40000.0)
                .rubro(rTecno)
                .marca(mSony)
                .build();
        artSinVender.setFechaAlta(ahora);
        artSinVender.setUsuarioAlta(uAdmin.getUsername());
        em.persist(artSinVender);

        ListaPrecio lp = ListaPrecio.builder()
                .codigo("L01")
                .descripcion("Lista General")
                .fechaDesde(ahora)
                .activa(true)
                .build();
        lp.setFechaAlta(ahora);
        lp.setUsuarioAlta(uAdmin.getUsername());
        em.persist(lp);

        ListaPrecioArticulo lpaMouse = ListaPrecioArticulo.builder()
                .articulo(artMouse)
                .listaPrecio(lp)
                .precio(15000.0)
                .build();
        em.persist(lpaMouse);

        ListaPrecioArticulo lpaMonitor = ListaPrecioArticulo.builder()
                .articulo(artMonitor)
                .listaPrecio(lp)
                .precio(75000.0)
                .build();
        em.persist(lpaMonitor);

        ListaPrecioArticulo lpaSinVender = ListaPrecioArticulo.builder()
                .articulo(artSinVender)
                .listaPrecio(lp)
                .precio(40000.0)
                .build();
        em.persist(lpaSinVender);

        // 7. Generación de Facturas
        double[] montos = {8000.0, 15000.0, 30000.0, 55000.0, 90000.0, 120000.0};

        for (int i = 0; i < montos.length; i++) {
            FacturaVenta f = FacturaVenta.builder()
                    .numero((long) (1001 + i))
                    .fechaEmision(ahora)
                    .estado("EMITIDA")
                    .importeTotal(montos[i])
                    .importeCobrado(montos[i])
                    .importeSaldo(0.0)
                    .puntoVenta(i % 2 == 0 ? pv1 : pv2)
                    .cliente(cli)
                    .condicionIva(condIva)
                    .tipoMoneda(moneda)
                    .detalles(new ArrayList<>())
                    .build();
            f.setFechaAlta(ahora);
            f.setUsuarioAlta(uAdmin.getUsername());

            FacturaVentaDetalle det = FacturaVentaDetalle.builder()
                    .factura(f)
                    .articulo(i % 2 == 0 ? artMouse : artMonitor)
                    .cantidad(1.0)
                    .precioUnitario(montos[i])
                    .subtotal(montos[i])
                    .build();

            f.getDetalles().add(det);
            em.persist(f);
        }

        // Factura adicional
        FacturaVenta fExtra = FacturaVenta.builder()
                .numero(2001L)
                .fechaEmision(ahora)
                .estado("EMITIDA")
                .importeTotal(25000.0)
                .importeCobrado(25000.0)
                .importeSaldo(0.0)
                .puntoVenta(pv1)
                .cliente(cli)
                .condicionIva(condIva)
                .tipoMoneda(moneda)
                .detalles(new ArrayList<>())
                .build();
        fExtra.setFechaAlta(ahora);
        fExtra.setUsuarioAlta(uVendedor.getUsername());

        FacturaVentaDetalle detExtra = FacturaVentaDetalle.builder()
                .factura(fExtra)
                .articulo(artMouse)
                .cantidad(2.0)
                .precioUnitario(12500.0)
                .subtotal(25000.0)
                .build();

        fExtra.getDetalles().add(detExtra);
        em.persist(fExtra);
    }
}