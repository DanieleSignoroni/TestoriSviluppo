package it.testori.thip.vendite.ordineVE;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.cbs.ResolverTicklerParametric;
import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.BaseObject;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.ErrorCodes;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.OneToMany;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.security.Group;
import com.thera.thermfw.security.User;
import com.thera.thermfw.tickler.TicklerTmpl;

import it.testori.thip.base.articolo.YArticoloCosto;
import it.testori.thip.base.generale.AssocHdrTpDocDgt;
import it.testori.thip.base.generaleVE.web.YCalcoloMarkup;
import it.testori.thip.produzione.rifornLinea.YPsnDatiRifornimentoLinea;
import it.testori.thip.vendite.generaleVE.GestioneCalcoloCosti;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.ArticoloCosto;
import it.thera.thip.base.articolo.ClasseD;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.comuniVenAcq.ContenitoreRiga;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRiga;
import it.thera.thip.base.comuniVenAcq.OrdineRigaLotto;
import it.thera.thip.base.comuniVenAcq.StatoEvasione;
import it.thera.thip.base.comuniVenAcq.TipoRiga;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.base.generale.ImportoInValutaEstera;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.profilo.UtenteAzienda;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.cs.ThipException;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.magazzino.movimenti.MovimentoMagazzino;
import it.thera.thip.magazzino.saldi.SaldoMagLotto;
import it.thera.thip.vendite.generaleVE.PersDatiVen;
import it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori;
import it.thera.thip.vendite.ordineVE.OrdineVendita;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaLotto;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaLottoPrm;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrm;

/**
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 28/08/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72097    28/08/2025  DSSOF3   Prima stesura
 * 72105	02/09/2025	DSSOF3   Gestione prima data promessa
 * 72141	24/09/2025  DSSOF3   Introduzione markup, errore articolo costo
 * 72157	08/10/2025	DSSOF3	 Aggiunta note cambio data consegna, modifiche generiche alla gestione date
 * 72238	03/12/2025	DSSOF3   Check per non lasciar rendere definitivo senza data consegna confermata, calcolo markup forzato.
 * 72242    10/12/2025  DSSOF3   Righe spesa, disabilitare personalizzazioni.
 * 72247	10/12/2025	DSSOF3	 Gestione allegati ordine vendita riga.
 * 72269	07/01/2026	DSSOF3	 Nuova gestione quadratura lotti
 * 72272	07/01/2026	DSSOF3	 Aggiunta valore totale ordinato in valuta a db, rimuovere update duplicate.
 * 72273	08/01/2026	DSSOF3	 Aggiunta valore residuo ordine vendita
 * 72304	20/01/2026	DSSOF3	 Residuo, se riga saldata imposto a 0.
 * 72317	26/01/2026	DSSOF3	 Notifica spostamento data di consegna confermata.
 * 72320	28/01/2026	DSSOF3	 Notifica spostamento data di consegna confermata, aggiunta ragione sociale cliente.
 * 72350	11/02/2026	DSSOF3	 Calcolo prezzo in EUR al salvataggio.
 * 72391	09/03/2026	DSSOF3	 In aggiornaDatiTestata() portare in testata il MAX delle date cons (cfm,rcs) se non null.
 * 72425	30/03/2026	DSSOF3	 Aggiunti metodi per lista di causali per le quali l'area applicativa non deve essere mandatory. Non considerare le righe annullate nella fase di cambio stato avanzamento dell'ordine testata.
 */

public class YOrdineVenditaRigaPrm extends OrdineVenditaRigaPrm {

	private static final String STMT_CLEAN_CAU_CAMBIO_DT_CONS = "UPDATE "+YOrdineVenditaRigaPrmTM.TABLE_NAME_EXT+"  "
			+ "SET "+YOrdineVenditaRigaPrmTM.R_COD_CAMBIO_DT_CONS+" = NULL "
			+ "WHERE "+YOrdineVenditaRigaPrmTM.ID_AZIENDA+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_ANNO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_NUMERO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_RIGA_ORD+" = ?";
	public static CachedStatement cCleanCausaleCambioDtCons = new CachedStatement(STMT_CLEAN_CAU_CAMBIO_DT_CONS);

	//72157
	private static final String STMT_CLEAN_NOTE_CAMBIO_DT_CONS = "UPDATE "+YOrdineVenditaRigaPrmTM.TABLE_NAME_EXT+"  "
			+ "SET "+YOrdineVenditaRigaPrmTM.NOTE_CAMBIO_DT_CONS+" = NULL "
			+ "WHERE "+YOrdineVenditaRigaPrmTM.ID_AZIENDA+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_ANNO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_NUMERO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_RIGA_ORD+" = ?";
	public static CachedStatement cCleanNoteCambioDtCons = new CachedStatement(STMT_CLEAN_NOTE_CAMBIO_DT_CONS);
	//72157

	//72272
	private static final String STMT_UPDATE_VLR_ORD_VA = "UPDATE "+YOrdineVenditaRigaPrmTM.TABLE_NAME_EXT+"  "
			+ "SET "+YOrdineVenditaRigaPrmTM.YVLR_ORD_VA+" = ? "
			+ "WHERE "+YOrdineVenditaRigaPrmTM.ID_AZIENDA+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_ANNO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_NUMERO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_RIGA_ORD+" = ?";
	public static CachedStatement cUpdateValoreOrdinatoVA = new CachedStatement(STMT_UPDATE_VLR_ORD_VA);
	//72272

	//72273
	private static final String STMT_UPDATE_VLR_RES_VA = "UPDATE "+YOrdineVenditaRigaPrmTM.TABLE_NAME_EXT+"  "
			+ "SET "+YOrdineVenditaRigaPrmTM.YVLR_RES_VA+" = ? "
			+ "WHERE "+YOrdineVenditaRigaPrmTM.ID_AZIENDA+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_ANNO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_NUMERO_ORD+" = ? "
			+ "AND "+YOrdineVenditaRigaPrmTM.ID_RIGA_ORD+" = ?";
	public static CachedStatement cUpdateValoreResiduoVA = new CachedStatement(STMT_UPDATE_VLR_RES_VA);
	//72273

