/**
 * Classe Saldo
 * Copyright (c) Thera SpA</b>
 * @author  Alessandra Zanoni 28/11/2000
 * @see SaldoTM
 *
 * Revisions:
 * Number  Date         Owner      Description
 *     17/12/2001 JF - Viene richiamato l'aggiornamento dell'ubicazione solo in caso di nuovo
 *                     saldo o nuova mappa udc.
 * 01739 19.04.04 GM - sostituito 'T' in STMT_QTA_IMPEGNATA
 * 07386 28/05/07 IT - Porting Oracle (Fix 7386)
 * 07529 28/06/07 Mz - gestione UM secondaria
 * 07596 09.07.07 GM - aggiunto metodo setQtaAll()
 * 07646 28.08.07 GM - cambiata chiamata di calcolaQta2Qta3()
 *       19.09.07 GM - non veniva valorizzato um1
 * 07895 24.10.07 GM - gestione umPrm e umSec per qtaDisponibile
 *       25.10.07 GM - la cancellazione di un saldo nel caso di UM a rapporto variabile
 *                     crea una riga mov. per azzerare qta2(qta3)
 *                   - modificato isCancellabile() per gestire eventuali errori
 * 08217 16.11.07 GM - nel caso di un mov. doppio il mov. di azzeramento del saldo andava in errore per chiave duplicata
 *       19.11.07 GM - lancio evento per trasmissione movimenti immediata
 * 09094 23/04/08 MM - Inserita gestione errori nell'evento invio movimenti a Thip
 * 10360 28.02.09 AR - Corretta spaccattura per TipoSaldo / Partita / Partner
 * 10841 05.05.09 GM - se il mov. è senza udc, ricercaSaldo() ritornava anche i saldi con udc
 * 14257 05.04.11 GM - aggiunto attributo indicePerPrelievo
 * 15941 07.03.12 IE - LOGIS NLS
 * 15947 16.04.12 MA - Utilizzo oggetti in Cache
 * 16273 23.04.12 GM - modificato gestione dei lock (con la versione 9 del DB2 si creavano disallineamenti nelle giacenze)
 * 16470 12.06.12 MA - Ottimizzazione accesso database
 * 19869 26.11.14 GM - aggiornamento del peso dell'ubicazione
 * 21157 10.03.15 GM - ricercaSaldo() ridefinito come non statico 
 * 23362 05.04.16 DB - viene ignorato il flag di aggiornamento dell'ubicazione nel caso di svuotamento completo del saldo
 * 26282 07.09.17 GM - gestione degli errori ritornati dal metodo settaAltriDati
 *                   - aggiunto metodo controlliPersonalizzati
 * 31021 02.04.20 GM - gestione 6 decimali  
 * 43891 13.11.24 BN - implimentazione versione web  
 * 43875 15.11.24 MN - Ottimizzare l'inizializzazione delle variabili static                 
 */


package it.thera.thip.logis.fis;
import it.thera.thip.logis.bas.CampoVariabile;
import it.thera.thip.logis.bas.EventoApplicativo;
import it.thera.thip.logis.bas.LogisPO;
import it.thera.thip.logis.bas.ParametriLogis;
import it.thera.thip.logis.bas.Utente;
import it.thera.thip.logis.lgb.GestioneMagazzino;
import it.thera.thip.logis.lgb.MagLogico;
import it.thera.thip.logis.lgb.Partner;
import it.thera.thip.logis.lgb.Societa;
import it.thera.thip.logis.prd.Articolo;
import it.thera.thip.logis.prd.CampoTrascodificaGruppo;
import it.thera.thip.logis.prd.Confezione;
import it.thera.thip.logis.prd.Gruppo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.base.Q6Calc;
import com.thera.thermfw.base.ResourceLoader;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.base.Utils;
import com.thera.thermfw.collector.BaseBOComponent;
import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.gui.cnr.OpenType;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.persist.TableManager;
import com.thera.thermfw.security.Security;
import com.thera.thermfw.web.WebForm;
;

public class Saldo extends LogisPO {


  // Diagnostica EasyCheck: nessuna modifica alla logica di persistenza.
  private static boolean logSaldoEasyCheck() {
    try {
      Utente u = Utente.getCurrentUtente();
      if (u != null) return "EASY_CHEK_WS_001".equals(u.getId());
      return Security.getCurrentUser() != null &&
          "EASY_CHEK_WS_001".equals(Security.getCurrentUser().getId());
    } catch (Exception ex) { return false; }
  }

  private static void logSaldo(String fase, String dettagli) {
    if (!logSaldoEasyCheck()) return;
    Trace.excStream.println("[EASYCHECK-SALDO] " + new Timestamp(System.currentTimeMillis())
        + " thread=" + Thread.currentThread().getName()
        + " fase=" + fase + " " + dettagli);
  }

  private String debugSaldo() {
    return "obj=" + System.identityHashCode(this)
        + " key=" + getKey() + " societa=" + getCodiceSocieta()
        + " codice=" + getCodice() + " onDB=" + onDB + " dirty=" + dirty
        + " tipoSaldo=" + getChiaveTipoSaldo() + " articolo=" + getChiaveArticolo()
        + " magLogico=" + getChiaveMagLogico() + " magFisico=" + getCodiceMagFisico()
        + " ubicazione=" + getChiaveUbicazione() + " udc=" + getCodiceMappaUdc()
        + " lotto=" + java.util.Arrays.toString(lotto)
        + " partita=" + partita + " dataPartita=" + dataPartita
        + " dataScadenza=" + dataScadenza + " qta1=" + qta1
        + " qta2=" + qta2 + " qta3=" + qta3 + " qtaConf=" + qtaConfezione
        + " rigaMovimento=" + getChiaveRigaMovimento();
  }

  private static String debugRiga(RigaMovimento rm) {
    if (rm == null) return "riga=null";
    return "riga=" + rm.getKey() + " saldoRiga=" + rm.getChiaveSaldo()
        + " societa=" + rm.getCodiceSocieta() + " gruppo=" + rm.getCodiceGruppo()
        + " articolo=" + rm.getCodiceArticolo() + " versione=" + rm.getVersione()
        + " magLogico=" + rm.getCodiceMagLogico() + " magFisico=" + rm.getChiaveMagFisico()
        + " ubicazione=" + rm.getCodiceUbicazione() + " udm=" + rm.getCodiceUdm()
        + " lotto=" + java.util.Arrays.toString(rm.getLotto())
        + " partita=" + rm.getPartita() + " dataPartita=" + rm.getDataPartita()
        + " dataScadenza=" + rm.getDataScadenza() + " qta1=" + rm.getQta1()
        + " qta2=" + rm.getQta2() + " qta3=" + rm.getQta3()
        + " segno=" + rm.getSegno();
  }

  public final static char VALIDO = 'V';
  public final static char SOSPESO = 'S';
  public final static char ANNULLATO = 'A';

  protected static final String FIS_RES = "it.thera.thip.logis.fis.resources.LogisFis";//25.10.07

  protected static final String SCHEMA = ParametriLogis.NOME_SCHEMA;

/**
 * Testo della query che calcola le quantità di merce già impegnata in missioni non terminate.
 */
  protected static String STMT_QTA_IMPEGNATA = "SELECT SUM(" + MissioneTM.QTA1_RIC + ")," +
                                                      "SUM(" + MissioneTM.QTA2_RIC + ")," +//24.10.07
                                                      "SUM(" + MissioneTM.QTA3_RIC + ") " +//24.10.07
                                               "FROM " + ParametriLogis.NOME_SCHEMA+"LMISSIONE M " + //Mod.7386
                                               "WHERE M." + MissioneTM.COD_SOCIETA + " = ?" +
                                                " AND M." + MissioneTM.COD_SALDO + " = ?" +
                                                " AND M." + MissioneTM.COD_CONFEZIONE + " IS NULL" +
                                                " AND M." + MissioneTM.STATO_MISSIONE + " <> '"+Missione.TERMINATA+"'";

  public static CachedStatement CS_STMT_QTA_IMPEGNATA = new CachedStatement(STMT_QTA_IMPEGNATA); // Fix 16470

/**
 * Testo della query che calcola le quantità di merce già impegnata in missioni non terminate.
 */
  protected static String STMT_QTA_CONF_IMPEGNATA = "SELECT SUM(" + MissioneTM.QTA1_RIC + ") " +
                                                    "FROM " + ParametriLogis.NOME_SCHEMA + "LMISSIONE M " + //Mod.7386
                                                    "WHERE M." + MissioneTM.COD_SOCIETA + " = ?" +
                                                     " AND M." + MissioneTM.COD_SALDO + " = ?" +
                                                     " AND M." + MissioneTM.COD_CONFEZIONE + " IS NOT NULL" +
                                                     " AND M." + MissioneTM.STATO_MISSIONE + " <> '"+Missione.TERMINATA+"'";

  public static CachedStatement CS_QTA_CONF_IMPEGNATA = new CachedStatement(STMT_QTA_CONF_IMPEGNATA); // Fix 16470

 /**
  *Dichiarazioni delle Proxy
  */
  protected Proxy societa = new Proxy (Societa.class);
  protected Proxy tipoSaldo = new Proxy (TipoSaldo.class);
  protected Proxy mappaUdc = new Proxy (MappaUdc.class);
  protected Proxy ubicazione = new Proxy (Ubicazione.class);
  protected Proxy magFisico = new Proxy (MagFisico.class);
  protected Proxy magLogico = new Proxy (MagLogico.class);
  protected Proxy gruppo = new Proxy (Gruppo.class);
  protected Proxy articolo = new Proxy (Articolo.class);
  protected Proxy confezione = new Proxy (Confezione.class);
  protected Proxy partner = new Proxy (Partner.class);
  protected Proxy rigaMovimento = new Proxy (RigaMovimento.class);
  protected Proxy causale = new Proxy (Causale.class);
  protected Proxy forma=new Proxy(Forma.class);
  /**
   * Attributi della classe
   */
   protected String codice;
   protected String note;
   protected char stato = VALIDO;
   protected String scomparto;
   protected String sottoUbicazione;
   protected String[] lotto = new String[5];
   protected BigDecimal qta1=new BigDecimal(0);
   protected BigDecimal qta2=new BigDecimal(0);
   protected BigDecimal qta3=new BigDecimal(0);
   protected BigDecimal qta4=new BigDecimal(0);
   protected BigDecimal qta5=new BigDecimal(0);

/**
 * Q.tà giacente espressa in confezioni.
 */
   protected BigDecimal qtaConfezione = new BigDecimal(0);
   protected Integer numUdm=new Integer(0);
   protected String umBase;
   protected String um2;
   protected String matricola;
   protected String codiceFifo;
   protected String partita;
   protected Date dataPartita;
   protected Date dataMovimento;
   protected String codiceUdm;
   protected BigDecimal volumeIngombro=new BigDecimal(0);
   protected BigDecimal lunghezza=new BigDecimal(0);
   protected BigDecimal larghezza=new BigDecimal(0);
   protected BigDecimal profondita=new BigDecimal(0);
   protected BigDecimal tara=new BigDecimal(0);

   protected BigDecimal qtaImpiego = new BigDecimal(0);   // Q.tà movimentata.

/**
 * Attributo non persistente, serve a numerare i saldi presenti sulla GuiTable
 * di Inserimento movimenti.
 */
   protected int indice=0;

/**
 * Attributo non persistente (calcolato): indica le quantità di merce (articolo)
 * del saldo corrente impiegata dalle missioni non terminate.
 */
   protected BigDecimal qtaDisponibile = new BigDecimal(0);

/**
 * Attributo non persistente (calcolato): indica le quantità di merce (confezione)
 * del saldo corrente impiegata dalle missioni non terminate.
 */
   protected BigDecimal qtaConfDisponibile = new BigDecimal(0);

/**
 * Attributo non persistente, serve per mantenere memorizzato il valore dell'anagrafica
 * che ho sulla riga movimento.
 */
  protected AnagraficaUdc anagUdc=null;

