/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.ClienteFrecuenteDAO;
import DTOs.ClienteFrecuenteDTO;
import Entidades.ClienteFrecuente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ClienteMapper;
import utilerias.EncriptarTelefono;
import utilerias.Utilerias;


/**
 *Logica de negocio para ClienteFrecuentes
 * @author Jazmin
 */
public class ClienteFrecuenteBO {
    
    private ClienteFrecuenteDAO dao;
    

    /**
     * Constructor que inicializa el DAO usando el patrón Singleton
     */

    

    public ClienteFrecuenteBO() {
        this.dao = ClienteFrecuenteDAO.getInstance();
      
    }
    /**
     * Guarda un cliente frecuente despues de validarse
     * @param clienteDTO datos del cliente capturado desde la capa de presentación
     * @return ClienteFrecuenteDTO con los datos guardados.
     */
    public ClienteFrecuenteDTO guardarCliente(ClienteFrecuenteDTO clienteDTO){
        Utilerias.esNulo(clienteDTO);
        Utilerias.esCadenadaVacia(clienteDTO.getNombres(), "Nombres");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoPaterno(), "Apellido Paterno");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoMaterno(), "Apellido Materno");
        Utilerias.esCadenadaVacia(clienteDTO.getTelefono(),"Teléfono");
        
        if(clienteDTO.getFechaRegistro() == null){
            clienteDTO.setFechaRegistro(LocalDateTime.now());
        }
        //mapeo dto a entidad
        ClienteFrecuente cliente = ClienteMapper.mapearDTOEntidad(clienteDTO);
        //encriptar antes de guardar
        String telefono = cliente.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefono);
        cliente.setTelefono(telefonoEncriptado);
        cliente = dao.guardarCliente(cliente);
        //mapeo entidad guardada a dto
        ClienteFrecuenteDTO resultado = ClienteMapper.mapearEntidadDTO(cliente);
        //desencriptar para volverlo legible 
        String telefonoDesencriptado = EncriptarTelefono.desencriptar(resultado.getTelefono());
        resultado.setTelefono(telefonoDesencriptado);
        return resultado;
            }
    
    /**
     * Elimina un cliente frecuente por ID
     * @param id identificador unico del cliente a eliminar
     */
    public void eliminarCliente(Long id){
        Utilerias.esNulo(id);
        dao.eliminarCliente(id);
    }
    /**
     * Modifica un cliente frecuente
     * @param dto datos modificados del cliente frecuente
     * @return ClienteFrecuenteDTO con la informacion actualizada.
     */
    public ClienteFrecuenteDTO modificarCliente(ClienteFrecuenteDTO dto){
        Utilerias.esNulo(dto);
        Utilerias.esNulo(dto.getId());
        Utilerias.esCadenadaVacia(dto.getNombres(), "Nombres");
        Utilerias.esCadenadaVacia(dto.getApellidoPaterno(), "Apellido Paterno");
        Utilerias.esCadenadaVacia(dto.getApellidoMaterno(), "Apellido Materno");
        Utilerias.esCadenadaVacia(dto.getTelefono(),"Teléfono");
        
        if(dto.getFechaRegistro() == null){
            dto.setFechaRegistro(LocalDateTime.now());
        }
        
        ClienteFrecuente cliente = ClienteMapper.mapearDTOEntidad(dto);
         //encriptar antes de actualizar
        String telefono = cliente.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefono);
        cliente.setTelefono(telefonoEncriptado);
        
        cliente = dao.modificarCliente(cliente);
        
        ClienteFrecuenteDTO resultado = ClienteMapper.mapearEntidadDTO(cliente);
        //desencriptar para volverlo legible 
        String telefonoDesencriptado = EncriptarTelefono.desencriptar(resultado.getTelefono());
        resultado.setTelefono(telefonoDesencriptado);
        return resultado;
      
       
    }
    /**
     * Obtiene la lista de todos los clientes frecuentes registrados.
     * @return lista de ClienteFrecuente con los datos actualizados
     */
    public List<ClienteFrecuenteDTO> verClientes(){
        List<ClienteFrecuente> lista = dao.verClientes();
        
        List<ClienteFrecuenteDTO> listaDtos = lista.stream().map(ClienteMapper :: mapearEntidadDTO)
                .collect(Collectors.toList());
        listaDtos.forEach(dto -> {
            String telEncriptado = dto.getTelefono();
            String telDesencriptado = EncriptarTelefono.desencriptar(telEncriptado);
            dto.setTelefono(telEncriptado);
        });
        return listaDtos;
    }
    
}
