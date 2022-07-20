/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import controllers.HibernateUtil;
import entidades.Concepto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import org.hibernate.Query;
import org.hibernate.Session;
import seguridad.LogIn;

/**
 *
 * @author rafael
 */

public class ListaConceptos extends BeanBase implements Serializable{
    private List items=null;

    public ListaConceptos() throws Exception{
        getLista();
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
    
    public void getLista() throws Exception {
        //Obtengo la empresa del usuario conectado
        LogIn login=getUsuarioConectado();
        login.getIdEmpresa();
        Session session;
        
        SelectItem item;
        List resultados=new ArrayList();
        session= HibernateUtil.getSessionFactory().openSession();

        item=new SelectItem();
        item.setValue(0);
        item.setLabel("Seleccione...");
        resultados.add(item);

        try{
            org.hibernate.Transaction tx =  session.beginTransaction();
            Query q=session.createQuery("from Concepto a where a.empresa = :empresaSel order by a.nombre");
            q.setParameter("empresaSel",cargaEmpresa(getUsuarioConectado().getIdEmpresa()));
            Iterator i=q.list().iterator();
            
            while (i.hasNext()){
                   Concepto p=(Concepto) i.next(); 
                   item=new SelectItem();
                   item.setValue(p.getId());
                   item.setLabel(p.getNombre());
                   System.out.println("Concepto: " + p.getNombre());
                   resultados.add(item);
            }
            session.getTransaction().commit();    
            
        } catch(Exception e) {
            e.printStackTrace();
            
        }
        this.setItems(resultados);
        session.close();
    }
    
    
        
}
