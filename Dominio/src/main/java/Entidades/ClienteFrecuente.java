package Entidades;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Representa a un cliente registrado en el sistema debido a que va al restaurante frecuentemente
 * Se le asignan comandas y gana puntos de fidelidad
 */
@Entity
public class ClienteFrecuente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clienteFrecuente")
    private Long id;
    
    @Column(name = "nombres", nullable = false, length = 30)
    private String nombres;
    
    @Column(name = "apellido_paterno", nullable = false, length = 30)
    private String apellidoPaterno;
            
    @Column(name = "apellido_materno", nullable = false, length = 30)
    private String apellidoMaterno;
    
    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;
    
    @Column(name = "correo", nullable = true, length = 50)
    private String correo;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column(name = "visitas", nullable = false)
    private int visitas = 0;
    
    @Column(name = "gasto_total", nullable = false)
    private double gastoTotal = 0;
    
    @OneToMany(mappedBy = "cliente")
    private List<Comanda> comandas = new ArrayList<>();
    
    @Transient
    private int puntosFidelidad;

    
    
    //Constructores
    /**
     * Constructor completo
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param correo 
     */
    public ClienteFrecuente(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String correo) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
    }
    
    /**
     * Constructor sin correo (pues es opcional)
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     */
    public ClienteFrecuente(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
    }
    
    /**
     * Constructor vacío
     */
    public ClienteFrecuente() {
    }
    
    
    
    /**
     * Asigna una comanda al cliente añadiéndola a lista y actualizando gasto total
     * 
     * @param comanda a asignar
     */
    private void asignarComanda(Comanda comanda) {
        if (comanda == null) {
            throw new IllegalArgumentException("Comanda nula");
        }

        if (comandas.contains(comanda)) {
            throw new IllegalStateException("Comanda ya registrada");
        }

        if (!"Cerrada".equals(comanda.getEstado())) {
            throw new IllegalStateException("Solo se pueden registrar comandas cerradas");
        }
        
        comandas.add(comanda);
        gastoTotal += comanda.getTotal();
    }
    
    
    
    //Getters y setters
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

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public double getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    /**
     * Como es atributo calculado, siempre obtiene el valor de la cantidad actualizada de gastoTotal
     * No se guarda en otros lugares, por lo que se ata a un valor establecido (gastoTotal)
     * 
     * @return puntos de fidelidad, que es el gasto total del cliente dividido entre 20, casteado a int
     */
    public int getPuntosFidelidad() {
        return (int) (gastoTotal/20);
    }

    public void setPuntosFidelidad(int puntosFidelidad) {
        this.puntosFidelidad = puntosFidelidad;
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
        if (!(object instanceof ClienteFrecuente)) {
            return false;
        }
        ClienteFrecuente other = (ClienteFrecuente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dominio.ClienteFrecuente[ id=" + id + " ]";
    }
}