package Entidades;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

/**
 * Representa a un cliente registrado en el sistema
 * @author Andre
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    
    @Column(name = "nombres", nullable = false, length = 30)
    private String nombres;
    
    @Column(name = "apellido_paterno", nullable = false, length = 30)
    private String apellidoPaterno;
            
    @Column(name = "apellido_materno", nullable = false, length = 30)
    private String apellidoMaterno;
    
    @Column(name = "telefono", nullable = false, length = 255)
    private String telefono;
    
    @Column(name = "correo", nullable = true, length = 50)
    private String correo;
    
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @OneToMany(mappedBy = "cliente", 
           cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
           fetch = FetchType.EAGER)
    private List<Comanda> comandas = new ArrayList<>();
    
    /**
     * Constructor completo
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param correo 
     */
    public Cliente(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro, String correo) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.correo = correo;
    }
    
    /**
     * Constructor sin correo
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     */
    public Cliente(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }
    
    /**
     * Constructor vacío
     */
    public Cliente() {
        
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

    public List<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }
    
    /**
     * Envuelve la lógica de agregar una comanda sin afectar directamente a la lista
     * 
     * @param comanda a agregar
     */
    public void agregarComanda(Comanda comanda) {
        comandas.add(comanda);
        comanda.setCliente(this);
    }
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cliente other = (Cliente) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}