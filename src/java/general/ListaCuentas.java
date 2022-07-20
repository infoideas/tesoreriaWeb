/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import controllers.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author rafael
 */
@SessionScoped
@Named("listacuentas")
public class ListaCuentas extends BeanBase implements Serializable {
    private List items=null;

    public ListaCuentas() throws Exception{
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
        Session session;
        SelectItem item;
        List resultados=new ArrayList();
        session= HibernateUtil.getSessionFactory().openSession();

        try{
            org.hibernate.Transaction tx =  session.beginTransaction();
            Query q=session.createSQLQuery("select a.id,a.tipoCuenta as tipoCuenta,a.numeroCuenta as numeroCuenta,"
                    + "a.nombreCuenta as nombreCuenta,b.nombre as nombreBanco "
                    + "from cuenta_fondos a left outer join banco b on ( b.id=a.idBanco) "
                    + "where idEmpresa= :idEmpresaSel "
                    + "order by b.nombre,a.numeroCuenta");
            q.setParameter("idEmpresaSel",getUsuarioConectado().getIdEmpresa());
            item=new SelectItem();
            item.setValue(0);
            item.setLabel("Seleccione...");
            resultados.add(item);

            String ls_nombreCuenta=null;
            String ls_numeroCuenta=null;
            List<Object[]> listaCuentas=(List<Object[]>) q.list();
            for (Object[] datos : listaCuentas) {
                    char ls_tipo;
                    ls_tipo=(char) datos[1];
                    switch (ls_tipo){
                        case 'M':
                          ls_nombreCuenta=(String) datos[3] ;
                          ls_numeroCuenta="";
                          break;                        
                        case 'A':
                          ls_nombreCuenta="Caja de Ahorro";
                          ls_numeroCuenta=(String) datos[2];
                          break;
                        case 'C':
                          ls_nombreCuenta="Cuenta corriente";
                          ls_numeroCuenta=(String) datos[2];
                          break;
                    }
                          
                   item=new SelectItem();
                   item.setValue(datos[0]);
                   item.setLabel(ls_nombreCuenta + " - " + ls_numeroCuenta );
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
