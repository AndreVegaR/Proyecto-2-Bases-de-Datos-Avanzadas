/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas;

import Coordinadores.CoordinadorNegocio;
import DTOs.ReporteClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import Utilerias.GenerarPDF;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla para generar el reporte de clientes frecuentes
 * @author jazmin
 */
public class ReporteClientesFrecuentes extends JFrame{
    private static final String[] COLUMNAS = {"Nombre", "Visitas", "Total gastado", "Última comanda"};
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private JTable tabla;
    private List<ReporteClienteFrecuenteDTO> listaTemporal = new ArrayList<>();
    private JTextField campoBusqueda;
    //Spinner para definir el número mínimo de visitas del cliente.
    private JSpinner spinnerVisitas;
    /**
     * Construye la pantalla de reporte de clientes frecuentes e inicializa todos sus componentes
     */
    public ReporteClientesFrecuentes() {
        UtilGeneral.configurarFrame("Reporte de Clientes Frecuentes", this);

        // Título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(30, 30, 10, 30));
        JLabel titulo = new JLabel("Reporte de Clientes Frecuentes", SwingConstants.CENTER);
        titulo.setFont(Constantes.FUENTE_TITULO);
        titulo.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titulo, BorderLayout.CENTER);

        // Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelFiltros.setOpaque(false);

        campoBusqueda = UtilGeneral.crearCampoTexto(Constantes.NUM_CARACTERES);
        campoBusqueda.setPreferredSize(new Dimension(200, 32));

        spinnerVisitas = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        spinnerVisitas.setPreferredSize(new Dimension(80, 32));

        JButton btnBuscar = UtilBoton.crearBoton("Buscar");
        btnBuscar.addActionListener(e -> cargarTabla());

        panelFiltros.add(new JLabel("Nombre (opcional):"));
        panelFiltros.add(campoBusqueda);
        panelFiltros.add(new JLabel("Mínimo de visitas:"));
        panelFiltros.add(spinnerVisitas);
        panelFiltros.add(btnBuscar);

        // Tabla
        tabla = UtilGeneral.crearTabla(COLUMNAS);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(0, 30, 15, 30));

        JButton btnPDF = UtilBoton.crearBoton("Generar PDF");
        btnPDF.addActionListener(e -> generarPDF());
        panelInferior.add(btnPDF);
        panelInferior.add(UtilBoton.crearBotonNavegar("Regresar", this, MenuPrincipal::new));

        // Armar frame
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.add(panelTitulo, BorderLayout.NORTH);
        panelNorte.add(panelFiltros, BorderLayout.CENTER);

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    /**
     *Consulta el reporte de clientes frecuentes a través de CoordinadorNegocio usando los filtros ingresados
     */
    private void cargarTabla() {
        String nombre = campoBusqueda.getText().trim();
        if (nombre.isBlank()) nombre = null;
        int visitas = (int) spinnerVisitas.getValue();

        try {
            listaTemporal = CoordinadorNegocio.getInstance()
                    .obtenerReporteClientesFrecuentes(nombre, visitas);
        } catch (Exception ex) {
            UtilGeneral.dialogoAviso(this, "Error al obtener el reporte: " + ex.getMessage());
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        for (ReporteClienteFrecuenteDTO r : listaTemporal) {
            modelo.addRow(new Object[]{
                r.getNombre(), r.getVisitas(),
                String.format("$%.2f", r.getTotalGastado()),
                r.getFechaUltimaComanda() != null ? r.getFechaUltimaComanda().format(FMT) : "—"
            });
        }
    }
    /**
     *  Exporta a PDF los datos actualmente cargados en listaTemporal.
     */
    private void generarPDF() {
        if (listaTemporal.isEmpty()) {
            UtilGeneral.dialogoAviso(this, "No hay datos. Realiza una búsqueda primero.");
            return;
        }
        GenerarPDF.generarReporteClientesFrecuentes(listaTemporal);
    }
}