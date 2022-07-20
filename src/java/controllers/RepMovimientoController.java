/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import entidades.Banco;
import entidades.Concepto;
import entidades.CuentaFondos;
import entidades.Empresa;
import entidades.FormaPago;
import entidades.Moneda;
import entidades.MovCuenta;
import entidades.MovCuentaDet;
import entidades.MovCuentaRet;
import entidades.Plantilla;
import general.Asiento;
import general.AsientoRealizado;
import general.BeanBase;
import general.ListaPlantillas;
import general.ParametroAsiento;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author rafael
 */
@SessionScoped
@Named("repmovimientocontroller")
public class RepMovimientoController extends BeanBase implements Serializable{
    private TimeZone zona = TimeZone.getTimeZone("America/Buenos_Aires");
    private java.util.Date fec_desde=null;
    private java.util.Date fec_hasta=null;
    private MovCuenta registroSel;
    private MovCuenta registroMod;
    private List <MovCuenta> lista=null;
    private List <MovCuenta> listaFiltrada=null;
    private String modo="";
    private int idConceptoSel;
    private int idPlantillaSel;
    private int idFormaPagoSel;
    private int idUsuario;
    private char tipoMov;
    private CuentaFondos cuentaFondos= new CuentaFondos();
    private int idMoneda;
    private List <MovCuenta> listaMovimientos =new ArrayList<MovCuenta>();
    private Date fecMov;
    private double saldoInicialEfectivo;
    private double saldoFinalEfectivo;

    public RepMovimientoController() {
        setIdUsuario(getUsuarioConectado().getIdUsuario());
        setCuentaFondos(new CuentaFondos());
        saldoInicialEfectivo=0;
        saldoFinalEfectivo=0;
        //Pongo fecha actual
        fecMov = new Date();
        listaMovimientos.clear();
    }

    public MovCuenta getRegistroSel() {
        return registroSel;
    }

    public void setRegistroSel(MovCuenta registroSel) {
        this.registroSel = registroSel;
    }

    public MovCuenta getRegistroMod() {
        return registroMod;
    }

    public void setRegistroMod(MovCuenta registroMod) {
        this.registroMod = registroMod;
    }

    public List<MovCuenta> getLista() {
        return lista;
    }

    public void setLista(List<MovCuenta> lista) {
        this.lista = lista;
    }

    public TimeZone getZona() {
        return zona;
    }

