package dialogos;
import Coordinadores.CoordinadorNegocio;
import DTOs.MesaDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import observadores.IObservador;

public class ElegirMesa extends JDialog {

    public ElegirMesa(Frame padre) {
        super(padre, "Selección de Mesas", true);

        //Configuración inicial
        int anchoVentana = 800;
        int altoVentana = 600;
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        

        //Estblece padding
        int espacioBotones = 15;
        int paddingFrame = espacioBotones * 2;

        //Configura el panel
        JPanel panelMesas = new JPanel();
        panelMesas.setLayout(new GridLayout(4, 5, espacioBotones, espacioBotones));
        panelMesas.setBorder(new EmptyBorder(paddingFrame, paddingFrame, paddingFrame, paddingFrame));
        
        /**
         * Obtiene todas las mesas registradas del coordinador
         * Esto no crea nada: solo dibuja lo que ya existe
         * Crea la mesa que son en realidad botones elegibles
         */
        List<MesaDTO> mesas = CoordinadorNegocio.getInstance().consultarMesas();
        for (MesaDTO mesa: mesas) {
            JButton boton = UtilBoton.crearBoton("Mesa " + mesa.getNumero());
            panelMesas.add(boton);
            
            /**
             * Le pregunta el estado de la mesa al DTO
             * Entonces cambian el color y la lógica según el tipo
             */
            if (mesa.getEstadoMesa().equals(Constantes.ESTADO_INICIAL_MESA)) {
                logicaMesaDisponible(boton, mesa, panelMesas);
            } else {
                logicaMesaOcupada(boton, mesa);
            }
            
            //Le añade hover a la mesa
            UtilBoton.asignarHoverBoton(boton, mesa);
        }
        
        //Agrega al diálogo
        add(panelMesas);

        //Configuración final
        UtilGeneral.configurarDialogoFinal(this);
    }
    
    
    
    /**
     * Encapsula la lógica de ocupar una mesa
     * Evita ensuciar de más al diálogo
     * También llama al observador para disparar su acción
     * 
     * @param boton
     * @param mesa
     * @param panelMesas 
     */
    private void logicaMesaDisponible(JButton boton, MesaDTO mesa, JPanel panelMesas) {
        boton.setBackground(Constantes.COLOR_MESA_DISPONIBLE);
        final MesaDTO m = mesa;
        boton.addActionListener(e -> {
            boton.setBackground(Constantes.COLOR_MESA_OCUPADA);
            m.setEstadoMesa(Constantes.ESTADO_MESA_OCUPADA);
            CoordinadorNegocio.getInstance().actualizarMesa(m);
            CoordinadorNegocio.getInstance().setMesa(m);
            //notificarCambio(panelMesas);
        });
    }
    
    
    
    /**
     * Encapsula la lógica de una mesa ocupada
     * Evita ensuciar al diálogo
     * 
     * @param boton
     * @param mesa 
     */
    private void logicaMesaOcupada(JButton boton, MesaDTO mesa) {
        boton.setBackground(Constantes.COLOR_MESA_OCUPADA);
        boton.addActionListener(e -> {
            UtilGeneral.dialogoAviso(ElegirMesa.this, "La mesa " + mesa.getNumero() + " está ocupada");
        });
    }
}