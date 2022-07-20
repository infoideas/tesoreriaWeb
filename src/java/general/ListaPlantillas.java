/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import controllers.HibernateUtil;
import entidades.Moneda;
import entidades.Plantilla;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.hibernate.Query;
import org.hibernate.Session;
import seguridad.LogIn;

/**
 *
 * @author rafael
 */

public class ListaPlantillas extends BeanBase implements Serializable{
    private List items=null;

    public ListaPlantillas() throws Exception{
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
    
    public void getLista(int idEjercicio) throws Exception {
        //Obtengo la empresa del usuario conectado
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
            Query q=session.createQuery("from Plantilla a where a.ejercicio.id = :idEjercicio order by a.nombre");
            q.setParameter("idEjercicio",idEjercicio);
            Iterator i=q.list().iterator();
            
            while (i.hasNext()){
                   Plantilla p=(Plantilla) i.next(); 
                   item=new SelectItem();
                   item.setValue(p.getId());
                   item.setLabel(p.getNombre());

                   resultados.add(item);
            }
            session.getTransaction().commit();    
            this.setItems(resultados);
            System.out.println("Lista: " + items);
            
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        finally{
            session.close();
        }
        
    }
    
    
        
}
