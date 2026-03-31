/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Entidades.Producto;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Angel
 * DAO de producto
 */
public class ProductoDAO {
    
    //Variable para usarla en getInstance
    private static ProductoDAO instancia;
    
    //Constructo privado para que solo se puede llamar a esta clase mediante el getInstance
    private ProductoDAO(){      
    }
    /*
    *METODO QUE REGRESA UNA INSTANCIA DE PRODUCTO Y SI NO LA CREA
    *@return instancia
    */
    public static ProductoDAO getInstance(){
        if(instancia == null){
            instancia = new ProductoDAO();
        }
        return instancia;
    }
    
    /*
    *METODO PARA CREAR UN PRODUCTO
    *@return Producto 
    *@param Producto a guardar en la base de datos
    */
    public Producto guardarProducto(Producto producto){
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            return producto;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al intentar guardar el producto");
        } finally {
            em.close();
        }
    }
    
    /*
    * METODO PARA ACTUALIZAR UN PRODUCTO
    *@return Producto actualizado 
    *@param Producto a actualizarlo en la base de datos
    */
    public Producto actualizarProducto(Producto producto){
        EntityManager em = ConexionBD.crearConexion();
        try{
            em.getTransaction().begin();
            em.merge(producto);
            em.getTransaction().commit();
            return producto;
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al intentar actualizar el producto");
        } finally {
            em.close();
        }
    }
    
    /*
    * METODO PARA ELIMINAR UN PRODUCTO 
    *@param ID del producto a eliminar en la base de datos
    */
   
    /*
    Use JPQL y primero hacemos el query para usar, despues con el setParameter damos el dato del id escrito en el metodo por el de la consulta
    y use executeUpdate por que ese se usa para eliminar o actualizar
    */
    public void eliminarProducto(Long id){
        EntityManager em = ConexionBD.crearConexion();
        try{
            String Query = "DELETE p FROM Producto p WHERE p.id = :id ";
            em.createQuery(Query, Producto.class).setParameter("id", id).executeUpdate();
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al intentar actualizar el producto");
        } finally {
            em.close();
        }
    }
    
    /*
    * METODO PARA BUSCAR UN PRODUCTO POR SU ID MEDIANTE CRITERIA QUERY
    *@param ID del producto a buscar en la base de datos
    *@return Producto buscado
    */
    public Producto buscarProductoPorId(Long id){
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            //Le pedimos al entity manager el criteria para hacer consultas
            CriteriaBuilder cb = em.getCriteriaBuilder();
            //Creamos una consulta para devolver objetos tipo Producto
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            //Definimos el FROM que es como SELECT p FROM Producto p
            Root<Producto> root = query.from(Producto.class);
            //query.select(root) elejimos la entitdad Producto
            //y donde el where es cb por el CriteriaBuilder que hace permite las consultas
            //root.get("id) accedemos al id del Producto de la entity y el otro id es el del metodo
            query.select(root).where(cb.equal(root.get("id"), id));
            return em.createQuery(query).getSingleResult();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al buscar el producto por el id");
        } finally {
            em.close();
        }
    }
    
    /*
    * METODO PARA VER TODOS LOS PRODUCTOS
    *@return Lista de productos
    */
    public List<Producto> verTodosLosProductos(){
        EntityManager em = ConexionBD.crearConexion();
        try{
            //Creamos el criteriaBuilder
            CriteriaBuilder cb = em.getCriteriaBuilder();
            //Creamos la consulta que devolvera objetos de tipo producto
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            //Definimos el from
            Root<Producto> root = query.from(Producto.class);
            //Seleccionamos todos los productos
            query.select(root);
            return em.createQuery(query).getResultList();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al ver los productos");
        } finally {
            em.close();
        }
    }
}