	protected java.sql.Date iDataConsegnaCfmStorica;

	//72141
	protected BigDecimal iMarkup;

	/**
	 * Attributo di servizio, e' la copia del {@link #getCostoUnitario()}.<br>
	 */
	protected BigDecimal iYCostoUnitario;
	//72141

	protected Proxy iCausaleCambioDataConsegna = new Proxy(
			it.testori.thip.vendite.ordineVE.YCausaleCambioDataCons.class);

	protected String iNoteCambioDataCons; //72157

	protected BigDecimal iPrezzoEuro;

	protected String iAltezzaRichiesta;
	protected String iLunghezzaRichiesta;

	protected Proxy iAreaApplicativa = new Proxy(it.thera.thip.base.articolo.ClasseD.class);

	protected OneToMany iYAllegati = new OneToMany(YAllegatiOrdVenRigPrm.class, this, 15, false); //72247

	protected BigDecimal iYValoreOrdinatoVA; //72272
	protected BigDecimal iYValoreResiduoVA; //72273

	protected String iChiaviLottiMultSel;

	public YOrdineVenditaRigaPrm() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setDataConsegnaCfmStorica(java.sql.Date dataConsegnaCfmStorica) {
		this.iDataConsegnaCfmStorica = dataConsegnaCfmStorica;
		setDirty();
	}

	public java.sql.Date getDataConsegnaCfmStorica() {
		return iDataConsegnaCfmStorica;
	}

