/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entidades.Empresa;
import general.BeanBase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import seguridad.EmpresaUsuario;
import seguridad.LogIn;

/**
 *
 * @author Propietario
 */
@ManagedBean(name="selempresacontroller")
@SessionScoped
public class SelEmpresaController extends BeanBase implements Serializable {
    private ArrayList<Empresa> listaEmpresas;

    public ArrayList<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public void setListaEmpresas(ArrayList<Empresa> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public SelEmpresaController() {
        //Obtengo el usuario conectado
        LogIn login=getUsuarioConectado();
        System.out.println("Usuario:" + login.getNombreUsuario());
        setListaEmpresas( (ArrayList<Empresa>) buscaListaEmpresas());
    }
    
    
    
    //Busca lista de empresas
    private List buscaListaEmpresas() {
        List resultList=null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q=session.createQuery("from Empresa order by nombre");
            resultList = q.list();
            //displayResult(resultList);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return resultList;    
    }
    
    public String seleccionarEmpresa(){
        FacesContext context=FacesContext.getCurrentInstance();
        Empresa empresa=(Empresa) context.getExternalContext().getRequestMap().get("item");
        
        LogIn login=getUsuarioConectado();
        login.setIdEmpresa(empresa.getId());
        login.setNombreEmpresa(empresa.getNombre());
        context.getExternalContext().getSessionMap().put("login",login);
        
        System.out.println("Empresa:" + login.getNombreEmpresa());
        return "Principal";
       
    }
    
}
