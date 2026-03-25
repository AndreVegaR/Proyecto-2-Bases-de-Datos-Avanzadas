package Entidades;
import Enumeradores.EstadoComanda;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



/**
 * Representa a una comanda, una orden pedida por clientes
 * Puede estar relacionada o no a un cliente frecuente previamente registrado
 */
@Entity
public class Comanda implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comanda")
    private Long id;
    
    @Column(name = "total", nullable = false)
    private double total;
    
    @Column(name = "folio", nullable = false, length = 15)
    private String folio;
    
    //Una comanda es abierta apenas se crea
    @Column(name = "estado", nullable = false)
    private EstadoComanda estadoComanda = EstadoComanda.ABIERTA;
    
    @ManyToOne
    @JoinColumn(name = "id_clienteFrecuente")
    @Column(name = "cliente", nullable = true)
    private ClienteFrecuente cliente;
    
    @Column(name = "comentarios", nullable = true, length = 200)
    private String comentarios;
    
    //Cuenta cada comanda aumentando por cada folio generado
    private static int contador = 0;
    
    //Registro de la fecha actual para reiniciar el contador
    private static LocalDate ultimaFecha = LocalDate.now();
    
    //@OneToMany(mappedBy = "comanda")
    //private List<DetalleComanda> detalles = new ArrayList<>();

    
    
    /**
     * Constructor vacío
     */
    public Comanda() {
        
    }
    
    
    
    /**
     * Genera un folio según un formato establecido
     * Extrae los datos de la fecha y genera un código numérico aleatorio
     * Formato del folio: OB-YYYYMMDD-XXX
     */
    private void generarFolio() {
        String PREFIJO = "OB";
        
        //Compara la fecha de hoy con la última guardada, si no coinciden reinicia contador y actualiza ultimaFecha
        LocalDate hoy = LocalDate.now();
        if (!hoy.equals(ultimaFecha)) {
            contador = 0;
            ultimaFecha = hoy;
        }

        //Transforma la información de la fecha en String en fomato AAAAMMDD
        String fechaString = ultimaFecha.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        //Aplica formato al número único consecutivo
        String clave = String.format("%03d", contador++);
        
        //Le da valor al folio
        folio = PREFIJO + "-" + fechaString + "-" + clave;
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

    public EstadoComanda getEstadoComanda() {
        return estadoComanda;
    }

    public void setEstadoComanda(EstadoComanda estadoComanda) {
        this.estadoComanda = estadoComanda;
    }

    public ClienteFrecuente getCliente() {
        return cliente;
    }

    public void setCliente(ClienteFrecuente cliente) {
        this.cliente = cliente;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Comanda.contador = contador;
    }

    public static LocalDate getUltimaFecha() {
        return ultimaFecha;
    }

    public static void setUltimaFecha(LocalDate ultimaFecha) {
        Comanda.ultimaFecha = ultimaFecha;
    }
    
    
    
    //Misceláneos
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comanda)) {
            return false;
        }
        Comanda other = (Comanda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dominio.Comanda[ id=" + id + " ]";
    }
}