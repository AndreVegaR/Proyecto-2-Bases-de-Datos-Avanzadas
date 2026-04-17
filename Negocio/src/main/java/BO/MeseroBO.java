/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.MeseroDAO;
import DTOs.MeseroDTO;
import Entidades.Mesero;
import java.util.ArrayList;
import java.util.List;

/**
 * Creo que esto no va a aqui, considera pasar la logica de validar pines en quiza UtilNegocio
 * 
 * @author Andre
 */
public class MeseroBO {
    
    /**
     * Consulta los meseros para presentación y así validar los pines
     * 
     * @return 
     */
    public List<MeseroDTO> consultarMeseros() {
        List<Mesero> entidades = MeseroDAO.getInstance().consultarMeseros();
        List<MeseroDTO> dtos = new ArrayList<>();
        for (Mesero e: entidades) {
            MeseroDTO dto = mapearEntidadDTO(e);
            dtos.add(dto);
        }
        return dtos;
    }
    
    
    
    /**
     * Mapea de entidad a DTO
     * Privado porque solo se usa aquí
     * 
     * @param entidad a mapear
     * 
     * @return entidad mapeada 
     */
    private MeseroDTO mapearEntidadDTO(Mesero entidad) {
        MeseroDTO dto = new MeseroDTO();
        dto.setNombres(entidad.getNombres());
        dto.setApellidoPaterno(entidad.getApellidoPaterno());
        dto.setApellidoMaterno(entidad.getApellidoMaterno());
        dto.setPin(entidad.getPin());
        return dto;
    }
}