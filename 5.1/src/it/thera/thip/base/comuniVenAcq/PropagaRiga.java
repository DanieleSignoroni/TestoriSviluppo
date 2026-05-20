package it.thera.thip.base.comuniVenAcq;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.collector.CollectionDataCollector;
import com.thera.thermfw.common.BusinessObjectAdapter;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.gui.cnr.OpenType;
import com.thera.thermfw.persist.BaseObject;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.web.servlet.UpdateMultipleRows;

import it.thera.thip.acquisti.ordineAC.RigaAcquisto;
import it.thera.thip.acquisti.documentoAC.DocumentoAcquisto;
import it.thera.thip.acquisti.documentoAC.DocumentoAcquistoRiga;
import it.thera.thip.acquisti.documentoAC.web.DocumentoAcquistoRigaPrmDataCollector;
import it.thera.thip.acquisti.generaleAC.PersDatiAcq;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.documenti.DocumentoBaseRiga;
import it.thera.thip.base.documenti.StatoAttivita;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.base.documenti.web.DocumentoDataCollector;
import it.thera.thip.cs.DatiComuniEstesi;

/*
 * Revisions:
 * Date      		Fix    			Owner            	Description
 * 23/01/2025    					SZ    				Versione Iniziale
 */
public class PropagaRiga extends BusinessObjectAdapter implements BaseObject{
	 
    public static final int MENSILE = '1';
    public static final int BIMESTRALE = '2';
    public static final String DOC_ACQ_RIGA = "DocumentoAcquistoRigaPrm";
    public static final String ORD_ACQ_RIGA = "OrdineAcquistoRigaPrm";
	  public static final char PROVVISORIO = '1';
	  public static final char DEFINITIVO  = '2';	
	  protected Proxy iAzienda = new Proxy(Azienda.class);
	  protected int iNumRighe = 0;
	  protected char iCadenza ='1';
	  protected char iStatoAvanzamento = StatoAvanzamento.DEFINITIVO;
	  protected String iAnnoTestata = null;
	  protected String iNumeroTestata = null;
	  protected Integer iRiga = null;
	  protected Integer iIdDetRiga = null;
	  protected BODataCollector boDataCollector = null;
	  protected String savedKey = "";
	  protected String iTipoRiga = null;
	  protected Vector iErroriDataCollector ;
	  
	  public PropagaRiga() {
	    setIdAziendaInternal(Azienda.getAziendaCorrente());
	    iErroriDataCollector = new Vector();
	  }

