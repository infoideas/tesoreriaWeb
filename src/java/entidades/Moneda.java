package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Moneda generated by hbm2java
 */
public class Moneda  implements java.io.Serializable {


     private Integer id;
     private String nombre;
     private Set<CuentaFondos> cuentaFondoses = new HashSet<CuentaFondos>(0);
     private Set<Empresa> empresas = new HashSet<Empresa>(0);

    public Moneda() {
    }

    public Moneda(String nombre, Set<CuentaFondos> cuentaFondoses, Set<Empresa> empresas) {
       this.nombre = nombre;
       this.cuentaFondoses = cuentaFondoses;
       this.empresas = empresas;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set<CuentaFondos> getCuentaFondoses() {
        return this.cuentaFondoses;
    }
    
    public void setCuentaFondoses(Set<CuentaFondos> cuentaFondoses) {
        this.cuentaFondoses = cuentaFondoses;
    }
    public Set<Empresa> getEmpresas() {
        return this.empresas;
    }
    
    public void setEmpresas(Set<Empresa> empresas) {
        this.empresas = empresas;
    }




}


