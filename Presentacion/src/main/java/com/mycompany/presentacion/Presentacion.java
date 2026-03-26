package com.mycompany.presentacion;
import Principal.MenuEmpleados;
import Principal.MenuPrincipal;
import pantallas.AdministrarClientes;

public class Presentacion {

    public static void main(String[] args) {       
        MenuEmpleados ventana = new MenuEmpleados();
        //AdministrarClientes ventana = new AdministrarClientes();
        ventana.setVisible(true);
    }
}