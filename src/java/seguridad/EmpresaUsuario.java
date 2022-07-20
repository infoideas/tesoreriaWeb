/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package seguridad;

/**
 *
 * @author rafael
 */
public class EmpresaUsuario {
    int idEmpresa;
    String nombre_fantasia;
    String nombrePais;
    String cuit;
    String direccion;

    public EmpresaUsuario() {
    }

    public EmpresaUsuario(int idEmpresa, String nombre_fantasia,String nombrePais,String cuit, String direccion) {
        this.idEmpresa = idEmpresa;
        this.nombre_fantasia = nombre_fantasia;
        this.nombrePais=nombrePais;
        this.cuit = cuit;
        this.direccion = direccion;
    }
        
    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre_fantasia() {
        return nombre_fantasia;
    }

    public void setNombre_fantasia(String nombre_fantasia) {
        this.nombre_fantasia = nombre_fantasia;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
    
    
}
