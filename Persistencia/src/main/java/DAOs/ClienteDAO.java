/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import conexion.ConexionBD;
import entidades.Cliente;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Angel
 * DAO para la entidad cliente y para hacer operaciones
 */
public class ClienteDAO {
    //Instancia que compartira todo el proyecto
    private static ClienteDAO instanciaCliente;
    
    //Constructor privado
    private ClienteDAO(){
        
    }
    //Metodo que regresa la instancia
    public static ClienteDAO getInstance(){
        if(instanciaCliente == null ){
            instanciaCliente = new ClienteDAO();
        }
        return instanciaCliente;
    }
    
    //Metodo para guardar el cliente
    /*
    Se crea el entity manager y simplemente se persiste con JPA
    */
    public Cliente guardarCliente(Cliente cliente){
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return cliente;
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    //Metodo para eliminar un cliente buscandolo por su id y encontrandolo por el find de JPA
    public Cliente eliminarCliente(Long id){
        EntityManager em = ConexionBD.crearConexion();
        
        try{
            em.getTransaction().begin();
            Cliente eliminarCliente = em.find(Cliente.class, id);
            em.remove(eliminarCliente);
            em.getTransaction().commit();
            return eliminarCliente;
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    //Metodo que modifica a un cliente seleccionandolo por id y usando merge de JPA
      public Cliente modificarCliente(Long id){
            EntityManager em = ConexionBD.crearConexion();
            
            try{
                em.getTransaction().begin();
                Cliente modificar = em.find(Cliente.class, id);
                em.merge(modificar);
                return modificar;
            }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        }
      
      //Metodo que muestra a todos los clientes mediante una consulta jqpl
      public List<Cliente> verClientes(){
          EntityManager em = ConexionBD.crearConexion();

          try{
              String jpql = "SELECT c FROM Cliente c";
              return em.createQuery(jpql, Cliente.class).getResultList();
          }finally {
            em.close();
        }
          
      }
}
