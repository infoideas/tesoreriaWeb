package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import java.util.Date;

/**
 * MovCuentaDet generated by hbm2java
 */
public class MovCuentaDet  implements java.io.Serializable {


     private Integer id;
     private FormaPago formaPago;
     private MovCuenta movCuenta;
     private Integer idBt;
     private String numBt;
     private Character tipoCheque;
     private Date vencCheque;
     private BigDecimal valor;
     private Integer idCartera;

    public MovCuentaDet() {
    }

	
    public MovCuentaDet(FormaPago formaPago, MovCuenta movCuenta, BigDecimal valor) {
        this.formaPago = formaPago;
        this.movCuenta = movCuenta;
        this.valor = valor;
    }
    public MovCuentaDet(FormaPago formaPago, MovCuenta movCuenta, Integer idBt, String numBt, Character tipoCheque, Date vencCheque, BigDecimal valor, Integer idCartera) {
       this.formaPago = formaPago;
       this.movCuenta = movCuenta;
       this.idBt = idBt;
       this.numBt = numBt;
       this.tipoCheque = tipoCheque;
       this.vencCheque = vencCheque;
       this.valor = valor;
       this.idCartera = idCartera;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public FormaPago getFormaPago() {
        return this.formaPago;
    }
    
    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
    public MovCuenta getMovCuenta() {
        return this.movCuenta;
    }
    
    public void setMovCuenta(MovCuenta movCuenta) {
        this.movCuenta = movCuenta;
    }
    public Integer getIdBt() {
        return this.idBt;
    }
    
    public void setIdBt(Integer idBt) {
        this.idBt = idBt;
    }
    public String getNumBt() {
        return this.numBt;
    }
    
    public void setNumBt(String numBt) {
        this.numBt = numBt;
    }
    public Character getTipoCheque() {
        return this.tipoCheque;
    }
    
    public void setTipoCheque(Character tipoCheque) {
        this.tipoCheque = tipoCheque;
    }
    public Date getVencCheque() {
        return this.vencCheque;
    }
    
    public void setVencCheque(Date vencCheque) {
        this.vencCheque = vencCheque;
    }
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public Integer getIdCartera() {
        return this.idCartera;
    }
    
    public void setIdCartera(Integer idCartera) {
        this.idCartera = idCartera;
    }




}


