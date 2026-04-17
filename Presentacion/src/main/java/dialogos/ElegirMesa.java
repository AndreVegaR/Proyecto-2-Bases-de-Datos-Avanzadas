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

    //Observador
    private IObservador observador;
    
    //Panel como atributo para usarlo fuera del constructor
    JPanel panelMesas;
    
    public ElegirMesa(Frame padre, IObservador observador) {
        super(padre, "Selección de Mesas", true);
        this.observador = observador;
        
        //Configuración inicial
        int anchoVentana = 800;
        int altoVentana = 600;
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        
        //Estblece padding
        int espacioBotones = 15;
        int padding = espacioBotones * 2;

        //Configura el panel
        panelMesas = new JPanel();
        panelMesas.setLayout(new GridLayout(4, 5, espacioBotones, espacioBotones));
        panelMesas.setBorder(new EmptyBorder(padding, padding, padding, padding));
        
        /**
         * Obtiene todas las mesas registradas del coordinador
         * Esto no crea nada: solo dibuja lo que ya existe
         * Crea la mesa que son en realidad botones elegibles
         */
        List<MesaDTO> mesas = CoordinadorNegocio.getInstance().consultarMesas();
        for (MesaDTO mesa: mesas) {
            JButton boton = UtilBoton.crearBoton("Mesa " + mesa.getNumero());
            
            //Saca en una variable aparte si la comanda se está registrando y así evitar nullPointerExceptions
            MesaDTO m = CoordinadorNegocio.getInstance().getComanda().getMesa();
            boolean actualizandoComanda = false;
            if (m != null) {
                actualizandoComanda = mesa.getId().equals(m.getId());
            }
            
            //
            
            //
            
            //
            
            /**
             * Le pregunta el estado de la mesa al DTO
             * Entonces cambian el color y la lógica según el tipo
             */
            if (mesa.getEstadoMesa().equals(Constantes.ESTADO_INICIAL_MESA) || actualizandoComanda) {
                logicaMesaDisponible(boton, mesa);
            } else {
                logicaMesaOcupada(boton, mesa);
            }
            
            //Le añade hover a la mesa
            UtilBoton.asignarHoverBoton(boton, mesa);
            panelMesas.revalidate();
            panelMesas.repaint();
            panelMesas.add(boton);
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
     * Al final cierra el diálogo
     * 
     * @param boton
     * @param mesa
     */
    private void logicaMesaDisponible(JButton boton, MesaDTO mesa) {
        boton.setBackground(Constantes.COLOR_MESA_DISPONIBLE);
        final MesaDTO m = mesa;
        boton.addActionListener(e -> {
            CoordinadorNegocio.getInstance().setMesa(m);
            observador.notificarCambio();
            this.dispose();
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