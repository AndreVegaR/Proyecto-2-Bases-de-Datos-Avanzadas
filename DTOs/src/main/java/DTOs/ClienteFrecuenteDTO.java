package DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para un cliente
 * @author Andre
 */
public class ClienteFrecuenteDTO extends ClienteDTO{
   
    //Atributos cliente frecuente
    int visitas;
    Double gastoTotal;
    int puntosFidelidad;

    /**
     * Constructor lleno
     * @param id
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param correo
     * @param fechaRegistro
     */
    public ClienteFrecuenteDTO(Long id,String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro, String correo, int visitas, Double gastoTotal, int puntosFidelidad) {
        super(id, nombres, apellidoPaterno, apellidoMaterno, telefono, fechaRegistro, correo);
        this.visitas = visitas;
        this.gastoTotal = gastoTotal;
        this.puntosFidelidad = puntosFidelidad;
       
    }
    
    /**
     * Constructor normal
     * @param id
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param correo
     * @param fechaRegistro
     */
    public ClienteFrecuenteDTO(Long id,String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro, String correo) {
        super(id, nombres, apellidoPaterno, apellidoMaterno, telefono, fechaRegistro, correo);
    }
    
    /**
     * Constructor vacío
     */
    public ClienteFrecuenteDTO() {
        super();
        
    }
    
    public Long getId() {
        return id;
    }

    //Getters y setters
   
    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public Double getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(Double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    public int getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
    } 
    
    /**
     * Obtiene la información específica de este tipo de cliente
     * Será útil para presentación
     * 
     * @return un String con la información adicional
     */
    @Override
    public String getInfoAdicional() {
        String info = "Información adicional del cliente " + getId() + ": \n"
                      + "Puntos de fidelidad: " + getPuntosFidelidad() +  "\n"
                      + "Número de visitas: " + getVisitas() + "\n"
                      + "Gasto total: " + getGastoTotal() + "\n";
        
        return info;
    }
}