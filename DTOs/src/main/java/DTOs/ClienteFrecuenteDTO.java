package DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para un cliente
 * @author Andre
 */
public class ClienteFrecuenteDTO {
    
    //Atributos de cliente
    Long id;
    String nombres;
    String apellidoPaterno;
    String apellidoMaterno;
    String telefono;
    String correo;
    LocalDateTime fechaRegistro;
    
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
        this.id=id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.correo = correo;
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
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.correo = correo;
    }
    
    /**
     * Constructor vacío
     */
    public ClienteFrecuenteDTO() {
        
    }
    
    public Long getId() {
        return id;
    }

    //Getters y setters
    public void setId(Long id) {    
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

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
}