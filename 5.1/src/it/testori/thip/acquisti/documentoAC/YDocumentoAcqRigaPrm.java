package it.testori.thip.acquisti.documentoAC;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.PersistentObject;

import it.testori.thip.base.articolo.YArticolo;
import it.testori.thip.base.articolo.YArticoloDatiMagaz;
import it.testori.thip.magazzino.generalemag.CreaLottiTestoriUtils;
import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaLottoPrm;
import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaLottoSec;
import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaPrm;
import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaSec;
import it.thera.thip.base.articolo.ArticoloDatiMagaz;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRiga;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRigaLotto;
import it.thera.thip.base.comuniVenAcq.DocumentoRigaLotto;
import it.thera.thip.base.comuniVenAcq.OrdineRigaLotto;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.cs.ThipException;
import it.thera.thip.datiTecnici.distinta.EsplosioneNodo;
import it.thera.thip.datiTecnici.modpro.EspNodoArticoloBase;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.magazzino.generalemag.PersDatiMagazzino;
import it.thera.thip.magazzino.movimenti.MovimentoMagazzino;
import it.thera.thip.magazzino.saldi.SaldoMagLotto;
import it.thera.thip.produzione.ordese.AttivitaEsecMateriale;
import it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori;

/**
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 04/06/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 71992    04/06/2025  DSSOF3   Prima stesura
 * 72014	26/06/2025	DSSOF3	 Gestione lotti pezze c/lav
 * 72018	27/06/2025	DSSOF3	 Rimozione settaggio riga provvisoria
 * 72320	28/01/2026	DSSOF3	 Generazione riga secondaria da modello, se su articolo ce' flag 'Floor stock C/Lav' allora non genero la riga, conto lavoro pezze, gestione lotti in caso di lotto subbio inesistente.
 */

public class YDocumentoAcqRigaPrm extends DocumentoAcqRigaPrm {

	protected boolean iGeneraLottiTsAuto; //72014

	protected String iChiaviLottiMultSel;

	public boolean isGeneraLottiTsAuto() {
		return iGeneraLottiTsAuto;
	}

	public void setGeneraLottiTsAuto(boolean iGeneraLottiTsAuto) {
		this.iGeneraLottiTsAuto = iGeneraLottiTsAuto;
	}

	public String getChiaviLottiMultSel() {
		return iChiaviLottiMultSel;
	}

	public void setChiaviLottiMultSel(String iChiaviLottiMultipliSelezionati) {
		this.iChiaviLottiMultSel = iChiaviLottiMultipliSelezionati;
	}

	public YDocumentoAcqRigaPrm() {
		setGeneraLottiTsAuto(false);
	}

