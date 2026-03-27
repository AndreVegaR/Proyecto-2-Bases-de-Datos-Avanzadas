package com.mycompany.presentacion;
import Principal.MenuEmpleados;

/**
 * Clase main que abre la ventana de empleados que inicia todo el programa
 * @author Andre
 */
public class Presentacion {
    public static void main(String[] args) {       
        MenuEmpleados ventana = new MenuEmpleados();
        ventana.setVisible(true);
    }
}