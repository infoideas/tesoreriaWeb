package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Empresa generated by hbm2java
 */
public class Empresa  implements java.io.Serializable {


     private Integer id;
     private Moneda moneda;
     private String nombre;
     private String cuit;
     private Set<Ejercicio> ejercicios = new HashSet<Ejercicio>(0);
     private Set<Recibo> recibos = new HashSet<Recibo>(0);
     private Set<CategoriaPercepcion> categoriaPercepcions = new HashSet<CategoriaPercepcion>(0);
     private Set<CuentaFondos> cuentaFondoses = new HashSet<CuentaFondos>(0);
     private Set<Solicitud> solicituds = new HashSet<Solicitud>(0);
     private Set<Concepto> conceptos = new HashSet<Concepto>(0);
     private Set<CategoriaRetencion> categoriaRetencions = new HashSet<CategoriaRetencion>(0);
     private Set<Iva> ivas = new HashSet<Iva>(0);

    public Empresa() {
    }

	
    public Empresa(Moneda moneda, String nombre, String cuit) {
        this.moneda = moneda;
        this.nombre = nombre;
        this.cuit = cuit;
    }
    public Empresa(Moneda moneda, String nombre, String cuit, Set<Ejercicio> ejercicios, Set<Recibo> recibos, Set<CategoriaPercepcion> categoriaPercepcions, Set<CuentaFondos> cuentaFondoses, Set<Solicitud> solicituds, Set<Concepto> conceptos, Set<CategoriaRetencion> categoriaRetencions, Set<Iva> ivas) {
       this.moneda = moneda;
       this.nombre = nombre;
       this.cuit = cuit;
       this.ejercicios = ejercicios;
       this.recibos = recibos;
       this.categoriaPercepcions = categoriaPercepcions;
       this.cuentaFondoses = cuentaFondoses;
       this.solicituds = solicituds;
       this.conceptos = conceptos;
       this.categoriaRetencions = categoriaRetencions;
       this.ivas = ivas;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Moneda getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCuit() {
        return this.cuit;
    }
    
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public Set<Ejercicio> getEjercicios() {
        return this.ejercicios;
    }
    
    public void setEjercicios(Set<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
    public Set<Recibo> getRecibos() {
        return this.recibos;
    }
    
    public void setRecibos(Set<Recibo> recibos) {
        this.recibos = recibos;
    }
    public Set<CategoriaPercepcion> getCategoriaPercepcions() {
        return this.categoriaPercepcions;
    }
    
    public void setCategoriaPercepcions(Set<CategoriaPercepcion> categoriaPercepcions) {
        this.categoriaPercepcions = categoriaPercepcions;
    }
    public Set<CuentaFondos> getCuentaFondoses() {
        return this.cuentaFondoses;
    }
    
    public void setCuentaFondoses(Set<CuentaFondos> cuentaFondoses) {
        this.cuentaFondoses = cuentaFondoses;
    }
    public Set<Solicitud> getSolicituds() {
        return this.solicituds;
    }
    
    public void setSolicituds(Set<Solicitud> solicituds) {
        this.solicituds = solicituds;
    }
    public Set<Concepto> getConceptos() {
        return this.conceptos;
    }
    
    public void setConceptos(Set<Concepto> conceptos) {
        this.conceptos = conceptos;
    }
    public Set<CategoriaRetencion> getCategoriaRetencions() {
        return this.categoriaRetencions;
    }
    
    public void setCategoriaRetencions(Set<CategoriaRetencion> categoriaRetencions) {
        this.categoriaRetencions = categoriaRetencions;
    }
    public Set<Iva> getIvas() {
        return this.ivas;
    }
    
    public void setIvas(Set<Iva> ivas) {
        this.ivas = ivas;
    }




}


