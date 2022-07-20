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
import general.ListaConceptos;
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
@Named("movimientocontroller")
public class MovimientoController extends BeanBase implements Serializable{
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
    private int idUsuario;
    private char tipoMov;
    private CuentaFondos cuentaFondos= new CuentaFondos();
    private int idMoneda;
    private List <MovCuenta> listaMovimientos =new ArrayList<MovCuenta>();
    private Date fecMov;
    private double saldoInicialEfectivo;
    private double saldoFinalEfectivo;
    private boolean bloqueadoGrabar;

    public MovimientoController() {
        nuevo();
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

    public boolean isBloqueadoGrabar() {
        return bloqueadoGrabar;
    }

    public void setBloqueadoGrabar(boolean bloqueadoGrabar) {
        this.bloqueadoGrabar = bloqueadoGrabar;
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
            Query q=session.createQuery("select a from MovCuenta a,CuentaFondos b where b.empresa = :empresaSel and "
                    + "a.cuentaFondos=b.id and a.fecMov >= :fec_desde and a.fecMov <= :fec_hasta order by a.fecMov,a.cuentaFondos");
            q.setParameter("empresaSel",cargaEmpresa(getUsuarioConectado().getIdEmpresa()));
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
    
    //Nuevo lote de movimientos
    public String nuevo(){
        setIdUsuario(getUsuarioConectado().getIdUsuario());
        setCuentaFondos(new CuentaFondos());
        saldoInicialEfectivo=0;
        saldoFinalEfectivo=0;
        //Pongo fecha actual
        fecMov = new Date();
        listaMovimientos.clear();
        bloqueadoGrabar=false;
        return "/vistas/movimientos/IngresarLoteMov";
    }        
    
    //Nueva movimiento
    public void nuevoMovimiento() throws Exception{
        idConceptoSel=0;
        idPlantillaSel=0;
        this.registroMod=new MovCuenta();
        this.registroMod.setIdUsuario(getUsuarioConectado().getIdUsuario());
        this.registroMod.setFecCarga(fecMov);
        this.registroMod.setFecMov(fecMov);
        
        //Obtengo ejercicio contable de la fecha actual para mostrar las plantillas
        int li_id_ejercicio=obtieneEjercicioContable(getUsuarioConectado().getIdEmpresa(),fecMov); 
        System.out.println("Ejercicio: " + li_id_ejercicio);
        ListaPlantillas listaPlantillas= new ListaPlantillas();
        listaPlantillas.getLista(li_id_ejercicio);
        FacesContext context=FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listaplantillas",listaPlantillas);
        
    }        
    
    //Agraga movimiento al lote
    public void agregaMovimiento(){
        
        FacesMessage msg;
        if (idConceptoSel==0){
            msg = new FacesMessage("Debe seleccionar un concepto");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        
        if (idPlantillaSel==0){
            msg = new FacesMessage("Debe seleccionar una plantilla");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        Plantilla plantillaSel=obtienePlantilla(idPlantillaSel);
        
        registroMod.setCuentaFondos(cuentaFondos);
        registroMod.setIdUsuario(idUsuario);
        registroMod.setTipoMov(tipoMov);
        registroMod.setTipoCambio(BigDecimal.ONE);
        registroMod.setValorOriginal(registroMod.getValor());
        Concepto concepto=obtieneConcepto(idConceptoSel);
        registroMod.setConcepto(concepto);
        Plantilla plantilla=obtienePlantilla(idPlantillaSel);
        registroMod.setPlantilla(plantilla);

        if(registroMod.getDc()=='D')
            saldoFinalEfectivo=saldoFinalEfectivo + registroMod.getValor().doubleValue();
        else
            saldoFinalEfectivo=saldoFinalEfectivo - registroMod.getValor().doubleValue();
        
        //Agrego nuevo detalle en efectivo
        MovCuentaDet det = new MovCuentaDet();
        det.setMovCuenta(registroMod);
        det.setFormaPago(obtieneFormaPago(1));  //Efectivo
        det.setValor(registroMod.getValor());
        
        registroMod.getMovCuentaDets().add(det);
        MovCuenta mov= new MovCuenta();
        mov=registroMod;
        listaMovimientos.add(mov);
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
    
    public void actualizaTotales(){
        double ld_total=0,ld_valor=0;
        for (MovCuentaDet g: getRegistroMod().getMovCuentaDets()) {
            if (g.getValor()!=null)
                ld_valor=g.getValor().doubleValue();
            ld_total=ld_total + ld_valor;
        }
        getRegistroMod().setValor(new BigDecimal(ld_total));
        getRegistroMod().setValorOriginal(new BigDecimal(ld_total));        
    }
    
    //Elimina detalle
    public void eliminaDetalle(MovCuentaDet i) {
       this.getRegistroMod().getMovCuentaDets().remove(i);
    }    

    //Elimina retención
    public void eliminaRetención(MovCuentaRet i) {
       this.getRegistroMod().getMovCuentaRets().remove(i);
    }    

   //Graba el movimiento
    public void graba(){
        String ls_tipo_compro;
        String ls_pdv,ls_num_fact;
        FacesMessage msg;

        //Obtengo la fecha de hoy para poner como fecha de carga
        java.util.Calendar fecha_carga = java.util.Calendar.getInstance();
        fecha_carga.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_carga.setTime(new Date());
        
        //Obtengo la fecha de hoy para poner como fecha de carga y fecha de movimiento
        java.util.Calendar fecha_mov = java.util.Calendar.getInstance();
        fecha_mov.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
        fecha_mov.setTime(new Date());
        fecha_mov.set(Calendar.HOUR_OF_DAY, 0);
        fecha_mov.set(Calendar.MINUTE, 0);
        fecha_mov.set(Calendar.SECOND, 0);
        fecha_mov.set(Calendar.MILLISECOND, 0);       
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        for (MovCuenta mov: listaMovimientos) {
            mov.setFecCarga(fecha_carga.getTime());
            mov.setFecMov(fecha_mov.getTime());
            try{
                //Grabo el movimiento
                session.saveOrUpdate(mov);
                
                AsientoRealizado asientoRealizado=contabilizaMovimiento(mov);
                if (asientoRealizado.getIdAsiento()==0){
                    session.getTransaction().rollback();
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage("mensajes", new FacesMessage(FacesMessage.SEVERITY_INFO,"Error al contabilizar: " + asientoRealizado.getObservaciones(),""));
                    return;
                }
                
            }
            catch (HibernateException e){
                session.getTransaction().rollback();
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
    
    //Selecciona cuenta
    public void onSeleccionaCuenta() {
        CuentaFondos cuenta=obtieneCuentaFondos(getCuentaFondos().getId());
        setIdMoneda(cuenta.getMoneda().getId());
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
            
    //Realizo asiento 
    public AsientoRealizado contabilizaMovimiento(MovCuenta mov){
        Asiento asiento= new Asiento();
        asiento.setIdEmpresa(1);        
        asiento.setNumPlantilla(mov.getPlantilla().getNumero());
        asiento.setDescripcion(mov.getObservaciones());

        //Convierto la fecha de movimiento a String para no tener problemas con JSON
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
    
    //Procesa cambio de concepto
    public void onSeleccionaConcepto() {
        if (idConceptoSel > 0){
            Session session = null;
            try {
                session=HibernateUtil.getSessionFactory().openSession();
                org.hibernate.Transaction tx=session.beginTransaction();
                Concepto concepto=(Concepto) session.get(Concepto.class,idConceptoSel);
                session.getTransaction().commit();
                registroMod.setConcepto(concepto);
                registroMod.setDc(concepto.getTipo());
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
            registroMod.setConcepto(null);
                
    }
    
        
}
