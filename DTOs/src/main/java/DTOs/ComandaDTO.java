package DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andre
 */
public class ComandaDTO {
    
    //Atributos
    private Long id;
    private double total;
    private String folio;
    private String estado;
    private String comentarios;
    private MesaDTO mesa;
    private ClienteDTO cliente;
    private List<DetallesComandaDTO> detalles = new ArrayList<>();

    //Constructores
    public ComandaDTO() {}
    public ComandaDTO(Long id, double total, String folio, String estado, String comentarios) {
        this.id = id;
        this.total = total;
        this.folio = folio;
        this.estado = estado;
        this.comentarios = comentarios;
    }
    public ComandaDTO(double total, String folio, String estado, String comentarios, MesaDTO mesa, ClienteDTO cliente) {
        this.total = total;
        this.folio = folio;
        this.estado = estado;
        this.comentarios = comentarios;
        this.mesa = mesa;
        this.cliente = cliente;
    }
    
    //Getters y setters
    public double getTotal() {
        double total = 0.0;
        for (DetallesComandaDTO detalle: getDetalles()) {
            total += detalle.getSubtotal();
        }
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public MesaDTO getMesa() {
        return mesa;
    }

    public void setMesa(MesaDTO mesa) {
        this.mesa = mesa;
    }
    
    public List<DetallesComandaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesComandaDTO> detalles) {
        this.detalles = detalles;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}