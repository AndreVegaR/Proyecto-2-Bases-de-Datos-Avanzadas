package DTOs;

/**
 * DTO de una mesa
 * 
 * @author Andre
 */
public class MesaDTO {
    
    //Atributos
    private Long id;
    private String estadoMesa;
    private int numero;  

    //Constructores
    public MesaDTO() {}
    public MesaDTO(String estadoMesa, int numero) {
        this.estadoMesa = estadoMesa;
        this.numero = numero;
    }
    
    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(String estadoMesa) {
        this.estadoMesa = estadoMesa;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}