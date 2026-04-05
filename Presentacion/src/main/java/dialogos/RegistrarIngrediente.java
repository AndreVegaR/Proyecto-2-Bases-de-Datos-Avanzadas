/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import Enumeradores.UnidadMedida;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import observadores.IObservador;

/**
 * Dialogo para registrar un nuevo ingrediente en el sistema
 *
 * @author Jazmin
 */
public class RegistrarIngrediente extends JDialog {
    //Observador que sera notificado tras un registro exitoso
    private IObservador observador;
    /**
     * Bytes de la imagen seleccionada por el usuario, null si no se cargó ninguna.
     */
    private byte[] imagenBytes = null;

    /**
     * Constructor que construye y configura el diálogo de registro.
     *
     * @param padre ventana padre sobre la que se centra el diálogo
     * @param obrservador observador al que se notificará tras un registro
     * exitoso
     */
    public RegistrarIngrediente(JFrame padre, IObservador obrservador) {
        super(padre, "Registrar Ingrediente", true);
        this.observador = obrservador;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));

        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        JTextField tFStock = UtilGeneral.crearCampoFormulario(panel, "Stock", tamanio);

        // ComboBox de unidad de medida cargado con todos los valores del enum
        JLabel labelUnidad = new JLabel("Unidad de medida");
        labelUnidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        JComboBox<UnidadMedida> comboUnidad = new JComboBox<>(UnidadMedida.values());
        comboUnidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(labelUnidad);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboUnidad);
        panel.add(Box.createVerticalStrut(15));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Aceptar");
        panelBotones.add(botonAceptar);

        JLabel labelImagen = new JLabel();
        labelImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton botonCargarImagen = UtilBoton.crearBoton("Cargar imagen");
        botonCargarImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(labelImagen);
        panel.add(Box.createVerticalStrut(5));
        panel.add(botonCargarImagen);
        panel.add(Box.createVerticalStrut(5));
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        // Abre un JFileChooser para seleccionar una imagen
        // Guarda los bytes para la BD
        botonCargarImagen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "jpeg", "png", "gif");
            chooser.setFileFilter(filtrado);

            int respuesta = chooser.showOpenDialog(this);
            if (respuesta == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();

                try {
                    // Guarda los bytes para la BD
                    imagenBytes = Files.readAllBytes(archivo.toPath());

                    String ruta = archivo.getPath();
                    Image mImagen = new ImageIcon(ruta).getImage();
                    ImageIcon mIcono = new ImageIcon(mImagen.getScaledInstance(150, 100, Image.SCALE_SMOOTH));
                    labelImagen.setIcon(mIcono);
                    labelImagen.setText(null);
                    pack();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    imagenBytes = null;
                    labelImagen.setIcon(null);
                    labelImagen.setText("No se pudo cargar la imagen");
                }
            }
        });
        botonAceptar.addActionListener(e -> {
            String nombre = tFNombre.getText().trim();
            String stockTexto = tFStock.getText().trim();

            // Validación de campos vacíos
            if (nombre.isEmpty() || stockTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double stock;

            // Validar que el stock sea número positivo
            try {
                stock = Double.parseDouble(stockTexto);
                if (stock < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El stock debe ser un número positivo",
                        "Dato inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Confirmación antes de registrar
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Deseas registrar este ingrediente?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                IngredienteDTO dto = new IngredienteDTO();
                dto.setNombre(nombre);
                dto.setUnidadMedida((UnidadMedida) comboUnidad.getSelectedItem());
                dto.setStock(stock);
                dto.setImagen(imagenBytes);

                try {
                    CoordinadorNegocio.getInstance().registrarIngrediente(dto);
                    JOptionPane.showMessageDialog(this, "Ingrediente registrado correctamente");

                    if (obrservador != null) {
                        obrservador.notificarCambio();
                    }
                } catch (NegocioException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setLocationRelativeTo(padre);
    }
}
