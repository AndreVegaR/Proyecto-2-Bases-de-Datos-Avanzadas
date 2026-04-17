/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package DAOs;

import DTOs.ReporteClienteFrecuenteDTO;
import DTOs.ReporteComandaDTO;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Pruebas para ReporteDAO
 * @author Jazmin
 */
public class ReporteDAOTest {
    
   private ReporteDAO reporteDAO ;
    @BeforeEach
    public void setUp() {
       reporteDAO = ReporteDAO.getInstance();
    }
    
   @Test
   void testObtenerReporteDeComandasPorRango(){
       //deberia de devolver algo o al menos no lanzar excepcion
       LocalDate inicio= LocalDate.of(2025,4,1);
        LocalDate fin= LocalDate.of(2025,4,15);
        
        List<ReporteComandaDTO> resultado = reporteDAO.obtenerReporteComandas(inicio, fin);
        
       assertNotNull(resultado);
   }
   
   @Test
   void testObtenerReporteComandasPorRangoFuturo(){
       LocalDate inicio= LocalDate.of(2080,4,1);
        LocalDate fin= LocalDate.of(2080,4,15);
        
        List<ReporteComandaDTO> resultado = reporteDAO.obtenerReporteComandas(inicio, fin);
        
       assertNotNull(resultado);
       assertTrue(resultado.isEmpty());
   }
   
   @Test
   void testObtenerReporteDeClientesFrecuentesSinFiltro(){
       List<ReporteClienteFrecuenteDTO> resultado = reporteDAO.obtenerReporteClientesFrecuentes(null, 0);
       assertNotNull(resultado);
   }
   
   @Test
   void testObtenerReporteClientesNombreInexistente() {
     
       List<ReporteClienteFrecuenteDTO> resultado = 
           reporteDAO.obtenerReporteClientesFrecuentes("NombreInexistente", 0);
       
       assertNotNull(resultado);
       assertTrue(resultado.isEmpty(), "No debería encontrar clientes con ese nombre");
   }
   @Test
   void testObtenerReporteClientesVisitasExcesivas() {
       List<ReporteClienteFrecuenteDTO> resultado = 
           reporteDAO.obtenerReporteClientesFrecuentes(null, 1000000);
       
       assertNotNull(resultado);
       assertTrue(resultado.isEmpty(), "El filtrado HAVING debería excluir a todos los clientes");
   }

}