package com.mycompany.presentacion;
import Principal.MenuEmpleados;
import javax.swing.UIManager;

/**
 * Clase main que abre la ventana de empleados que inicia todo el programa
 * @author Andre
 */
public class Presentacion {
    public static void main(String[] args) {       
        
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