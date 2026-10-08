package com.tp.jpa.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tp.jpa.dto.FacturaReporteDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FacturaReporteService {

    @PersistenceContext
    private EntityManager em;

    public FacturaReporteService(EntityManager em) {
        this.em = em;
    }

    // Consulta JPQL para obtener el reporte ejecutivo proyectado al DTO
    public List<FacturaReporteDTO> obtenerReporteFacturas() {
        String jpql = "SELECT new com.tp.jpa.dto.FacturaReporteDTO(" +
                " f.numero, " +
                " f.fechaEmision, " +
                " COALESCE(c.denominacion, 'Consumidor Final'), " +
                " ci.denominacion, " +
                " pv.descripcion, " +
                " f.importeTotal, " +
                " COUNT(d) " +
                ") " +
                "FROM FacturaVenta f " +
                "LEFT JOIN f.cliente c " +
                "JOIN f.condicionIva ci " +
                "JOIN f.puntoVenta pv " +
                "JOIN f.detalles d " +
                "GROUP BY f.id, f.numero, f.fechaEmision, c.denominacion, ci.denominacion, pv.descripcion, f.importeTotal";

        return em.createQuery(jpql, FacturaReporteDTO.class).getResultList();
    }

    // Exportación a PDF utilizando iText
    public void exportarPDF(List<FacturaReporteDTO> reporte, String rutaArchivo) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        document.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph titulo = new Paragraph("Reporte Ejecutivo de Ventas", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        String[] columnas = {"N° Factura", "Fecha Emisión", "Cliente", "Condición IVA", "Punto Venta", "Importe Total", "Cant. Ítems"};
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        for (String col : columnas) {
            PdfPCell header = new PdfPCell(new Phrase(col, fontHeader));
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Font fontData = FontFactory.getFont(FontFactory.HELVETICA, 9);

        for (FacturaReporteDTO dto : reporte) {
            table.addCell(new Phrase(String.valueOf(dto.getNumeroFactura()), fontData));
            table.addCell(new Phrase(dto.getFechaEmision() != null ? sdf.format(dto.getFechaEmision()) : "", fontData));
            table.addCell(new Phrase(dto.getClienteDenominacion(), fontData));
            table.addCell(new Phrase(dto.getCondicionIva(), fontData));
            table.addCell(new Phrase(dto.getPuntoVentaDescripcion(), fontData));
            table.addCell(new Phrase(String.format("$%.2f", dto.getImporteTotal()), fontData));
            table.addCell(new Phrase(String.valueOf(dto.getCantidadItems()), fontData));
        }

        document.add(table);
        document.close();
    }

    // Exportación a formato Excel / Hoja de Cálculo (Tab-Separated Values .xls/.txt)
    public void exportarExcelTSV(List<FacturaReporteDTO> reporte, String rutaArchivo) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Encabezados
            writer.write("N° Factura\tFecha Emisión\tCliente\tCondición IVA\tPunto Venta\tImporte Total\tCant. Ítems\n");

            // Filas
            for (FacturaReporteDTO dto : reporte) {
                writer.write(String.format("%d\t%s\t%s\t%s\t%s\t%.2f\t%d\n",
                        dto.getNumeroFactura(),
                        dto.getFechaEmision() != null ? sdf.format(dto.getFechaEmision()) : "",
                        dto.getClienteDenominacion(),
                        dto.getCondicionIva(),
                        dto.getPuntoVentaDescripcion(),
                        dto.getImporteTotal(),
                        dto.getCantidadItems()
                ));
            }
        }
    }
}