	public void setCausaleCambioDataConsegna(YCausaleCambioDataCons causalecambiodataconsegna) {
		String oldObjectKey = getKey();
		this.iCausaleCambioDataConsegna.setObject(causalecambiodataconsegna);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public YCausaleCambioDataCons getCausaleCambioDataConsegna() {
		return (YCausaleCambioDataCons) iCausaleCambioDataConsegna.getObject();
	}

	public void setCausaleCambioDataConsegnaKey(String key) {
		String oldObjectKey = getKey();
		iCausaleCambioDataConsegna.setKey(key);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getCausaleCambioDataConsegnaKey() {
		return iCausaleCambioDataConsegna.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		if (iCausaleCambioDataConsegna != null) {
			String key = iCausaleCambioDataConsegna.getKey();
			iCausaleCambioDataConsegna.setKey(KeyHelper.replaceTokenObjectKey(key, 1, idAzienda));
		}
		if(iAreaApplicativa != null) {
			String key = iAreaApplicativa.getKey();
			iAreaApplicativa.setKey(KeyHelper.replaceTokenObjectKey(key, 1, idAzienda));
		}
		if (iYAllegati != null) {
			iYAllegati.setFatherKeyChanged();
		}
	}

	@Override
	public void setAnnoDocumento(String annoDocumento) {
		super.setAnnoDocumento(annoDocumento);
		if (iYAllegati != null) {
			iYAllegati.setFatherKeyChanged();
		}
	}

	@Override
	public void setNumeroDocumento(String numeroDocumento) {
		super.setNumeroDocumento(numeroDocumento);
		if (iYAllegati != null) {
			iYAllegati.setFatherKeyChanged();
		}
	}

	@Override
	public void setNumeroRigaDocumento(Integer numeroRigaDocumento) {
		super.setNumeroRigaDocumento(numeroRigaDocumento);
		if (iYAllegati != null) {
			iYAllegati.setFatherKeyChanged();
		}
	}

	public void setIdCodiceCambioDtCons(String idCodiceCambioDtCons) {
		String key = iCausaleCambioDataConsegna.getKey();
		iCausaleCambioDataConsegna.setKey(KeyHelper.replaceTokenObjectKey(key, 2, idCodiceCambioDtCons));
		setDirty();
	}

	public String getIdCodiceCambioDtCons() {
		String key = iCausaleCambioDataConsegna.getKey();
		String objIdCodiceCambioDtCons = KeyHelper.getTokenObjectKey(key, 2);
		return objIdCodiceCambioDtCons;
	}

	//72141
	public void setMarkup(BigDecimal markup) {
		this.iMarkup = markup;
		setDirty();
	}

	public BigDecimal getMarkup() {
		return iMarkup;
	}

	public BigDecimal getYCostoUnitario() {
		return iYCostoUnitario;
	}

	public void setYCostoUnitario(BigDecimal iYCostoUnitario) {
		this.iYCostoUnitario = iYCostoUnitario;
		setDirty();
	}
	//72141

	//72157
	public String getNoteCambioDataCons() {
		return iNoteCambioDataCons;
	}

	public void setNoteCambioDataCons(String iNoteCambioDataCons) {
		this.iNoteCambioDataCons = iNoteCambioDataCons;
		setDirty();
	}
	//72157

	public BigDecimal getPrezzoEuro() {
		return iPrezzoEuro;
	}

	public void setPrezzoEuro(BigDecimal iPrezzoEuro) {
		this.iPrezzoEuro = iPrezzoEuro;
		setDirty();
	}

	public String getAltezzaRichiesta() {
		return iAltezzaRichiesta;
	}

	public void setAltezzaRichiesta(String iAltezzaRichiesta) {
		this.iAltezzaRichiesta = iAltezzaRichiesta;
		setDirty();
	}

	public String getLunghezzaRichiesta() {
		return iLunghezzaRichiesta;
	}

	public void setLunghezzaRichiesta(String iLunghezzaRichiesta) {
		this.iLunghezzaRichiesta = iLunghezzaRichiesta;
		setDirty();
	}

	public void setAreaApplicativa(ClasseD classeD) {
		this.iAreaApplicativa.setObject(classeD);
		setDirty();
	}

	public ClasseD getAreaApplicativa() {
		return (ClasseD)iAreaApplicativa.getObject();
	}

	public void setAreaApplicativaKey(String key) {
		iAreaApplicativa.setKey(key);
		setDirty();
	}

	public String getAreaApplicativaKey() {
		return iAreaApplicativa.getKey();
	}

	public void setIdAreaApplicativa(String idAreaApplicativa) {
		iAreaApplicativa.setKey(KeyHelper.replaceTokenObjectKey(iAreaApplicativa.getKey(),2,idAreaApplicativa));
		setDirty();
	}

	public String getIdAreaApplicativa() {
		return KeyHelper.getTokenObjectKey(iAreaApplicativa.getKey(),2);
	}

	//72272
	public BigDecimal getYValoreOrdinatoVA() {
		return iYValoreOrdinatoVA;
	}

	public void setYValoreOrdinatoVA(BigDecimal iYValoreOrdinatoVA) {
		this.iYValoreOrdinatoVA = iYValoreOrdinatoVA;
		setDirty();
	}
	//72272

	//72273
	public BigDecimal getYValoreResiduoVA() {
		return iYValoreResiduoVA;
	}

	public void setYValoreResiduoVA(BigDecimal iYValoreResiduoVA) {
		this.iYValoreResiduoVA = iYValoreResiduoVA;
		setDirty();
	}
	//72273

	public String getChiaviLottiMultSel() {
		return iChiaviLottiMultSel;
	}

	public void setChiaviLottiMultSel(String iChiaviLottiMultipliSelezionati) {
		this.iChiaviLottiMultSel = iChiaviLottiMultipliSelezionati;
	}

	@Override
	public void completaBO() {
		super.completaBO();
		//72247
		if(getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
			caricaAllegati(false);
		//72247
	}

	//72247
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void caricaAllegati(boolean controlloPresenti) {
		Vector associazioniDef = AssocHdrTpDocDgt.recuperaAssociazioniDefault("OrdineVenditaRigaPrm", true);
		if(associazioniDef != null && associazioniDef.size() > 0) {
			for (Iterator iterator = associazioniDef.iterator(); iterator.hasNext();) {
				AssocHdrTpDocDgt associazione = (AssocHdrTpDocDgt) iterator.next();

				if(controlloPresenti && isAssociazioneAllegatoPresente(associazione.getIdTipoDocumentoDigitale()))
					continue;

				YAllegatiOrdVenRigPrm allegato = (YAllegatiOrdVenRigPrm) Factory.createObject(YAllegatiOrdVenRigPrm.class);
				allegato.setOrdineVenditaRigaPrm(this);
				allegato.setAssociazionedocumento(associazione);
				allegato.setVincolo(associazione.getVincolo());

				getYAllegati().add(allegato);

			}
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean isAssociazioneAllegatoPresente(String idTipoDocDgt) {
		Iterator iterAllegati = getYAllegati().iterator();
		while(iterAllegati.hasNext()) {
			YAllegatiOrdVenRigPrm allegato = (YAllegatiOrdVenRigPrm) iterAllegati.next();
			if(allegato.getIdTipoDocDgt().equals(idTipoDocDgt))
				return true;
		}
		return false;
	}
	//72247

	@Override
	protected void copiaValoriInOldRiga() {
		super.copiaValoriInOldRiga();
		YOrdineVenditaRigaPrm ovr = (YOrdineVenditaRigaPrm) iOldRiga;
		ovr.setDataConsegnaCfmStorica(getDataConsegnaCfmStorica());
		ovr.setCausaleCambioDataConsegna(getCausaleCambioDataConsegna());
	}

	@Override
	public int save() throws SQLException {
		OrdineVendita tes = (OrdineVendita) getTestata();
		if(getYCostoUnitario() != null) { //72141
			setCostoUnitario(getYCostoUnitario());
		}
		if(!isOnDB() //72157
				&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
				&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
				|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) { //72242
			setDataConsegnaConfermata(null);
			setSettConsegnaConfermata(null);
			setDataConsegnaProduzione(null);
			setSettConsegnaProduzione(null);
			setDataConsegnaCfmStorica(null);
		}
		if(!isOnDB()) {
			//72157 remmare
			//setDataConsegnaCfmStorica(getDataConsegnaConfermata()); //Storicizzo sul nuovo la data consegna confermata
			//72105 Inizio
		}else {
			//72157
			if((YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
					|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) {
				if(isOnDB() 
						&& getDataConsegnaConfermata() != null 
						&& getDataConsegnaCfmStorica() == null
						&& getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO
						&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)) { //72242
					setDataConsegnaCfmStorica(getDataConsegnaConfermata());
				}
				//72157
				if (getCausaleCambioDataConsegna() != null
						&& getCausaleCambioDataConsegna().isModificaDataOrigine()
						&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)) { //72242
					setDataConsegnaCfmStorica(getDataConsegnaConfermata());
				}
				DocumentoOrdineRiga ovr = (DocumentoOrdineRiga) iOldRiga;
				if(ovr != null && isQuantitaCambiata() && getStatoEvasione() != StatoEvasione.SALDATO
						&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)) { //72242
					setDataConsegnaConfermata(null);
					setStatoAvanzamento(StatoAvanzamento.PROVVISORIO);
				}
				if (ovr != null
						&& !datiUguali(ovr.getDataConsegnaRichiesta(), getDataConsegnaRichiesta())
						&& getStatoEvasione() != StatoEvasione.SALDATO
						&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)){ //72242
					setDataConsegnaConfermata(null);
					setStatoAvanzamento(StatoAvanzamento.PROVVISORIO);
				}
			}
		}
		//72105 Fine
		if(!isOnDB() && getIdAreaApplicativa() == null) {
			setIdAreaApplicativa(getArticolo().getIdClasseD());
		}

		//72238
		if(getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO) //72242
			calcoloMarkup();
		//72238

		if(getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
			caricaAllegati(true);

		//72350 <
		if(getTipoRiga() == TipoRiga.MERCE) {
			calcolaPrezzoInEUR();
		}
		//72350 >

		if(getChiaviLottiMultSel() != null)
			popolaLottiSelezioneMultipla();

		int rc = super.save();

		//..Se l'utente ha cambiato data consegna tramite il campo della causale allora svuoto la causale (fatto cosi perche' almeno registro i log del campo)
		if(rc > 0 && isOnDB()) {
			DocumentoOrdineRiga ovr = (DocumentoOrdineRiga) iOldRiga;
			if (ovr != null
					&& !datiUguali(ovr.getDataConsegnaConfermata(), getDataConsegnaConfermata())
					&& getCausaleCambioDataConsegna() != null
					&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
					&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
					|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) { //72242
				svuotaCausaleCambioDataConsegna();
			}
			//72157
			if(ovr != null && getNoteCambioDataCons() != null && !getNoteCambioDataCons().isEmpty()
					&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
					&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
					|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) { //72242
				String oldIdCambioCau = ((YOrdineVenditaRigaPrm)ovr).getIdCodiceCambioDtCons();
				boolean cleanNotes = false;
				if((oldIdCambioCau == null && getIdCodiceCambioDtCons() != null)){
					cleanNotes = true;
				}else if(oldIdCambioCau != null && getIdCodiceCambioDtCons() == null) {
					cleanNotes = true;
				}else if(oldIdCambioCau != null && getIdCodiceCambioDtCons() != null && !oldIdCambioCau.equals(getIdCodiceCambioDtCons())) {
					cleanNotes = true;
				}
				if(cleanNotes) {
					svuotaNoteCambioDataConsegna();
				}
			}
			//72157

			//72272
			BigDecimal valoreOrdinatoVA = getValoreOrdinatoVA();
			if(valoreOrdinatoVA != null) {
				aggiornaValoreOrdinatoVA(valoreOrdinatoVA);
			}
			//72272
			//72273
			BigDecimal valoreConsegnatoVA = getValoreConsegnatoVA();
			if(valoreConsegnatoVA != null && valoreOrdinatoVA != null) {
				BigDecimal valoreResiduoVA = valoreOrdinatoVA.subtract(valoreConsegnatoVA).max(BigDecimal.ZERO);
				//72304 <
				if(getStatoEvasione() == StatoEvasione.SALDATO) {
					valoreResiduoVA = BigDecimal.ZERO;
				}
				//72304 > 
				aggiornaValoreResiduoVA(valoreResiduoVA);
			}
			//72273
			//72317 <
			if (ovr != null
					&& !datiUguali(ovr.getDataConsegnaConfermata(), getDataConsegnaConfermata())
					&& ((OrdineVendita)getTestata()).getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO
					&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
					|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))){
				scadenzaCambioDataConsegnaCfm();
			}
			// 72317 >
		}
		return rc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void popolaLottiSelezioneMultipla() throws ThipException {
		boolean adattaQta = SelezioneMultiplaLottoTestori.isCausaliTestataAdattaQuantitaLottiRiga(getTestata().getIdCau());
		List<OrdineVenditaRigaLottoPrm> nuoviLotti = costruisciOrdineVenditaRigaLottoPrmListSelezioneMultipla();
		OrdineRigaLotto lottoDummy = dammiLottoDummy(getRigheLotto());
		if (nuoviLotti.isEmpty()) {
			return;
		}
		if(adattaQta) {
			if(nuoviLotti.size() > 0) {
				getRigheLotto().remove(lottoDummy);
				List<BigDecimal> totQtaLottiEsistenti = caricaQuantitaLottoPublic(getRigheLotto());
				ArrayList totQtaLottiNuovi = caricaQuantitaLottoPublic(nuoviLotti);
				BigDecimal qtaInUMPrm = ((BigDecimal) totQtaLottiNuovi.get(1)).add(((BigDecimal) totQtaLottiEsistenti.get(1)));
				getQuantitaOrdinata().setQuantitaInUMPrm(qtaInUMPrm);
				getQuantitaOrdinata().setQuantitaInUMRif(qtaInUMPrm);
				getRigheLotto().addAll(nuoviLotti);
			}
		}else {
			List<OrdineVenditaRigaLottoPrm> righe = getRigheLotto();
			BigDecimal qtaRichiesta = getServizioQta().getQuantitaInUMPrm();

			for (OrdineVenditaRigaLottoPrm nuovo : nuoviLotti) {

				righe.add(nuovo);

				BigDecimal totale = (BigDecimal) caricaQuantitaLottoPublic(righe).get(1);
				if (totale.compareTo(qtaRichiesta) <= 0) {
					continue;
				}

				BigDecimal eccedenza = totale.subtract(qtaRichiesta);
				if (lottoDummy != null) {
					eccedenza = riduciDummy(lottoDummy, eccedenza);

					if (lottoDummy.getQtaInUMPrmMag().compareTo(BigDecimal.ZERO) == 0) {
						righe.remove(lottoDummy);
						lottoDummy = null;
					}
				}

				if (eccedenza.compareTo(BigDecimal.ZERO) > 0) {
					for (OrdineVenditaRigaLottoPrm lotto : righe) {
						if (eccedenza.compareTo(BigDecimal.ZERO) <= 0) {
							break;
						}
						
						if (lotto == nuovo) {
							continue;		                
						}
						
						if (Lotto.LOTTO_DUMMY.equals(lotto.getIdLotto())) {
							continue;
						}

						BigDecimal qta = lotto.getQtaInUMPrmMag();

						if (qta.compareTo(BigDecimal.ONE) > 0) {
							BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
							lotto.setQtaInUMPrmMag(qta.subtract(riduzione));
							lotto.setQtaInUMRif(lotto.getQtaInUMPrmMag());
							eccedenza = eccedenza.subtract(riduzione);
						}
					}
				}
				if (eccedenza.compareTo(BigDecimal.ZERO) > 0) {
					throw new ThipException("Impossibile riallineare le quantità dei lotti");
				}
			}
		}
	}

	public BigDecimal riduciDummy(OrdineRigaLotto dummy, BigDecimal eccedenza) {
		if (dummy == null || eccedenza.compareTo(BigDecimal.ZERO) <= 0) {
			return eccedenza;
		}
		BigDecimal qta = dummy.getQtaInUMPrmMag();
		BigDecimal riduzione = qta.min(eccedenza);
		BigDecimal nuovaQta = qta.subtract(riduzione);
		
		dummy.setQtaInUMPrmMag(nuovaQta);
		dummy.setQtaInUMRif(nuovaQta);

		return eccedenza.subtract(riduzione);
	}

	public BigDecimal riduciLottiEsistenti(List<OrdineVenditaRigaLottoPrm> lotti,
			BigDecimal eccedenza) {
		for (OrdineVenditaRigaLottoPrm lotto : lotti) {
			if (eccedenza.compareTo(BigDecimal.ZERO) <= 0) break;
			if (Lotto.LOTTO_DUMMY.equals(lotto.getIdLotto())) continue;
			BigDecimal qta = lotto.getQtaInUMPrmMag();
			if (qta.compareTo(BigDecimal.ONE) > 0) {
				BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
				lotto.setQtaInUMPrmMag(qta.subtract(riduzione));
				lotto.setQtaInUMRif(lotto.getQtaInUMPrmMag());
				eccedenza = eccedenza.subtract(riduzione);
			}
		}
		return eccedenza;
	}

	public BigDecimal riduciLotto(OrdineVenditaRigaLottoPrm lotto,
			BigDecimal eccedenza) {
		if (eccedenza.compareTo(BigDecimal.ZERO) <= 0) {
			return eccedenza;
		}
		BigDecimal qta = lotto.getQtaInUMPrmMag();
		if (qta.compareTo(BigDecimal.ONE) > 0) {
			BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
			lotto.setQtaInUMPrmMag(qta.subtract(riduzione));
			lotto.setQtaInUMRif(lotto.getQtaInUMPrmMag());
			eccedenza = eccedenza.subtract(riduzione);
		}
		return eccedenza;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List costruisciOrdineVenditaRigaLottoPrmListSelezioneMultipla() {
		List lotti = new ArrayList();
		Iterator<String> chiaviSelezionati = SelezioneMultiplaLottoTestori.getListLottiSelezioneMultipla(getChiaviLottiMultSel()).iterator();
		while(chiaviSelezionati.hasNext()) {
			String lottoKey = (String)chiaviSelezionati.next();
			Lotto lt;
			try {
				lt = (Lotto) Lotto.elementWithKey(Lotto.class, lottoKey, PersistentObject.NO_LOCK);
				if(lt != null) { 
					List saldi = it.testori.thip.vendite.documentoVE.web.YRecuperaQuantitaLotto.getSaldiMag(getIdAzienda(),
							(getIdMagazzino()),
							getIdArticolo(), getIdVersioneRcs(), getIdConfigurazione(), lt.getCodiceLotto(), null,
							MovimentoMagazzino.LOTTO);
					if(saldi.size() > 0) {
						SaldoMagLotto saldoMagLotto = (SaldoMagLotto) saldi.get(0);
						BigDecimal giacenza = saldoMagLotto.getDatiSaldo().getQtaGiacenzaUMPrim();
						if(giacenza.compareTo(BigDecimal.ZERO) > 0) {
							OrdineVenditaRigaLotto rigaLotto = (OrdineVenditaRigaLotto)creaLotto();
							rigaLotto.setFather(this);
							rigaLotto.setLotto(lt);
							rigaLotto.setIdArticolo(getIdArticolo());

							rigaLotto.setQtaInUMPrmMag(giacenza);
							rigaLotto.setQtaInUMRif(giacenza);

							lotti.add(rigaLotto);
						}
					}
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return lotti;
	}

	//72350 <
	protected void calcolaPrezzoInEUR() {
		try {
			OrdineVendita tes = (OrdineVendita) getTestata();
			if(getPrezzo() != null && !tes.getIdValuta().equals("EUR")) {
				ImportoInValutaEstera imp = (ImportoInValutaEstera) Factory.createObject(ImportoInValutaEstera.class);
				imp.setFattCambioOper(tes.getCambio());
				imp.convertiEstPrim(tes.getIdValuta(), getPrezzo(), tes.getDataDocumento());
				setPrezzoEuro(imp.getImportaValPrim());
			}else {
				setPrezzoEuro(getPrezzo());
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	//72350 >

	//72317
	protected void scadenzaCambioDataConsegnaCfm() {
		DocumentoOrdineRiga ovr = (DocumentoOrdineRiga) iOldRiga;
		OrdineVendita testata = ((OrdineVendita)getTestata());
		try {
			TicklerTmpl ticklerTemplate = TicklerTmpl.elementWithKey(getTicketTicklerIdCambioDataConsegnaCfm(), PersistentObject.NO_LOCK);
			if(ticklerTemplate != null 
					&& ticklerTemplate.getAssignGroup() != null
					&& !isUtenteInGroup(ticklerTemplate.getAssignGroup(), UtenteAzienda.getUtenteAziendaConnesso().getId())) {
				Class<?> actionClass = Class.forName("com.thera.thermfw.cbs.ResolverTicklerParametric");
				ResolverTicklerParametric resTickler = (ResolverTicklerParametric) actionClass.newInstance();
				resTickler.setTicklerTemplate(ticklerTemplate);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String params = "{0}="+testata.getNumeroDocumentoFormattato();
				params += ";{1}="+getNumeroRigaDocumento();
				params += ";{2}="+testata.getIdCliente() + " - " + testata.getCliente().getRagioneSociale(); //72320 aggiunta rag soc
				params += ";{3}="+sdf.format(ovr.getDataConsegnaConfermata());
				params += ";{4}="+sdf.format(getDataConsegnaConfermata());
				resTickler.setParameters(params);
				resTickler.setInstance((BaseObject)this);
				resTickler.setInstanceHDRName("OrdineVenditaRigaPrm");
				resTickler.saveTickler();
			}
		}catch (Exception e) {
			Trace.println("OrdineVenditaRigaPrm - eccezione nella creazione del tickler: " + e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean isUtenteInGroup(Group group, String currentUserId) {
		boolean existeUtente = false;
		List users = null;
		if (group != null) {
			users = group.getUsers();
		}
		if( users != null && !users.isEmpty()){
			Iterator it = users.iterator();
			while(it.hasNext()){
				User user = (User)it.next();
				String userId = user.getId();
				if(userId != null && currentUserId != null && userId.equals(currentUserId)){
					existeUtente = true;
				}
			}
		}
		return existeUtente;
	}

	public static String getTicketTicklerIdCambioDataConsegnaCfm() {
		return ParametroPsn.getValoreParametroPsn("pers.vendite.ordineVE.ScadenzaDataConsegnaCfm", "TicklerId");
	}
	//72317

	//72238
	protected void calcoloMarkup() {
		try {
			OrdineVendita tes = (OrdineVendita) getTestata();
			BigDecimal costoUnitario = getCostoUnitario() != null ? getCostoUnitario() : BigDecimal.ZERO;
			BigDecimal prezzo = getPrezzo() != null ? getPrezzo() : BigDecimal.ZERO;
			BigDecimal markup = null;
			if(!tes.getIdValuta().equals("EUR") && prezzo != null) {
				ImportoInValutaEstera imp = (ImportoInValutaEstera) Factory.createObject(ImportoInValutaEstera.class);
				imp.setFattCambioOper(tes.getCambio());
				imp.convertiEstPrim(tes.getIdValuta(), prezzo, tes.getDataDocumento());
				prezzo = imp.getImportaValPrim();
			}
			if (prezzo != null && costoUnitario != null && costoUnitario.compareTo(BigDecimal.ZERO) > 0) {
				markup = YCalcoloMarkup.calcolaMarkup(costoUnitario, prezzo);
				setMarkup(markup);
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
	}
	//72238

	@Override
	public int saveOwnedObjects(int rc) throws SQLException {
		rc = super.saveOwnedObjects(rc);
		rc = iYAllegati.save(rc); //72247
		return rc;
	}

	public int deleteOwnedObjects() throws SQLException {
		int ret = super.deleteOwnedObjects();
		if (ret < ErrorCodes.NO_ROWS_UPDATED) {
			return ret;
		}
		return getYAllegatiInternal().delete(); //72247
	}

	//72247
	@SuppressWarnings("rawtypes")
	public List getYAllegati() {
		return getYAllegatiInternal();
	}

	protected OneToMany getYAllegatiInternal() {
		if (iYAllegati.isNew())
			iYAllegati.retrieve();
		return iYAllegati;
	}
	//72247

	//72157
	@SuppressWarnings("rawtypes")
	@Override
	protected void aggiornaDatiTestata() throws ThipException {
		super.aggiornaDatiTestata();
		OrdineVendita tes = (OrdineVendita) getTestata();
		if(tes.getStatoEvasione() != StatoEvasione.SALDATO) {
			boolean tutteDefinitive = true;
			Iterator iterRighe = getTestata().getRighe().iterator();
			while(iterRighe.hasNext()) {
				YOrdineVenditaRigaPrm riga = (YOrdineVenditaRigaPrm) iterRighe.next();
				if(!riga.getKey().equals(getKey()) 
						&& riga.getDatiComuniEstesi().getStato() != DatiComuniEstesi.ANNULLATO //72425
						&& riga.getStatoAvanzamento() != StatoAvanzamento.DEFINITIVO) {
					tutteDefinitive = false;
				}
			}
			if(getDatiComuniEstesi().getStato() != DatiComuniEstesi.ANNULLATO //72425
					&& getStatoAvanzamento() != StatoAvanzamento.DEFINITIVO) {
				tutteDefinitive = false;
			}

			if(tutteDefinitive)
				tes.setStatoAvanzamento(StatoAvanzamento.DEFINITIVO);
			else
				tes.setStatoAvanzamento(StatoAvanzamento.PROVVISORIO);
		}
		//72391 <
		tes.setDataConsegnaConfermata(getMaxDataOrNull(DATA_CONSEGNA_CFM));
		tes.setDataConsegnaRichiesta(getMaxDataOrNull(DATA_CONSEGNA_RCS));
		//72391 >
	}

	//72157

	//72391 <
	public static final char DATA_CONSEGNA_CFM = '0';
	public static final char DATA_CONSEGNA_RCS = '1';

	@SuppressWarnings("rawtypes")
	public Date getMaxDataOrNull(char tipoData) {
		Date dt = null;
		Iterator iterRighe = getTestata().getRighe().iterator();

		while (iterRighe.hasNext()) {
			YOrdineVenditaRigaPrm riga = (YOrdineVenditaRigaPrm) iterRighe.next();
			if((riga.getTipoRiga() == TipoRiga.MERCE 
					||riga.getTipoRiga() == TipoRiga.OMAGGIO)) {
				Date data = null;
				switch (tipoData) {
				case DATA_CONSEGNA_CFM:
					data = riga.getDataConsegnaConfermata();
					break;
				case DATA_CONSEGNA_RCS:
					data = riga.getDataConsegnaRichiesta();
					break;
				default:
					break;
				}
				if (data == null) {
					return null;
				}
				if (dt == null || data.after(dt)) {
					dt = data;
				}
			}
		}
		return dt;
	}
	//72391 >

	//72141
	@Override
	public boolean initializeOwnedObjects(boolean result) {
		boolean ret = super.initializeOwnedObjects(result);
		setYCostoUnitario(getCostoUnitario());
		result = iYAllegati.initialize(result); //72247
		return ret;
	}
	//72141

	protected int svuotaCausaleCambioDataConsegna() throws SQLException {
		PreparedStatement ps = cCleanCausaleCambioDtCons.getStatement();
		Database db = ConnectionManager.getCurrentDatabase();
		db.setString(ps, 1, getIdAzienda());
		db.setString(ps, 2, getAnnoDocumento());
		db.setString(ps, 3, getNumeroDocumento());
		db.setString(ps, 4, getNumeroRigaDocumento().toString());
		//ps.execute(); 72272 remmare
		if (ps.executeUpdate() >= 0)
			return 1;
		return -100;
	}

	//72157
	protected int svuotaNoteCambioDataConsegna() throws SQLException {
		PreparedStatement ps = cCleanNoteCambioDtCons.getStatement();
		Database db = ConnectionManager.getCurrentDatabase();
		db.setString(ps, 1, getIdAzienda());
		db.setString(ps, 2, getAnnoDocumento());
		db.setString(ps, 3, getNumeroDocumento());
		db.setString(ps, 4, getNumeroRigaDocumento().toString());
		//ps.execute(); 72272 remmare
		if (ps.executeUpdate() >= 0)
			return 1;
		return -100;
	}
	//72157

	//72272
	protected int aggiornaValoreOrdinatoVA(BigDecimal val) throws SQLException {
		PreparedStatement ps = cUpdateValoreOrdinatoVA.getStatement();
		Database db = ConnectionManager.getCurrentDatabase();
		ps.setBigDecimal(1, val);
		db.setString(ps, 2, getIdAzienda());
		db.setString(ps, 3, getAnnoDocumento());
		db.setString(ps, 4, getNumeroDocumento());
		db.setString(ps, 5, getNumeroRigaDocumento().toString());
		if (ps.executeUpdate() >= 0)
			return 1;
		return -100;
	}
	//72272

	//72273
	protected int aggiornaValoreResiduoVA(BigDecimal val) throws SQLException {
		PreparedStatement ps = cUpdateValoreResiduoVA.getStatement();
		Database db = ConnectionManager.getCurrentDatabase();
		ps.setBigDecimal(1, val);
		db.setString(ps, 2, getIdAzienda());
		db.setString(ps, 3, getAnnoDocumento());
		db.setString(ps, 4, getNumeroDocumento());
		db.setString(ps, 5, getNumeroRigaDocumento().toString());
		if (ps.executeUpdate() >= 0)
			return 1;
		return -100;
	}
	//72273

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errors = super.checkAll(components);
		OrdineVendita tes = (OrdineVendita) getTestata();

		//..Se la data di consegna e' cambiata  ma non ho indicato un codice di cambio causale non permetto la validazione dell'oggetto
		if(isOnDB() && (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
				&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
				|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) { //72242
			DocumentoOrdineRiga ovr = (DocumentoOrdineRiga) iOldRiga;
			if (ovr != null
					&& ((YOrdineVenditaRigaPrm)ovr).getDataConsegnaCfmStorica() != null
					&& !datiUguali(ovr.getDataConsegnaConfermata(), getDataConsegnaConfermata())
					&& getCausaleCambioDataConsegna() == null) {
				errors.add(new ErrorMessage("YT_0000001"));
			}
			if((isQuantitaCambiata() || (ovr != null
					&& !datiUguali(ovr.getDataConsegnaRichiesta(), getDataConsegnaRichiesta())))
					&& getRilascioOrdineProd() == RILASCIO_ORD_PRD_RILASCIATO) {
				errors.add(new ErrorMessage("BAS0000078","Ordine gia' rilasciato in produzione, impossibile cambiare quantita o data consegna richiesta"));
			}
		}

		//72238
		if(getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO
				&& getDataConsegnaConfermata() == null
				&& (getTipoRiga() == TipoRiga.MERCE || getTipoRiga() == TipoRiga.OMAGGIO)
				&& (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() == null 
				|| !tes.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario()))) { //72242
			errors.add(new ErrorMessage("BAS0000078","Non e' possibile rendere la riga definitiva senza data consegna confermata"));
		}
		//72238
		/*ErrorMessage emArtCos = controllaArticoloCosto();
		if(emArtCos != null)
			errors.add(emArtCos);
		 */

		return errors;
	}

	//72269
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ErrorMessage checkQuadraturaLotti() {
		Articolo articolo = getArticolo();
		if (articolo != null && articolo.isArticLotto()) {
			if (isOnDB()) {
				// 1) Trovo l'ultimo lotto "reale" (non dummy)
				OrdineRigaLotto ultimoLottoReale = null;
				OrdineRigaLotto rigaDummy = null;
				for (Object obj : getRigheLotto()) {
					OrdineRigaLotto orl = (OrdineRigaLotto) obj;
					if (orl != null && orl.getIdLotto() != null && !Lotto.LOTTO_DUMMY.equals(orl.getIdLotto())) {
						ultimoLottoReale = orl;
					}
					if (orl != null && orl.getIdLotto() != null && Lotto.LOTTO_DUMMY.equals(orl.getIdLotto())) {
						rigaDummy = orl;
					}
				}

				// Se non ho lotti reali, non posso fare quadratura qui
				if (ultimoLottoReale == null) {
					return super.checkQuadraturaLotti();
				}

				ArrayList totQtaLotto = caricaQuantitaLotto(getRigheLotto());

				if (rigaDummy != null) {
					BigDecimal totQtaLottoUmRif = (BigDecimal) totQtaLotto.get(0);
					totQtaLottoUmRif = totQtaLottoUmRif.subtract(rigaDummy.getQtaInUMRif());
					totQtaLotto.set(0, totQtaLottoUmRif);

					BigDecimal totQtaLottoUmPrmMag = (BigDecimal) totQtaLotto.get(1);
					totQtaLottoUmPrmMag = totQtaLottoUmPrmMag.subtract(rigaDummy.getQtaInUMPrmMag());
					totQtaLotto.set(1, totQtaLottoUmPrmMag);
				}

				BigDecimal qtaInUMRif = getQuantitaOrdinata().getQuantitaInUMRif();
				BigDecimal totQtaVenLotto = (BigDecimal)(totQtaLotto.get(0));

				if(totQtaVenLotto.compareTo(qtaInUMRif) != 0) {
					if(totQtaVenLotto.compareTo(qtaInUMRif) > 0) {
						BigDecimal sommaLottiPrecedenti = totQtaVenLotto.subtract(ultimoLottoReale.getQtaInUMRif());
						BigDecimal qtaAQuadratura = qtaInUMRif.subtract(sommaLottiPrecedenti);
						if (qtaAQuadratura.compareTo(BigDecimal.ZERO) <= 0) {
							return new ErrorMessage("BAS0000078","La quadratura dell'ultimo lotto e' 0 o negativa, sistemare a mano");
						}
						ultimoLottoReale.getQuantitaOrdinata().setQuantitaInUMRif(qtaAQuadratura);
						ultimoLottoReale.getQuantitaOrdinata().setQuantitaInUMPrm(ultimoLottoReale.getArticolo().convertiUM(qtaAQuadratura, getUMRif(), getUMPrm(), getArticoloVersRichiesta()));
						if(rigaDummy != null) {
							getRigheLotto().remove(rigaDummy);
						}
					}else if(totQtaVenLotto.compareTo(qtaInUMRif) < 0) {
						if(rigaDummy != null) {
							rigaDummy.setQtaInUMRif(qtaInUMRif.subtract(totQtaVenLotto));
							rigaDummy.getQuantitaOrdinata().setQuantitaInUMPrm(ultimoLottoReale.getArticolo().convertiUM(qtaInUMRif.subtract(totQtaVenLotto), getUMRif(), getUMPrm(), getArticoloVersRichiesta()));
						}else {
							OrdineVenditaRigaLotto ltDummy = creaLottiDummy(totQtaVenLotto, totQtaVenLotto, totQtaVenLotto);
							ltDummy.setQtaInUMRif(qtaInUMRif.subtract(totQtaVenLotto));
							ltDummy.getQuantitaOrdinata().setQuantitaInUMPrm(ultimoLottoReale.getArticolo().convertiUM(qtaInUMRif.subtract(totQtaVenLotto), getUMRif(), getUMPrm(), getArticoloVersRichiesta()));
							getRigheLotto().add(ltDummy);
						}
					}
				}
			}
		}
		return super.checkQuadraturaLotti();
	}
	//72269

	//72141
	public ErrorMessage controllaArticoloCosto() {
		Integer idVersione = (getArticoloVersRichiesta() == null) ? ContenitoreRiga.VERSIONE_DUMMY : getArticoloVersRichiesta().getIdVersione();
		Integer idConfigurazione = (getConfigurazione() == null) ? null : getConfigurazione().getIdConfigurazione();
		ArticoloCosto articoloCosto =
				ArticoloCosto.elementWithKey(
						Azienda.getAziendaCorrente(),
						getIdArticolo(),
						idVersione,
						idConfigurazione,
						PersDatiVen.getCurrentPersDatiVen().getTipoCosto() != null ? PersDatiVen.getCurrentPersDatiVen().getTipoCosto().getIdTipoCosto() : ""
						);
		if (articoloCosto != null 
				&& articoloCosto instanceof YArticoloCosto
				&& ((YArticoloCosto)articoloCosto).getRilevatoErrore()
				&& ((YArticoloCosto)articoloCosto).getMessaggioErrore() != null) {
			return new ErrorMessage(GestioneCalcoloCosti.YT_0000003,new String[] {((YArticoloCosto)articoloCosto).getMessaggioErrore()});
		}
		return null;
	}
	//72141

	@SuppressWarnings("rawtypes")
	public ArrayList caricaQuantitaLottoPublic(List righeLotto) {
		return caricaQuantitaLotto(righeLotto);
	}

	//72247
	@SuppressWarnings("rawtypes")
	public ErrorMessage checkObbligatorietaAllegati(char vincolo) {
		ErrorMessage em = null;
		Iterator iterAllegati = getYAllegati().iterator();
		while(iterAllegati.hasNext()) {
			YAllegatiOrdVenRigPrm allegato = (YAllegatiOrdVenRigPrm) iterAllegati.next();
			if(allegato.getVincolo() == vincolo) {
				boolean allegatoPresente = allegato.presenzaAllegato();
				if(!allegatoPresente) {
					return new ErrorMessage("BAS0000078",allegato.getAssociazionedocumento().getTipodocdgt().getDescrizione().getDescrizione()+" - " +allegato.descrizioneVincolo());
				}
			}
		}
		return em;
	}
	//72247

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YOrdineVenditaRigaPrm yOrdineVenditaRigaPrm = (YOrdineVenditaRigaPrm) obj;
		if (yOrdineVenditaRigaPrm.iDataConsegnaCfmStorica != null)
			iDataConsegnaCfmStorica = (java.sql.Date) yOrdineVenditaRigaPrm.iDataConsegnaCfmStorica.clone();
		iCausaleCambioDataConsegna.setEqual(yOrdineVenditaRigaPrm.iCausaleCambioDataConsegna);
		iAreaApplicativa.setEqual(yOrdineVenditaRigaPrm.iAreaApplicativa);
		iYAllegati.setEqual(yOrdineVenditaRigaPrm.iYAllegati);
	}

	//72425 <
	public static boolean isCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria(OrdineVendita ov) {
		if(ov != null && getListaCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria().contains(ov.getIdCau())) {
			return true;
		}
		return false;
	}

	public static List<String> getListaCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria() {
		List<String> list = new ArrayList<String>();
		String val = getValoreParametroCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria();
		if(val != null && !val.isEmpty()) {
			if(val.contains("@")) {
				list.addAll(Arrays.asList(val.split("@")));
			}else {
				list.add(val.trim());
			}
		}
		return list;
	}

	private static String getValoreParametroCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria() {
		return ParametroPsn.getValoreParametroPsn("pers.vendite.ordineVE.OrdineVenditaRigaPrm", "CausaliAreaAppNonObbl");
	}
	//72425 >

}