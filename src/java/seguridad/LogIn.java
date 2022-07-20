/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package seguridad;

import controllers.HibernateUtil;
import entidades.CategoriaPercepcion;
import entidades.CategoriaRetencion;
import entidades.Concepto;
import entidades.CuentaFondos;
import entidades.Empresa;
import entidades.Iva;
import entidades.Recibo;
import entidades.Solicitud;
import general.BeanBase;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author rafael
 */
public class LogIn extends BeanBase implements Serializable {
     private int idUsuario;
     private String nombreUsuario;
     private String claveUsuario;
     private String nombreUsuarioCompleto;
     private String estado;
     private String observaciones;
     private int idEmpresa;
     private String nombreEmpresa;
     private String nombrePais;
     private String moneda;
     private String simboloMoneda;
     private boolean conectado;

     JAXBContext jc;
     InputStream xml;
     

    public LogIn() {
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuarioCompleto() {
        return nombreUsuarioCompleto;
    }

    public void setNombreUsuarioCompleto(String nombreUsuarioCompleto) {
        this.nombreUsuarioCompleto = nombreUsuarioCompleto;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSimboloMoneda() {
        return simboloMoneda;
    }

    public void setSimboloMoneda(String simboloMoneda) {
        this.simboloMoneda = simboloMoneda;
    }
     
    
    //Iniciar sesión de usuario
    public String iniciarSesion() {
        
        FacesMessage msg;
        //Valido permiso para la transacción        
        try {
            if (!validaPermiso(getUsuarioConectado().getNombreUsuario(),ResourceBundle.getBundle("general/Permisos").getString("IngresarModulo"))){
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Transacción no autorizada","Estancia");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
                
        } catch (UnsupportedEncodingException ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,ex.getLocalizedMessage(),"Tropas");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        };        
        
        //Consumo el servicio web de LogIn de administrador
        String uri = String.format(this.getURI_BACKEND() + "/seguridad/validaLoginUsuarioAdmin?nombreUsuario=%1s&clave=%1s&tipoUsuario=A&tipoRespuesta=XML",nombreUsuario,claveUsuario);        
        System.out.println("URI:" + uri);
        String ls_resultado=null;
        URL url;
         try {
            url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
             
            if (connection.getResponseCode() != 200) {
                    throw new RuntimeException("Error: HTTP error code : " + connection.getResponseCode());
            }else
            {
                jc = JAXBContext.newInstance(UsuarioAdmin.class);
                xml = connection.getInputStream();
                        UsuarioAdmin usuario = new UsuarioAdmin();
                        usuario = (UsuarioAdmin) jc.createUnmarshaller().unmarshal(xml);
                        System.out.println("Usuario:" +  usuario.getObservaciones());

                        if (usuario.getEstado().equals("HABILITADO")){
                            this.setIdUsuario(usuario.getId());
                            this.setNombreUsuarioCompleto(usuario.getNombreCompletoUsuario());                            
                            this.setEstado(usuario.getEstado());
                            this.setConectado(true);
                            
                            //Obtengo datos de la empresa
                            Session session = null;
                            Iterator i=null;
                            Query q=null;
                            Empresa p = null;
                            int li_canti_empresas=0;
                            try{
                                session= HibernateUtil.getSessionFactory().openSession();
                                org.hibernate.Transaction tx =  session.beginTransaction();
                                q=session.createQuery("from Empresa a");
                                i=q.list().iterator();
                                session.getTransaction().commit();  
                                li_canti_empresas=q.list().size();                                
                                p=null;
                                if (q.list().size()==1){
                                    p=(Empresa) i.next(); 
                                }
                            }
                            catch(HibernateException e){
                                session.getTransaction().rollback();
                            }
                            finally{
                                session.close();
                            }
                            
                            if (li_canti_empresas==1){
                                System.out.println("Pasó...");
                                //Obtengo datos de la única empresa
                                this.setIdEmpresa(p.getId());
                                this.setNombreEmpresa(p.getNombre());
                                this.setMoneda(p.getMoneda().getNombre());
                                this.setNombreUsuario(nombreUsuario);
                                this.setNombreUsuarioCompleto(usuario.getNombreCompletoUsuario());                            
                                this.setEstado(usuario.getEstado());
                                this.setConectado(true);
                                FacesContext context=FacesContext.getCurrentInstance();
                                context.getExternalContext().getSessionMap().put("login",this);
                                ls_resultado="Principal";                          
                            }
                            else
                                ls_resultado="SelEmpresa";
                        }
                        else{
                            this.setConectado(false);
                            this.setObservaciones(usuario.getObservaciones());
                        }
                            
                        
            }
            
             
         } catch (MalformedURLException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
         } catch (JAXBException ex) {
             Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return ls_resultado;
        
    }
    
    //Método para que el usuario cierre la sesi�n
    public void cerrarSesion() throws Exception {
            try {
             //Obtengo la sesion
             FacesContext context=FacesContext.getCurrentInstance();
             HttpSession session=(HttpSession)context.getExternalContext().getSession(false);
             
             this.setConectado(false);
             //Invalido la sesion
             session.invalidate();
             
             FacesContext.getCurrentInstance().getExternalContext().redirect("/tesoreriaWeb/LogIn.xhtml");
             return;
            }
             catch (Exception e){
              //return("error");   
            }
            
        
    }
}