  //Fix 07529 inizio
  protected String um1;
  //Fix 07529 fine

  protected AnagraficaUdc getAnagUdc() {
    return anagUdc;
  }

  protected void setAnagUdc(AnagraficaUdc au) {
    anagUdc = au;
  }

/**
 * Viene settato a true se ho creato una nuova udc.
 */
   protected boolean nuovaUdc=false;

   //24.10.07 - inizio
   protected BigDecimal qta2Missionata = new BigDecimal(0);
   protected BigDecimal qta3Missionata = new BigDecimal(0);

   protected BigDecimal qta2Disponibile = new BigDecimal(0);
   protected BigDecimal qta3Disponibile = new BigDecimal(0);

   public BigDecimal getQta2Disponibile() {
     return qta2Disponibile;
   }

   public BigDecimal getQta3Disponibile() {
     return qta3Disponibile;
   }
   //24.10.07 - fine

   ////////////////////METODI DI GET E SET DEGLI ATTRIBUTI DELLA CLASSE////////
   /**
    * Metodo di set del codice di saldo
    */
   public void setCodice (String cod) {
    codice = cod;
    dirty = true;
    onDB = false;
   }

  /**
   * Metodo di get del codice di saldo
   */
   public String getCodice() {
     return codice;
   }

  protected Date dataScadenza;

  public void setDataScadenza(Date d) {
    dataScadenza = d;
    dirty = true;
  }

  public Date getDataScadenza() {
    return dataScadenza;
  }

  /**
   * Metodo di set delle note di saldo
   */
  public void setNote (String n) {
    note = n;
    dirty = true;
  }

  /**
   * Metodo di get delle note di saldo
   */
   public String getNote() {
     return note;
   }

  /**
   * Metodo diset dello stato di saldo
   */
   public void setStato (char st) {
     stato = st;
     dirty = true;
   }

  /**
   * Metodo di get dello stato di saldo
   */
   public char getStato() {
     return stato;
   }

  /**
   * Metodo di set dello scomparto
   */
   public void setScomparto (String scomp) {
     scomparto = scomp;
     dirty = true;
   }

   /**
    * Metodo di get dello scomparto
    */
   public String getScomparto() {
     return scomparto;
   }

  /**
   * Metodo di set della sottoUbicazione
   */
   public void setSottoUbicazione (String sottoubicaz) {
     sottoUbicazione = sottoubicaz;
     dirty = true;
   }

  /**
   * Metodo di get della sottoUbicazione
   */
   public String getSottoUbicazione() {
     return sottoUbicazione;
   }

  /**
   * Metodo di get di lotto
   */
   public String[] getLotto() {
    return lotto;
  }

 /**
  * Metodo di set di lotto
  */
  public void setLotto(String[] s) {
    lotto = s;
    dirty = true;
  }

 /**
  * Metodo di get di lotto
  */
  public String getLotto(int pos) {
    if ((pos>=0) && (pos<lotto.length)) {
      return lotto[pos];
    }
    else {
      return "";
    }
  }

 /**
  * Metodo di set di lotto
  */
  public void setLotto(String s, int pos) {
    if ((pos>=0) && (pos<lotto.length)) {
      lotto[pos] = s;
      dirty = true;
    }
  }

  public void setLotto1(String lo){
    setLotto(lo,0);
  }

  public String getLotto1(){
    return lotto[0];
  }

  public void setLotto2(String lo){
    setLotto(lo,1);
  }

  public String getLotto2(){
    return lotto[1];
  }

  public void setLotto3(String lo){
    setLotto(lo,2);
  }

  public String getLotto3(){
    return lotto[2];
  }

  public void setLotto4(String lo){
    setLotto(lo,3);
  }

  public String getLotto4(){
    return lotto[3];
  }

  public void setLotto5(String lo){
    setLotto(lo,4);
  }

  public String getLotto5(){
    return lotto[4];
  }

  public String getLotto1GUITable(){
    return getLotto(0);
  }

  public String getLotto2GUITable(){
    return getLotto(1);
  }

  public String getLotto3GUITable(){
    return getLotto(2);
  }

  public void setQtaConfezione(BigDecimal q1) {
    qtaConfezione = q1;
    dirty = true;
  }

  public BigDecimal getQtaConfezione() {
    return qtaConfezione;
  }

 /**
  * Metodo di set della prima quantita
  */
  public void setQta1 (BigDecimal q1) {
     qta1 = q1;
     dirty = true;
  }

  /**
   * Metodo di get della prima quantita
   */
   public BigDecimal getQta1() {
     return qta1;
   }

  /**
   * Metodo di get della seconda quantita
   */
   public void setQta2 (BigDecimal q2) {
     qta2 = q2;
     dirty = true;
   }

  /**
   * Metodo di get della seconda quantita
   */
   public BigDecimal getQta2() {
     return qta2;
   }

  /**
   * Metodo di set della terza quantita
   */
   public void setQta3 (BigDecimal q3) {
     qta3 = q3;
     dirty = true;
   }

  /**
   * Metodo di get della terza quantita
   */
   public BigDecimal getQta3() {
     return qta3;
   }

  /**
   * Metodo di set della quarta quantita
   */
   public void setQta4 (BigDecimal q4) {
     qta4 = q4;
     dirty = true;
   }

  /**
   * Metodo di get della quarta quantita
   */
   public BigDecimal getQta4() {
     return qta4;
   }

  /**
   * Metodo di set della quinta quantita
   */
   public void setQta5 (BigDecimal q5) {
     qta5 = q5;
     dirty = true;
   }

  /**
   * Metodo di get della quinta quantita
   */
   public BigDecimal getQta5() {
     return qta5;
   }

  /**
   * Metodo di set del numero di colli
   */
   public void setNumUdm (Integer n) {
     numUdm = n;
     dirty = true;
   }

  /**
   * Metodo di get del numero di colli
   */
   public Integer getNumUdm() {
     return numUdm;
   }

  /**
   * Metodo di set di umBase
   */
   public void setUmBase (String u) {
     umBase = u;
     dirty = true;
   }

  /**
   * Metodo di get di umBase
   */
   public String getUmBase() {
     return umBase;
   }

  /**
   * Metodo di set di um2
   */
   public void setUm2 (String u) {
     um2 = u;
     dirty = true;
   }

  /**
   * Metodo di get di um2
   */
   public String getUm2() {
     return um2;
   }

  /**
   * Metodo di set di matricola
   */
   public void setMatricola (String m) {
     matricola = m;
     dirty = true;
   }

  /**
   * Metodo di get di matricola
   */
   public String getMatricola() {
     return matricola;
   }

  /**
   * Metodo di set di codiceFifo
   */
   public void setCodiceFifo (String c) {
     codiceFifo = c;
     dirty = true;
   }

  /**
   * Metodo di get di codiceFifo
   */
   public String getCodiceFifo() {
     return codiceFifo;
   }

  /**
   * Metodo di set di partita entrata
   */
   public void setPartita (String pe) {
     partita = pe;
     dirty = true;
   }

  /**
   * Metodo di get di partita entrata
   */
   public String getPartita() {
     return partita;
   }

  /**
   * Metodo di set di data di entrata della partita
   */
   public void setDataPartita (Date dpe) {
     dataPartita = dpe;
     dirty = true;
   }

  /**
   * Metodo di get di data di entrata della partita
   */
   public Date getDataPartita() {
     return dataPartita;
   }


  /**
   * Metodo di set di data di movimento partita
   */
   public void setDataMovimento(Date dm) {
     dataMovimento = dm;
     dirty = true;
   }

  /**
   * Metodo di get di data di movimento
   */
   public Date getDataMovimento() {
     return dataMovimento;
   }

  /**
   * Metodo di set del codice del collo dell'articolo
   */
   public void setCodiceUdm (String cca) {
     codiceUdm = cca;
     dirty = true;
   }

  /**
   * Metodo di get del codice del collo dell'articolo
   */
   public String getCodiceUdm() {
     return codiceUdm;
   }

