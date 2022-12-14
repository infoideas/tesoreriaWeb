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
@Named("gastoscontroller")
public class GastosController extends BeanBase implements Serializable{
    private TimeZone zona = TimeZone.getTimeZone("America/Buenos_Aires");
    private List <MovCuenta> listaMovimientos =new ArrayList<MovCuenta>();    
    private CuentaFondos cuentaFondos= new CuentaFondos();
    private int idMoneda;
    private int idUsuario;
    private int idUnidad;
    private Date fecMov;
    private double saldoInicialEfectivo;
    private double saldoFinalEfectivo;
    private boolean bloqueadoGrabar;
    private List listaUnidades;
    private List <TipoGasto> listaGastos =new ArrayList<TipoGasto>();
    
    public GastosController() {
        nuevo();
    }

    public TimeZone getZona() {
        return zona;
    }

    public void setZona(TimeZone zona) {
        this.zona = zona;
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

    public List getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public List<TipoGasto> getListaGastos() {
        return listaGastos;
    }

    public void setListaGastos(List<TipoGasto> listaGastos) {
        this.listaGastos = listaGastos;
    }

    
    //Nuevo lote de movimientos
    public void nuevo(){
        setIdUsuario(getUsuarioConectado().getIdUsuario());
        setCuentaFondos(new CuentaFondos());
        listaGastos.clear();
        listaMovimientos.clear();
        saldoInicialEfectivo=0;
        saldoFinalEfectivo=0;
        //Pongo fecha actual
        fecMov = new Date();
        bloqueadoGrabar=false;
        idUnidad=0;
        
        try {
            listaUnidades=buscaListaUnidadesNegocio();
        } catch (Exception ex) {
            Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }        
        
    //Retorna lista de unidades de negocio
    public List buscaListaUnidadesNegocio() throws Exception {
        CallableStatement s=null;
        ResultSet r=null;
        int li_id;
        String ls_nombre;
        List resultados = new ArrayList();
        SelectItem item;
        Connection conexion=null;
        
        try {
             Conector conector = new Conector();  
             conexion = conector.connect("estancia");
             s=conexion.prepareCall("{call sp_get_lista_unidades_negocio ( )}");
             r=s.executeQuery();
             
             item=new SelectItem();
             item.setValue(0);
             item.setLabel("Seleccione...");
             resultados.add(item);
             
             while (r.next()){
                   li_id=r.getInt("id");
                   ls_nombre=r.getString("nombre");

                   item=new SelectItem();
                   item.setValue(li_id);
                   item.setLabel(ls_nombre);
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
    
    //Retorna lista de unidades de negocio
    public List buscaListaGastosUnidad(int idUnidad) throws Exception {
        CallableStatement s=null;
        ResultSet r=null;
        int li_id,li_plantilla,li_orden;
        String ls_nombre;
        List resultados = new ArrayList();
        TipoGasto item;
        Connection conexion=null;
        
        try {
             Conector conector = new Conector();  
             conexion = conector.connect("estancia");
             s=conexion.prepareCall("{call sp_get_lista_gastos_unidad_negocio ( ? )}");
             s.setInt(1,idUnidad);
             r=s.executeQuery();
            
             while (r.next()){
                   li_id=r.getInt("idGasto");
                   ls_nombre=r.getString("nombre");
                   li_plantilla=r.getInt("plantilla");
                   li_orden=r.getInt("orden");

                   item=new TipoGasto();
                   item.setId(li_id);
                   item.setNombre(ls_nombre);
                   item.setPlantilla(li_plantilla);
                   item.setOrden(li_orden);
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
    
    //Selecciona unidad de negocio
    public void onSeleccionaUnidad() {
        try {
            listaGastos=buscaListaGastosUnidad(idUnidad);
        } catch (Exception ex) {
            Logger.getLogger(GastosController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        System.out.println("Plantilla contable: " + mov.getPlantilla().getNumero() + "-" + mov.getPlantilla().getNombre());
        
        asiento.setNumPlantilla(mov.getPlantilla().getNumero());
        asiento.setDescripcion(mov.getObservaciones());

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
        
        listaMovimientos.clear();
        //Preparo los movimientos de ingreso de efectivo para cada uno de los locales
        for (TipoGasto item: listaGastos) {
            
            if(item.getValor() <= 0){
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,"Debe ingresar el importe del gasto: " + item.getNombre(),""));
                return;
            }

            if(item.getDescripcion()==null || item.getDescripcion().isEmpty() ){
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,"Debe ingresar la descripción para el gasto: " + item.getNombre(),""));
                return;
            }
            
            MovCuenta movLocal= new MovCuenta();
            movLocal.setCuentaFondos(cuentaFondos);
            movLocal.setIdUsuario(idUsuario);
            movLocal.setFecMov(fecha_mov.getTime());
            movLocal.setFecCarga(lda_fecha);
            movLocal.setObservaciones(item.getNombre().concat("-").concat(item.getDescripcion()));
            movLocal.setTipoMov('E');  //Tipo Mov: Egreso
            movLocal.setDc('C');  //Egreso o gasto
            movLocal.setTipoCambio(BigDecimal.ONE);
            movLocal.setValor(new BigDecimal(item.getValor()));
            movLocal.setValorOriginal(new BigDecimal(item.getValor()));
            Concepto concepto=obtieneConcepto(getCONCEPTO_GASTOS());  //Concepto de cobranzas
            movLocal.setConcepto(concepto);
            Plantilla plantilla=obtienePlantilla(li_id_ejercicio,item.getPlantilla());
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
        for (TipoGasto item: listaGastos) {
            double ld_valor;
            ld_valor=item.getValor();
            ld_total=ld_total + ld_valor;
        }
        saldoFinalEfectivo=saldoInicialEfectivo - ld_total;
    }
    
    //Elimina gasto de la lista
    public void eliminaGasto(TipoGasto i) {
       listaGastos.remove(i);
       actualizaSaldoFinal();
    }
    
}
