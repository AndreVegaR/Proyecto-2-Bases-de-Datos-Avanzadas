/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.MeseroDAO;
import DTOs.MeseroDTO;
import Entidades.Mesero;
import excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import utilerias.UtilNegocio;

/**
 * Creo que esto no va a aqui, considera pasar la logica de validar pines en quiza UtilNegocio
 * 
 * @author Andre
 */
public class MeseroBO {
    
    private static MeseroBO instancia = null;

    private MeseroBO() {
    }
    public static MeseroBO getInstance() {
        if (instancia == null) {
            instancia = new MeseroBO();
        }
        return instancia;
    }
    
    
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
     * Regresa un mesero con base en su pin
     * 
     * @param pin
     * @return 
     */
    public MeseroDTO consultarMesero(String pin) {
        if (pin == null) {
            throw new NegocioException("Pin nulo");
        }
        UtilNegocio.validarDescripcion(pin);
        Mesero meseroEncontrado = MeseroDAO.getInstance().buscarPorPin(pin);
        if (meseroEncontrado != null) {
            return mapearEntidadDTO(meseroEncontrado);
        }
        return null;
    }
    
    
    /**
     * Regresa un arreglo con los pines de los meseros que ya existen
     * 
     * @return 
     */
    public List<String> consultarPinesExistentes() {
        List<MeseroDTO> meseros = consultarMeseros();
        if (meseros != null) {
            List<String> pines = new ArrayList<>();
            for (MeseroDTO m: meseros) {
                pines.add(m.getPin());
            }
            return pines;
        }
        return null;
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