   /////////////////////PROXY A SOCIETA//////////////////////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto Societa
  */
  public void setSocieta (Societa s){
    societa.setObject (s);
    String soc = s.getKey();
    setChiaveTipoSaldo(KeyHelper.replaceTokenObjectKey(getChiaveTipoSaldo(), 1, soc));
    setChiaveMagLogico(KeyHelper.replaceTokenObjectKey(getChiaveMagLogico(), 1, soc));
    setChiavePartner(KeyHelper.replaceTokenObjectKey(getChiavePartner(),1, soc));
    setChiaveCausale(KeyHelper.replaceTokenObjectKey(getChiaveCausale(),1, soc));
    setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(),1, soc));
    dirty = true;
    onDB = false;
  }

 /**
  * Metodo che ritorna l'oggetto referenziato
  * @return oggetto referenziato
  */
  public Societa getSocieta (){
    return (Societa) societa.getObject();
  }

 /**
  * Metodo di set della chiave dell'oggetto referenziato
  */
  public void setCodiceSocieta (String soc) {
    societa.setKey(soc);
    setChiaveTipoSaldo(KeyHelper.replaceTokenObjectKey(getChiaveTipoSaldo(), 1, soc));
    //testataCollo.setKey(soc);
    //testataMovimento.setKey(soc);
    setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(),1, soc));
    setChiaveCausale(KeyHelper.replaceTokenObjectKey(getChiaveCausale(),1,soc));
    setChiaveMagLogico(KeyHelper.replaceTokenObjectKey(getChiaveMagLogico(),1,soc));
    setChiavePartner(KeyHelper.replaceTokenObjectKey(getChiavePartner(),1, soc));
    dirty = true;
    onDB = false;
  }

 /**
  * Metodo di get della chiave dell'oggetto referenziato
  */
  public String getCodiceSocieta () {
    return societa.getKey();
  }
  ///////////////////FINE PROXY SOCIETA///////////////////////////////////////

  /////////////////////////PROXY TIPOSALDO//////////////////////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto TipoSaldo
  */
  public void setTipoSaldo (TipoSaldo ts){
    tipoSaldo.setObject (ts);
    dirty = true;
  }

 /**
  * Metodo di get dell'oggetto referenziato
  * @return oggetto referenziato
  */
  public TipoSaldo getTipoSaldo (){
    return (TipoSaldo) tipoSaldo.getObject();
  }

 /**
  * Metodo di set della prima parte della chiave di TipoSaldo
  */
  public void setChiaveTipoSaldo(String chiave){
    tipoSaldo.setKey(chiave);
    dirty = true;
  }

 /**
  * Metodo di get della prima parte della chiave di TipoSaldo
  */
  public String getChiaveTipoSaldo(){
    return tipoSaldo.getKey();
  }

 /**
  * Metodo di set della seconda parte della chiave di TipoSaldo
  */
  public void setCodiceTipoSaldo(String cod){
    setChiaveTipoSaldo(KeyHelper.replaceTokenObjectKey(getChiaveTipoSaldo(), 2, cod));
    dirty = true;
  }

 /**
  * Metodo di get della seconda parte della chiave di TipoSaldo
  */
  public String getCodiceTipoSaldo() {
    return KeyHelper.getTokenObjectKey(getChiaveTipoSaldo(), 2);
  }
 ///////////////////FINE PROXY TIPOSALDO///////////////////////////////////////

  ////////////////////PROXY MAPPAUDC//////////////////////////////////////////
  /**
   * Metodo di set dell'oggetto da referenziare
   * @param  oggetto MappaUdc
   */
  public void setMappaUdc (MappaUdc m){
    mappaUdc.setObject (m);
    dirty = true;
  }

 /**
  * Metodo di get dell'oggetto referenziato
  * @return oggetto referenziato
  */
  public MappaUdc getMappaUdc (){
    return (MappaUdc) mappaUdc.getObject();
  }

 /**
  * Metodo di set della prima parte della chiave dell'oggetto referenziato
  */
  public void setCodiceMappaUdc(String cod){
    mappaUdc.setKey(cod);
    dirty = true;
  }

 /**
  * Metodo di get della prima parte chiave dell'oggetto referenziato
  */
  public String getCodiceMappaUdc(){
    return mappaUdc.getKey();
  }
  ///////////////////////FINE PROXY MAPPAUDC//////////////////////////////////

  ///////////////////////////PROXY MAGFISICO///////////////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto MagFisico
  */
  public void setMagFisico (MagFisico mf){
    magFisico.setObject (mf);
    dirty = true;
  }

 /**
  * Metodo che ritorna l'oggetto referenziato
  * @return oggetto referenziato
  */
  public MagFisico getMagFisico (){
    return (MagFisico) magFisico.getObject();
  }

 /**
  * Metodo di set della chiave di MagFisico
  */
  public void setCodiceMagFisico(String cod){
    magFisico.setKey(cod);
    setChiaveUbicazione(KeyHelper.replaceTokenObjectKey(getChiaveUbicazione(),1,cod));
    dirty = true;
  }

  /**
   * Metodo di get della chiave di MagFisico
   */
  public String getCodiceMagFisico(){
    return magFisico.getKey();
  }
  //////////////////////////////FINE PROXY MAGFISICO////////////////////

  //////////////////////////////////PROXY UBICAZIONE//////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto Ubicazione
  */
  public void setUbicazione (Ubicazione ubicaz){
    ubicazione.setObject (ubicaz);
    dirty = true;
  }

 /**
  * Metodo che ritorna l'oggetto referenziato
  * @return oggetto referenziato
  */
  public Ubicazione getUbicazione (){
    return (Ubicazione) ubicazione.getObject();
  }

 /**
  * Metodo di set della prima parte della chiave di Ubicazione
  */
  public void setChiaveUbicazione(String chiave){
    ubicazione.setKey(chiave);
    dirty = true;
  }

  /**
   * Metodo di get della prima parte della chiave di Ubicazione
   */
  public String getChiaveUbicazione(){
    return ubicazione.getKey();
  }

  /**
   * Metodo di set della seconda parte della chiave di Ubicazione
   */
  public void setCodiceUbicazione(String cod){
    setChiaveUbicazione(KeyHelper.replaceTokenObjectKey(getChiaveUbicazione(), 2, cod));
    dirty = true;
  }

  /**
   * Metodo di get della seconda parte della chiave di Ubicazione
   */
  public String getCodiceUbicazione() {
    return KeyHelper.getTokenObjectKey(getChiaveUbicazione(), 2);
  }
  //////////////////////////FINE PROXY UBICAZIONE//////////////////////////////

  ///////////////////////////PROXY MAGLOGICO//////////////////////////
   /**
   * Metodo che imposta l'oggetto da referenziare
   * @param  oggetto MagLogico
   */
  public void setMagLogico (MagLogico ml){
    magLogico.setObject (ml);
    dirty = true;
  }

  /**
   * Metodo che ritorna l'oggetto referenziato
   * @return oggetto referenziato
   */
  public MagLogico getMagLogico (){
    return (MagLogico) magLogico.getObject();
  }

  /**
   * Metodo di set della prima parte della chiave di MagLogico
   */
  public void setChiaveMagLogico(String chiave){
    magLogico.setKey(chiave);
    dirty = true;
  }

  /**
   * Metodo di get della prima parte della chiave di MagLogico
   */
  public String getChiaveMagLogico(){
    return magLogico.getKey();
  }

  /**
   * Metodo di set della seconda parte della chiave di MagLogico
   */
  public void setCodiceMagLogico(String cod){
    setChiaveMagLogico(KeyHelper.replaceTokenObjectKey(getChiaveMagLogico(), 2, cod));
    dirty = true;
  }

  /**
   * Metodo di get della seconda parte della chiave di MagLogico
   */
  public String getCodiceMagLogico() {
    return KeyHelper.getTokenObjectKey(getChiaveMagLogico(), 2);
  }
  //////////////////////////////FINE PROXY MAGLOGICO/////////////////////////////

  ////////////////////////PROXY GRUPPO///////////////////////////////////////
  /**
   * Metodo che imposta l'oggetto da referenziare QS PROXY NON SERVE!!!
   * @param  oggetto Gruppo
   */
  public void setGruppo (Gruppo gr){
    gruppo.setObject (gr);
    dirty=true;
  }

  /**
   * Metodo che ritorna l'oggetto referenziato
   * @return oggetto referenziato
   */
  public Gruppo getGruppo (){
    return (Gruppo) gruppo.getObject();
  }

  public void setCodiceGruppo(String key){
    gruppo.setKey(key);
    setChiaveArticolo(KeyHelper.replaceTokenObjectKey(getChiaveArticolo(), 1, key));
    setChiaveConfezione(KeyHelper.replaceTokenObjectKey(getChiaveConfezione(), 1, key));
    dirty=true;
  }

  public String getCodiceGruppo(){
    return gruppo.getKey();
  }
  ///////////////////////////////FINE PROXY GRUPPO//////////////////////////////

  /////////////////////////////PROXY ARTICOLO///////////////////////////////////
  /**
   * Metodo che imposta l'oggetto da referenziare
   * @param  oggetto Articolo
   */
  public void setArticolo (Articolo art){
    articolo.setObject(art);
    dirty = true;
  }

  /**
   * Metodo che ritorna l'oggetto referenziato
   * @return oggetto referenziato
   */
  public Articolo getArticolo (){
    return (Articolo) articolo.getObject();
  }

 /**
  * Metodo di get della chiave di Articolo
  */
  public String getChiaveArticolo(){
    return articolo.getKey();
  }

 /**
  * Metodo di set della chiave di Articolo, che la "spacchetta secondo un ordine definito"
  */
  public void setChiaveArticolo(String key){
    articolo.setKey(key);
    dirty = true;
  }

 /**
  * Metodo di get della seconda parte della chiave di Articolo
  */
  public void setCodiceArticolo(String codAr) {
    setChiaveArticolo(KeyHelper.replaceTokenObjectKey(getChiaveArticolo(), 2, codAr));
    setChiaveConfezione(KeyHelper.replaceTokenObjectKey(getChiaveConfezione(), 2, codAr));
    dirty = true;
  }

  /**
   * Metodo di get della seconda parte della chiave di Articolo
   */
  public String getCodiceArticolo () {
    return KeyHelper.getTokenObjectKey(getChiaveArticolo(),2);
  }

  /**
   * Metodo di set della terza parte della chiave primaria di Articolo
   */
  public void setVersione (String vers) {
    setChiaveArticolo(KeyHelper.replaceTokenObjectKey(getChiaveArticolo(), 3, vers));
    setChiaveConfezione(KeyHelper.replaceTokenObjectKey(getChiaveConfezione(), 3, vers));
    dirty = true;
  }

  /**
   * Metodo di get della terza parte della chiave primaria di Articolo
   */
  public String getVersione () {
    return KeyHelper.getTokenObjectKey(getChiaveArticolo(),3);
  }
  //////////////////////////FINE PROXY ARTICOLO//////////////////////////////

  //////////////////////PROXY CONFEZIONE //////////////////////////////////////
  /**
   * Metodo che imposta l'oggetto da referenziare
   * @param  oggetto Confezione
   */
  public void setConfezione (Confezione conf){
    confezione.setObject (conf);
    dirty = true;
  }

  /**
   * Metodo che ritorna l'oggetto referenziato
   * @return oggetto referenziato
   */
  public Confezione getConfezione (){
    return (Confezione) confezione.getObject();
  }
  /**
   * Metodo di set delle prime tre parti della chiave di Confezione
   */
  public void setChiaveConfezione(String chiave){
    confezione.setKey(chiave);
    dirty = true;
  }

  /**
   * Metodo di get delle prime tre parti della chiave di Confezione
   */
  public String getChiaveConfezione(){
    return confezione.getKey();
  }

 /**
  * Metodo di get della quarta parte della chiave di Confezione
  */
  public void setCodiceConfezione(String cod){
    setChiaveConfezione(KeyHelper.replaceTokenObjectKey(getChiaveConfezione(), 4, cod));
    dirty = true;
  }

 /**
  * Metodo di get della quarta parte della chiave di Confezione
  */
  public String getCodiceConfezione() {
    return KeyHelper.getTokenObjectKey(getChiaveConfezione(), 4);
  }
  //////////////////////FINE PROXY CONFEZIONE///////////////////////////////


  ///////////////////////PROXY PARTNER //////////////////////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto Partner
  */
  public void setPartner (Partner p){
    partner.setObject (p);
    dirty = true;
  }

 /**
  * Metodo che ritorna l'oggetto referenziato
  * @return oggetto referenziato
  */
  public Partner getPartner (){
    return (Partner) partner.getObject();
  }

 /**
  * Metodo di set della prima parte della chiave di Partner
  */
  public void setChiavePartner(String chiave){
    partner.setKey(chiave);
    dirty = true;
  }

 /**
  * Metodo di get della prima parte della chiave di Partner
  */
  public String getChiavePartner(){
    return partner.getKey();
  }

 /**
  * Metodo di set della seconda parte della chiave di Partner
  */
  public void setCodicePartner(String cod){
    setChiavePartner(KeyHelper.replaceTokenObjectKey(getChiavePartner(), 2, cod));
    dirty = true;
  }

 /**
  * Metodo di get della seconda parte della chiave di Partner
  */
  public String getCodicePartner() {
    return KeyHelper.getTokenObjectKey(getChiavePartner(), 2);
  }
