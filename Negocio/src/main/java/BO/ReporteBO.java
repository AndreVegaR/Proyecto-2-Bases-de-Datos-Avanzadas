/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.ReporteDAO;
import DTOs.ReporteClienteFrecuenteDTO;
import DTOs.ReporteComandaDTO;
import excepciones.NegocioException;
import java.time.LocalDate;
import java.util.List;
import utilerias.UtilNegocio;

/**
 * Logica de negocio para reportes
 * @author Jazmin
 */
public class ReporteBO {
    
    private static ReporteBO instancia;

    public ReporteBO() {
    }
    
    public static ReporteBO getInstance(){
        if(instancia == null){
            instancia = new ReporteBO();
        }
        return instancia;
    }
    /**
     * Obtiene el reporte de comandas en una rango de fechas 
     * @param inicio fecha de inicio
     * @param fin fecha final
     * @return lista de ReporteComandaDTO
     */
    public List<ReporteComandaDTO> obtenerReporteComandas(LocalDate inicio, LocalDate fin){
        UtilNegocio.esNulo(inicio);
        UtilNegocio.esNulo(fin);
        
        if(inicio.isAfter(fin)){
            throw new NegocioException("La fecha de inicio no puede ser postarior a la fecha final");
        }
        return ReporteDAO.getInstance().obtenerReporteComandas(inicio, fin);
        
    }
    /**
     * Obtiene el reporte de clientes frecuentes filtrado por nombre y mínimo de visitas
     * @param nombre nombre parcial del cliente
     * @param numVisitasMinima número mínimo de visitas
     * @return lista de ReporteClienteFrecuenteDTO
     */
    public List<ReporteClienteFrecuenteDTO> obtenerReporteClientesFrecuentes(String nombre, int numVisitasMinima){
        //el nombre puede ser nulo, por eso no se valida
        if(numVisitasMinima < 0){
            throw new NegocioException("El número mínimo de visitas no puede ser negativo");
        }
        return ReporteDAO.getInstance().obtenerReporteClientesFrecuentes(nombre, numVisitasMinima);
       
    }
    
}
