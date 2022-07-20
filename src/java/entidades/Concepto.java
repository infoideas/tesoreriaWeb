package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Concepto generated by hbm2java
 */
public class Concepto  implements java.io.Serializable {


     private Integer id;
     private Empresa empresa;
     private String nombre;
     private char tipo;
     private Set<MovCuenta> movCuentas = new HashSet<MovCuenta>(0);

    public Concepto() {
    }

	
    public Concepto(Empresa empresa, String nombre) {
        this.empresa = empresa;
        this.nombre = nombre;
    }
    public Concepto(Empresa empresa, String nombre, Set<MovCuenta> movCuentas) {
       this.empresa = empresa;
       this.nombre = nombre;
       this.movCuentas = movCuentas;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Empresa getEmpresa() {
        return this.empresa;
    }
    
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set<MovCuenta> getMovCuentas() {
        return this.movCuentas;
    }
    
    public void setMovCuentas(Set<MovCuenta> movCuentas) {
        this.movCuentas = movCuentas;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    

}


