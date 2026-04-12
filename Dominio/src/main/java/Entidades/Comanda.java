package Entidades;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Representa a una comanda, una orden pedida por clientes
 * Puede estar relacionada o no a un cliente frecuente previamente registrado
 * 
 * @author Andre
 */
@Entity
@Table(name = "comanda")
public class Comanda implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comanda")
    private Long id;
    
    @Column(name = "total", nullable = false)
    private double total;
    
    @Column(name = "folio", nullable = false, length = 15)
    private String folio;
    
    @Column(name = "estado", nullable = false, length = 30)
    private String estado;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    //Le pertenece a un cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = true)
    private Cliente cliente;
    
    //Está asignada a una mesa
    @ManyToOne
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    //Tiene muchos detallesComanda
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallesComanda> detalles = new ArrayList<>();
    
    /**
     * Constructores
     */
    public Comanda() {}
    public Comanda(double total, String folio, String estado, Cliente cliente, Mesa mesa) {
        this.total = total;
        this.folio = folio;
        this.estado = estado;
        this.cliente = cliente;
        this.mesa = mesa;
    }
    public Comanda(double total, String folio, String estado, Cliente cliente, Mesa mesa, LocalDateTime fechaRegistro) {
        this.total = total;
        this.folio = folio;
        this.estado = estado;
        this.cliente = cliente;
        this.mesa = mesa;
        this.fechaRegistro = fechaRegistro;
    }
    
    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
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

    public void setEstado(String estadoComanda) {
        this.estado = estadoComanda;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<DetallesComanda> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallesComanda> detalles) {
        this.detalles = detalles;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}