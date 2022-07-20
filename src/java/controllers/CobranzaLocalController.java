/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import database.Conector;
import entidades.Concepto;
import entidades.CuentaFondos;
import entidades.MovCuenta;
import entidades.MovCuentaDet;
import entidades.Plantilla;
import general.Asiento;
import general.AsientoRealizado;
import general.BeanBase;
import general.ParametroAsiento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.component.button.Button;

/**
 *
 * @author Propietario
 */
@SessionScoped
@Named("cobranzalocalcontroller")
public class CobranzaLocalController extends BeanBase implements Serializable{
    private TimeZone zona = TimeZone.getTimeZone("America/Buenos_Aires");
    private List <LocalCarniceria> listaLocales =new ArrayList<LocalCarniceria>();
    private List <MovCuenta> listaMovimientos =new ArrayList<MovCuenta>();    
    private CuentaFondos cuentaFondos= new CuentaFondos();
    private int idMoneda;
    private int idUsuario;
    private Date fecMov;
    private double saldoInicialEfectivo;
    private double saldoFinalEfectivo;
    private boolean bloqueadoGrabar;
    
    public CobranzaLocalController() {
        nuevo();
    }

    public TimeZone getZona() {
        return zona;
    }

    public void setZona(TimeZone zona) {
        this.zona = zona;
    }

    public List<LocalCarniceria> getListaLocales() {
        return listaLocales;
    }

    public void setListaLocales(List<LocalCarniceria> listaLocales) {
        this.listaLocales = listaLocales;
    }

    public CuentaFondos getCuentaFondos() {
        return cuentaFondos;
    }

    public void setCuentaFondos(CuentaFondos cuentaFondos) {
        this.cuentaFondos = cuentaFondos;
    }

