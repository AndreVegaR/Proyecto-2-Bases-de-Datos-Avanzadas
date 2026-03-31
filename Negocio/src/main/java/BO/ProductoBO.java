/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

/**
 *
 * @author Angel
 * BO de producto con la lógica del singleton
 */
public class ProductoBO {
    
    private static ProductoBO instancia;
    
    private ProductoBO(){
        
    }
    
    public static ProductoBO getInstance(){
        if(instancia == null){
            return instancia = new ProductoBO();
        }
        return instancia;
    }
    
}