/**
 * Restituisce il codice del partner e la sua descrizione, serve per la
 * visualizzazione nelle GUITable.
 */
  public String getDescrizionePartner(){
    if(getPartner()!=null){
      return getPartner().getCodice()+" - "+getPartner().getDescrizione();
    }
    else return "";
  }
  ///////////////////////////FINE PROXY PARTNER//////////////////////////////


  ////////////////////PROXY RIGAMOVIMENTO//////////////////////////////////////
  /**
   * Metodo che imposta l'oggetto da referenziare
   * @param  oggetto RigaMovimento DA SCOMMENTARE QUANDO CI SARA LA CLASSE
   */
  public void setRigaMovimento (RigaMovimento rm){
    rigaMovimento.setObject (rm);
    dirty = true;
  }

  /**
   * Metodo che ritorna l'oggetto referenziato
   * @return oggetto referenziato
   */
  public RigaMovimento getRigaMovimento (){
    return (RigaMovimento) rigaMovimento.getObject();
  }

  /**
   * Metodo di set della prima parte della chiave di RigaMovimento
   */
  public void setChiaveRigaMovimento(String chiave){
    rigaMovimento.setKey(chiave);
    dirty = true;
  }

  /**
   * Metodo di get della prima parte della chiave di RigaMovimento
   */
  public String getChiaveRigaMovimento(){
    return rigaMovimento.getKey();
  }

  /**
   * Metodo di set della seconda parte della chiave di RigaMovimento
   */
  public void setCodiceMovimento(String codMov){
    setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 2, codMov));
    dirty = true;
  }

  /**
   * Metodo di get della seconda parte della chiave di RigaMovimento
   */
  public String getCodiceMovimento() {
    return KeyHelper.getTokenObjectKey(getChiaveRigaMovimento(), 2);
  }

  /**
   * Metodo di set della terza parte della chiave di RigaMovimento
   */
  public void setCodiceRigaMovimento(Integer cod){
    if(cod!=null){
//      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 3,Integer.toString(cod.intValue())));
//      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 3,""+cod.intValue()));
      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 3,cod.toString()));
      dirty = true;
    }
  }

  /**
   * Metodo di get della terza parte della chiave di RigaMovimento
   */
  public Integer getCodiceRigaMovimento() {
    String cod=KeyHelper.getTokenObjectKey(getChiaveRigaMovimento(), 3);
    if (cod != null && !cod.equals(""))
      return new Integer(cod);
    return null;
  }

  /**
   * Metodo di set della quarta parte della chiave di RigaMovimento
   */
  public void setCodiceDettMovimento(Integer codDett){
    if(codDett!=null){
//      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 4, Integer.toString(codDett.intValue())));
//      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 4, ""+codDett.intValue()));
      setChiaveRigaMovimento(KeyHelper.replaceTokenObjectKey(getChiaveRigaMovimento(), 4, codDett.toString()));
      dirty = true;
    }
  }

  /**
   * Metodo di get della quarta parte della chiave di RigaMovimento
   */
  public Integer getCodiceDettMovimento() {
    String cod=KeyHelper.getTokenObjectKey(getChiaveRigaMovimento(), 4);
    if (cod != null && !cod.equals(""))
      return new Integer(cod);
    return null;
  }
  ////////////////////////FINE PROXY RIGAMOVIMENTO///////////////////////////

  ///////////////////////////PROXY CAUSALE/////////////////////////////////////
 /**
  * Metodo che imposta l'oggetto da referenziare
  * @param  oggetto Causale
  */
  public void setCausale (Causale c){
    causale.setObject (c);
    dirty = true;
  }

 /**
  * Metodo che ritorna l'oggetto referenziato
  * @return oggetto referenziato
  */
  public Causale getCausale (){
    return (Causale) causale.getObject();
  }

 /**
  * Metodo di set della prima parte della chiave di Causale
  */
  public void setChiaveCausale(String chiave){
    causale.setKey(chiave);
    dirty = true;
  }

 /**
  * Metodo di get della prima parte della chiave di Causale
  */
  public String getChiaveCausale(){
    return causale.getKey();
  }

 /**
  * Metodo di set della seconda parte della chiave di Causale
  */
  public void setCodiceCausale(String cod){
    setChiaveCausale(KeyHelper.replaceTokenObjectKey(getChiaveCausale(), 2, cod));
    dirty = true;
  }

 /**
  * Metodo di get della seconda parte della chiave di Causale
  */
  public String getCodiceCausale() {
    return KeyHelper.getTokenObjectKey(getChiaveCausale(), 2);
  }

  public void setVolumeIngombro(BigDecimal v){
    volumeIngombro=v;
    dirty=true;
  }

  public BigDecimal getVolumeIngombro(){
    return volumeIngombro;
  }

  public void setLunghezza(BigDecimal t){
    lunghezza=t;
    dirty=true;
  }

  public BigDecimal getLunghezza(){
    return lunghezza;
  }
  public void setLarghezza(BigDecimal t){
    larghezza=t;
    dirty=true;
  }

  public BigDecimal getLarghezza(){
    return larghezza;
  }

  public void setProfondita(BigDecimal t){
    profondita=t;
    dirty=true;
  }

  public BigDecimal getProfondita(){
    return profondita;
  }

  public void setTara(BigDecimal t){
    tara=t;
    dirty=true;
  }

  public BigDecimal getTara(){
    return tara;
  }

  public void setForma(Forma f){
    forma.setObject(f);
    dirty=true;
  }

  public Forma getForma(){
    return (Forma) forma.getObject();
  }

  public void setChiaveForma(String key){
    forma.setKey(key);
    dirty=true;
  }

  public String getChiaveForma(){
    return forma.getKey();
  }

  public void setIndice(int i){
    indice=i;
  }

  public int getIndice(){
    return indice;
  }

  //05.04.11 - inizio
  /**
   * Attributo non persistente,
   * contiene l'indice utilizzato per la scelta del saldo da assegnare alla missione
   */
   protected BigInteger indicePerPrelievo = new BigInteger("0");

   public void setIndicePerPrelievo(BigInteger i){
     indicePerPrelievo=i;
   }

   public BigInteger getIndicePerPrelievo(){
     return indicePerPrelievo;
   }
   //05.04.11 - fine


  public void setKey(String key) {
    String codSoc = KeyHelper.getTokenObjectKey(key, 1);  // Codice della società
    setCodiceSocieta(codSoc);
    codice = KeyHelper.getTokenObjectKey(key, 2);         // Codice del saldo
    //setChiaveDestiDef(KeyHelper.replaceTokenObjectKey(getChiaveDestiDef(), 2, codice));
    dirty = true;
    onDB = false;
  }

  public String getKey() {
    String[] keys = {societa.getKey(), codice};
    return KeyHelper.buildObjectKey(keys);
  }

