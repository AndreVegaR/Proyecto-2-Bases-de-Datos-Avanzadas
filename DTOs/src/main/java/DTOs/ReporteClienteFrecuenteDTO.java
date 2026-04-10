/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.time.LocalDateTime;

/**
 *  DTO utlizado para la información del reporte de clientes
 * @author Jazmin
 */
public class ReporteClienteFrecuenteDTO {
    private String nombre;
    private Long visitas;
    private Double totalGastado;
    private LocalDateTime fechaUltimaComanda;

    public ReporteClienteFrecuenteDTO() {
    }

    public ReporteClienteFrecuenteDTO(String nombre, Long visitas, Double totalGastado, LocalDateTime fechaUltimaComanda) {
        this.nombre = nombre;
        this.visitas = visitas;
        this.totalGastado = totalGastado;
        this.fechaUltimaComanda = fechaUltimaComanda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getVisitas() {
        return visitas;
    }

    public void setVisitas(Long visitas) {
        this.visitas = visitas;
    }

    public Double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(Double totalGastado) {
        this.totalGastado = totalGastado;
    }

    public LocalDateTime getFechaUltimaComanda() {
        return fechaUltimaComanda;
    }

    public void setFechaUltimaComanda(LocalDateTime fechaUltimaComanda) {
        this.fechaUltimaComanda = fechaUltimaComanda;
    }
    
    
    
}
