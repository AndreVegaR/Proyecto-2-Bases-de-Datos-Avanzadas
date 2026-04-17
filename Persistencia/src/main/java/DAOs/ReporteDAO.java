/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DTOs.ReporteClienteFrecuenteDTO;
import DTOs.ReporteComandaDTO;
import Entidades.Cliente;
import Entidades.Comanda;
import Entidades.Mesa;
import conexion.ConexionBD;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
        CriteriaBuilder cb = ent.getCriteriaBuilder();
        CriteriaQuery<ReporteComandaDTO> query = cb.createQuery(ReporteComandaDTO.class);

        //Origen y joins
        Root<Comanda> comanda = query.from(Comanda.class);
        Join<Comanda, Mesa> mesa = comanda.join("mesa");
        Join<Comanda, Cliente> cliente = comanda.join("cliente");

        //Arma la consulta con los atributos
        query.select(cb.construct(
                ReporteComandaDTO.class,
                comanda.get("fechaRegistro"),
                mesa.get("numero"),           
                comanda.get("total"),        
                comanda.get("estado"),         
                cliente.get("nombres")        
        ));

        //Filtro where con la fecha
        Predicate rangoFechas = cb.between(
                comanda.get("fechaRegistro"), 
                inicio.atStartOfDay(), 
                fin.atTime(23, 59, 59)
        );
        query.where(rangoFechas);

        try {
            return ent.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error al generar el reporte de comandas: " + e.getMessage());
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
        CriteriaBuilder cb = ent.getCriteriaBuilder();
        CriteriaQuery<ReporteClienteFrecuenteDTO> query = cb.createQuery(ReporteClienteFrecuenteDTO.class);

        //Define origen y join
        Root<Cliente> cliente = query.from(Cliente.class);
        Join<Cliente, Comanda> comanda = cliente.join("comandas", JoinType.LEFT);

        //Arma el select base
        query.select(cb.construct(
                ReporteClienteFrecuenteDTO.class,
                cb.concat(cliente.get("nombres"), 
                    cb.concat(" ", cb.concat(cliente.get("apellidoPaterno"), 
                    cb.concat(" ", cliente.get("apellidoMaterno"))))),
                cb.count(comanda),
                cb.sum(comanda.get("total")),
                cb.max(comanda.get("fechaRegistro"))
        ));

        //Parte del where para filtrar por el nombre
        if (nombre != null && !nombre.trim().isEmpty()) {
            String pattern = "%" + nombre.toLowerCase() + "%";
            query.where(cb.like(cb.lower(cliente.get("nombres")), pattern));
        }

        //Agrupa por
        query.groupBy(
                cliente.get("nombres"), 
                cliente.get("apellidoPaterno"), 
                cliente.get("apellidoMaterno")
        );

        //Número de visitas
        query.having(cb.ge(cb.count(comanda), (long) numVisitasMinima));

        try {
            return ent.createQuery(query).getResultList();
        } catch (Exception e) {
            throw new PersistenceException("Error al generar reporte de clientes: " + e.getMessage());
        } finally {
            ent.close();
        }
    }
}