	@Override
	public int save() throws SQLException {
		if(isOnDB() && isGeneraLottiTsAuto()) {
			creaLottiAutomaticiTestori();
		}
		if(getChiaviLottiMultSel() != null)
			popolaLottiSelezioneMultipla();
		int rc = super.save();
		return rc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void popolaLottiSelezioneMultipla() {
		OrdineRigaLotto lottoDummy = null;
		ArrayList totQtaLotti = (ArrayList) caricaLeQuantitaLotto(getRigheLotto());
		ArrayList righeLotto = (ArrayList)getRigheLotto();
		((ArrayList<DocumentoAcqRigaLottoPrm>)righeLotto).stream().filter(r -> Lotto.LOTTO_DUMMY.equals((r.getIdLotto()))).findFirst().ifPresent(righeLotto::remove);
		BigDecimal totQtaPrmMagLotti = (BigDecimal)(totQtaLotti.get(1));
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
							DocumentoAcqRigaLottoPrm rigaLotto = (DocumentoAcqRigaLottoPrm)creaLotto();
							rigaLotto.setFather(this);
							rigaLotto.setLotto(lt);
							rigaLotto.setIdArticolo(getIdArticolo());

							rigaLotto.setQtaInUMPrm(giacenza);
							rigaLotto.setQtaInUMAcq(giacenza);

							totQtaPrmMagLotti = totQtaPrmMagLotti.add(giacenza);

							if (totQtaPrmMagLotti.compareTo(getServizioQta().getQuantitaInUMPrm()) > 0) {
								BigDecimal eccedenza = totQtaPrmMagLotti.subtract(getServizioQta().getQuantitaInUMPrm());
								//1. Riduco il dummy
								if (eccedenza.compareTo(BigDecimal.ZERO) > 0) {
									if (lottoDummy != null) {
										BigDecimal qta = lottoDummy.getQtaInUMPrmMag();
										if (qta.compareTo(BigDecimal.ONE) > 0) {
											BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
											lottoDummy.setQtaInUMPrmMag(qta.subtract(riduzione));
											lottoDummy.setQtaInUMRif(lottoDummy.getQtaInUMPrmMag());
											eccedenza = eccedenza.subtract(riduzione);
										}
									}
								}
								//2. Riduco i lotti già presenti (NON dummy)
								for (DocumentoAcqRigaLottoPrm r : (List<DocumentoAcqRigaLottoPrm>) righeLotto) {
									if (eccedenza.compareTo(BigDecimal.ZERO) <= 0) break;
									if (!Lotto.LOTTO_DUMMY.equals(r.getIdLotto())) {
										BigDecimal qta = r.getQtaInUMPrm();
										if (qta.compareTo(BigDecimal.ONE) > 0) {
											BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
											r.setQtaInUMPrm(qta.subtract(riduzione));
											r.setQtaInUMAcq(r.getQtaInUMPrm());
											eccedenza = eccedenza.subtract(riduzione);
										}
									}
								}
								//3. Riduco anche il lotto corrente (quello che stai aggiungendo)
								if (eccedenza.compareTo(BigDecimal.ZERO) > 0) {
									BigDecimal qta = rigaLotto.getQtaInUMPrm();
									if (qta.compareTo(BigDecimal.ONE) > 0) {
										BigDecimal riduzione = qta.subtract(BigDecimal.ONE).min(eccedenza);
										rigaLotto.setQtaInUMPrm(qta.subtract(riduzione));
										rigaLotto.setQtaInUMAcq(rigaLotto.getQtaInUMPrm());
										eccedenza = eccedenza.subtract(riduzione);
									}
								}
								// 4. Se ancora non basta errore
								if (eccedenza.compareTo(BigDecimal.ZERO) > 0) {
									throw new IllegalStateException("Impossibile riallineare le quantità dei lotti");
								}
								// Ricalcolo totale corretto
								totQtaPrmMagLotti = getServizioQta().getQuantitaInUMPrm();
							}
							getRigheLotto().add(rigaLotto);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		if(lottoDummy != null && totQtaPrmMagLotti.compareTo(getServizioQta().getQuantitaInUMPrm()) == 0) {
			getRigheLotto().remove(lottoDummy);
		}
	}

	@Override
	public boolean hasGestioneAFloorStock(AttivitaEsecMateriale attMateriale) {
		boolean hasGestioneAFloorStock = super.hasGestioneAFloorStock(attMateriale);
		//72320 <
		if(attMateriale != null
				&& attMateriale.getArticolo() != null
				&& ((YArticolo)attMateriale.getArticolo()).isFloorStockContoLavoro()) {
			hasGestioneAFloorStock = true;
		}
		//72320 >
		return hasGestioneAFloorStock;
	}

	@Override
	protected DocumentoOrdineRiga generaRigaSecondaria(EsplosioneNodo datiRigaSec, int sequenza) throws SQLException {
		DocumentoOrdineRiga rigaSec = super.generaRigaSecondaria(datiRigaSec, sequenza);
		//72320 <
		if(rigaSec != null
				&& rigaSec.getArticolo() != null
				&& ((YArticolo)rigaSec.getArticolo()).isFloorStockContoLavoro()) {
			rigaSec = null;
		}
		//72320 >
		return rigaSec;
	}

	@Override
	public DocumentoOrdineRiga generaRigaSecondariaModello(EspNodoArticoloBase datiRigaSec, int sequenza)
			throws SQLException {
		DocumentoOrdineRiga rigaSec = super.generaRigaSecondariaModello(datiRigaSec, sequenza);
		//72320 <
		if(rigaSec != null
				&& rigaSec.getArticolo() != null
				&& ((YArticolo)rigaSec.getArticolo()).isFloorStockContoLavoro()) {
			rigaSec = null;
		}
		//72320 >
		return rigaSec;
	}

	@Override
	protected void creaLottiAutomatici() {
		char tipoProvenienza = getLavEsterna() ? CreaLottiTestoriUtils.CONTO_LAVORO : CreaLottiTestoriUtils.ACQUISTO;
		if(!CreaLottiTestoriUtils.isArticoloGestioneLottiTestori(getArticolo(), tipoProvenienza)) {
			super.creaLottiAutomatici();
		}else if(CreaLottiTestoriUtils.isArticoloGestioneSubbiFeltri(getArticolo(), tipoProvenienza)
				&& getArticolo().getArticoloDatiMagaz().getGenAutLotAcq() == ArticoloDatiMagaz.CREA_DA_DOCUMENTO) {
			try {
				creaLottiAutomaticiTestori();
			} catch (ThipException e) {
				e.printStackTrace(Trace.excStream);
			}
		}else {
			controllaProponiPresenzaLottoDummy();
			//setStatoAvanzamento(StatoAvanzamento.PROVVISORIO); fix 72018 remmare
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void creaLottiAutomaticiTestori() throws ThipException {
		char tipoProvenienza = getLavEsterna() ? CreaLottiTestoriUtils.CONTO_LAVORO : CreaLottiTestoriUtils.ACQUISTO;
		//.Controllo che sulla riga secondaria sia presente un lotto non DUMMY
		//		boolean trovato = false;
		String idLottoSottoPezza = null; //72320
		if(CreaLottiTestoriUtils.isArticoloGestionePezze(getArticolo(), tipoProvenienza)) {
			Iterator iterRigheSec = getRigheSecondarie().iterator();
			while(iterRigheSec.hasNext()) {
				DocumentoAcqRigaSec rigaSec = (DocumentoAcqRigaSec) iterRigheSec.next();
				Iterator iterLots = rigaSec.getRigheLotto().iterator();
				while(iterLots.hasNext()) {
					DocumentoAcqRigaLottoSec rl = (DocumentoAcqRigaLottoSec) iterLots.next();
					if(!rl.getIdLotto().equals(Lotto.LOTTO_DUMMY)) {
						//						trovato = true;
						idLottoSottoPezza = rl.getIdLotto(); //72320
					}
				}
			}
			//			if(!trovato && tipoProvenienza != CreaLottiTestoriUtils.CONTO_LAVORO) { //72320
			//				throw new ThipException("Per poter creare il lotto della riga primaria va prima indicato il lotto sulla riga secondaria");
			//			}
		}
		CreaLottiTestoriUtils pal = getCreaProposizioneAutLottoTestori();
		//72320 Inizio <
		if(CreaLottiTestoriUtils.isArticoloGestionePezze(getArticolo(), tipoProvenienza)
				/*&& tipoProvenienza == CreaLottiTestoriUtils.CONTO_LAVORO*/) {
			pal.setGeneraCodiceLottoAutomatico(false);
			String idLotto;
			String lottoPrm = null;
			Iterator iterLotti = getRigheLotto().iterator();
			while(iterLotti.hasNext()) {
				DocumentoAcqRigaLottoPrm docAcqRigLotPrm = (DocumentoAcqRigaLottoPrm) iterLotti.next();
				if(!docAcqRigLotPrm.getIdLotto().equals(LOTTO_DUMMY)) {
					lottoPrm = docAcqRigLotPrm.getIdLotto();
					break;
				}
			}
			//..Gestione della creazione lotto per le pezze
			//.. 1 - se e' il primo lotto della riga primaria e ho il codice della sotto pezza (riga secondaria) allora compongo il codice con quella sotto pezza
			//.. 2 - se non e' il primo lotto della riga primaria allora calcolo il prossimo progressivo partendo da quel lotto
			//.. 3 - se e' il primo lotto della riga primaria ma non ho il codice della sotto pezza genero il primo lotto attaccandoci 01
			try {
				DocumentoOrdineRigaLotto unicoLottoEff = getUnicoLottoEffettivo();
				if(unicoLottoEff == null && idLottoSottoPezza != null) {
					if(idLottoSottoPezza.length() > 8) {
						idLottoSottoPezza = idLottoSottoPezza.substring(0,8);
					}
					String nuovoProgressivo = CreaLottiTestoriUtils.getMaxProgressivoPezzeFromLotto(getIdAzienda(), idLottoSottoPezza);
					idLotto = idLottoSottoPezza + nuovoProgressivo;
					pal.setIdLotto(idLotto);
				}else if(lottoPrm != null) {
					if(lottoPrm.length() > 8) {
						lottoPrm = lottoPrm.substring(0,8);
					}
					String nuovoProgressivo = CreaLottiTestoriUtils.getMaxProgressivoPezzeFromLotto(getIdAzienda(), lottoPrm);
					pal.setIdLotto(lottoPrm + nuovoProgressivo);
				}else {
					idLotto = CreaLottiTestoriUtils.buildNextIdProgressivoLotto(YArticoloDatiMagaz.SUBBIO, tipoProvenienza);
					idLotto = idLotto + String.format("%0" + 2 + "d", 1);
					pal.setIdLotto(idLotto);
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		//72320 Fine >
		List lottiAuto = pal.creaLottiAutomatici();
		if(lottiAuto != null && !lottiAuto.isEmpty()) {
			getRigheLotto().clear();  
			for (int j = 0; j < lottiAuto.size(); j++) {
				Lotto lt = (Lotto)lottiAuto.get(j);
				DocumentoAcqRigaLottoPrm lotto = (DocumentoAcqRigaLottoPrm)Factory.createObject(DocumentoAcqRigaLottoPrm.class);
				lotto.setFather(this);
				lotto.setIdAzienda(getIdAzienda());
				lotto.setIdArticolo(lt.getCodiceArticolo());
				lotto.setIdLotto(lt.getCodiceLotto());
				lotto.setQtaAttesaEvasione(lotto.getQtaAttesaEvasione().add(getQtaAttesaEvasione()));
				lotto.setQtaPropostaEvasione(lotto.getQtaPropostaEvasione().add(getQtaPropostaEvasione()));
				getRigheLotto().add(lotto);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public CreaLottiTestoriUtils getCreaProposizioneAutLottoTestori() {
		BigDecimal qta = new BigDecimal("0");
		if (getStatoAvanzamento() == StatoAvanzamento.PROVVISORIO && getQtaPropostaEvasione() != null){
			qta = getQtaPropostaEvasione().getQuantitaInUMPrm();
		}
		if (getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO && getQtaAttesaEvasione() != null){
			qta = getQtaAttesaEvasione().getQuantitaInUMPrm();
		}
		List lottiOrig = new ArrayList();
		char tipo = PersDatiMagazzino.TIPO_ACQ;
		if(getCausaleRiga().isLavEsterna())
			tipo = PersDatiMagazzino.TIPO_CL;
		return CreaLottiTestoriUtils.creaProposizioneAutLotto(tipo,
				getNumeroDocumento(),
				getAnnoDocumento(),
				getTestata().getDataDocumento(),
				getNumeroRigaDocumento(),
				null,
				getIdArticolo(),
				getIdVersioneSal(),
				getIdEsternoConfig(),
				getIdMagazzino(),
				getIdCommessa(),
				getIdFornitore(),
				PersDatiMagazzino.CREA_DA_DOCUMENTO,
				lottiOrig,
				null,
				null,
				qta);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void controllaProponiPresenzaLottoDummy() {
		Iterator i = getRigheLotto().iterator();
		boolean lottoDummyPresente = false;
		DocumentoRigaLotto lottoDummy = null;
		QuantitaInUMRif totaleQtaAttesaLotti = (QuantitaInUMRif)Factory.createObject(QuantitaInUMRif.class);
		QuantitaInUMRif totaleQtaPropostaLotti = (QuantitaInUMRif)Factory.createObject(QuantitaInUMRif.class);
		totaleQtaAttesaLotti.azzera();
		totaleQtaPropostaLotti.azzera();
		boolean quadratura = true;

		while (i.hasNext())
		{
			DocumentoRigaLotto rigaLotto = (DocumentoRigaLotto)i.next();
			if (rigaLotto!=null){
				if (rigaLotto.getIdLotto().equals(LOTTO_DUMMY))
				{
					lottoDummyPresente = true;
					lottoDummy = rigaLotto;
				}
				else {
					if (rigaLotto.getQtaAttesaEvasione().getQuantitaInUMPrm()!=null)
						totaleQtaAttesaLotti=totaleQtaAttesaLotti.add(rigaLotto.getQtaAttesaEvasione());
					if(rigaLotto.getQtaPropostaEvasione().getQuantitaInUMPrm()!=null)
						totaleQtaPropostaLotti=totaleQtaPropostaLotti.add(rigaLotto.getQtaPropostaEvasione());
				}
			}
		}
		quadratura = controllaQuadraturaQtaLotti(totaleQtaPropostaLotti, totaleQtaAttesaLotti);
		if (getRigheLotto().isEmpty() && quadratura &&
				getServizioQta().getQuantitaInUMRif().compareTo(ZERO_DEC) == 0){
			quadratura = false;
		}

		if (!quadratura){
			DocumentoRigaLotto lottoEffettivo = (DocumentoRigaLotto)getUnicoLottoEffettivo();
			if (lottoEffettivo != null && !getArticolo().getArticoloDatiMagaz().isLottoUnitario()){
				if (this.getStatoAvanzamento()==StatoAvanzamento.PROVVISORIO){
					lottoEffettivo.setQtaPropostaEvasione(getQtaPropostaEvasione());
				}
				if (this.getStatoAvanzamento()==StatoAvanzamento.DEFINITIVO){
					lottoEffettivo.setQtaAttesaEvasione(getQtaAttesaEvasione());
				}
			}
			else{
				if (!lottoDummyPresente){
					lottoDummy = this.creaLottoDummy();
				}
				if (this.getStatoAvanzamento()==StatoAvanzamento.PROVVISORIO &&
						(!totaleQtaPropostaLotti.isZero() || lottoDummy.getQtaPropostaEvasione().compareTo(getQtaPropostaEvasione())!=0 ||
						(this.getOldRiga()!=null && getStatoAvanzamento()!=this.getOldRiga().getStatoAvanzamento()))){
					lottoDummy.setQtaPropostaEvasione(getQtaPropostaEvasione().subtract(totaleQtaPropostaLotti));
				}
				if (this.getStatoAvanzamento()==StatoAvanzamento.DEFINITIVO &&
						(!totaleQtaAttesaLotti.isZero() || lottoDummy.getQtaAttesaEvasione().compareTo(getQtaAttesaEvasione())!=0 ||
						(this.getOldRiga()!=null && this.getStatoAvanzamento()!=this.getOldRiga().getStatoAvanzamento()))){
					lottoDummy.setQtaAttesaEvasione(getQtaAttesaEvasione().subtract(totaleQtaAttesaLotti));
				}
				if (!lottoDummyPresente)
					getRigheLotto().add(lottoDummy);
			}
		}else {
			getRigheLotto().remove(lottoDummy);
		}
	}
}
