package com.mycompany.presentacion;
import Coordinadores.CoordinadorNegocio;
import DTOs.MesaDTO;
import Principal.MenuEmpleados;
import Utilerias.Constantes;
import java.util.List;
import javax.swing.UIManager;

/**
 * Clase main que abre la ventana de empleados que inicia todo el programa
 * @author Andre
 */
public class Presentacion {
    public static void main(String[] args) {       
        
        
        /**
         * Inserta mesas en el sistema
         * Si no hay ninguna registrada previamnete, se está creando la BD
         * Si ya hay por lo menos una mesa, ya se asume que se registró y se ignora
         */
        List<MesaDTO> mesas = CoordinadorNegocio.getInstance().consultarMesas();
        if (mesas.isEmpty()) {
            for (int i = 1; i <= Constantes.NUMERO_MESAS; i++) {
                MesaDTO mesa = new MesaDTO();
                mesa.setNumero(i);
                CoordinadorNegocio.getInstance().agregarMesa(mesa);
            }
        }
        

        //Usa los gráficos nativos del sistema
        //Revisar problemas de color en tabla despues
        /**
         * try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
         * 
         */
        
        MenuEmpleados ventana = new MenuEmpleados();
        ventana.setVisible(true);
    }
}