	  protected void preparaDataCollector() {
		  if(boDataCollector == null) {
			  if(getIdNumeroTestata() != null) {
		        BODataCollector rigaBODC = null;
		        ClassADCollection classADCollRiga;
				try {
					classADCollRiga = ClassADCollectionManager.collectionWithName(getTipoRiga());
			        String collectorNameRiga = classADCollRiga.getBODataCollector();
			        if (collectorNameRiga != null)
			        {
			          rigaBODC = (BODataCollector)Factory.createObject(collectorNameRiga);
			          rigaBODC.initialize(classADCollRiga.getClassName(), true);
			        }
			        else
			          rigaBODC = new BODataCollector(classADCollRiga.getClassName(), true);
			        
					rigaBODC.setMode(OpenType.COPY);
					rigaBODC.retrieve(getKeyOrdRiga());		
					((DocumentoDataCollector) rigaBODC).impostaSecondoCausale();
					((DocumentoDataCollector) rigaBODC).setOnBO();
					modificaDataCollector(((DocumentoDataCollector) rigaBODC));
					
				  if(rigaBODC.getBo() != null)
					  boDataCollector = rigaBODC;
				  			
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		  }
	  }
	  public void modificaDataCollector(DocumentoDataCollector rigaBODC) {
	        if (rigaBODC instanceof DocumentoAcquistoRigaPrmDataCollector){
	        	DocumentoAcquistoRigaPrmDataCollector rigaBODCPrm = (DocumentoAcquistoRigaPrmDataCollector)rigaBODC;
				  QuantitaInUMRif qtaInUMRif = getRigaQuantitaInUMRif((DocumentoAcquistoRiga) rigaBODCPrm.getBo());
				  ((DocumentoAcquistoRiga) rigaBODCPrm.getBo()).setQtaInUMAcq(qtaInUMRif.getQuantitaInUMRif());
				  ((DocumentoAcquistoRiga) rigaBODCPrm.getBo()).setQtaInUMPrm(qtaInUMRif.getQuantitaInUMPrm());
				  ((DocumentoAcquistoRiga) rigaBODCPrm.getBo()).setQtaInUMSec(qtaInUMRif.getQuantitaInUMSec());
	        }
	  }
	 public QuantitaInUMRif getRigaQuantitaInUMRif(DocumentoAcquistoRiga rigaDocAcqOrig) {
			DocumentoAcquisto docAcqTestataOrig = (DocumentoAcquisto) rigaDocAcqOrig.getTestata();		

			if(rigaDocAcqOrig.getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO) {
				if(docAcqTestataOrig.getCollegatoAMagazzino() == StatoAttivita.ESEGUITO )
					return rigaDocAcqOrig.getQtaRicevuta();
				
				else 
					return rigaDocAcqOrig.getQtaAttesaEvasione();
				
			}
			return rigaDocAcqOrig.getQtaPropostaEvasione();
	  }
	 
	  public BODataCollector getBODataCollector() {
		  return this.boDataCollector;
	  }
	  public void setIdAnnoTestata(String idAnnoTestata){
	    this.iAnnoTestata = idAnnoTestata;
	  }
	  public String getIdAnnoTestata(){
	  	return this.iAnnoTestata;
	  }
	  public void setIdNumeroTestata(String idNumeroTestata){
	    this.iNumeroTestata = idNumeroTestata;
	  }
	  public String getIdNumeroTestata(){
	  	return this.iNumeroTestata;
	  }
	  public void setIdRiga(Integer idRiga){
	    this.iRiga = idRiga;
	  }
	  public Integer getIdRiga(){
	  	return this.iRiga ;
	  }
	  public void setIdDetRiga(Integer idDetRiga){
		  this.iIdDetRiga = idDetRiga;
	  }
	  public Integer getIdDetRiga(){
	  	return this.iIdDetRiga ;
	  }
	  public void setCadenza(char cadenza) {
		  this.iCadenza = cadenza;
	  }
	  public char getCadenza() {
		  return this.iCadenza;
	  }
	  
	  public void setStatoAvanzamento(char statoAvanzamento) {
		  this.iStatoAvanzamento = statoAvanzamento;
	  }
	  public char getStatoAvanzamento() {
		  return this.iStatoAvanzamento;
	  }
	  public void setNumRighe(int numRighe) {
		  this.iNumRighe = numRighe;
	  }
	  
	  public int getNumRighe() {
		  return this.iNumRighe;
	  }
	  
	  public String getTipoRiga() {
		  return this.iTipoRiga;
	  }
	  
	  public void setTipoRiga(String tipoRiga) {
		  this.iTipoRiga = tipoRiga;
	  }
	  public void setAzienda(Azienda azienda) {
	    setIdAziendaInternal(azienda.getKey());
	    iAzienda.setObject(azienda);
	  }
	  public Azienda getAzienda() {
	    return(Azienda)iAzienda.getObject();
	  }
	  public void setAziendaKey(String key) {
	    iAzienda.setKey(key);
	    setIdAziendaInternal(key);
	  }

	  public String getAziendaKey() {
	    return iAzienda.getKey();
	  }

	  public void setIdAzienda(String idAzienda) {
	    setIdAziendaInternal(idAzienda);
	    iAzienda.setKey(idAzienda);
	  }
	  public String getIdAzienda() {
	    String key = iAzienda.getKey();
	    return key;
	  }
	  protected void setIdAziendaInternal(String idAzienda) {
		    iAzienda.setKey(idAzienda);
	  }
	  
	  public String getSavedKey() {
		  return this.savedKey;
	  }

	  public Vector getErroriDataCollector() {
		  return this.iErroriDataCollector;
	  }
	  public int save(boolean force) throws SQLException{
		  preparaDataCollector();
		  int ret= 0;
		  BODataCollector ordRigaDC =  getBODataCollector();
		  
		  DocumentoOrdineRiga iDocOrig = (DocumentoOrdineRiga) ordRigaDC.getBo();
		  int max = getNumRighe();
		  Date dataRigaDestConsegnaConfermata = iDocOrig.getDataConsegnaConfermata();
		  Date dataRigaDestConsegnaRichiesta = iDocOrig.getDataConsegnaRichiesta();
		  //72XXX nullPointer check per ns pers che suvota qs data <
		  //boolean isDataConsegnaConfermataFineMesse = isDataFineMesse(dataRigaDestConsegnaConfermata);
		  boolean isDataConsegnaConfermataFineMesse = false;
		  if(dataRigaDestConsegnaConfermata != null)
			  isDataConsegnaConfermataFineMesse = isDataFineMesse(dataRigaDestConsegnaConfermata);
		  //72XXX >
		  boolean isDataDestConsegnaRichiestaFineMesse = isDataFineMesse(dataRigaDestConsegnaRichiesta); //72XXX cambio parametro in quello corretto
		  int passo = PersDatiAcq.getCurrentPersDatiAcq().getPassoNumerazioneRigaOrd();
		  if(passo == 0) passo = 1;
		  int seq = getNextSequenzaRiga(this);
		  for(int i =1 ; i <= max ; i++ ) {
			  //72XXX nullPointer check per ns pers che suvota qs data <
			  //Date newdateConsegnaConfermata = getDataSuccesive(dataRigaDestConsegnaConfermata,i,isDataConsegnaConfermataFineMesse);
			  Date newdateConsegnaConfermata = null;
			  if(dataRigaDestConsegnaConfermata != null)
				  newdateConsegnaConfermata = getDataSuccesive(dataRigaDestConsegnaConfermata,i,isDataConsegnaConfermataFineMesse);
			  //72XXX >
			  Date newdateConsegnaRichiesta = getDataSuccesive(dataRigaDestConsegnaRichiesta,i,isDataDestConsegnaRichiestaFineMesse);
			  ordRigaDC = completaDatiNuovoRiga(ordRigaDC,newdateConsegnaConfermata,newdateConsegnaRichiesta,seq);
			    if (ordRigaDC.check() == BODataCollector.ERROR) {
			    	getErroriDataCollector().addAll(ordRigaDC.getErrorList().getErrors());
			    	return ErrorCodes.GENERIC_ERROR;//se il salvataggio di una riga restituisce un errore -100, l'errore registrato è quello con id BAS0000036 , sostituire con un errore evidente
			      }
			      else {
					  ((DocumentoOrdineRiga) ordRigaDC.getBo()).setSequenzaRiga(seq);
					  ((DocumentoOrdineRiga) ordRigaDC.getBo()).setNumeroRigaDocumento(seq);
					  preparaDataCollectorPerCopia(ordRigaDC);
					  ret = ordRigaDC.save();
					  if(ret != BODataCollector.ERROR) {
						  getErroriDataCollector().addAll(ordRigaDC.getErrorList().getErrors());
						  savedKey += ((DocumentoOrdineRiga) ordRigaDC.getBo()).getKey() + UpdateMultipleRows.TOKEN_KEY;
						  seq += passo;
						  ret++;
					  }else
					  {
						  getErroriDataCollector().addAll(ordRigaDC.getErrorList().getErrors());
						  return ErrorCodes.GENERIC_ERROR;//se il salvataggio di una riga restituisce un errore -100, l'errore registrato è quello con id BAS0000036 , sostituire con un errore evidente
					  }
			      }

		  }
		  return ret;
	  }
	  
	  public void preparaDataCollectorPerCopia(BODataCollector ordRigaDC) {
		  ordRigaDC.setAutoCheck(false);
		  ordRigaDC.getBo().setOnDB(false);
			Enumeration e = ordRigaDC.getSecondaryDataCollectors();
			while (e.hasMoreElements()) {
				Object sdc = e.nextElement();
				if (sdc instanceof CollectionDataCollector) {
					Enumeration rows = ((CollectionDataCollector) sdc).getRowsDataCollectors();
					while (rows.hasMoreElements()) {
						BODataCollector row = (BODataCollector) rows.nextElement();
						row.getBo().setOnDB(false);
						row.setMode(OpenType.COPY);
					}
				}
		  }
	  }
	  public BODataCollector completaDatiNuovoRiga(BODataCollector ordRigaDC , Date newdateConsegnaConfermata ,Date newdateConsegnaRichiesta , int seq) {		  
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setSequenzaRiga(seq);
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setNumeroRigaDocumento(seq);
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setStatoAvanzamento(getStatoAvanzamento());
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setDataConsegnaConfermata(newdateConsegnaConfermata);
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setOldDataConsegnaConfermata(newdateConsegnaConfermata);
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setDataConsegnaRichiesta(newdateConsegnaRichiesta);
			((DocumentoOrdineRiga) ordRigaDC.getBo()).setOldDataConsegnaRichiesta(newdateConsegnaRichiesta);
			
		      if (((DocumentoOrdineRiga) ordRigaDC.getBo()).getSpecializzazioneRiga() == DocumentoBaseRiga.RIGA_PRIMARIA)
		      {
	              Iterator i = ((RigaPrimaria)ordRigaDC.getBo()).getRigheSecondarie().iterator();
	              while(i.hasNext())
	              {
	            	  DocumentoOrdineRiga riga = (DocumentoOrdineRiga)i.next();
	            	  riga.setDataConsegnaConfermata(newdateConsegnaConfermata);
	            	  riga.setOldDataConsegnaConfermata(newdateConsegnaConfermata);
	            	  riga.setDataConsegnaRichiesta(newdateConsegnaRichiesta);
	            	  riga.setOldDataConsegnaRichiesta(newdateConsegnaRichiesta);
	              }
		      }

			return ordRigaDC;
	  }
	  protected Date getDataSuccesive(Date dataRiga,int nbr,boolean isDataFineMesse) {
		  return getProssimoMese(dataRiga, nbr,getCadenza() ,isDataFineMesse);
	  }
	  
	  protected boolean isDataFineMesse(Date dataRiga) {
		  Calendar calendar = Calendar.getInstance(); 
		  calendar.setTime(dataRiga);
		  int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
		  return calendar.get(Calendar.DAY_OF_MONTH) == lastDayOfMonth;
	  }
	  
	  protected Date getProssimoMese(Date dataRiga , int nbr,char cadenza , boolean isDataFineMesse) {
		  Calendar calendar = Calendar.getInstance(); 
		  calendar.setTime(dataRiga);
		  if(cadenza == MENSILE) {
			  calendar.add(Calendar.MONTH, nbr);
		  }
		  else
		  {
			  calendar.add(Calendar.MONTH, 2 * nbr);
		  }
		  if(isDataFineMesse) {
			  int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			  calendar.set(Calendar.DAY_OF_MONTH, lastDay);
			  
		  }
		  return new Date(calendar.getTimeInMillis());
	  }
	  
	  public String getKeyOrdRiga() {
		  String chiave = KeyHelper.buildObjectKey(new String[] {getIdAzienda(), getIdAnnoTestata(), getIdNumeroTestata(),String.valueOf(getIdRiga()) ,String.valueOf(getIdDetRiga())});
		  return chiave;
	  }
	  
	 public static synchronized int getNextSequenzaRiga(PropagaRiga rigaOrigine) {
		 int nuovaSequenza =0 ;
		 DocumentoOrdineRiga riga = (DocumentoOrdineRiga) rigaOrigine.getBODataCollector().getBo();
		 if(rigaOrigine.getTipoRiga() != null && rigaOrigine.getTipoRiga().equals(ORD_ACQ_RIGA)) {
			 nuovaSequenza = RigaAcquisto.getNumeroNuovaRiga(riga.getTestata());
		 }
		 else if(rigaOrigine.getTipoRiga() != null && rigaOrigine.getTipoRiga().equals(DOC_ACQ_RIGA)) {
			 nuovaSequenza = DocumentoAcquistoRiga.getNumeroNuovaRiga(riga.getTestata());
		 }
		 return nuovaSequenza;
	 }
		 
	 public ErrorMessage checkNumRighe() {
		 if(getNumRighe() < 1) {
			 return new ErrorMessage("THIP40T964");
		 }
		 return null;
	 }
	@Override
	public void setDirty() {
		// TODO Auto-generated method stub
		
	}
}
