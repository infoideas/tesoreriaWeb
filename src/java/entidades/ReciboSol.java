package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.math.BigDecimal;

/**
 * ReciboSol generated by hbm2java
 */
public class ReciboSol  implements java.io.Serializable {


     private Integer id;
     private Recibo recibo;
     private Solicitud solicitud;
     private BigDecimal valor;

    public ReciboSol() {
    }

    public ReciboSol(Recibo recibo, Solicitud solicitud, BigDecimal valor) {
       this.recibo = recibo;
       this.solicitud = solicitud;
       this.valor = valor;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Recibo getRecibo() {
        return this.recibo;
    }
    
    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }
    public Solicitud getSolicitud() {
        return this.solicitud;
    }
    
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }




}


