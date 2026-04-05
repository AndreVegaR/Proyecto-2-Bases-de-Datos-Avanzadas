/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Entidades.Ingrediente;
import Enumeradores.UnidadMedida;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * DAO para la entidad Ingrediente
 *
 * @author Jazmin
 */
public class IngredienteDAO {

    private static IngredienteDAO instancia;

    public IngredienteDAO() {
    }

    public static IngredienteDAO getInstance() {
        if (instancia == null) {
            instancia = new IngredienteDAO();
        }
        return instancia;
    }

    /**
     * Guarda un ingrediente nuevo
     *
     * @param ingrediente a persistir
     * @return ingrediente con ID generado
     */
    public Ingrediente guardarIngrediente(Ingrediente ingrediente) {
        EntityManager ent = ConexionBD.crearConexion();

        try {
            ent.getTransaction().begin();
            ent.persist(ingrediente);
            ent.getTransaction().commit();
            return ingrediente;
        } catch (Exception e) {
            if (ent.getTransaction().isActive()) {
                ent.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el ingrediente");
        } finally {
            ent.close();
        }
    }

    /**
     * Actualiza el stock de un ingrediente existente
     *
     * @param id del ingrediente a actualizar
     * @param stock nuevo valor del stock
     * @return ingrediente con el stock actualizado
     */
    public Ingrediente actualizarStock(Long id, Double stock) {
        EntityManager ent = ConexionBD.crearConexion();
        try {
            ent.getTransaction().begin();
            Ingrediente existente = ent.find(Ingrediente.class, id);
            if (existente == null) {
                throw new PersistenciaException("No se encontró el Ingrediente con ID: " + id);
            }
            existente.setStock(stock);
            ent.getTransaction().commit();
            return existente;
        } catch (Exception e) {
            if (ent.getTransaction().isActive()) {
                ent.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el stock del ingrediente");
        } finally {
            ent.close();
        }
    }

    /**
     * Elimina un ingrediente por su ID
     *
     * @param id del ingrediente
     * @return ingrediente eliminado
     */
    public Ingrediente eliminarIngrediente(Long id) {
        EntityManager ent = ConexionBD.crearConexion();
        try {
            ent.getTransaction().begin();
            Ingrediente ingrediente = ent.find(Ingrediente.class, id);
            if (ingrediente != null) {
                ent.remove(ingrediente);
            }
            ent.getTransaction().commit();
            return ingrediente;
        } catch (Exception e) {
            if (ent.getTransaction().isActive()) {
                ent.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el ingrediente");
        } finally {
            ent.close();
        }
    }

    /**
     * Busca un ingrediente por su ID
     *
     * @param id del ingrediente
     * @return ingrediente encontrado o null
     */
    public Ingrediente buscarPorId(Long id) {
        EntityManager ent = ConexionBD.crearConexion();
        try {
            return ent.find(Ingrediente.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el ingrediente con ID: " + id);
        } finally {
            ent.close();
        }
    }

    /**
     * Busca ingredientes por nombre parcial o unidad de medida Se usa desde el
     * módulo de productos para seleccionar ingredientes
     *
     * @param nombre parcial del ingrediente (puede ser vacío)
     * @param unidadMedida a filtrar (puede ser null)
     * @return lista de ingredientes que coinciden
     */
    public List<Ingrediente> buscarIngredientes(String nombre, UnidadMedida unidadMedida) {
        EntityManager ent = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT i FROM Ingrediente i WHERE "
                    + "(:nombre IS NULL OR LOWER(i.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND "
                    + "(:unidadMedida IS NULL OR i.unidadMedida = :unidadMedida)";
            return ent.createQuery(jpql, Ingrediente.class).setParameter("nombre", nombre)
                    .setParameter("unidadMedida", unidadMedida).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistenciaException("Error al buscar los ingredientes " + e);
        } finally {
            ent.close();
        }
    }

    /**
     * Verifica si ya existe un ingrediente con el mismo nombre y unidad de
     * medida Se usa para evitar duplicados antes de guardar o actualizar
     *
     * @param nombre del ingrediente
     * @param unidadMedida del ingrediente
     * @return true si ya existe, false si no
     */
    public boolean existeIngrediente(String nombre, UnidadMedida unidadMedida) {
        EntityManager ent = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT COUNT(i) FROM Ingrediente i WHERE i.nombre = :nombre "
                    + "AND i.unidadMedida = :unidadMedida";
            Long count = ent.createQuery(jpql, Long.class)
                    .setParameter("nombre", nombre)
                    .setParameter("unidadMedida", unidadMedida)
                    .getSingleResult();
            //si ya existe no se podra guardar
            return count > 0;
        } catch (Exception e) {
            throw new PersistenciaException("Ya existe un ingrediente con ese nombre y unidad de medida");
        } finally {
            ent.close();
        }
    }
}
