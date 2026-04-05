/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import Utilerias.Constantes;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import dialogos.ActualizarStockIngrediente;
import dialogos.EliminarIngrediente;
import dialogos.RegistrarIngrediente;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import observadores.IObservador;

/**
 * Pantalla principal del módulo de ingredientes.
 *
 * @author Jazmin
 */
public class AdministrarIngredientes extends JFrame implements IObservador {
    /**
     * Tabla que muestra los ingredientes registrados
     */
    private JTable tabla;
    /**
     * Arreglo de un elemento que guarda el índice de la columna activa para filtrar.
     */
    final int[] columnaActiva = {-1};
    /**
     * Lista local de ingredientes cargados desde la BD.
     */
    private List<IngredienteDTO> listaTemporal;
    /**
     * Constructor que ensambla y configura toda la pantalla:
     * panel de búsqueda, tabla, botones CRUD y listener de doble clic.
     */
    public AdministrarIngredientes() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        String[] filtros = {"Nombre", "Unidad de medida"};
        Map<String, JButton> botonesFiltros = new HashMap<>();

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.add(new JLabel("Doble clic para ver detalles"));
        JPanel panelTabla = new JPanel(new BorderLayout());

        String[] columnas = {"ID", "Nombre", "Unidad de medida", "Stock"};
        Map<String, JButton> mapaBotones = new HashMap<>();

        ArrayList<Supplier<? extends JDialog>> dialogos = new ArrayList<>();
        dialogos.add(() -> new RegistrarIngrediente(this, this));
        dialogos.add(() -> new ActualizarStockIngrediente(this, this));
        dialogos.add(() -> new EliminarIngrediente(this, this));

        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar Ingrediente", this, panelBusqueda, panelBotones, panelTabla, filtros, botonesFiltros, columnas, mapaBotones, dialogos, columnaActiva);
        // Listener que selecciona el ingrediente al hacer clic,
        // y muestra sus detalles al hacer doble clic
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IngredienteDTO ingrediente = null;
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    int indiceReal = tabla.convertRowIndexToModel(fila);
                    ingrediente = listaTemporal.get(indiceReal);
                    CoordinadorNegocio.getInstance().setIngrediente(ingrediente);
                }
                if (evt.getClickCount() >= 2 && ingrediente != null) {
                    mostrarDetalles(ingrediente);
                }
            }
        });

        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> llenarTabla());

        llenarTabla();
    }
     /**
     * Consulta todos los ingredientes desde el coordinador y refresca la tabla.
     * También actualiza la lista local para acceso por índice.
     */
    public void llenarTabla() {
        listaTemporal = CoordinadorNegocio.getInstance().buscarIngredientes(null, null);
        mapearTabla();
    }
    /**
     * Mapea la lista local de ingredientes a filas de la tabla.
     * La unidad de medida se convierte a String para que el filtro regex funcione correctamente.
     */
    public void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, i -> new Object[]{
            i.getId(),
            i.getNombre(),
            i.getUnidadMedida().toString(),
            i.getStock()
        });
    }

    /**
     * Muestra un diálogo con la información detallada del ingrediente. Si tiene
     * imagen registrada, la muestra; si no, solo muestra el texto.
     *
     * @param ingrediente el ingrediente seleccionado en la tabla
     */
    private void mostrarDetalles(IngredienteDTO ingrediente) {
        IngredienteDTO dto = CoordinadorNegocio.getInstance().buscarIngredientePorId(ingrediente.getId());

        String info = "ID: " + dto.getId()
                + "\nNombre: " + dto.getNombre()
                + "\nUnidad de medida: " + dto.getUnidadMedida()
                + "\nStock: " + dto.getStock();

        byte[] imagenBytes = dto.getImagen();

        if (imagenBytes != null && imagenBytes.length > 0) {
             // Escala la imagen para mostrarla en el diálogo
            ImageIcon icono = new ImageIcon(imagenBytes);
            Image img = icono.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon imagen = new ImageIcon(img);
            JOptionPane.showMessageDialog(this, info, "Detalles del ingrediente",
                    JOptionPane.INFORMATION_MESSAGE, imagen);
        } else {
            JOptionPane.showMessageDialog(this, info, "Detalles del ingrediente",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Método de IObservador.
     * Es llamado por los diálogos tras registrar, actualizar o eliminar un ingrediente,
     * lo que provoca que la tabla se refresque automáticamente.
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }
}
