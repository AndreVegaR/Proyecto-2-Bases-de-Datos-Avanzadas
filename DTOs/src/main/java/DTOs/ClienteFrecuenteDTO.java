package DTOs;
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
    public ClienteFrecuenteDTO(Long id,String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String fechaRegistro, String correo, int visitas, Double gastoTotal, int puntosFidelidad) {
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
    public ClienteFrecuenteDTO(Long id,String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String fechaRegistro, String correo) {
        super(id, nombres, apellidoPaterno, apellidoMaterno, telefono, fechaRegistro, correo);
    }
    
    
    /**
     * Constructor de copia:
     * Le pasas un cliente base y ya mapea todos los atributos repetidos
     * 
     * @param cliente que le copiará sus atributos
     */
    public ClienteFrecuenteDTO(ClienteDTO cliente) {
        this.setId(cliente.getId());
        this.setNombres(cliente.getNombres());
        this.setApellidoPaterno(cliente.getApellidoPaterno());
        this.setApellidoMaterno(cliente.getApellidoMaterno());
        this.setTelefono(cliente.getTelefono());
        this.setCorreo(cliente.getCorreo());
    }
    
    /**
     * Constructor vacío
     */
    public ClienteFrecuenteDTO() {
        super();
        
    }
    
    /**
     * Obtiene la información específica de este tipo de cliente
     * Le agrega los detalles a la información base
     * Será útil para presentación
     * 
     * @return un String con la información adicional
     */
    @Override
    public String getInfoAdicional() {
        String info = infoBase();
        info += "Información adicional del cliente \n"
                    + "Puntos de fidelidad: " + getPuntosFidelidad() +  "\n"
                    + "Número de visitas: " + getVisitas() + "\n"
                    + "Gasto total: $" + getGastoTotal() + "\n"; 
        return info;
    }
    
    /**
     * Muestra el tipo de cliente específico que es
     * 
     * @return el tipo de cliente
     */
    @Override
    public String getTipo() {
        return "Frecuente";
    }
    
    
    //Getters y setters
    public Long getId() {
        return id;
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