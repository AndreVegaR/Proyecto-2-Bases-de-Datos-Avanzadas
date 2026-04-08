/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.time.LocalDateTime;

/**
 * DTO utlizado para la información del reporte de comandas
 * @author Jazmin
 */
public class ReporteComandaDTO {
    private LocalDateTime fecha;
    private String mesa;
    private Double total;
    private String estado;
    private String cliente;

    public ReporteComandaDTO() {
    }

    public ReporteComandaDTO(LocalDateTime fecha, String mesa, Double total, String estado, String cliente) {
        this.fecha = fecha;
        this.mesa = mesa;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
    
    
    
}
