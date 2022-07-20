/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import entidades.Concepto;
import general.BeanBase;
import general.ListaConceptos;
import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author rafael
 */
@SessionScoped
@Named("conceptocontroller")
public class ConceptoController extends BeanBase implements Serializable {
    Concepto registroSel;
    Concepto registroMod;
    private String modo="";
    String mensaje="";
    private List <Concepto> lista=null;
    private TimeZone Zona = TimeZone.getDefault();

    public ConceptoController() {
        getListaDatos();
    }

    public Concepto getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(Concepto registroSel) {
        this.registroSel = registroSel;
    }

    public Concepto getRegistroMod() {
        return registroMod;
    }

    public void setRegistroMod(Concepto registroMod) {
        this.registroMod = registroMod;
    }

    public List<Concepto> getLista() {
        return lista;
    }

    public void setLista(List<Concepto> lista) {
        this.lista = lista;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public TimeZone getZona() {
        return Zona;
    }

    public void setZona(TimeZone Zona) {
        this.Zona = Zona;
    }

    
    //Obtiene lista de todos los registros
    public void getListaDatos(){
        FacesMessage msg;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            org.hibernate.Transaction tx=session.beginTransaction();
            Query q=session.createQuery("from Concepto a");
            this.lista=(List<Concepto>) q.list();
            session.getTransaction().commit();
        }
        catch (HibernateException e){
                session.getTransaction().rollback();
                e.printStackTrace();
                msg = new FacesMessage("Error: " + e.getCause().getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
        }
        finally{
            session.close();
        }
    }
    
    //Nuevo registro
    public void nuevo(){
        this.registroSel=new Concepto();
        this.registroMod=new Concepto();        
        registroMod.setEmpresa(cargaEmpresa(getUsuarioConectado().getIdEmpresa()));
        this.modo="N";
        //Obtengo la empresa actual
    }    
    
    //Obtiene los detalles del registro seleccionado
    public void edita(){
        this.registroMod=new Concepto();
        //Pongo los datos a modificar del registro seleccionado
        registroMod.setEmpresa(registroSel.getEmpresa());
        registroMod.setId(registroSel.getId());
        registroMod.setNombre(registroSel.getNombre());
        registroMod.setTipo(registroSel.getTipo());
        this.modo="M";
    }

    //Elimina registro seleccionado
    public void elimina() {
       FacesContext context=FacesContext.getCurrentInstance();
       Concepto o=getRegistroSel();
       FacesMessage msg;
        
       if (o!=null){
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                session.delete(o);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                    session.getTransaction().rollback();
                    msg = new FacesMessage("Error: " + e.getCause().getMessage());
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return;
            }
            finally {
                session.close();
            }
            lista.remove(o);
            getListaDatos();
            msg = new FacesMessage("Eliminación exitosa!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
       }

    }
    
    public void graba(){
        FacesMessage msg;  
        Session session;
        session=HibernateUtil.getSessionFactory().openSession();
    
        Concepto u=this.getRegistroMod();
        try{
            session.beginTransaction();
            session.saveOrUpdate(u);
            session.getTransaction().commit();
        }
        catch (HibernateException e){
            session.getTransaction().rollback();
            msg = new FacesMessage("Error: " + e.getCause().getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        finally {
         session.close();
        }
        getListaDatos();
        
        try {
            //Actualizo lista de conceptos
            ListaConceptos listaConceptos= new ListaConceptos();
            FacesContext context=FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("listaconceptos",listaConceptos);
            
        } catch (Exception ex) {
            Logger.getLogger(ConceptoController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        msg = new FacesMessage("Actualización exitosa!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
