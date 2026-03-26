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
import utilerias.Utilerias;


/**
 *
 * @author Jazmin
 */
public class ClienteFrecuenteBO {
    private ClienteFrecuenteDAO dao;

    public ClienteFrecuenteBO() {
        this.dao = ClienteFrecuenteDAO.getInstance();
      
    }
    /**
     * Guarda un cliente frecuente despues de validarse
     * @param clienteDTO
     * @return 
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
        cliente = dao.guardarCliente(cliente);
        //mapeo entidad guardada a dto
        return ClienteMapper.mapearEntidadDTO(cliente);
            }
    
    /**
     * Elimina un cliente frecuente por ID
     * @param id 
     */
    public void eliminarCliente(Long id){
        Utilerias.esNulo(id);
        dao.eliminarCliente(id);
    }
    /**
     * Modifica un cliente frecuente
     * @param dto
     * @return 
     */
    public ClienteFrecuenteDTO modificarCliente(ClienteFrecuenteDTO dto){
        Utilerias.esNulo(dto);
        Utilerias.esNulo(dto.getId());
        ClienteFrecuente cliente = ClienteMapper.mapearDTOEntidad(dto);
        cliente = dao.modificarCliente(cliente);
        return ClienteMapper.mapearEntidadDTO(cliente);
       
    }
    /**
     * Obtener todos los clientes frecuentes
     * @return 
     */
    public List<ClienteFrecuenteDTO> verClientes(){
        List<ClienteFrecuente> lista = dao.verClientes();
        
        return lista.stream().map(ClienteMapper :: mapearEntidadDTO)
                .collect(Collectors.toList());
    }
    
}
