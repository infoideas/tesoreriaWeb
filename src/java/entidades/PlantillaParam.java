package entidades;
// Generated 13 nov. 2021 10:48:18 by Hibernate Tools 4.3.1



/**
 * PlantillaParam generated by hbm2java
 */
public class PlantillaParam  implements java.io.Serializable {


     private Integer id;
     private Plantilla plantilla;
     private short orden;
     private String nombre;

    public PlantillaParam() {
    }

    public PlantillaParam(Plantilla plantilla, short orden, String nombre) {
       this.plantilla = plantilla;
       this.orden = orden;
       this.nombre = nombre;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Plantilla getPlantilla() {
        return this.plantilla;
    }
    
    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }
    public short getOrden() {
        return this.orden;
    }
    
    public void setOrden(short orden) {
        this.orden = orden;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}