    public void setZona(TimeZone zona) {
        this.zona = zona;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public List<MovCuenta> getListaFiltrada() {
        return listaFiltrada;
    }

    public void setListaFiltrada(List<MovCuenta> listaFiltrada) {
        this.listaFiltrada = listaFiltrada;
    }

    public Date getFec_desde() {
        return fec_desde;
    }

    public void setFec_desde(Date fec_desde) {
        this.fec_desde = fec_desde;
    }

    public Date getFec_hasta() {
        return fec_hasta;
    }

    public void setFec_hasta(Date fec_hasta) {
        this.fec_hasta = fec_hasta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public char getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(char tipoMov) {
        this.tipoMov = tipoMov;
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

    public int getIdConceptoSel() {
        return idConceptoSel;
    }

    public void setIdConceptoSel(int idConceptoSel) {
        this.idConceptoSel = idConceptoSel;
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

    public int getIdPlantillaSel() {
        return idPlantillaSel;
    }

    public void setIdPlantillaSel(int idPlantillaSel) {
        this.idPlantillaSel = idPlantillaSel;
    }

    public List<MovCuenta> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<MovCuenta> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public int getIdFormaPagoSel() {
        return idFormaPagoSel;
    }

    public void setIdFormaPagoSel(int idFormaPagoSel) {
        this.idFormaPagoSel = idFormaPagoSel;
    }
    
    //Obtiene lista de todos las entregas
    public void buscaListaDatos(){
        FacesMessage msg;
        if (fec_desde==null){
            msg = new FacesMessage("Debe ingresar la fecha inicial de la búsqueda");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        
        if (fec_hasta==null){
            msg = new FacesMessage("Debe ingresar la fecha final de la búsqueda");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }

        java.util.Calendar fecha_desde = java.util.Calendar.getInstance();
        fecha_desde.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_desde.setTime(fec_desde);
        fecha_desde.set(Calendar.HOUR_OF_DAY, 0);
        fecha_desde.set(Calendar.MINUTE, 0);
        fecha_desde.set(Calendar.SECOND, 0);
        fecha_desde.set(Calendar.MILLISECOND, 0);
        java.util.Date lda_fecha_desde = new java.sql.Date(fecha_desde.getTimeInMillis());

        java.util.Calendar fecha_hasta = java.util.Calendar.getInstance();
        fecha_hasta.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_hasta.setTime(fec_hasta);
        fecha_hasta.set(Calendar.HOUR_OF_DAY, 23);
        fecha_hasta.set(Calendar.MINUTE, 59);
        fecha_hasta.set(Calendar.SECOND, 59);
        fecha_hasta.set(Calendar.MILLISECOND, 0);
        java.util.Date lda_fecha_hasta = new java.sql.Date(fecha_hasta.getTimeInMillis());        
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        try{
            org.hibernate.Transaction tx=session.beginTransaction();
            Query q=session.createQuery("select a from MovCuenta a, MovCuentaDet b "
                    + "where a.cuentaFondos = :cuentaSel "
                    + "and   b.movCuenta = a "
                    + "and   b.formaPago.id = :idFormaPagoSel "
                    + "and   a.fecMov >= :fec_desde and a.fecMov <= :fec_hasta "
                    + "order by a.fecMov,a.cuentaFondos");
            q.setParameter("cuentaSel",cuentaFondos);
            q.setParameter("idFormaPagoSel",idFormaPagoSel);
            q.setParameter("fec_desde",lda_fecha_desde);
            q.setParameter("fec_hasta",lda_fecha_hasta);
            this.lista=(List<MovCuenta>) q.list();
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

    }
    
    //Obtiene los detalles del registro seleccionado
    public String edita(){
        FacesMessage msg;
        
        if (registroSel != null)
        {
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                this.registroMod =(MovCuenta) session.get(MovCuenta.class,registroSel.getId());
                Hibernate.initialize(this.registroMod.getMovCuentaDets());
                session.getTransaction().commit();
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

            this.modo="M";
            return "/vistas/movimientos/Edit";
        }
        else
            return null;
    }
 
    
    //Obtiene los detalles del registro que se hace clic
    public String onClick(MovCuenta p) throws Exception{
        FacesMessage msg;
        registroSel=p;
        if (registroSel != null)
        {
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                this.registroMod =(MovCuenta) session.get(MovCuenta.class,registroSel.getId());
                Hibernate.initialize(this.registroMod.getMovCuentaDets());
                session.getTransaction().commit();
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

            this.modo="M";
            return "/vistas/ventas/Edit";
        }
        else
            return null;
    }
    
    //Selecciona cuenta
    public void onSeleccionaCuenta() {
        CuentaFondos cuenta=obtieneCuentaFondos(getCuentaFondos().getId());
        System.out.println("Cuenta: " + cuenta.getNombreCuenta());
        System.out.println("Fecha: " + fec_desde);
        setIdMoneda(cuenta.getMoneda().getId());
        //Obtengo el saldo anterior de la cuenta en efectivo
        double ld_saldo = 0;
        if (fec_desde!= null){
            //Fecha para consultar el saldo hasta el día anterior
            java.util.Calendar fec_saldo = java.util.Calendar.getInstance();
            fec_saldo.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
            fec_saldo.setTime(fec_desde);
            fec_saldo.add(Calendar.DATE,-1); //Resto un día
            fec_saldo.set(Calendar.HOUR_OF_DAY, 23);
            fec_saldo.set(Calendar.MINUTE, 59);
            fec_saldo.set(Calendar.SECOND, 59);
            System.out.println("Fecha del saldo: " + fec_saldo.getTime());
            ld_saldo=obtenerSaldo(cuenta,fec_saldo.getTime(),idFormaPagoSel);
        }
            
        System.out.println("Saldo: " + ld_saldo);
        setSaldoInicialEfectivo(ld_saldo);
        setSaldoFinalEfectivo(ld_saldo);
        listaMovimientos.clear();
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
        
    //Obtiene plantilla
    public Plantilla obtienePlantilla(int codPlantilla){
            Plantilla plantilla = null;
            Session session = null;
            try{
                session=HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                plantilla=(Plantilla) session.get(Plantilla.class,codPlantilla);
                session.getTransaction().commit();
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
                return null;
            }
            finally {
                session.close();
            }
            return plantilla;
    }
    
        
}
