package DTOs;
import java.time.LocalDateTime;

/**
 * DTO base para la entidad Cliente.
 * Contiene únicamente los campos del cliente genérico,
 * sin importar si es frecuente o no.
 * @author Jazmin
 */
public class ClienteDTO {
     //Atributos de cliente
    Long id;
    String nombres;
    String apellidoPaterno;
    String apellidoMaterno;
    String telefono;
    String correo;
    LocalDateTime fechaRegistro;
    //Constructor vacio
    public ClienteDTO() {
    }
    
    /**
     * Cosntructor completo
     * @param id
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param fechaRegistro
     * @param correo 
     */
    public ClienteDTO(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro,String correo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.correo = correo;
        
    }
    //Getter y Setter
    public Long getId() {
        return id;
    }

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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
    
}
