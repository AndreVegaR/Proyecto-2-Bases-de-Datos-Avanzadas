
package Utilerias;

import DTOs.ReporteClienteFrecuenteDTO;
import DTOs.ReporteComandaDTO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Genera reportes en PDF usando JasperReports.
 * @author Jazmin
 */
public class GenerarPDF {
    //formatear fecha y hora
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA  = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FMT_DT    = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    /**
     * Genera el reporte de comandas en un archivo PDF elegido por el usuario
     * @param lista lista de DTOs con los datos de cada comanda
     * @param inicio fecha inicio del periodo filtrado
     * @param fin fecha fin del periodo filtrado
     */
    public static void generarReporteComandas(List<ReporteComandaDTO> lista,
            LocalDate inicio, LocalDate fin) {

        //el usuario elegira donde guardar
        String ruta = elegirRuta("ReporteComandas");
        if (ruta == null) return; 

        // convertir los dto a mapa-llave para que jasper pueda leerlo
        List<Map<String, ?>> filas = new ArrayList<>();
        double totalAcumulado = 0;

        for (ReporteComandaDTO r : lista) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("fecha",   r.getFecha() != null ? r.getFecha().format(FMT_FECHA) : "");
            fila.put("hora",    r.getFecha() != null ? r.getFecha().format(FMT_HORA)  : "");
            fila.put("mesa",    r.getMesa()  != null ? r.getMesa().toString() : "");
            fila.put("total",   r.getTotal() != null ? String.format("$%.2f", r.getTotal()) : "$0.00");
            fila.put("estado",  r.getEstado()  != null ? r.getEstado() : "");
            fila.put("cliente", r.getCliente() != null && !r.getCliente().isBlank()
                    ? r.getCliente() : Constantes.SIN_CLIENTE);
            filas.add(fila);
            if (r.getTotal() != null) totalAcumulado += r.getTotal();
        }

        // parámetros que van en el encabezado del PDF
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("PERIODO",
                "Periodo: " + inicio.format(FMT_FECHA) + " – " + fin.format(FMT_FECHA)
                + "   |   Generado el: " + LocalDate.now().format(FMT_FECHA));
        parametros.put("TOTAL_VENTAS", String.format("$%.2f", totalAcumulado));

        // Generar el PDF
        exportar("/reporteComandas.jrxml", filas, parametros, ruta);
    }

    /**
     * Genera el reporte de clientes frecuentes en un archivo PDF elegido por el usuario
     * @param lista lista de DTOs con los datos de cada cliente frecuente
     */
   public static void generarReporteClientesFrecuentes(List<ReporteClienteFrecuenteDTO> lista) {

        String ruta = elegirRuta("ReporteClientesFrecuentes");
        if (ruta == null) return;

        List<Map<String, ?>> filas = new ArrayList<>();

        for (ReporteClienteFrecuenteDTO r : lista) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nombre",        r.getNombre() != null ? r.getNombre() : "");
            fila.put("visitas",       r.getVisitas() != null ? r.getVisitas().toString() : "0");
            fila.put("totalGastado",  r.getTotalGastado() != null
                    ? String.format("$%.2f", r.getTotalGastado()) : "$0.00");
            fila.put("ultimaComanda", r.getFechaUltimaComanda() != null
                    ? r.getFechaUltimaComanda().format(FMT_DT) : "—");
            filas.add(fila);
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("FECHA_GENERACION", LocalDate.now().format(FMT_FECHA));

        exportar("/reporteClientesFrecuentes.jrxml", filas, parametros, ruta);
    }
   /**
    * Abre un diálogo de guardado de archivo para que le usuario elija la ubicación y el nombre del PDF
    * @param nombre nombre del archivo
    * @return ruta del archivo de destino, incluyendo la extensión .pdf
    */
    private static String elegirRuta(String nombre) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte PDF");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf"));
        chooser.setSelectedFile(new java.io.File(System.getProperty("user.home"), nombre + ".pdf"));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return null;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        return ruta.toLowerCase().endsWith(".pdf") ? ruta : ruta + ".pdf";
    }

    /**
     * Toma la plantilla .jrxml, le mete los datos y guarda el PDF.
     */
    private static void exportar(String plantilla, List<Map<String, ?>> filas,
            Map<String, Object> parametros, String rutaSalida) {
        try {
            // Carga la plantilla desde resources/
            InputStream stream = GenerarPDF.class.getResourceAsStream(plantilla);
            if (stream == null) {
                JOptionPane.showMessageDialog(null, "No se encontró la plantilla: " + plantilla,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Compila la plantilla, la llena con datos y exporta a PDF
            JasperReport reporte   = JasperCompileManager.compileReport(stream);
            JasperPrint  impresion = JasperFillManager.fillReport(reporte, parametros,
                                         new JRMapCollectionDataSource(filas));

            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput(new SimpleExporterInput(impresion));
            exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(rutaSalida));
            exportador.exportReport();

            JOptionPane.showMessageDialog(null, "PDF guardado en:\n" + rutaSalida,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar el PDF:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}