    public int getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(int idMoneda) {
        this.idMoneda = idMoneda;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFecMov() {
        return fecMov;
    }

    public void setFecMov(Date fecMov) {
        this.fecMov = fecMov;
    }

    public double getSaldoInicialEfectivo() {
        return saldoInicialEfectivo;
    }

    public void setSaldoInicialEfectivo(double saldoInicialEfectivo) {
        this.saldoInicialEfectivo = saldoInicialEfectivo;
    }

    public double getSaldoFinalEfectivo() {
        return saldoFinalEfectivo;
    }

    public void setSaldoFinalEfectivo(double saldoFinalEfectivo) {
        this.saldoFinalEfectivo = saldoFinalEfectivo;
    }

    
    public List<MovCuenta> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<MovCuenta> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public boolean isBloqueadoGrabar() {
        return bloqueadoGrabar;
    }

    public void setBloqueadoGrabar(boolean bloqueadoGrabar) {
        this.bloqueadoGrabar = bloqueadoGrabar;
    }

    

    //Nuevo lote de movimientos
    public void nuevo(){
        setIdUsuario(getUsuarioConectado().getIdUsuario());
        setCuentaFondos(new CuentaFondos());
        listaLocales.clear();
        listaMovimientos.clear();
        saldoInicialEfectivo=0;
        saldoFinalEfectivo=0;
        //Pongo fecha actual
        fecMov = new Date();
        bloqueadoGrabar=false;
        
        try {
            listaLocales=buscaListaLocalesCarniceria();
        } catch (Exception ex) {
            Logger.getLogger(CobranzaLocalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        
        
    //Retorna lista de locales de carnicería
    public List buscaListaLocalesCarniceria() throws Exception {
        CallableStatement s=null;
        ResultSet r=null;
        int li_idLocal,li_idClienteEstancia;
        String ls_nombreCliente,ls_nombreLocal,ls_responsable;
        String ls_direccion,ls_provincia,ls_localidad;
        int li_plantilla;
        List resultados = new ArrayList();
        LocalCarniceria item;
        Connection conexion=null;
        
        try {
             Conector conector = new Conector();  
             conexion = conector.connect("estancia");
             s=conexion.prepareCall("{call sp_get_lista_locales ( )}");
             r=s.executeQuery();
            
             while (r.next()){
                   li_idLocal=r.getInt("idLocal");
                   li_idClienteEstancia=r.getInt("idClienteEstancia");
                   ls_nombreCliente=r.getString("nombreCliente");
                   ls_nombreLocal=r.getString("nombreLocal");
                   ls_responsable=r.getString("responsable");
                   ls_direccion=r.getString("direccion");
                   ls_provincia=r.getString("provincia");
                   ls_localidad=r.getString("localidad");
                   li_plantilla=r.getInt("plantilla");

                   item=new LocalCarniceria();
                   item.setId(li_idLocal);
                   item.setNombre(ls_nombreLocal);
                   item.setDireccion(ls_direccion);
                   item.setLocalidad(ls_localidad);
                   item.setPlantilla(li_plantilla);
                   resultados.add(item);
                } 
             s.close();
           } catch (SQLException e){
                    e.getMessage();
                    return null;
           }
           finally{
            if (r != null) {r.close();}
            if (s != null) {s.close();}
            if (conexion != null) conexion.close(); 
           }
           return resultados;
          
    }        
    
    //Selecciona cuenta
    public void onSeleccionaCuenta() {
        CuentaFondos cuenta=obtieneCuentaFondos(getCuentaFondos().getId());
        //Obtengo el saldo actual de la cuenta en efectivo
        java.util.Calendar fec_saldo = java.util.Calendar.getInstance();
        fec_saldo.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fec_saldo.setTime(fecMov);
        fec_saldo.set(Calendar.HOUR_OF_DAY, 23);
        fec_saldo.set(Calendar.MINUTE, 59);
        fec_saldo.set(Calendar.SECOND, 59);
        System.out.println("Fecha del saldo: " + fec_saldo.getTime());        
        double ld_saldo=obtenerSaldo(cuenta,fec_saldo.getTime(),1);
        setSaldoInicialEfectivo(ld_saldo);
        setIdMoneda(cuenta.getMoneda().getId());
        //Actualizo saldo final
        actualizaSaldoFinal();
    }
    
    //Obtiene cuenta fondos
    public CuentaFondos obtieneCuentaFondos(int codCuenta){
            CuentaFondos cuenta = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                cuenta=(CuentaFondos) session.get(CuentaFondos.class,codCuenta);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return cuenta;
    }
    
    //Obtiene plantilla
    public Plantilla obtienePlantilla(int idEjercicio,int numPlantilla){
            Plantilla plantilla = null;
            Session session = null;
            
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                Query q=session.createQuery("from Plantilla a where a.ejercicio.id = :idEjercicio and numero = :numPlantilla"
                        + " order by a.nombre");
                q.setParameter("idEjercicio",idEjercicio);
                q.setParameter("numPlantilla",numPlantilla);
                
                List lista=q.list();
                if (lista.size()==1)
                   plantilla=(Plantilla) lista.get(0);

                session.getTransaction().commit();
                System.out.println("Plantilla: " + plantilla.getNombre());
                return plantilla;

            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
    }
            
    //Realizo asiento 
    public AsientoRealizado contabilizaMovimiento(MovCuenta mov){
        Asiento asiento= new Asiento();
        asiento.setIdEmpresa(1);        
        asiento.setNumPlantilla(mov.getPlantilla().getNumero());
        asiento.setDescripcion(mov.getObservaciones());

//        System.out.println("Fecha:  " + mov.getFecMov());
//        //Fecha del movimiento
//        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
//        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
//        fecha_mov.setTime(mov.getFecMov());
//        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
//        fecha_mov.set(Calendar.MINUTE, 0);
//        fecha_mov.set(Calendar.SECOND, 0);
//        fecha_mov.set(Calendar.MILLISECOND, 0);
//        

        //Convierto la fecha a String para no tener problemas con JSON
        TimeZone gmtZone = TimeZone.getTimeZone("America/Buenos_Aires");
        DateFormat destDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        destDateFormat.setTimeZone(gmtZone);
        String ls_fecha;
        ls_fecha=destDateFormat.format(mov.getFecMov());
        System.out.println("Fecha string: " + ls_fecha);
        asiento.setFecMov(ls_fecha);
        
        asiento.setIdUsuario(getUsuarioConectado().getIdUsuario());
       
        ParametroAsiento p= new ParametroAsiento();
        p.setOrden(1);
        p.setNombre("Efectivo");
        p.setValor(mov.getValor().doubleValue());
       
        asiento.getListaPar().add(p);
        AsientoRealizado resul=realizaAsiento(asiento);
        return resul;
        
    }    
    
    //Graba el movimiento
    public void graba(){
        String ls_tipo_compro;
        String ls_pdv,ls_num_fact;
        FacesMessage msg;
        
        //Obtengo la fecha de hoy para poner como fecha de carga y fecha de movimiento
        java.util.Calendar fecha=java.util.Calendar.getInstance();
        java.sql.Date lda_fecha=new java.sql.Date(fecha.getTimeInMillis());
        setFecMov(lda_fecha);

        //Fecha del movimiento
        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_mov.setTime(new Date());
        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
        fecha_mov.set(Calendar.MINUTE, 0);
        fecha_mov.set(Calendar.SECOND, 0);
        fecha_mov.set(Calendar.MILLISECOND, 0);        
        
        //Obtengo ejercicio contable de la fecha actual para mostrar las plantillas
        int li_id_ejercicio=obtieneEjercicioContable(getUsuarioConectado().getIdEmpresa(),lda_fecha); 
        System.out.println("Ejercicio: " + li_id_ejercicio);
        
        //Preparo los movimientos de ingreso de efectivo para cada uno de los locales
        for (LocalCarniceria local: listaLocales) {
            
            if(local.getValor() <= 0){
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,"Debe ingresar el valor cobrado en el local " + local.getNombre(),""));
                return;
            }
            
            MovCuenta movLocal= new MovCuenta();
            movLocal.setCuentaFondos(cuentaFondos);
            movLocal.setIdUsuario(idUsuario);
            movLocal.setFecMov(fecha_mov.getTime());
            movLocal.setFecCarga(lda_fecha);
            movLocal.setObservaciones("Cobranza local: " + local.getNombre());
            movLocal.setTipoMov('C');  //Tipo Mov: Cobranza
            movLocal.setDc('D');  //Ingresos
            movLocal.setTipoCambio(BigDecimal.ONE);
            movLocal.setValor(new BigDecimal(local.getValor()));
            movLocal.setValorOriginal(new BigDecimal(local.getValor()));
            Concepto concepto=obtieneConcepto(getCONCEPTO_COBRANZAS());  //Concepto de cobranzas
            movLocal.setConcepto(concepto);
            Plantilla plantilla=obtienePlantilla(li_id_ejercicio,local.getPlantilla());
            movLocal.setPlantilla(plantilla);

            //Agrego nuevo detalle en efectivo
            MovCuentaDet det = new MovCuentaDet();
            det.setMovCuenta(movLocal);
            det.setFormaPago(obtieneFormaPago(getEFECTIVO()));  //Efectivo
            det.setValor(movLocal.getValor());
        
            movLocal.getMovCuentaDets().add(det);
            listaMovimientos.add(movLocal);
            
        }

        //Grabo los movimientos en tesorería y contabilizo cada uno
        Session session=HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        for (MovCuenta mov: listaMovimientos) {
            try{
                //Grabo el movimiento
                session.saveOrUpdate(mov);
                
                AsientoRealizado asientoRealizado=contabilizaMovimiento(mov);
                if (asientoRealizado.getIdAsiento()==0){
                    session.getTransaction().rollback();
                    session.close();
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al contabilizar: " + asientoRealizado.getObservaciones(),""));
                    return;
                }
                
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                session.close();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),""));
                return;
            }
        }

        session.getTransaction().commit();        
        session.close();
        bloqueadoGrabar=true;
        msg = new FacesMessage("Actualización exitosa!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
    }
        
    //Obtiene concepto
    public Concepto obtieneConcepto(int codFP){
            Concepto concepto = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                concepto=(Concepto) session.get(Concepto.class,codFP);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return concepto;
    }    
    
    
    public void actualizaSaldoFinal(){
        System.out.println("Actualizando...");
        double ld_total = 0;
        for (LocalCarniceria local: listaLocales) {
            double ld_valor;
            ld_valor=local.getValor();
            ld_total=ld_total + ld_valor;
        }
        saldoFinalEfectivo=saldoInicialEfectivo + ld_total;
    }
    
    //Elimina local de la lista
    public void eliminaLocal(LocalCarniceria i) {
       listaLocales.remove(i);
       actualizaSaldoFinal();
    }   
}