/**
 * Applica le modifiche al saldo relativo alla riga movimento. Il flag di storno applica
 * le quantita' con segno invertito.
 * @param rigaMovimento da essa vengono presi gli attributi da settare sul saldo
 * @param storno indica se fare uno storno oppure un'aggiunta.
 * @revision 25/07/01 JF: modifica per ricerca saldo...non viene fatta se ho gia' settato
 * un saldo sulla riga movimento
 */
  public static synchronized Vector applica(RigaMovimento rm, boolean storno){
    Saldo s = null;
    Ubicazione u = null;
    Vector errori = new Vector();
    boolean logga = false;
    if (Utente.getCurrentUtente() == null) {
        if("EASY_CHEK_WS_001".equals(Security.getCurrentUser().getId()))
        	logga = true;
    } else {
    	  if("EASY_CHEK_WS_001".equals(Utente.getCurrentUtente().getId()))
          	logga = true;
     }
    try {
      logSaldo("APPLICA-START", "storno=" + storno + " " + debugRiga(rm));
      saldiOmologhi.removeAllElements();
      if(logga)
    	  Trace.excStream.println("-> Saldo su riga movimento ? "+(rm.getSaldo() != null ? "true, codice : "+rm.getChiaveSaldo() : "false"));
      if (rm.getSaldo() != null) {
        try {
          //s = Saldo.elementWithKey(rm.getChiaveSaldo(), PersistentObject.OPTIMISTIC_LOCK);
          s = Saldo.elementWithKey(rm.getChiaveSaldo(), PersistentObject.CURSOR_LOCK);//23.04.12
        } catch(Exception e) {
          e.printStackTrace(Trace.excStream);
          ErrorMessage err = new ErrorMessage("LOGIS01054",e.getMessage());
          errori.addElement(err);
        }
        if (errori.size() > 0)
          return errori;
        if (s != null)
          saldiOmologhi.addElement(s);
        logSaldo("SALDO-DIRETTO", s == null ? "non trovato" : s.debugSaldo());
      } else {
        //10.03.15 - inizio
        //errori = ricercaSaldo(rm);
        Saldo sal = (Saldo) Factory.createObject(Saldo.class);
        errori = sal.ricercaSaldo(rm);
        logSaldo("RICERCA-ESITO", "omologhi=" + saldiOmologhi.size() + " errori=" + errori.size());
        if(logga) {
        	Trace.excStream.println("-> Ricerca saldo, saldi trovati : "+saldiOmologhi.size());
        }
        //10.03.15 - fine
        if (errori.size() > 0)
          return errori;
      }
      if (saldiOmologhi.size() == 0) {
        s = (Saldo) Factory.createObject(Saldo.class);
        logSaldo("NUOVO-SALDO", "nessun omologo; " + debugRiga(rm));
        errori = s.settaAttributiDaRigaMovimento(rm, storno);
        logSaldo("NUOVO-SALDO-ATTRIBUTI", s.debugSaldo() + " errori=" + errori.size());
        if(logga)
        	Trace.excStream.println("-> Ricerca saldo, saldi omologhi 0, creo il saldo da riga movimento");
        if (errori.size() > 0)
          return errori;
        if (s.getArticolo().getFlagInvExec()) {
          //ErrorMessage err = new ErrorMessage("LOGIS01158","Articolo "+s.getCodiceArticolo());//Fix 15941
          ErrorMessage err = new ErrorMessage("LOGIST0444", s.getCodiceArticolo());//Fix 15941
          errori.addElement(err);
          return errori;
        }
      } else {
        s = (Saldo) saldiOmologhi.elementAt(0);
        logSaldo("SALDO-ESISTENTE", s.debugSaldo());
        if (rm.getCodiceUdm() != null &&
            !rm.getCodiceUdm().equals("")) {
          if (rm.getArticolo() == null ||
              (rm.getArticolo() != null &&
               !s.getArticolo().getKey().equals(rm.getArticolo().getKey()))) {
            ErrorMessage err = new ErrorMessage("LOGIS01067");
            errori.addElement(err);
            return errori;
          }
        }
        errori = s.settaAltriDati(rm);
        //07.09.17 - inizio
        if (errori.size() > 0)
          return errori;
        errori = s.controlliPersonalizzati(rm);
        if (errori.size() > 0)
          return errori;
        //07.09.17 - fine
      }
      boolean retUbic = false;
      boolean updateUbic = aggiornaUbicazione(s.getUbicazione());//23.04.12
      if (updateUbic){//23.04.12
        try {
          //retUbic = s.getUbicazione().retrieve(PersistentObject.OPTIMISTIC_LOCK);
            retUbic = s.getUbicazione().retrieve( PersistentObject.CURSOR_LOCK);//23.04.12
          u = s.getUbicazione();
        } catch (Exception ex) {
          ex.printStackTrace(Trace.excStream);
          //ErrorMessage err = new ErrorMessage("LOGIS01054", "In recupero dell'ubicazione: " + ex.getMessage());//Fix 15941
          ErrorMessage err = new ErrorMessage("LOGIST0445", ex.getMessage());//Fix 15941
          errori.addElement(err);
          return errori;
        }
        if (!retUbic) {
          //ErrorMessage err = new ErrorMessage("LOGIS01054", "La risorsa 'Ubicazione " + s.getChiaveUbicazione().replace(KeyHelper.KEY_SEPARATOR.charAt(0), '/') + "' è allocata da altri: ripetere l'operazione.");//Fix 15941
          ErrorMessage err = new ErrorMessage("LOGIST0447", s.getChiaveUbicazione().replace(KeyHelper.KEY_SEPARATOR.charAt(0), '/') + "' è allocata da altri: ripetere l'operazione.");//Fix 15941
          errori.addElement(err);
          return errori;
        }
      }
      boolean add = (!storno && rm.getSegno() == RigaMovimento.PIU) ||
                    (storno && rm.getSegno() == RigaMovimento.MENO);
      logSaldo("QUANTITA-PRIMA", "add=" + add + " " + s.debugSaldo());
      s.settaQuantita(rm, add);
      logSaldo("QUANTITA-DOPO", s.debugSaldo());
      if (updateUbic){//23.04.12
        if (add)
          u.setNumDepositi(u.getNumDepositi() + 1);
        else    
          u.setNumPrelievi(u.getNumPrelievi() + 1);
      } 
      BigDecimal volume = rm.getVolumeIngombro();
      BigDecimal lunghezza = rm.getLunghezza();
      BigDecimal larghezza = rm.getLarghezza();
      BigDecimal altezza = rm.getProfondita();
      BigDecimal peso = new BigDecimal(0);
      if (rm.getMappaUdc() != null &&
          rm.getMappaUdc().getAnagraficaUdc().getDim().getFlagCheckPeso()) {
        if (rm.getMappaUdc().getAnagraficaUdc().getDim().getPesoMax() != null)
          peso = rm.getMappaUdc().getAnagraficaUdc().getDim().getPesoMax();
        if (rm.getMappaUdc().getAnagraficaUdc().getTara() != null)
          peso = peso.add(rm.getMappaUdc().getAnagraficaUdc().getTara());
      } else {
        if (rm.getAnagraficaUdc() != null &&
            rm.getAnagraficaUdc().getDim().getFlagCheckPeso()) {
          if (rm.getAnagraficaUdc().getDim().getPesoMax() != null)
            peso = rm.getAnagraficaUdc().getDim().getPesoMax();
          if (rm.getAnagraficaUdc().getTara() != null)
            peso = peso.add(rm.getAnagraficaUdc().getTara());
        } else {
          if (rm.getArticolo() != null &&
              rm.getArticolo().getPesoUnitario() != null)
            peso = peso.add(rm.getArticolo().getPesoUnitario().multiply(rm.getQta1()));//27.05.14
        }
      }
      //25.10.07 - inizio
      Vector v = s.isCancellabile();
      logSaldo("CANCELLABILE", "risultato=" + v + " " + s.debugSaldo());
      if (v.get(0) instanceof ErrorMessage){
        errori.addAll(v);
      }
      else {
        //if (s.isCancellabile()) {
        Boolean b = (Boolean)v.get(0);
        if (b.booleanValue()) {
      //25.10.07 - fine
          errori = s.svuotamento();
          if (errori.size() > 0)
            return errori;
        }
        else {
          logSaldo("AGGIORNAMENTO-CHIAMATA", s.debugSaldo());
          errori = s.aggiornamento(add);
          logSaldo("AGGIORNAMENTO-ESITO", "errori=" + errori + " " + s.debugSaldo());
          if (errori.size() > 0)
            return errori;
          if (s.getMappaUdc() == null ||
              s.nuovaUdc) { // Non in caso di aggiunta.
            if (updateUbic){//23.04.12
              if (add)
                errori = u.aggiornamento(s.getForma(),
                    peso,
                    volume,
                    lunghezza,
                    larghezza,
                    altezza,
                    s.qtaImpiego);
              else
                errori = s.getUbicazione().svuotamento(s.getForma(), s.qtaImpiego, peso);//27.05.14
            }
          }
        }
      }
    }
    //23.04.12 - inizio
    finally {
      if (s!= null) {
        try {
          s.unlock();
        }
        catch(Exception ex){
          Trace.excStream.println("Eccezione durante UNLOCK di saldo: " + s.getKey());
          ex.printStackTrace(Trace.excStream);
        }
      }
      if (u!= null){
        try {
          u.unlock();
        }
        catch(Exception ex) {
          Trace.excStream.println("Eccezione durante UNLOCK di ubicazione: " + u.getKey());
          ex.printStackTrace(Trace.excStream);
        }
      }
    }
    //23.04.12 - fine
    return errori;
  }

  //23.04.12 - inizio
  // metodo da utilizzare per decidere se aggiornare l'ubicazione
  public static boolean aggiornaUbicazione(Ubicazione ubic) {
    return ubic.getFlagAggiornamentoUbic();
  }
  //23.04.12 - fine
  
  protected static Vector saldiOmologhi = new Vector();


  protected static Vector ricercaSaldo(RigaMovimento rm, Vector saldi) {
    //10.03.15 - inizio
    //return ricercaSaldo(rm);
    Saldo s= (Saldo) Factory.createObject(Saldo.class);
    return s.ricercaSaldo(rm);
    //10.03.15 - fine

  }
  
  //protected static Vector ricercaSaldo(RigaMovimento rm) {
  protected Vector ricercaSaldo(RigaMovimento rm) {//10.03.15
    String where = null;
    Vector errori = new Vector();
    logSaldo("RICERCA-START", debugRiga(rm));
    if (rm.getCodiceUdm() != null &&
        !rm.getCodiceUdm().equals(""))
      where = SaldoTM.COD_UDM + " = '" + rm.getCodiceUdm() + "'";
    else {
      if (rm.getCodiceGruppo() != null &&
          !rm.getCodiceGruppo().equals(""))
        where = SaldoTM.COD_GRUPPO + " = '" + rm.getCodiceGruppo() + "'";
      else {
        //ErrorMessage err = new ErrorMessage("LOGIS01003", "Gruppo");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0448");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getCodiceArticolo() != null &&
          !rm.getCodiceArticolo().equals(""))
        where += " AND " + SaldoTM.COD_ARTICOLO + " = '" + rm.getCodiceArticolo() + "'";
      else {
        //ErrorMessage err = new ErrorMessage("LOGIS01003","Articolo");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0449");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getVersione() != null &&
          !rm.getVersione().equals(""))
        where += " AND " + SaldoTM.VERSIONE + " = '" + rm.getVersione() + "'";
      else {
        //ErrorMessage err=new ErrorMessage("LOGIS01003","Versione");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0450");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getConfezione() != null &&
          !rm.getConfezione().getFlagDivisibile())
        where += " AND " + SaldoTM.COD_CONFEZIONE + " = '" + rm.getCodiceConfezione() + "'";
      else
        where += " AND " + SaldoTM.COD_CONFEZIONE + " IS NULL";
      if (rm.getCodiceSocieta() != null &&
          !rm.getCodiceSocieta().equals(""))
        where += " AND " + SaldoTM.COD_SOCIETA + " = '" + rm.getCodiceSocieta() + "'";
      else {
        //ErrorMessage err=new ErrorMessage("LOGIS01003","Impresa C.");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0451");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getCodiceMagLogico() != null &&
          !rm.getCodiceMagLogico().equals(""))
        where += " AND " + SaldoTM.COD_MAG_LOGICO + " = '" + rm.getCodiceMagLogico() + "'";
      else{
        //ErrorMessage err=new ErrorMessage("LOGIS01003","Mag. logico");//Fix
        ErrorMessage err = new ErrorMessage("LOGIST0454");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getChiaveMagFisico() != null &&
          !rm.getChiaveMagFisico().equals(""))
        where += " AND " + SaldoTM.COD_MAG_FISICO + " = '" + rm.getChiaveMagFisico() + "'";
      else{
        //ErrorMessage err=new ErrorMessage("LOGIS01003","Centro D.");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0455");//Fix 15941

        errori.addElement(err);
      }
      if (rm.getCodiceUbicazione() != null &&
          !rm.getCodiceUbicazione().equals(""))
        where += " AND " + SaldoTM.COD_UBICAZIONE + " = '" + rm.getCodiceUbicazione() + "'";
      else{
        //ErrorMessage err=new ErrorMessage("LOGIS01003","Ubicazione");//Fix 15941
        ErrorMessage err = new ErrorMessage("LOGIST0456");//Fix 15941
        errori.addElement(err);
      }
      if (rm.getUbicazione() != null) {
        if (rm.getUbicazione().getFlagGestSottoUbicaz()) {
          if (rm.getSottoUbicazione()!=null &&
              !rm.getSottoUbicazione().equals(""))
            where += " AND " + SaldoTM.SOTTO_UBICAZIONE + " = '" + rm.getSottoUbicazione() + "'";
          else{
            //ErrorMessage err=new ErrorMessage("LOGIS01003","Sottoubicazione");//Fix 15941
            ErrorMessage err = new ErrorMessage("LOGIST0457");//Fix 15941
            errori.addElement(err);
          }
        }
      }
      if (rm.getChiaveMappaUdc() != null &&
          !rm.getChiaveMappaUdc().equals(""))
        where += " AND " + SaldoTM.COD_MAPPA_UDC + " = '" + rm.getChiaveMappaUdc() + "'";
      else {
        if (rm.getAnagraficaUdc() != null)
          where += " AND " + SaldoTM.COD_MAPPA_UDC + " = '" + rm.getChiaveAnagraficaUdc() + "'";
        else //05.05.09
          where += " AND " + SaldoTM.COD_MAPPA_UDC + " IS NULL ";//05.05.09
      }
      if (rm.getAnagraficaUdc() != null) {
        if (rm.getAnagraficaUdc().getFlagGestioneScomparto()) {
          if (rm.getScomparto() != null &&
              !rm.getScomparto().equals(""))
            where += " AND " + SaldoTM.SCOMPARTO + " = '" + rm.getScomparto() + "'";
          else{
            //ErrorMessage err=new ErrorMessage("LOGIS01003","Scomparto");//Fix 15941
            ErrorMessage err=new ErrorMessage("LOGIST0457");//Fix 15941
            errori.addElement(err);
          }
        }
      }
      if (rm.getCausale().getTipoSaldo().getFlagPartita()) {
        if (rm.getPartita() != null &&
            !rm.getPartita().equals(""))
          where += " AND " + SaldoTM.PARTITA + " = '" + rm.getPartita() + "'";
        if (rm.getDataPartita() != null) {
          String dpTmp = ConnectionManager.getCurrentDatabase().getLiteral(rm.getDataPartita());
          where += " AND " + SaldoTM.DATA_PARTITA + " = " + dpTmp;
        }
      }
      if (rm.getCausale().getTipoSaldo().getFlagPartnerPartita()) {
        if (rm.getTestataMovimento().getCodiceDestinatario() != null &&
            !rm.getTestataMovimento().getCodiceDestinatario().equals(""))
          where += " AND " + SaldoTM.COD_PARTNER + " = '" + rm.getTestataMovimento().getCodiceDestinatario() + "'";
      }
      if (rm.getDataScadenza() != null) {
        String dpTmp = ConnectionManager.getCurrentDatabase().getLiteral(rm.getDataScadenza());
        where += " AND " + SaldoTM.DATA_SCADENZA + " = " + dpTmp;
      } else
        where += " AND " + SaldoTM.DATA_SCADENZA + " IS NULL";
      String[] keys = {rm.getCodiceGruppo(),
                       rm.getArticolo().getCodClassificGestMag(),
                       rm.getCodiceSocieta(),
                       rm.getCodiceMagLogico(),
                       rm.getChiaveMagFisico()};
      GestioneMagazzino g = null;
      try {
//        g = GestioneMagazzino.elementWithKey(KeyHelper.buildObjectKey(keys), PersistentObject.NO_LOCK);  Fix 15947
        g = (GestioneMagazzino)GestioneMagazzino.readOnlyElementWithKey(GestioneMagazzino.class,KeyHelper.buildObjectKey(keys)); // Fix 15947
      } catch(Exception e){
        e.printStackTrace(Trace.excStream);
        ErrorMessage err=new ErrorMessage("LOGIS01054",e.getMessage());
        errori.addElement(err);
      }
      if (g != null) {
        if (g.getFlagGestioneLotto01())
          if(rm.getLotto(0)!=null && !rm.getLotto(0).equals(""))
            where += " AND " + SaldoTM.LOTTO01 + " = '" + rm.getLotto(0) + "'";
          else{
            ErrorMessage err=new ErrorMessage("LOGIS01003", ((CampoTrascodificaGruppo) rm.getArticolo().getGruppo().getLotti().get(0)).getDescrizione());
            errori.addElement(err);
          }
        if (g.getFlagGestioneLotto02())
          if (rm.getLotto(1)!=null && !rm.getLotto(1).equals(""))
            where += " AND " + SaldoTM.LOTTO02 + " = '" + rm.getLotto(1) + "'";
          else{
            ErrorMessage err=new ErrorMessage("LOGIS01003", ((CampoTrascodificaGruppo) rm.getArticolo().getGruppo().getLotti().get(1)).getDescrizione());
            errori.addElement(err);
          }
        if (g.getFlagGestioneLotto03())
          if(rm.getLotto(2)!=null && !rm.getLotto(2).equals(""))
            where += " AND " + SaldoTM.LOTTO03 + " = '" + rm.getLotto(2)+"'";
          else{
            ErrorMessage err=new ErrorMessage("LOGIS01003", ((CampoTrascodificaGruppo) rm.getArticolo().getGruppo().getLotti().get(2)).getDescrizione());
            errori.addElement(err);
          }
        if(g.getFlagGestioneLotto04())
          if(rm.getLotto(3)!=null && !rm.getLotto(3).equals(""))
            where += " AND " + SaldoTM.LOTTO04 + " = '" + rm.getLotto(3)+"'";
          else{
            ErrorMessage err=new ErrorMessage("LOGIS01003", ((CampoTrascodificaGruppo) rm.getArticolo().getGruppo().getLotti().get(3)).getDescrizione());
            errori.addElement(err);
          }
        if(g.getFlagGestioneLotto05())
          if(rm.getLotto(4)!=null && !rm.getLotto(4).equals(""))
            where += " AND " + SaldoTM.LOTTO05 + " = '" + rm.getLotto(4)+"'";
          else{
            ErrorMessage err=new ErrorMessage("LOGIS01003", ((CampoTrascodificaGruppo) rm.getArticolo().getGruppo().getLotti().get(4)).getDescrizione());
            errori.addElement(err);
          }
      }
    }
    if (errori.size() > 0)
      return errori;
    try {
      logSaldo("RICERCA-WHERE", "where=" + where);
      saldiOmologhi = Saldo.retrieveList(where, "", true);
      logSaldo("RICERCA-DB", "trovati=" + saldiOmologhi.size());
      for (int ix = 0; ix < saldiOmologhi.size(); ix++)
        logSaldo("RICERCA-CANDIDATO", ((Saldo) saldiOmologhi.elementAt(ix)).debugSaldo());
    } catch(Exception e) {
      e.printStackTrace(Trace.excStream);
      ErrorMessage err = new ErrorMessage("LOGIS01054",e.getMessage());
      errori.addElement(err);
      return errori;
    }
    for (int i = 0; i < saldiOmologhi.size(); i++) {    // 10360 AR
      Saldo so = (Saldo) saldiOmologhi.elementAt(i);
      if (so.getChiaveTipoSaldo().equals(rm.getCausale().getChiaveTipoSaldo()))
        continue;
      if (so.getTipoSaldo().getFlagPartita() &&
          !Utils.areEqual(rm.getPartita(), so.getPartita()) &&
          !Utils.areEqual(rm.getDataPartita(), so.getDataPartita())) {
        saldiOmologhi.removeElementAt(i--);
        continue;
      }
      if (so.getTipoSaldo().getFlagPartnerPartita() &&
          !Utils.areEqual(rm.getTestataMovimento().getCodiceDestinatario(), so.getCodicePartner()))
        saldiOmologhi.removeElementAt(i--);
    }
    logSaldo("RICERCA-FINALE", "omologhi=" + saldiOmologhi.size());
    return errori;
  }

  //25.10.07 - inizio
  //protected boolean isCancellabile() {
  protected Vector isCancellabile() {
    Vector ret = new Vector();
    ret.add(new Boolean(false));
    //25.10.07 - fine
    if (!getTipoSaldo().getFlagDinamico())
      return ret;
    if (qta1.compareTo(new BigDecimal(0)) != 0)
      return ret;
    if (getConfezione() != null &&
        !getConfezione().getFlagDivisibile() &&
        qtaConfezione.compareTo(new BigDecimal(0)) != 0)
      return ret;
    //25.10.07 - inizio
    if (getArticolo().isUmPrmRapVariabile() &&
        qta2.compareTo(new BigDecimal(0)) != 0){
        ret = creaMovimentoDiSvuotamento(2);
        if (!ret.isEmpty())
          return ret;
    }
    if (getArticolo().isUmSecRapVariabile() &&
        qta3.compareTo(new BigDecimal(0)) != 0){
      ret = creaMovimentoDiSvuotamento(3);
      if (!ret.isEmpty())
        return ret;
    }
    ret.removeAllElements();
    ret.add(new Boolean(true));
    return ret;
    //25.10.07 - fine
  }

  protected Vector settaAltriDati(RigaMovimento rm) {
    return new Vector();
  }

  //07.09.17 - inizio
  protected Vector controlliPersonalizzati(RigaMovimento rm) {
    return new Vector();
  }
  //07.09.17 - fine
  
  protected Vector settaAttributiDaRigaMovimento(RigaMovimento rm, boolean storno) {
    Vector errori= new Vector();
    setSocieta(rm.getTestataMovimento().getSocieta());
    setTipoSaldo(rm.getCausale().getTipoSaldo());
    if (getTipoSaldo().getTipoCodice().getTipoNumeratore() == null) {
      ErrorMessage err = new ErrorMessage("LOGIS01066",getTipoSaldo().getCodiceSocieta()+"/"+ getTipoSaldo().getCodice());
      errori.addElement(err);
    }
    try {
      String prossimoCodice = getTipoSaldo().getTipoCodice().dammiProssimoCodice();
      logSaldo("NUMERATORE", "tipoSaldo=" + getChiaveTipoSaldo() + " prossimoCodice=" + prossimoCodice);
      setCodice(prossimoCodice);
    } catch(Exception e) {
      e.printStackTrace(Trace.excStream);
      //ErrorMessage err=new ErrorMessage("LOGIS01065","Saldo");//Fix 15941
      ErrorMessage err=new ErrorMessage("LOGIST0459");//Fix 15941
      errori.addElement(err);
    }
    setMagLogico(rm.getMagLogico());
    if (getTipoSaldo() != null &&
        getTipoSaldo().getFlagPartnerPartita())
      setPartner(rm.getTestataMovimento().getDestinatario());
    setCodiceGruppo(rm.getCodiceGruppo());
    setArticolo(rm.getArticolo());
    if (rm.getConfezione() != null &&
        !rm.getConfezione().getFlagDivisibile())
      setConfezione(rm.getConfezione());
    setLotto(rm.getLotto());
    setCodiceUdm(rm.getCodiceUdm());
    setMagFisico(rm.getMagFisico());
    setCodiceMagFisico(rm.getChiaveMagFisico());
    setCodiceUbicazione(rm.getCodiceUbicazione());
    setSottoUbicazione(rm.getSottoUbicazione());
    setScomparto(rm.getScomparto());
    setMappaUdc(rm.getMappaUdc());
    setAnagUdc(rm.getAnagraficaUdc());
    setUmBase(rm.getUmBase());
    setUm1(rm.getUm1());//19.09.07
    setUm2(rm.getUm2());
    setMatricola(rm.getMatricola());
    if (getTipoSaldo() != null &&
        getTipoSaldo().getFlagPartita()) {
      partita = rm.getPartita();
      dataPartita = rm.getDataPartita();
    }
    dataScadenza = rm.getDataScadenza();
    if (rm.getCodiceFifo() != null && !rm.getCodiceFifo().equals(""))
      setCodiceFifo(rm.getCodiceFifo());
    else {
      Timestamp ts=new Timestamp(new java.util.Date().getTime());
      setCodiceFifo(ts.toString().substring(0,19));
    }
    errori.addAll(settaAltriDati(rm));
    return errori;
  }

  protected Vector settaAttributiMappaUdc(MappaUdc m){
    m.setAnagraficaUdc(getAnagUdc());
    m.setCodiceMagFisico(getCodiceMagFisico());
    m.setUbicazione(getUbicazione());
    m.setSottoUbicazione(getSottoUbicazione());
    m.setNumUdm(getNumUdm());
    m.setCheckFormatoEsterno(getAnagUdc().getCheckFormatoEsterno());
    m.setFormaEsterna(getAnagUdc().getFormaEsterna());
    m.setCheckFormatoInterno(getAnagUdc().getCheckFormatoInterno());
    m.setFormaInterna(getAnagUdc().getFormaInterna());
    m.setFlagFormato(getAnagUdc().getFlagFormato());
    if (getAnagUdc().getFlagFormato() == AnagraficaUdc.STATICO)
      m.setFormato(getAnagUdc().getFormato());
    if (m.getUbicazione() != null &&
        m.getUbicazione().getFlagPrenotabile())
      m.setFlagUbicProvvisoria(true);
    return m.aggiornamento(getRigaMovimento());
  }

