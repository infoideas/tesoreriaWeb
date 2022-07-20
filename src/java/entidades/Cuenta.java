package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Cuenta generated by hbm2java
 */
public class Cuenta  implements java.io.Serializable {


     private Integer id;
     private Ejercicio ejercicio;
     private String cuentaNumero;
     private String cuentaNombre;
     private char dc;
     private short nivel;
     private char imputable;
     private char cc;
     private char ajustaInflacion;
     private char flujoFondos;
     private Set<AsientoDet> asientoDets = new HashSet<AsientoDet>(0);
     private Set<MovPeriodo> movPeriodos = new HashSet<MovPeriodo>(0);
     private Set<PlantillaDet> plantillaDets = new HashSet<PlantillaDet>(0);
     private Set<Iva> ivas = new HashSet<Iva>(0);

    public Cuenta() {
    }

	
    public Cuenta(Ejercicio ejercicio, String cuentaNumero, String cuentaNombre, char dc, short nivel, char imputable, char cc, char ajustaInflacion, char flujoFondos) {
        this.ejercicio = ejercicio;
        this.cuentaNumero = cuentaNumero;
        this.cuentaNombre = cuentaNombre;
        this.dc = dc;
        this.nivel = nivel;
        this.imputable = imputable;
        this.cc = cc;
        this.ajustaInflacion = ajustaInflacion;
        this.flujoFondos = flujoFondos;
    }
    public Cuenta(Ejercicio ejercicio, String cuentaNumero, String cuentaNombre, char dc, short nivel, char imputable, char cc, char ajustaInflacion, char flujoFondos, Set<AsientoDet> asientoDets, Set<MovPeriodo> movPeriodos, Set<PlantillaDet> plantillaDets, Set<Iva> ivas) {
       this.ejercicio = ejercicio;
       this.cuentaNumero = cuentaNumero;
       this.cuentaNombre = cuentaNombre;
       this.dc = dc;
       this.nivel = nivel;
       this.imputable = imputable;
       this.cc = cc;
       this.ajustaInflacion = ajustaInflacion;
       this.flujoFondos = flujoFondos;
       this.asientoDets = asientoDets;
       this.movPeriodos = movPeriodos;
       this.plantillaDets = plantillaDets;
       this.ivas = ivas;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Ejercicio getEjercicio() {
        return this.ejercicio;
    }
    
    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }
    public String getCuentaNumero() {
        return this.cuentaNumero;
    }
    
    public void setCuentaNumero(String cuentaNumero) {
        this.cuentaNumero = cuentaNumero;
    }
    public String getCuentaNombre() {
        return this.cuentaNombre;
    }
    
    public void setCuentaNombre(String cuentaNombre) {
        this.cuentaNombre = cuentaNombre;
    }
    public char getDc() {
        return this.dc;
    }
    
    public void setDc(char dc) {
        this.dc = dc;
    }
    public short getNivel() {
        return this.nivel;
    }
    
    public void setNivel(short nivel) {
        this.nivel = nivel;
    }
    public char getImputable() {
        return this.imputable;
    }
    
    public void setImputable(char imputable) {
        this.imputable = imputable;
    }
    public char getCc() {
        return this.cc;
    }
    
    public void setCc(char cc) {
        this.cc = cc;
    }
    public char getAjustaInflacion() {
        return this.ajustaInflacion;
    }
    
    public void setAjustaInflacion(char ajustaInflacion) {
        this.ajustaInflacion = ajustaInflacion;
    }
    public char getFlujoFondos() {
        return this.flujoFondos;
    }
    
    public void setFlujoFondos(char flujoFondos) {
        this.flujoFondos = flujoFondos;
    }
    public Set<AsientoDet> getAsientoDets() {
        return this.asientoDets;
    }
    
    public void setAsientoDets(Set<AsientoDet> asientoDets) {
        this.asientoDets = asientoDets;
    }
    public Set<MovPeriodo> getMovPeriodos() {
        return this.movPeriodos;
    }
    
    public void setMovPeriodos(Set<MovPeriodo> movPeriodos) {
        this.movPeriodos = movPeriodos;
    }
    public Set<PlantillaDet> getPlantillaDets() {
        return this.plantillaDets;
    }
    
    public void setPlantillaDets(Set<PlantillaDet> plantillaDets) {
        this.plantillaDets = plantillaDets;
    }
    public Set<Iva> getIvas() {
        return this.ivas;
    }
    
    public void setIvas(Set<Iva> ivas) {
        this.ivas = ivas;
    }




}

