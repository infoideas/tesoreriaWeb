package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.math.BigDecimal;

/**
 * AsientoDet generated by hbm2java
 */
public class AsientoDet  implements java.io.Serializable {


     private Integer id;
     private Asiento asiento;
     private CentroCosto centroCosto;
     private Cuenta cuenta;
     private String descripcion;
     private char dc;
     private BigDecimal valor;

    public AsientoDet() {
    }

	
    public AsientoDet(Asiento asiento, Cuenta cuenta, char dc) {
        this.asiento = asiento;
        this.cuenta = cuenta;
        this.dc = dc;
    }
    public AsientoDet(Asiento asiento, CentroCosto centroCosto, Cuenta cuenta, String descripcion, char dc, BigDecimal valor) {
       this.asiento = asiento;
       this.centroCosto = centroCosto;
       this.cuenta = cuenta;
       this.descripcion = descripcion;
       this.dc = dc;
       this.valor = valor;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Asiento getAsiento() {
        return this.asiento;
    }
    
    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }
    public CentroCosto getCentroCosto() {
        return this.centroCosto;
    }
    
    public void setCentroCosto(CentroCosto centroCosto) {
        this.centroCosto = centroCosto;
    }
    public Cuenta getCuenta() {
        return this.cuenta;
    }
    
    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public char getDc() {
        return this.dc;
    }
    
    public void setDc(char dc) {
        this.dc = dc;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }




}