/**
 * Cambia le quantita' sommando o sottraendo le quentita' di riga movimento
 * a seconda del valore del flag add (secondo  parametro)
 */
  protected void settaQuantita(RigaMovimento rm, boolean add) {
    if (add) {
      setQta1(getQta1().add(rm.getQta1()));
      setQta2(getQta2().add(rm.getQta2()));
      setQta3(getQta3().add(rm.getQta3()));
      setQta4(getQta4().add(rm.getQta4()));
      setQta5(getQta5().add(rm.getQta5()));
      setQtaConfezione(getQtaConfezione().add(rm.getQtaConfezione()));
      setNumUdm(new Integer(getNumUdm().intValue() + rm.getNumUdm().intValue()));
    } else {
      setQta1(getQta1().subtract(rm.getQta1()));
      setQta2(getQta2().subtract(rm.getQta2()));
      setQta3(getQta3().subtract(rm.getQta3()));
      setQta4(getQta4().subtract(rm.getQta4()));
      setQta5(getQta5().subtract(rm.getQta5()));
      setQtaConfezione(getQtaConfezione().subtract(rm.getQtaConfezione()));
      setNumUdm(new Integer(getNumUdm().intValue() - rm.getNumUdm().intValue()));
    }
    setRigaMovimento(rm);
    setCausale(rm.getCausale());
    dataMovimento = rm.getTestataMovimento().getDataMovimento();
    setForma(rm.getForma());
    if (rm.getMappaUdc() != null ||
        rm.getAnagraficaUdc() != null)
      qtaImpiego = new BigDecimal(1);
    else {
      if (rm.getArticolo().getFlagGestioneQtaUdm() == Articolo.VARIABILE)
        qtaImpiego = new BigDecimal(1);
      else {
        if (rm.getArticolo().getQtaUdm() == null ||
            rm.getArticolo().getQtaUdm().compareTo(new BigDecimal(0)) == 0)
          qtaImpiego = new BigDecimal(0);
        else {
          if (add)
            //qtaImpiego = rm.getQta1().divide(rm.getArticolo().getQtaUdm(), 6, BigDecimal.ROUND_HALF_DOWN);//02.04.20
          	qtaImpiego = Q6Calc.get().divide(rm.getQta1(),rm.getArticolo().getQtaUdm(), 6, BigDecimal.ROUND_HALF_DOWN);//02.04.20
          else
            //qtaImpiego = rm.getQta1().divide(rm.getArticolo().getQtaUdm(), 6, BigDecimal.ROUND_HALF_UP);//02.04.20
          	qtaImpiego = Q6Calc.get().divide(rm.getQta1(),rm.getArticolo().getQtaUdm(), 6, BigDecimal.ROUND_HALF_UP);//02.04.20
        }
      }
    }
  }

  public TableManager getTableManager() throws java.sql.SQLException {
    return SaldoTM.getInstance();
  }

  public static Saldo elementWithKey(String key, int lockType) throws SQLException{
    Saldo newObj= (Saldo) Factory.createObject(Saldo.class);
    newObj.setKey(key);
    if (!newObj.retrieve(lockType)) newObj=null;
    return newObj;
  }

  //private static final Saldo instance=(Saldo) Factory.createObject(Saldo.class);
  private static Saldo instance;//Fix 43875

	public static java.util.Vector retrieveList(String where, String orderBy, boolean ott)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		// Fix 43875 inizio
		if (instance == null) {
			instance = (Saldo) Factory.createObject(Saldo.class);
		}
		// Fix 43875 fine
		return instance.getTableManager().retrieveList(where, orderBy, ott);
	}

  public BigDecimal getQtaDisponibile() {
    return qtaDisponibile;
  }

  public BigDecimal getQtaConfDisponibile() {
    return qtaConfDisponibile;
  }

