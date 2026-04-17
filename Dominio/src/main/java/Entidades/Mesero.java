package Entidades;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Mesero que administra las comandas
 * 
 * @author Andre
 */
@Entity
@Table(name = "meseros")
public class Mesero implements Serializable {
    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesero")
    private Long id;
    
    @Column(name = "nombres", nullable = false, length = 30)
    private String nombres;
    
    @Column(name = "apellido_paterno", nullable = false, length = 30)
    private String apellidoPaterno;
            
    @Column(name = "apellido_materno", nullable = false, length = 30)
    private String apellidoMaterno;
    
    @Column(name = "pin", nullable = false, unique = true)
    private String pin;

    public Mesero() {
    }

    public Mesero(String nombres, String apellidoPaterno, String apellidoMaterno, String pin) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.pin = pin;
    }

    public Mesero(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String pin) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.pin = pin;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}