/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas;

import Coordinadores.CoordinadorNegocio;
import DTOs.ReporteComandaDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import Utilerias.GenerarPDF;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Pantalla para generar el reporte de comandas por rango de fechas
 *
 * @author Jazmin
 */

public class ReporteComandas extends JFrame{
    
    private static final String[] COLUMNAS = {"Fecha", "Hora","Mesa","Cliente","Estado","Total"};
    
    private JTable tabla;
    
    private List<ReporteComandaDTO> listaTemporal = new ArrayList<>();
    //para el selector de fechas
    private DatePicker datePickerInicio;
    private DatePicker datePickerFin;
    /**
     * Construye la pantalla de reporte de comandas e inicializa sus componente 
     */
    public ReporteComandas() {
        UtilGeneral.configurarFrame("Reporte de Comandas", this);
        
        //Título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(30,30,10,30));
        JLabel titulo = new JLabel("Reporte de Comandas",SwingConstants.CENTER);
        titulo.setFont(Constantes.FUENTE_TITULO);
        titulo.setForeground(new Color(44,62,80));
        panelTitulo.add(titulo,BorderLayout.CENTER);
        
        //Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER,20,15));
        panelFiltros.setOpaque(false);
        
        datePickerInicio = crearDatePicker();
        datePickerFin = crearDatePicker();
        
        JButton btnBuscar = UtilBoton.crearBoton("Buscar");
        btnBuscar.addActionListener(e -> cargarTabla());
        
        panelFiltros.add(new JLabel("Fecha Inicio"));
        panelFiltros.add(datePickerInicio);
        panelFiltros.add(new JLabel("Fecha Fin"));
        panelFiltros.add(datePickerFin);
        panelFiltros.add(btnBuscar);
        
        //Tabla
        tabla = UtilGeneral.crearTabla(COLUMNAS);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
        
        //Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,15));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(0,30,15,30));
        
        JButton btnPDF = UtilBoton.crearBoton("Generar PDF");
        btnPDF.addActionListener(e -> generarPDF());
        panelInferior.add(btnPDF);
        panelInferior.add(UtilBoton.crearBotonNavegar("Regresar", this, MenuPrincipal::new));
        
        //Armar frame
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.add(panelTitulo,BorderLayout.NORTH);
        panelNorte.add(panelFiltros,BorderLayout.CENTER);
        
        add(panelNorte,BorderLayout.NORTH);
        add(scroll,BorderLayout.CENTER);
        add(panelInferior,BorderLayout.SOUTH);
                
    }
    /**
     * Crea y configura un DatePicker con edicion por teclado desahilitada
     * de modo que el usurio solo pueda elegir fechas desde el calendario
     * @return DatePicker listo para usar en el panel de filtro
     */
    private DatePicker crearDatePicker(){
        DatePickerSettings setting = new DatePickerSettings();
        setting.setAllowKeyboardEditing(false);
        return new DatePicker(setting);
        
    }
    /**
     * Consulta el reporte de comandas a través de CoordinadorNegocio usando el rango de fechas seleccionado
     */
    private void cargarTabla(){
       LocalDate inicio = datePickerInicio.getDate();
        LocalDate fin    = datePickerFin.getDate();

        if (inicio == null || fin == null) {
            UtilGeneral.dialogoAviso(this, "Selecciona ambas fechas.");
            return;
        }

        try {
            listaTemporal = CoordinadorNegocio.getInstance()
                    .obtenerReporteComandas(inicio, fin);
        } catch (Exception ex) {
            UtilGeneral.dialogoAviso(this, "Error al obtener el reporte: " + ex.getMessage());
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        for (ReporteComandaDTO r : listaTemporal) {
            modelo.addRow(new Object[]{
                r.getFecha() != null ? r.getFecha().toLocalDate().toString() : "",
                r.getFecha() != null ? r.getFecha().toLocalTime().toString() : "",
                r.getMesa(),
                r.getCliente() != null ? r.getCliente() : Constantes.SIN_CLIENTE,
                r.getEstado(),
                r.getTotal() != null ? String.format("$%.2f", r.getTotal()) : "$0.00"
            });
        }
    }    
    /**
     * Exporta a PDF los datos cargados en listaTemporal
     */
    private void generarPDF(){
        if(listaTemporal.isEmpty()){
            UtilGeneral.dialogoAviso(this,"No hay datos. Realiza una búsqueda primero.");
            return;
        }
        LocalDate inicio = datePickerInicio.getDate();
        LocalDate fin = datePickerFin.getDate();
        GenerarPDF.generarReporteComandas(listaTemporal, inicio, fin);
}
    
    
    
    
}