/**
 * Calcola la quantità disponibile per articolo e confezione.
 */
  public BigDecimal calcolaDisponibile() {
    if (getConfezione() == null ||
        getConfezione().getFlagDivisibile()) {
      qtaDisponibile = qta1.subtract(calcolaImpegnata());
      qta2Disponibile = qta2.subtract(qta2Missionata);//24.10.07
      qta3Disponibile = qta3.subtract(qta3Missionata);//24.10.07
      qtaConfDisponibile = new BigDecimal(0);
    } else {
      qtaConfDisponibile = qtaConfezione.subtract(calcolaConfImpegnata());
      qtaDisponibile = new BigDecimal(0);
      qta2Disponibile = new BigDecimal(0);//24.10.07
      qta3Disponibile = new BigDecimal(0);//24.10.07
    }
    return qtaDisponibile;
  }

/**
 * Calcola la quantità impegnata in missioni non terminate.
 */
  protected BigDecimal calcolaImpegnata() {
    BigDecimal qtaMissionata = new BigDecimal(0);
    String val = null;
    //24.10.07 - inizio
    qta2Missionata = new BigDecimal(0);
    qta3Missionata = new BigDecimal(0);
    String val2 = null;
    String val3 = null;
    //24.10.07 - fine
    boolean eccezione = false;
// Fix 16470 inizio
    ResultSet rs = null;
    synchronized(CS_STMT_QTA_IMPEGNATA){
    try {
//      CachedStatement stmt = new CachedStatement(); // Creo lo statement della query.
//      stmt.setStmtString(STMT_QTA_IMPEGNATA);       // Associo il contenuto.
      //Mod.7386
      //stmt.getStatement().setString(1, getCodiceSocieta());
      //stmt.getStatement().setString(2, codice);
      Database db = ConnectionManager.getCurrentDatabase();
//      db.setString(stmt.getStatement(), 1, getCodiceSocieta());
//      db.setString(stmt.getStatement(), 2, codice);
      //Mod.7386 - fine
//      ResultSet rs = stmt.executeQuery();           // Eseguo la query.
      PreparedStatement ps = CS_STMT_QTA_IMPEGNATA.getStatement();
      db.setString(ps, 1, getCodiceSocieta());
      db.setString(ps, 2, codice);
      rs = ps.executeQuery();
// Fix 16470 fine
      while (rs.next()){                             // Analizzo i risultati ...
        val = rs.getString(1);
        val2 = rs.getString(2);//24.10.07
        val3 = rs.getString(3);//24.10.07
      }
//      rs.close(); Fix 16470
    } catch (SQLException ex) {
      ex.printStackTrace(Trace.excStream);//Fix 15941
      eccezione = true;
    }
// Fix 16470 inizio
    finally{
      if (rs != null) try {rs.close();}catch (SQLException sqle) {sqle.printStackTrace(Trace.excStream);}
    }
    }
// Fix 16470 fine
    if (eccezione){            // Permette di gestire le eccezioni da lock del record sull'SQL.
      qtaMissionata = qta1;   //  Evita le missioni multiple sullo stesso saldo.
      qta2Missionata = qta2;//24.10.07
      qta3Missionata = qta3;//24.10.07
    }
    if (val != null)
      qtaMissionata = new BigDecimal(val.replace(',', '.'));
    //24.10.07 - inizio
    if (val2 != null)
      qta2Missionata = new BigDecimal(val2.replace(',', '.'));
    if (val3 != null)
      qta3Missionata = new BigDecimal(val3.replace(',', '.'));
    //24.10.07 - fine
    return qtaMissionata;
  }

/**
 * Calcola la quantità impegnata in missioni non terminate.
 */
  protected BigDecimal calcolaConfImpegnata() {
    BigDecimal qtaMissionata = new BigDecimal(0);
    String val = null;
    boolean eccezione = false;
// Fix 16470 inizio
    ResultSet rs = null;
    synchronized (CS_QTA_CONF_IMPEGNATA){
    try {
//      CachedStatement stmt = new CachedStatement(); // Creo lo statement della query.
//      stmt.setStmtString(STMT_QTA_CONF_IMPEGNATA);       // Associo il contenuto.
      //Mod.7386
      //stmt.getStatement().setString(1, getCodiceSocieta());
      //stmt.getStatement().setString(2, codice);
      Database db = ConnectionManager.getCurrentDatabase();
//      db.setString(stmt.getStatement(), 1, getCodiceSocieta());
//      db.setString(stmt.getStatement(), 2, codice);
      //Mod.7386 - fine
//      ResultSet rs = stmt.executeQuery();           // Eseguo la query.
      PreparedStatement ps = CS_QTA_CONF_IMPEGNATA.getStatement();
      db.setString(ps, 1, getCodiceSocieta());
      db.setString(ps, 2, codice);
      rs = ps.executeQuery();
// Fix 16470 fine
      while (rs.next())                             // Analizzo i risultati ...
        val = rs.getString(1);
//      rs.close(); Fix 16470
    } catch (SQLException ex) {
      ex.printStackTrace(Trace.excStream);//Fix 15941
      eccezione = true;
    }
// Fix 16470 inizio
    finally{
      if (rs != null) try {rs.close();}catch (SQLException sqle) {sqle.printStackTrace(Trace.excStream);}
    }
    }
// Fix 16470 fine
    if (eccezione)            // Permette di gestire le eccezioni da lock del record sull'SQL.
      qtaMissionata = qta1;   //  Evita le missioni multiple sullo stesso saldo.
    if (val != null)
      qtaMissionata = new BigDecimal(val.replace(',', '.'));
    return qtaMissionata;
  }

/**
 * Permette la cancellazione forzata del saldo.
 */
  public Vector forzaCancellazione() {
    if (qta1.compareTo(new BigDecimal(0)) != 0) {
      Vector errori = new Vector();
      //ErrorMessage err = new ErrorMessage("LOGIS01054", "Saldo di quantita' non nulla.");//Fix 15941
      ErrorMessage err = new ErrorMessage("LOGIST0460");//Fix 15941
      errori.addElement(err);
      return errori;
    }
    if (getMappaUdc() != null)
      qtaImpiego = new BigDecimal(1);
    return svuotamento(true);
  }

  protected Vector svuotamento() {
    return svuotamento(false);
  }

  protected Vector svuotamento(boolean forzaCancella) {
    MappaUdc udc = getMappaUdc();
    Ubicazione ubic = getUbicazione();
    Vector errori = new Vector();
    if (onDB) {
      int err1 = ErrorCodes.NO_ROWS_UPDATED;
      try {
        err1 = delete();
      } catch(Exception e) {
        e.printStackTrace(Trace.excStream);
        //ErrorMessage err=new ErrorMessage("LOGIS01054","Cancellazione saldo "+e.getMessage());//Fix 15941
        ErrorMessage err=new ErrorMessage("LOGIST0462", e.getMessage());//Fix 15941
        errori.addElement(err);
        return errori;
      }
      if (err1 < ErrorCodes.OK){
        //ErrorMessage err=new ErrorMessage("LOGIS01068","(" + err1 + ") saldo: "+getKey());//Fix 15941
        ErrorMessage err=new ErrorMessage("LOGIST0464", new String[]{String.valueOf(err1),getKey()});//Fix 15941
        errori.addElement(err);
        return errori;
      }
    }
    if (udc != null) {
      if (udc.isCancellabile(getRigaMovimento(), forzaCancella)) {
        errori = udc.svuotamento(getRigaMovimento());
        if (errori.size() > 0)
          return errori;
      } else
        return errori;
    }
    // fix 23362
    boolean updateUbic = aggiornaUbicazione(ubic);//23.04.12
    if (updateUbic){
    // fine fix 23362  
      errori = ubic.svuotamento(getForma(), qtaImpiego, ubic.getPesoOccupato());//27.05.14
    }  
    return errori;
  }

  public Vector aggiornamento(boolean add) {
    Vector errori = new Vector();
    nuovaUdc = false;
    boolean logga = false;
    if (Utente.getCurrentUtente() == null) {
        if("EASY_CHEK_WS_001".equals(Security.getCurrentUser().getId()))
        	logga = true;
    } else {
    	  if("EASY_CHEK_WS_001".equals(Utente.getCurrentUtente().getId()))
          	logga = true;
     }
    if(logga) {
    	Trace.excStream.println("-> Aggiornamento saldo, onDB = "+onDB);
    }
    logSaldo("AGGIORNAMENTO-START", "add=" + add + " " + debugSaldo());
    if (!onDB) {
      MappaUdc udc = null;
      if (getCodiceMappaUdc() != null &&
          !getCodiceMappaUdc().equals("")) {
        try {
          udc = MappaUdc.elementWithKey(getCodiceMappaUdc(), PersistentObject.NO_LOCK);
        } catch(Exception e) {
          e.printStackTrace(Trace.excStream);
          //ErrorMessage err=new ErrorMessage("LOGIS01054","Ricerca mappa UdC: "+ e.getMessage());//Fix 15941
          ErrorMessage err=new ErrorMessage("LOGIST0465", e.getMessage());//Fix 15941
          errori.addElement(err);
          return errori;
        }
/*        if (udc != null) {
          setCodiceMagFisico(udc.getCodiceMagFisico());
          setCodiceUbicazione(udc.getCodiceUbicazione());
          setSottoUbicazione(udc.getSottoUbicazione());
        } else {*/
        if (udc == null) {
          if (anagUdc != null) {
            if (!anagUdc.getTipoUdc().getFlagDinamica()) {
              if (!anagUdc.getCodice().equals(getCodiceMappaUdc())) {
                ErrorMessage err=new ErrorMessage("LOGIS01064", getCodiceMappaUdc());
                errori.addElement(err);
                return errori;
              }
            } else {
              try {
                if (!anagUdc.getTipoCodiceGeneratore().validaSintassi(getCodiceMappaUdc(), new java.util.Date()).bRisultato){
                  ErrorMessage err=new ErrorMessage("LOGIS01064",getCodiceMappaUdc());
                  errori.addElement(err);
                  return errori;
                }
              } catch(Exception e) {
                //ErrorMessage err=new ErrorMessage("LOGIS01054","Validazione codice UdC :"+e.getMessage());//Fix 15941
                ErrorMessage err=new ErrorMessage("LOGIST0466",e.getMessage());//Fix 15941
                errori.addElement(err);
                return errori;
              }
            }
            udc = (MappaUdc) Factory.createObject(MappaUdc.class);
            udc.setCodice(getCodiceMappaUdc());
            nuovaUdc = true;
            errori = settaAttributiMappaUdc(udc);   // Salva UdC e movimentoUdC
            if (errori.size()>0)
              return errori;
          } else {
            //ErrorMessage err=new ErrorMessage("LOGIS01003","Anagrafica UdC");//Fix 15941
            ErrorMessage err=new ErrorMessage("LOGIST0468");//Fix 15941
            errori.addElement(err);
            return errori;
          }
        }
        setMappaUdc(udc);
      } else
        if (anagUdc != null) {
          try {
            udc = MappaUdc.elementWithKey(anagUdc.getCodice(), PersistentObject.NO_LOCK);
          } catch(Exception e) {
            e.printStackTrace(Trace.excStream);
            //ErrorMessage err=new ErrorMessage("LOGIS01054","Ricerca mappa UdC: "+ e.getMessage());//Fix 15941
            ErrorMessage err=new ErrorMessage("LOGIST0469", e.getMessage());//Fix 15941
            errori.addElement(err);
            return errori;
          }
/*          if (udc != null) {
            setCodiceMagFisico(udc.getCodiceMagFisico());
            setCodiceUbicazione(udc.getCodiceUbicazione());
            setSottoUbicazione(udc.getSottoUbicazione());
          } else {*/
          if (udc == null) {
            udc = (MappaUdc) Factory.createObject(MappaUdc.class);
            nuovaUdc = true;
            udc.setCodice(anagUdc.creaMappaUdc());
            errori = settaAttributiMappaUdc(udc);   // Salva UdC e movimentoUdC
            if (errori.size() > 0)
              return errori;
          }
          setMappaUdc(udc);
        }
    }
    if (add) {
      if (getUbicazione().getFlagEsclusioneDeposito()) {
        ErrorMessage err=new ErrorMessage("LOGIS01076",getUbicazione().getKey());
        errori.addElement(err);
        return errori;
      }
    } else {
      if (getUbicazione().getFlagEsclusionePrelievo()) {
        ErrorMessage err=new ErrorMessage("LOGIS01077",getUbicazione().getKey());
        errori.addElement(err);
        return errori;
      }
    }
    if (errori.size() > 0)
      return errori;
    else {
      int err1 = ErrorCodes.NO_ROWS_UPDATED;
      try {
        logSaldo("SAVE-PRIMA", "operazioneAttesa=" + (onDB ? "UPDATE" : "INSERT") + " " + debugSaldo());
        err1 = save();
        logSaldo("SAVE-DOPO", "codiceEsito=" + err1 + " " + debugSaldo());
      } catch(SQLException e){
        logSaldo("SAVE-SQL-ERROR", "sqlState=" + e.getSQLState() + " errorCode=" + e.getErrorCode()
            + " message=" + e.getMessage() + " " + debugSaldo());
        e.printStackTrace(Trace.excStream);
        //ErrorMessage err=new ErrorMessage("LOGIS01054","Salvataggio saldo: "+e.getMessage());//Fix 15941
        ErrorMessage err=new ErrorMessage("LOGIST0470", e.getMessage());//Fix 15941
        errori.addElement(err);
        return errori;
      }
      if (err1<ErrorCodes.OK) {
        logSaldo("SAVE-ESITO-NEGATIVO", "codiceEsito=" + err1 + " " + debugSaldo());
        //ErrorMessage err=new ErrorMessage("LOGIS01069", "(" + err1 + ") Saldo "+getKey());//Fix 15941
        ErrorMessage err=new ErrorMessage("LOGIST0471", new String[]{String.valueOf(err1), getKey()});//Fix 15941
        errori.addElement(err);
        return errori;
      }
      return errori;
    }
  }

