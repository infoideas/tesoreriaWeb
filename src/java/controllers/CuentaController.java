/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controllers;

import entidades.Banco;
import entidades.CuentaFondos;
import general.BeanBase;
import general.ListaCuentas;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author rafael
 */
@javax.enterprise.context.SessionScoped
@Named("cuentacontroller")
public class CuentaController extends BeanBase implements Serializable {
    CuentaFondos registroSel;
    CuentaFondos registroMod;
    private String modo="";
    String mensaje="";
    private List <CuentaFondos> lista=null;
    private TimeZone Zona = TimeZone.getDefault();
    private int idBancoSel;

    public CuentaController() {
        getListaDatos();
    }

    public CuentaFondos getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(CuentaFondos registroSel) {
        this.registroSel = registroSel;
    }

    public CuentaFondos getRegistroMod() {
        return registroMod;
    }

    public void setRegistroMod(CuentaFondos registroMod) {
        this.registroMod = registroMod;
    }

    public List<CuentaFondos> getLista() {
        return lista;
    }

    public void setLista(List<CuentaFondos> lista) {
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

    public int getIdBancoSel() {
        return idBancoSel;
    }

    public void setIdBancoSel(int idBancoSel) {
        this.idBancoSel = idBancoSel;
    }

    
    //Obtiene lista de todos los registros
    public void getListaDatos(){
        FacesMessage msg;
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            org.hibernate.Transaction tx=session.beginTransaction();
            Query q=session.createQuery("from CuentaFondos a where a.empresa = :empresaSel");
            q.setParameter("empresaSel",cargaEmpresa(getUsuarioConectado().getIdEmpresa()));
            this.lista=(List<CuentaFondos>) q.list();
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
        this.registroSel=new CuentaFondos();
        this.registroMod=new CuentaFondos();        
        this.modo="N";
        idBancoSel=0;
        //Obtengo la empresa actual
        registroMod.setEmpresa(cargaEmpresa(getUsuarioConectado().getIdEmpresa()));
    }    
    
    //Obtiene los detalles del registro seleccionado
    public void edita(){
        FacesMessage msg;
        if (registroSel != null)
        {
            this.setIdBancoSel(0);
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                this.registroMod =(CuentaFondos) session.get(CuentaFondos.class,registroSel.getId());
                //Hibernate.initialize(this.registroMod.getTropaDets());
                session.getTransaction().commit();
                if (registroMod.getBanco()!=null)
                    this.setIdBancoSel(registroMod.getBanco().getId().intValue());
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
           
            this.modo="M";
        }
    }

    //Elimina registro seleccionado
    public void elimina() {
       FacesContext context=FacesContext.getCurrentInstance();
       CuentaFondos o=getRegistroSel();
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
        
        char lc_tipo;
        lc_tipo=registroMod.getTipoCuenta();
        if (lc_tipo=='M'){
            String ls_nombre;            
            ls_nombre=registroMod.getNombreCuenta();
            if (ls_nombre==null || ls_nombre.isEmpty()){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe ingresar el nombre de la cuenta","Cuentas");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;  
            }
        }
        else{
            if(registroMod.getBanco()==null){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar un banco","Cuentas");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;  
            }
            String ls_num_cuenta;
            ls_num_cuenta=registroMod.getNumeroCuenta();
            if (ls_num_cuenta==null || ls_num_cuenta.isEmpty()){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe ingresar el número de cuenta","Cuentas");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;  
            }
        }
        
        Session session;
        session=HibernateUtil.getSessionFactory().openSession();
    
        CuentaFondos u=this.getRegistroMod();
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
        catch (Exception e){
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
            //Actualizo lista de rubros
            ListaCuentas listaCuentaFondos= new ListaCuentas();
            FacesContext context=FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("listacuentas",listaCuentaFondos);
            
        } catch (Exception ex) {
            Logger.getLogger(CuentaController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        msg = new FacesMessage("Actualización exitosa!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    //Procesa cambio de Banco
    public void onSeleccionaBanco() {
        if (idBancoSel > 0){
            Session session = null;
            try {
                session=HibernateUtil.getSessionFactory().openSession();
                org.hibernate.Transaction tx=session.beginTransaction();
                Banco banco=(Banco) session.get(Banco.class,idBancoSel);
                session.getTransaction().commit();
                registroMod.setBanco(banco);
            }
                catch (HibernateException e){
                session.getTransaction().rollback();
                FacesMessage msg = new FacesMessage("Error: " + e.getLocalizedMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return;
            }
            finally {
                session.close();
            }
        }
        else
            registroMod.setBanco(null);
                
    }
    
    //Selecciona tipo de cuenta
    public void onSeleccionaTipoCuenta() {
        if (registroMod.getTipoCuenta()=='M')
            registroMod.setBanco(null);
    }
    
    //Obtiene banco
    public Banco obtieneBanco(int codBanco){
            FacesMessage msg;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Banco banco=(Banco) session.get(Banco.class,codBanco);
                session.getTransaction().commit();
                return banco;
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                msg = new FacesMessage("Error: " + e.getCause().getMessage());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
            finally {
                session.close();
            }
    }
}
