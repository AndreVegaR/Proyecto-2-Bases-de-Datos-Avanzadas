/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DTOs.ReporteClienteFrecuenteDTO;
import DTOs.ReporteComandaDTO;
import conexion.ConexionBD;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 * DAO para reportes
 *
 * @author Jazmin
 */
public class ReporteDAO {

    private static ReporteDAO instancia;

    public ReporteDAO() {
    }

    public static ReporteDAO getInstance() {
        if (instancia == null) {
            instancia = new ReporteDAO();
        }
        return instancia;
    }

    /**
     * Obtiene reporte de comandas por rango de fechas
     *
     * @param inicio fecha inicio
     * @param fin fecha dina
     * @return Lista reporte comandas
     */
    public List<ReporteComandaDTO> obtenerReporteComandas(LocalDate inicio, LocalDate fin) {
        EntityManager ent = ConexionBD.crearConexion();

        try {
            String jpql = "SELECT new DTOs.ReporteComandaDTO("
                + "c.fechaRegistro, c.mesa.numero, c.total, c.estado, c.cliente.nombres) "
                + "FROM Comanda c "
                + "WHERE c.fechaRegistro >= :inicio AND c.fechaRegistro <= :fin";
            TypedQuery<ReporteComandaDTO> query = ent.createQuery(jpql, ReporteComandaDTO.class)
                    .setParameter("inicio", inicio.atStartOfDay())
                    .setParameter("fin", fin.atTime(23, 59, 59));
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error al generar el reporte de comandas" + e.getMessage());
        } finally {
            ent.close();
        }

    }

    /**
     * Obtiene el reporte de clientes frecuentes filtrado por nombre y número
     * mínimo de visitas
     *
     * @param nombre nombre parcial del cliente a buscar
     * @param numVisitasMinima número mínimo de visitas del cliente
     * @return Lista de ReporteClienteFrecuenteDTO con la información del
     * reporte
     */
    public List<ReporteClienteFrecuenteDTO> obtenerReporteClientesFrecuentes(String nombre, int numVisitasMinima) {
        EntityManager ent = ConexionBD.crearConexion();

        try {
            String comando = "SELECT new DTOs.ReporteClienteFrecuenteDTO("
                    + "CONCAT(c.nombres, ' ', c.apellidoPaterno, ' ', c.apellidoMaterno), COUNT(com), SUM(com.total), MAX(com.fechaRegistro))"
                    + " FROM Cliente c LEFT JOIN c.comandas com "
                    + " WHERE (:nombre IS NULL OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :nombre, '%')))"
                    + " GROUP BY c.nombres, c.apellidoPaterno, c.apellidoMaterno"
                    + " HAVING COUNT(com) >= :visitas";
            TypedQuery<ReporteClienteFrecuenteDTO> query = ent.createQuery(comando, ReporteClienteFrecuenteDTO.class)
                    .setParameter("visitas", (long) numVisitasMinima)
                    .setParameter("nombre", nombre);
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Erro al generar reporte de clientes " + e.getMessage());
        } finally {
            ent.close();
        }
    }

}