/**
 * Attributo di servizio, non persistente.
 */
  protected char segno = RigaMovimento.PIU;

  public char getSegno() {
    return segno;
  }

  public void setSegno(char s) {
    segno = s;
    dirty = true;
  }

/**
 * Attributo di servizio, non persistente.
 */
  protected BigDecimal qtaAzzerare = new BigDecimal(0);

  public BigDecimal getQtaAzzerare() {
    return qtaAzzerare;
  }

  public void setQtaAzzerare(BigDecimal qa) {
    qtaAzzerare = qa;
    dirty = true;
  }

  //Fix 07529 inizio
  public void setUm1(String u) {
    um1 = u;
    setDirty();
  }

  public String getUm1() {
    return um1;
  }
  //Fix 07529 fine

  //09.07.07 - inizio
  public void setQtaAll(BigDecimal qta1){
    Vector qta = getArticolo().calcolaQta2Qta3(qta1,getQta2(),getQta3());//28.08.07
    setQta1(qta1);
    setQta2((BigDecimal)qta.get(0));
    setQta3((BigDecimal)qta.get(1));
  }
  //09.07.07 - fine

  //25.10.07 - inizio
  // creazione mov. che azzera il residuo qta2/qta3 quando qta1=0, così da poter cancellare il saldo
  // j=2 azzerare qta2, j=3 azzerare qta2
  protected Vector creaMovimentoDiSvuotamento(int j){
    Vector errori = new Vector();
    TestataMovimento tm = getRigaMovimento().getTestataMovimento();
    RigaMovimento rm = (RigaMovimento) Factory.createObject(RigaMovimento.class);
    rm.setTestataMovimento(tm);
    RigaMovimento rm1 = (RigaMovimento)rigaMovimento.getObject();
    rm.setCodice(rm1.getCodice());
    rm.setCodiceDettaglio(new Integer(rm1.getCodiceDettaglio().intValue() +10));//16.11.07
    String causalePos = ParametriLogis.valoreDiSezioneChiave(ParametriLogis.CAUSALI_AZZERA_SALDI, ParametriLogis.CAUSALE_PIU);
    String causaleNeg = ParametriLogis.valoreDiSezioneChiave(ParametriLogis.CAUSALI_AZZERA_SALDI, ParametriLogis.CAUSALE_MENO);
    if (causalePos.equals(ParametriLogis.NULL) ||
        causaleNeg.equals(ParametriLogis.NULL)) {
      rm.setCausale(rm1.getCausale());
      rm.setSegno(rm1.getCausale().getSegno());
      if (j == 2)
        rm.setQta2(getQta2());
      if (j == 3)
        rm.setQta3(getQta3());
      if (rm.getSegno() == Causale.PIU) {
        rm.setQta2(getQta2().negate());
        rm.setQta3(getQta3().negate());
      }
    }
    else {
      // mov. di rettifica negativo
      if (j==2 && getQta2().compareTo(new BigDecimal(0)) < 0 ||
          j==3 && getQta3().compareTo(new BigDecimal(0)) < 0){
        rm.setCodiceCausale(causalePos);
        rm.setSegno(Causale.PIU);
      }
      else {
        // mov. di rettifica positivo
        rm.setCodiceCausale(causaleNeg);
        rm.setSegno(Causale.MENO);
      }
      if (j == 2)
        rm.setQta2(getQta2().abs());
      if (j == 3)
        rm.setQta3(getQta3().abs());
    }
    rm.setPartita(partita);
    rm.setDataPartita(dataPartita);
    rm.setLotto(getLotto(0),0);
    rm.setLotto(getLotto(1),1);
    rm.setLotto(getLotto(2),2);
    rm.setLotto(getLotto(3),3);
    rm.setLotto(getLotto(4),4);
    rm.setDataScadenza(dataScadenza);
    rm.setUmBase(getUmBase());
    rm.setUm1(getUm1());
    rm.setUm2(getUm2());
    /*rm.setVolumeIngombro(getVolumeIngombro());
    rm.setLunghezza(getLunghezza());
    rm.setLarghezza(getLarghezza());
    rm.setProfondita(getProfondita());*/
    rm.setTipoUdm(getArticolo().getTipoUdm());
    if (getArticolo().getFlagGestioneQtaUdm() == Articolo.VARIABILE)
      rm.setForma(getArticolo().dammiForma());
    else
      rm.setForma(getForma());
    rm.setArticolo(getArticolo());
    rm.setMagLogico(getMagLogico());
    rm.setChiaveMagFisico(tm.getChiaveMagFisico());
    rm.setUbicazione(getUbicazione());
    rm.setCodiceFifo(getCodiceFifo());
    rm.setCodiceSaldo(getCodice());
    rm.setNatura(RigaMovimento.MOVIM_ART);
    String testoNote = ResourceLoader.getString(FIS_RES, "movimentoAzzeraSaldo");
    rm.setNote(testoNote);
    int err = ErrorCodes.NO_ROWS_UPDATED;
    try {
      err = rm.save();
    }
    catch (Exception e) {
      e.printStackTrace(Trace.excStream);
      //errori.addElement(new ErrorMessage("LOGIS01054", "Salvataggio riga movimento " + e.getMessage()));//Fix 15941
      errori.addElement(new ErrorMessage("LOGIST0393", e.getMessage()));//Fix 15941
      return errori;
    }
    if (err < ErrorCodes.OK) {
      //errori.addElement(new ErrorMessage("LOGIS01069", "(2) Errore: " + err + " - Riga movimento: " + getKey().replace(KeyHelper.KEY_SEPARATOR.charAt(0), '/')));//Fix 15941
      errori.addElement(new ErrorMessage("LOGIST0397", new String[]{String.valueOf(err), getKey().replace(KeyHelper.KEY_SEPARATOR.charAt(0), '/')}));//Fix 15941
      return errori;
    }
    //19.11.07 - inizio
    else {
//09094 MM inizio
//      if (ParametriLogis.INSTALL_PANTHERA &&
//          !EventoApplicativo.creaEvento(rm, RigaMovimento.MOVIMENTO_A_THIP))
//        errori.addElement(new ErrorMessage("LOGIS01054", "Errore nella trasmissione dati a ERP"));
      if (ParametriLogis.INSTALL_PANTHERA) {
        Collection e = EventoApplicativo.creaEventoNotificaErrori(rm, RigaMovimento.MOVIMENTO_A_THIP);
        if (e != null)
          errori.addAll(e);
//09094 MM fine
      }
    }
    //19.11.07 - fine
    return errori;
  }
  //25.10.07 - fine
  //Fix 43891 ini
  public Vector checkAll(BaseComponentsCollection components) {
	  Vector errors = new Vector();
	  if(isUsaInWeb())
		  checkLootiField(components);
	  errors = super.checkAll(components);
	  return errors;
  }
  
	public void checkLootiField(BaseComponentsCollection components) {
		if (getGruppo() != null) {
			List pc = getGruppo().getLotti();
			for (int i = 0; i < pc.size(); i++) {
				CampoVariabile cv = (CampoVariabile) pc.get(i);
				String codiceCampo = cv.getCodiceCampo();
				if (codiceCampo.equals("LOTTO01")) {
					BaseBOComponent lotto1 = ((BaseBOComponent) components.getComponent("Lotto1"));
					lotto1.getComponentManager().setMandatory(cv.getAbilitazione() && cv.getObbligatorio());
				} else if (codiceCampo.equals("LOTTO02")) {
					BaseBOComponent lotto2 = ((BaseBOComponent) components.getComponent("Lotto2"));
					lotto2.getComponentManager().setMandatory(cv.getAbilitazione() && cv.getObbligatorio());
				} else if (codiceCampo.equals("LOTTO03")) {
					BaseBOComponent lotto3 = ((BaseBOComponent) components.getComponent("Lotto3"));
					lotto3.getComponentManager().setMandatory(cv.getAbilitazione() && cv.getObbligatorio());
				} else if (codiceCampo.equals("LOTTO04")) {
					BaseBOComponent lotto4 = ((BaseBOComponent) components.getComponent("Lotto4"));
					lotto4.getComponentManager().setMandatory(cv.getAbilitazione() && cv.getObbligatorio());
				} else if (codiceCampo.equals("LOTTO05")) {
					BaseBOComponent lotto5 = ((BaseBOComponent) components.getComponent("Lotto5"));
					lotto5.getComponentManager().setMandatory(cv.getAbilitazione() && cv.getObbligatorio());
				}
			}
		}

  }
  // Fix 43891 fine

}
