package DTOs;

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
    String fechaRegistro;
    
    //Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String fechaRegistro,String correo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.correo = correo;
    }
    public ClienteDTO(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String correo, String fechaRegistro) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
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

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    /**
     * Regresa la información adicional para la pantalla de la tabla
     * Se declara en la superclase solo para afirmar que cualquier subclase tiene este método
     * No debería hacer nada aquí, y eso está bien
     * 
     * @return nada en este caso en específico; no se necesita
     */
    public String getInfoAdicional() {
        return "";
    }
    
}
