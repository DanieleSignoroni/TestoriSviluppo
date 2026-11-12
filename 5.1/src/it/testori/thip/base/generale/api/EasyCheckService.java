package it.testori.thip.base.generale.api;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.rs.errors.ErrorUtils;
import com.thera.thermfw.rs.errors.PantheraApiException;
import com.thera.thermfw.web.WebForm;

import it.testori.thip.base.articolo.YArticoloDatiMagaz;
import it.testori.thip.base.generale.YVariabiliApplicazione;
import it.testori.thip.easycheck.InterfacciaEasyCheck;
import it.testori.thip.easycheck.PezzaGreggiaField;
import it.testori.thip.easycheck.PezzaLavorata;
import it.testori.thip.logis.bas.YCostantiTestori;
import it.testori.thip.magazzino.generalemag.CreaLottiTestoriUtils;
import it.testori.thip.magazzino.generalemag.YLotto;
import it.testori.thip.produzione.ordese.TipoTaglioPezza;
import it.testori.thip.produzione.ordese.YAttivitaEsecMateriale;
import it.testori.thip.produzione.ordese.YAttivitaEsecProdotto;
import it.testori.thip.produzione.ordese.YAttivitaEsecutiva;
import it.testori.thip.vendite.documentoVE.web.YRecuperaQuantitaLotto;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.dipendente.Dipendente;
import it.thera.thip.base.dipendente.DipendenteTM;
import it.thera.thip.base.generale.NumeratoreException;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.magazzino.movimenti.MovimentoMagazzino;
import it.thera.thip.magazzino.saldi.SaldoMagLotto;
import it.thera.thip.produzione.documento.CausaleDocProduzione;
import it.thera.thip.produzione.documento.CMDocPrdEsec;
import it.thera.thip.produzione.documento.DocumentoPrdRigaLottoMat;
import it.thera.thip.produzione.documento.DocumentoPrdRigaLottoMatTM;
import it.thera.thip.produzione.documento.DocumentoPrdRigaLottoVrs;
import it.thera.thip.produzione.documento.DocumentoPrdRigaMateriale;
import it.thera.thip.produzione.documento.DocumentoPrdRigaMaterialeTM;
import it.thera.thip.produzione.documento.DocumentoPrdRigaVersamento;
import it.thera.thip.produzione.documento.DocumentoProduzione;
import it.thera.thip.produzione.documento.DocumentoProduzioneTM;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiMat;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiPrd;
import it.thera.thip.produzione.ordese.AttivitaEsecMateriale;
import it.thera.thip.produzione.ordese.AttivitaEsecProdotto;
import it.thera.thip.produzione.ordese.AttivitaEsecutiva;
import it.thera.thip.produzione.ordese.AttivitaEsecutivaTM;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;

/**
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 28/11/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72236    28/11/2025  DSSOF3   Prima stesura
 * 72273	08/01/2026	DSSOF3	 Modifica gestione note
 * 72320	28/01/2026	DSSOF3	 Modifica gestione prelievo, permettere la consuntivazione di piu pezze greggie.
 * 72467	05/05/2026	DSSOF3   Se per il lotto materiale non c'e' giacenza nel magazzino di prelievo ma su un altro magazzino prelevo comunque.
 */

public class EasyCheckService {

	//72320 <
	private static final String STMT_PEZZA_GREGGIA_SCARICATA = "SELECT "
			+ "	DP.* "
			+ "FROM "
			+ "	"+DocumentoProduzioneTM.TABLE_NAME+" DP "
			+ "INNER JOIN "+DocumentoPrdRigaMaterialeTM.TABLE_NAME+" DPM "
			+ "ON "
			+ "	DP."+DocumentoProduzioneTM.ID_AZIENDA+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_AZIENDA+" "
			+ "	AND DP."+DocumentoProduzioneTM.ID_ANNO_DOC+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_ANNO_DOC+" "
			+ "	AND DP."+DocumentoProduzioneTM.ID_NUMERO_DOC+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_NUMERO_DOC+" "
			+ "INNER JOIN "+DocumentoPrdRigaLottoMatTM.TABLE_NAME+" DPL "
			+ "ON  "
			+ "	DPL."+DocumentoPrdRigaLottoMatTM.ID_AZIENDA+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_AZIENDA+" "
			+ "	AND DPL."+DocumentoPrdRigaLottoMatTM.ID_ANNO_DOC+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_ANNO_DOC+" "
			+ "	AND DPL."+DocumentoPrdRigaLottoMatTM.ID_NUMERO_DOC+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_NUMERO_DOC+" "
			+ "	AND DPL."+DocumentoPrdRigaLottoMatTM.ID_RIGA_DOC+" = DPM."+DocumentoPrdRigaMaterialeTM.ID_RIGA_DOC+" "
			+ "WHERE "
			+ "	DP."+DocumentoProduzioneTM.ID_AZIENDA+" = ? "
			+ "	AND DP."+DocumentoProduzioneTM.NUM_RITORNO+" = ? "
			+ "	AND DP."+DocumentoProduzioneTM.R_CAU_DOC_PRD+" = ? "
			+ "	AND DPL."+DocumentoPrdRigaLottoMatTM.ID_LOTTO+" = ? ";
	public CachedStatement cPezzaGreggiaScaricata = new CachedStatement(STMT_PEZZA_GREGGIA_SCARICATA);
	//72320 >

	private static EasyCheckService instance;

	public static EasyCheckService getInstance() {
		if(instance == null) {
			instance = (EasyCheckService) Factory.createObject(EasyCheckService.class);
		}
		return instance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject riceviPezzaLavorata(JSONObject payload) {
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		Status status = Status.OK;
		Collection<ErrorMessage> errors = new ArrayList<>();

		PezzaLavorata pezza = PezzaLavorata.fromJson(payload, errors);
		if (!errors.isEmpty()) {
			status = Status.BAD_REQUEST;
		} else {
			String bollaLavorazione = pezza.getProductionOrder();
			AttivitaEsecutiva atvEsec = leggiAtvEsec(bollaLavorazione);
			InterfacciaEasyCheck interfaceEasyCheck = InterfacciaEasyCheck.getCurrentInterfacciaEasyCheck();
			if(atvEsec != null
					//&& atvEsec.getStatoAttivita() != AttivitaEsecutiva.COMPLETATO_ATV
					&& interfaceEasyCheck != null) {
				AttivitaEsecProdotto prodotto = atvEsec.getAtvEsecPrdPrimario();
				if(prodotto != null) {
					try {
						DocumentoProduzione docPrdPrel = esisteDocumentoProd(interfaceEasyCheck.getIdCausaleDocProdPrl(), bollaLavorazione, pezza.getRawPieceCode());
						if(docPrdPrel == null) {
							AttivitaEsecMateriale materiale = null;
							if(atvEsec.getMateriali().size() > 0)
								materiale = ((AttivitaEsecMateriale) atvEsec.getMateriali().get(0));
							else
								throw new PantheraApiException(Status.BAD_REQUEST,
										new ErrorMessage("BAS0000078","Materiale non presente in attivita' esecutiva"));
							Lotto lottoMateriale = getLotto(materiale.getIdArticolo(), pezza.getRawPieceCode());
							if(lottoMateriale != null) {
								BigDecimal qtaPrel = BigDecimal.ZERO;
								List saldi = YRecuperaQuantitaLotto.getSaldiMag(Azienda.getAziendaCorrente(), materiale.getIdMagazzinoPrl(), 
										materiale.getIdArticolo(), materiale.getIdVersione(), materiale.getIdConfigurazione(), lottoMateriale.getCodiceLotto(),
										null, MovimentoMagazzino.LOTTO);
								if(saldi.size() > 0) {
									SaldoMagLotto saldoMagLotto = (SaldoMagLotto) saldi.get(0);
									qtaPrel = saldoMagLotto.getDatiSaldo().getQtaGiacenzaUMPrim();
								}
								//72467 <
								if(qtaPrel == null || qtaPrel.compareTo(BigDecimal.ZERO) == 0) {
									saldi = YRecuperaQuantitaLotto.getSaldiMag(Azienda.getAziendaCorrente(), null, 
											materiale.getIdArticolo(), materiale.getIdVersione(), materiale.getIdConfigurazione(), lottoMateriale.getCodiceLotto(),
											null, MovimentoMagazzino.LOTTO);
									Iterator iterSaldi = saldi.iterator();
									while(iterSaldi.hasNext()) {
										SaldoMagLotto saldoMagLotto = (SaldoMagLotto) iterSaldi.next();
										if(saldoMagLotto.getDatiSaldo().getQtaGiacenzaUMPrim() != null
												&& saldoMagLotto.getDatiSaldo().getQtaGiacenzaUMPrim().compareTo(BigDecimal.ZERO) > 0) {
											qtaPrel = saldoMagLotto.getDatiSaldo().getQtaGiacenzaUMPrim();
										}
									}
								}
								//72467 >
								AttivitaEsecLottiMat atvEsecLtMat = ((YAttivitaEsecMateriale)materiale).generaNuovoLottoMateriale(lottoMateriale);
								atvEsecLtMat.setQtaRichiestaUMPrm(qtaPrel);

								materiale.getLottiMateriali().add(atvEsecLtMat);

								docPrdPrel = creaDocumentoProduzioneEasyCheck(atvEsec.getOrdineEsecutivo(), atvEsec, qtaPrel, BigDecimal.ZERO, null, null, null, true);
								docPrdPrel.setMinutiRilevati(BigDecimal.ONE);
								docPrdPrel.caricaRighe(DatiComuniEstesi.INCOMPLETO);
								//docPrdPrel.creaRigheLottoMateriale(materiale, (DocumentoPrdRigaMateriale) docPrdPrel.getMaterialiColl().get(0)); 72320 rem

								//72320 inizio <
								DocumentoPrdRigaMateriale rigaPrl = (DocumentoPrdRigaMateriale) docPrdPrel.getMaterialiColl().get(0);

								rigaPrl.getLottiColl().clear();

								DocumentoPrdRigaLottoMat lotto = (DocumentoPrdRigaLottoMat) Factory.createObject(DocumentoPrdRigaLottoMat.class);
								lotto.setIdAzienda(rigaPrl.getIdAzienda());
								lotto.setIdAnnoDoc(rigaPrl.getIdAnnoDoc());
								lotto.setIdNumeroDoc(rigaPrl.getIdNumeroDoc());
								lotto.setIdRigaDoc(rigaPrl.getIdRigaDoc());
								lotto.setIdArticolo(atvEsecLtMat.getIdArticolo());
								lotto.setIdLotto(atvEsecLtMat.getIdLotto());
								lotto.setRUmPrmMag(atvEsecLtMat.getLotto().getArticolo().getIdUMPrmMag());
								lotto.setRUmSecMag(atvEsecLtMat.getLotto().getArticolo().getIdUMSecMag());
								lotto.setQtaPrlUmPrm(qtaPrel);

								rigaPrl.getLottiColl().add(lotto);
								//72320 fine >

								//docPrdPrel.setMinutiRilevati(BigDecimal.ONE);
								Dipendente dipByOpCode = recuperaDipendenteByOperatorCode(pezza.getOperatorCode());
								if(dipByOpCode != null)
									docPrdPrel.setDichiarante(dipByOpCode);

								if(!salvaEConvalidaDocumentoProduzione(docPrdPrel, null, errors)) {
									status = Status.BAD_REQUEST;
								}
							}
						}
						if(status == Status.OK && ErrorUtils.getInstance().areAllWarnings(new Vector<ErrorMessage>(errors))) {
							// La convalida del prelievo ha aggiornato l'ordine su un'altra connessione.
							String attivitaKey = atvEsec.getKey();
							atvEsec = (AttivitaEsecutiva) Factory.createObject(AttivitaEsecutiva.class);
							atvEsec.setKey(attivitaKey);
							if(!atvEsec.retrieve()) {
								throw new SQLException("Attivita esecutiva non trovata dopo il prelievo: "+attivitaKey);
							}
							prodotto = atvEsec.getAtvEsecPrdPrimario();
							if(prodotto == null) {
								throw new SQLException("Prodotto primario non trovato dopo il prelievo: "+attivitaKey);
							}
							//..Carico il lotto (pezza specolata) sul prodotto, se non esiste lo creo
							BODataCollector boDCNewLt = generaLottoPezzaLavorata(pezza, prodotto);
							if(boDCNewLt.getErrorList().getErrors().isEmpty()) {
								Lotto lottoPrd = (Lotto) boDCNewLt.getBo();
								AttivitaEsecLottiPrd atvEsecLtPrd = ((YAttivitaEsecProdotto)prodotto).generaNuovoLottoProdotto(lottoPrd);
								atvEsecLtPrd.setQtaRichiestaUMPrm(atvEsecLtPrd.getAttivitaEsecProdotto().getQtaRichiestaUMPrm());

								prodotto.getLottiProdotti().add(atvEsecLtPrd);

								//..imposto la quantita' lorda (metri netti + bonificata)
								BigDecimal qtaLorda = pezza.getNetQuantityMeters();
								qtaLorda = qtaLorda.add(pezza.getAllowance() != null ? pezza.getAllowance() : BigDecimal.ZERO);

								DocumentoProduzione docPrd = creaDocumentoProduzioneEasyCheck(atvEsec.getOrdineEsecutivo(), atvEsec, qtaLorda, BigDecimal.ZERO, null, null, null, false);
								docPrd.setMinutiRilevati(BigDecimal.ONE);
								docPrd.setSaldo(DocumentoProduzione.AUTOMATICO);
								docPrd.caricaRighe(DatiComuniEstesi.INCOMPLETO);

								DocumentoPrdRigaVersamento rigaVrs = docPrd.getRigaVersamentoProdottoPrimario();
								//docPrd.creaRigheLottoVersamento(prodotto, rigaVrs);

								//72320 < 
								//svuoto eventuali lotti caricati dallo std, consuntivo sempre e solo quello che mi arriva dalla specola 
								rigaVrs.getLottiColl().clear();
								//72320 >

								DocumentoPrdRigaLottoVrs lotto = (DocumentoPrdRigaLottoVrs) Factory.createObject(DocumentoPrdRigaLottoVrs.class);
								lotto.setIdAzienda(rigaVrs.getIdAzienda());
								lotto.setIdAnnoDoc(rigaVrs.getIdAnnoDoc());
								lotto.setIdNumeroDoc(rigaVrs.getIdNumeroDoc());
								lotto.setIdRigaDoc(rigaVrs.getIdRigaDoc());
								lotto.setIdArticolo(atvEsecLtPrd.getIdArticolo());
								lotto.setIdLotto(atvEsecLtPrd.getIdLotto());
								lotto.setRUmPrmMag(atvEsecLtPrd.getLotto().getArticolo().getIdUMPrmMag());
								lotto.setRUmSecMag(atvEsecLtPrd.getLotto().getArticolo().getIdUMSecMag());
								lotto.setQtaVrsUmPrm(qtaLorda);

								rigaVrs.getLottiColl().add(lotto);

								if(rigaVrs.getLottiColl().size() == 0) {
									throw new PantheraApiException(Status.BAD_REQUEST, new ErrorMessage("BAS0000078","Ordine esecutivo completato non e' possibile ricevere altri lotti, intervenire manualmente"));
								}else {
									DocumentoPrdRigaLottoVrs rigaVrsLt = (DocumentoPrdRigaLottoVrs) rigaVrs.getLottiColl().get(0);
									rigaVrsLt.setQtaVrsUmPrm(qtaLorda);
								}

								//docPrd.setMinutiRilevati(BigDecimal.ONE);
								Dipendente dipByOpCode = recuperaDipendenteByOperatorCode(pezza.getOperatorCode());
								if(dipByOpCode != null)
									docPrd.setDichiarante(dipByOpCode);

								if(salvaEConvalidaDocumentoProduzione(docPrd, lottoPrd, errors)) {
									result.put("message", "Creato correttamente il documento di produzione : "+docPrd.getNumeroDocFormattato());
								}else {
									status = Status.BAD_REQUEST;
								}
							}else {
								errors.addAll(boDCNewLt.getErrorList().getErrors());
								status = Status.BAD_REQUEST;
							}
						}
					}catch (SQLException e) {
						errors.add(new ErrorMessage("BAS0000078","Eccezione non gestita: "+e.getMessage()));
						e.printStackTrace(Trace.excStream);
						status = Status.INTERNAL_SERVER_ERROR;
					} finally {
						// I documenti sono gia' conclusi singolarmente, come nel caricamento standard.
						// Ripulisce eventuali scritture rimaste sulla connessione chiamante.
						try {
							ConnectionManager.rollback();
						} catch(SQLException rollbackException) {
							rollbackException.printStackTrace(Trace.excStream);
							errors.add(new ErrorMessage("BAS0000078", "Rollback EasyCheck non riuscito: "+rollbackException.getMessage()));
							result.remove("message");
							status = Status.INTERNAL_SERVER_ERROR;
						}
					}
				}else {
					errors.add(new ErrorMessage("BAS0000078","Non e' stato trovato nessuna prodotto primario nell'attivita: "+KeyHelper.formatKeyString(atvEsec.getKey())));
					status = Status.INTERNAL_SERVER_ERROR;
				}

			}else {
				if(atvEsec == null)
					errors.add(new ErrorMessage("BAS0000078","Non e' stata trovata nessuna attivita con codice: "+bollaLavorazione));
				/*else if(atvEsec.getStatoAttivita() == AttivitaEsecutiva.COMPLETATO_ATV)
					errors.add(new ErrorMessage("BAS0000078","L'attivita e' in stato completato"));*/
				else if(interfaceEasyCheck == null)
					errors.add(new ErrorMessage("BAS0000078","Interfaccia Easy Check non definita"));
				status = Status.BAD_REQUEST;
			}
		}

		result.put("errors", ErrorUtils.getInstance().toJSON(errors));
		response.put("response", result);
		response.put("status", status);
		return response;
	}

	/**
	 * Come CMDocPrdEsec: persiste il nuovo documento sospeso prima della convalida.
	 * Il commit rende visibili anche i lotti alla connessione separata.
	 * Se la convalida fallisce, prima annulla i movimenti e poi cancella il documento.
	 */
	@SuppressWarnings("unchecked")
	protected boolean salvaEConvalidaDocumentoProduzione(DocumentoProduzione documento, Lotto lottoProdotto,
			Collection<ErrorMessage> errors) throws SQLException {
		if(documento.isOnDB()) {
			throw new SQLException("La creazione EasyCheck richiede un nuovo documento di produzione: "+documento.getKey());
		}
		documento.getDatiComuniEstesi().setStato(DatiComuniEstesi.SOSPESO);
		BODataCollector boDC = YCostantiTestori.createDataCollector("DocumentoProduzione");
		boDC.setAutoCommit(false);
		boDC.setBo(documento);
		boDC.loadAttValue();
		int ret = boDC.save();
		errors.addAll(boDC.getErrorList().getErrors());
		if(ret != BODataCollector.OK) {
			if(ErrorUtils.getInstance().areAllWarnings(new Vector<ErrorMessage>(errors))) {
				errors.add(new ErrorMessage("BAS0000078", "Documento sospeso non salvato: "+documento.getKey()+" (esito "+ret+")"));
			}
			return false;
		}
		ConnectionManager.commit();

		boolean nuovaConnessione = false;
		boolean convalidato = false;
		Exception erroreConvalida = null;
		try {
			ConnectionManager.pushConnection();
			nuovaConnessione = true;
			// Una nuova istanza rilegge anche le collezioni, senza accedere ai campi protetti Thera.
			DocumentoProduzione daConvalidare = (DocumentoProduzione) Factory.createObject(DocumentoProduzione.class);
			daConvalidare.setKey(documento.getKey());
			if(!daConvalidare.retrieve()) {
				throw new SQLException("Documento sospeso non trovato per la convalida: "+documento.getKey());
			}
			Vector<ErrorMessage> erroriConvalida = new Vector<ErrorMessage>();
			erroriConvalida.addAll(daConvalidare.cancellaRigheQtaZero());
			if(ErrorUtils.getInstance().areAllWarnings(erroriConvalida)) {
				erroriConvalida.addAll(daConvalidare.passaggioStatoSospesoValido());
			}
			errors.addAll(erroriConvalida);
			if(ErrorUtils.getInstance().areAllWarnings(erroriConvalida)) {
				if(daConvalidare.save() < 0) {
					throw new SQLException("Documento convalidato non salvato: "+documento.getKey());
				}
				if(lottoProdotto != null) {
					// Rilegge il lotto: la convalida puo' averne aggiornato dati e timestamp.
					Lotto lottoDaAggiornare = (Lotto) Factory.createObject(Lotto.class);
					lottoDaAggiornare.setKey(lottoProdotto.getKey());
					if(!lottoDaAggiornare.retrieve()) {
						throw new SQLException("Lotto non trovato dopo la convalida: "+lottoProdotto.getKey());
					}
					lottoDaAggiornare.setRifDocProd(daConvalidare.getNumeroDocFormattato());
					lottoDaAggiornare.setRifRigaDocProd(daConvalidare.getRigaVersamentoProdottoPrimario().getIdRigaAttivita().toString());
					lottoDaAggiornare.setDataDocProd(daConvalidare.getDataDichiarazione());
					if(lottoDaAggiornare.save() < 0) {
						throw new SQLException("Riferimenti del documento non salvati sul lotto: "+lottoProdotto.getKey());
					}
				}
				ConnectionManager.commit();
				convalidato = true;
			}
		} catch(SQLException | RuntimeException e) {
			erroreConvalida = e;
		} finally {
			if(nuovaConnessione) {
				try {
					if(!convalidato) {
						ConnectionManager.rollback();
					}
				} finally {
					ConnectionManager.popConnection();
				}
			}
		}

		if(!convalidato) {
			// Solo dopo il rollback della convalida: nessuno storno su movimenti gia' annullati.
			try {
				cancellaDocumentoProduzioneNonConvalidato(documento);
			} catch(SQLException | RuntimeException e) {
				if(erroreConvalida != null) {
					e.addSuppressed(erroreConvalida);
				}
				throw e;
			}
		}
		if(erroreConvalida instanceof SQLException) {
			throw (SQLException) erroreConvalida;
		}
		if(erroreConvalida instanceof RuntimeException) {
			throw (RuntimeException) erroreConvalida;
		}
		return convalidato;
	}

	protected void cancellaDocumentoProduzioneNonConvalidato(DocumentoProduzione documento) throws SQLException {
		CMDocPrdEsec esecutore = (CMDocPrdEsec) Factory.createObject(CMDocPrdEsec.class);
		int ret = esecutore.cancellaDocumenti(documento);
		if(ret < 0) {
			throw new SQLException("Cancellazione del documento EasyCheck non riuscita: "+documento.getKey()+" (esito "+ret+")");
		}
		ConnectionManager.commit();
		esecutore.ripristinaNumeratore(documento.getNumeratoreHandler());
	}

	@SuppressWarnings("rawtypes")
	public Dipendente recuperaDipendenteByOperatorCode(String operatorCode) {
		if(operatorCode == null)
			return null;
		String where = " "+DipendenteTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND "+DipendenteTM.MATRICOLA+" = '"+operatorCode+"' ";
		try {
			Vector v = Dipendente.retrieveList(Dipendente.class, where, "", false);
			if(!v.isEmpty()) {
				return (Dipendente) v.get(0);
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	public Lotto getLotto(String idArticolo, String idLotto) {
		try {
			return (Lotto) Lotto.elementWithKey(Lotto.class, KeyHelper.buildObjectKey(new String[] {
					Azienda.getAziendaCorrente(),
					idArticolo,idLotto
			}), PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	public BODataCollector generaLottoPezzaLavorata(PezzaLavorata pezzaLavorata, AttivitaEsecProdotto prodotto) {
		BODataCollector boDC = null;
		try {
			boDC = YCostantiTestori.createDataCollector("Lotto");
			Lotto lt = (Lotto) boDC.getBo();
			lt.setCodiceAzienda(Azienda.getAziendaCorrente());
			lt.setArticolo(prodotto.getArticolo());
			lt.setCodiceLotto(pezzaLavorata.getHeaderID());
			boolean onDB = lt.retrieve();
			if(onDB) {
				boDC.setMode(WebForm.UPDATE);
			}
			if(lt instanceof YLotto) {
				((YLotto) lt).setPezzaGreggia(pezzaLavorata.getRawPieceCode());
				((YLotto) lt).setQuantitaBonifico(pezzaLavorata.getAllowance());
				((YLotto) lt).setAltezzaMinima(pezzaLavorata.getMinWidth());
				((YLotto) lt).setAltezzaMassima(pezzaLavorata.getMaxWidth());
				((YLotto) lt).setPesoKg(pezzaLavorata.getPieceWeight());
				((YLotto) lt).setIdDifettosita(pezzaLavorata.getFinalQuality());
				if(pezzaLavorata.getWidthQuality() != null && !pezzaLavorata.getWidthQuality().equals("1"))
					((YLotto) lt).setIdDifettositaAltezza(pezzaLavorata.getWidthQuality());
			}
			//72273 Inizio
			//lt.setNote(pezzaLavorata.getPieceNote());
			String pieceNote = pezzaLavorata.getPieceNote();
			if(pieceNote != null) {
				if(pieceNote.length() > 50) {
					pieceNote = pieceNote.substring(0,50);
				}
				lt.setStringaRisUte1(pieceNote);
			}
			//72273 Fine
			boDC.setAutoCommit(false);
			boDC.save();
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return boDC;
	}

	public JSONObject ritornaDatiPezzaGreggia(JSONObject payload) {
		JSONObject response = new JSONObject();
		JSONObject result = new JSONObject();
		Status status = Status.OK;
		Collection<ErrorMessage> errors = new ArrayList<>();

		PezzaGreggiaField pezza = PezzaGreggiaField.fromPayload(payload, errors);
		if (!errors.isEmpty()) {
			status = Status.BAD_REQUEST;
		} else {
			String bollaLavorazione = pezza.getProductionOrder();
			AttivitaEsecutiva atvEsec = leggiAtvEsec(bollaLavorazione);
			AttivitaEsecProdotto prodottoPrimario = null;
			if(atvEsec != null)
				prodottoPrimario = atvEsec.getAtvEsecPrdPrimario();
			if(atvEsec != null 
					&& atvEsec.getMateriali().size() > 0
					&& ((AttivitaEsecMateriale) atvEsec.getMateriali().get(0)).getArticolo().getArticoloDatiMagaz().getCodAutLotProd()  == YArticoloDatiMagaz.PEZZE
					&& atvEsec.getStatoAttivita() != AttivitaEsecutiva.COMPLETATO_ATV
					&& (prodottoPrimario != null && prodottoPrimario.getStatoVersamento() != AttivitaEsecProdotto.COMPLETATO)) {
				AttivitaEsecMateriale materiale = ((AttivitaEsecMateriale) atvEsec.getMateriali().get(0));
				AttivitaEsecutiva attivita = materiale.getAttivitaEsecutiva();
				String progressivo = null;
				try {
					YLotto lotto = (YLotto) Lotto.elementWithKey(Lotto.class, KeyHelper.buildObjectKey(new String[] {
							Azienda.getAziendaCorrente(),materiale.getIdArticolo(),pezza.getRawPieceCode()}),PersistentObject.NO_LOCK);
					if(lotto != null) {
						if(attivita.isAttivitaFinale() 
								&& ((YAttivitaEsecutiva)attivita).getTipoTaglio() != TipoTaglioPezza.NON_SIGNIFICATIVO){
							progressivo = CreaLottiTestoriUtils.getMaxLottoProgressivoMonoChar(attivita.getIdAzienda(), lotto.getCodiceLotto(), ((YAttivitaEsecutiva)attivita).getTipoTaglio());
						}else {
							progressivo = CreaLottiTestoriUtils.getMaxProgressivoPezzeFromLottoNonFormattato(attivita.getIdAzienda(), lotto.getCodiceLotto());
						}
						String pieceHeaderSkid = "";
						if(progressivo == null) {
							pieceHeaderSkid = pezza.getRawPieceCode();
						}else {
							pieceHeaderSkid = lotto.getCodiceLotto() + progressivo;
						}

						result.put("success", true);
						result.put("message", "Pezza greggia processata e validata con successo.");
						result.put("pieceHeaderSkid", pieceHeaderSkid);

						JSONObject itemDataPanthera = new JSONObject();
						itemDataPanthera.put("pieceCode", materiale.getIdArticolo());
						itemDataPanthera.put("itemDescription", materiale.getArticolo().getDescrizioneArticoloNLS().getDescrizione());
						itemDataPanthera.put("standardWeight", materiale.getArticolo().getPesoNetto() != null ? materiale.getArticolo().getPesoNetto() : 0);

						YVariabiliApplicazione FBPE1 = (YVariabiliApplicazione) YVariabiliApplicazione.elementWithKey(YVariabiliApplicazione.class,
								KeyHelper.buildObjectKey(new String[] {
										Azienda.getAziendaCorrente(),"FBPE1"
								}), PersistentObject.NO_LOCK);
						YVariabiliApplicazione FBPE2 = (YVariabiliApplicazione) YVariabiliApplicazione.elementWithKey(YVariabiliApplicazione.class,
								KeyHelper.buildObjectKey(new String[] {
										Azienda.getAziendaCorrente(),"FBPE2"
								}), PersistentObject.NO_LOCK);

						itemDataPanthera.put("parameter1", (FBPE1 != null ? FBPE1.getValore() : 0));
						itemDataPanthera.put("parameter2", (FBPE2 != null ? FBPE2.getValore() : 0)); 

						BigDecimal altezzaMin = materiale.getArticolo().getLunghezza() != null ? materiale.getArticolo().getLunghezza() : BigDecimal.ZERO;
						BigDecimal altezzaMax = materiale.getArticolo().getAltezza() != null ? materiale.getArticolo().getAltezza() : BigDecimal.ZERO;
						itemDataPanthera.put("minHeight", String.valueOf(altezzaMin));
						itemDataPanthera.put("maxHeight", String.valueOf(altezzaMax));

						result.put("itemDataPanthera", itemDataPanthera);

					}else {
						errors.add(new ErrorMessage("BAS0000078","Lotto con codice :"+pezza.getRawPieceCode()+" su materiale "+materiale.getIdArticolo() + " non esistente in Panthera"));
						status = Status.BAD_REQUEST;
					}

				}catch (Exception e) {
					errors.add(new ErrorMessage("BAS0000078","Eccezione non gestita: "+e.getMessage()));
					status = Status.INTERNAL_SERVER_ERROR;
					e.printStackTrace(Trace.excStream);
				}
			}else {
				if(atvEsec == null)
					errors.add(new ErrorMessage("BAS0000078","Non e' stata trovata nessuna attivita con codice: "+bollaLavorazione));
				else if(atvEsec.getMateriali().size() == 0)
					errors.add(new ErrorMessage("BAS0000078","Non e' stata trovato nessun materiale per l'attivita: "+bollaLavorazione));
				else if(((AttivitaEsecMateriale) atvEsec.getMateriali().get(0)).getArticolo().getArticoloDatiMagaz().getCodAutLotProd() != YArticoloDatiMagaz.PEZZE)
					errors.add(new ErrorMessage("BAS0000078","Il materiale dell'attivita non e' una pezza"));
				else if(atvEsec.getStatoAttivita() == AttivitaEsecutiva.COMPLETATO_ATV)
					errors.add(new ErrorMessage("BAS0000078","L'attivita e' in stato completato"));
				if(prodottoPrimario != null && prodottoPrimario.getStatoVersamento() == AttivitaEsecProdotto.COMPLETATO) {
					errors.add(new ErrorMessage("BAS0000078","Il prodotto di questa attivita' e' stato versato interamente, se si desidera versare un altra pezza cambiare la quantita' dell'ordine esecutivo"));
				}
				status = Status.BAD_REQUEST;
			}
		}

		result.put("errors", ErrorUtils.getInstance().toJSON(errors));
		response.put("response", result);
		response.put("status", status);
		return response;
	}

	@SuppressWarnings("rawtypes")
	public AttivitaEsecutiva leggiAtvEsec(String bollaLavorazione) {
		String where = AttivitaEsecutivaTM.ID_AZIENDA + " = '" + Azienda.getAziendaCorrente() + "' AND " +
				AttivitaEsecutivaTM.NUM_RITORNO + " = '" + bollaLavorazione + "'";
		try {
			Vector vect = AttivitaEsecutiva.retrieveList(where, "", true);
			if (!vect.isEmpty()) {
				return (AttivitaEsecutiva) vect.firstElement();
			}
		}
		catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	public DocumentoProduzione creaDocumentoProduzioneEasyCheck(OrdineEsecutivo ordEsec,
			AttivitaEsecutiva atvEsec,
			BigDecimal quantita, BigDecimal quantitaScarto,
			String idUMSec, BigDecimal qtaSec, BigDecimal qtaScartoSec, boolean prelievo) throws NumeratoreException {
		InterfacciaEasyCheck interfaccia = InterfacciaEasyCheck.getCurrentInterfacciaEasyCheck();
		if(interfaccia != null)
			return creaDocumentoProduzione(ordEsec, atvEsec, quantita, quantitaScarto, idUMSec, qtaSec,
					qtaScartoSec, interfaccia.getIdNumeratoreDocPrd(),
					interfaccia.getIdSerieDocPrd(), (prelievo ? interfaccia.getCausaleDocProdPrl() : interfaccia.getCausaleDocProdVrs()), interfaccia.getDipendenteRilevazione());
		return null;
	}

	/**
	 * Metodo per la creazione di un documento di produzione data un'attivita e alcuni dati necessari.<br>
	 * La risorsa sul quale rileva e' quella principale.
	 * @param ordineEsecutivo
	 * @param attivitaEsecutiva
	 * @param quantita
	 * @param quantitaScarto
	 * @param idUMSec
	 * @param qtaSec
	 * @param qtaScartoSec
	 * @return
	 * @throws NumeratoreException 
	 */
	public DocumentoProduzione creaDocumentoProduzione(OrdineEsecutivo ordEsec,
			AttivitaEsecutiva atvEsec,
			BigDecimal quantita, BigDecimal quantitaScarto,
			String idUMSec, BigDecimal qtaSec, BigDecimal qtaScartoSec,
			String idNumeratore, String idSerie, CausaleDocProduzione cauDoc, Dipendente dichiarante) throws NumeratoreException {
		DocumentoProduzione docPrd = (DocumentoProduzione) Factory.createObject(DocumentoProduzione.class);
		docPrd.setIdAzienda(Azienda.getAziendaCorrente());
		docPrd.setIdAnnoDoc(String.valueOf(TimeUtils.getCurrentYear()));
		docPrd.getNumeratoreHandler().setAnno(String.valueOf(TimeUtils.getCurrentYear()));
		docPrd.getNumeratoreHandler().setIdNumeratore(idNumeratore);
		docPrd.getNumeratoreHandler().setIdSerie(idSerie);
		docPrd.impostaSottoserie();
		docPrd.setNumeroDocFormattato(docPrd.getNumeratoreHandler().getIdProgressivoFormattato());
		docPrd.setIdNumeroDoc(docPrd.getNumeratoreHandler().getIdProgressivo());
		docPrd.setCausale(cauDoc);
		docPrd.setNumeroRitorno(atvEsec.getNumeroRitorno());
		docPrd.setDichiarante(dichiarante);
		docPrd.setRisorsa(atvEsec.getAtvEsecRsrPrincipale().getRisorsa());
		docPrd.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
		docPrd.setIdNumeroOrd(ordEsec.getIdNumeroOrdine());
		docPrd.setNumeroOrdFormattato(ordEsec.getNumeroOrdFmt());
		docPrd.setIdRigaAttivita(atvEsec.getIdRigaAttivita());
		docPrd.setRAttivita(atvEsec.getIdAttivita());
		docPrd.setRStabilimento(ordEsec.getIdStabilimento());
		docPrd.setRArticolo(ordEsec.getIdArticolo());
		docPrd.setRVersione(ordEsec.getIdVersione());
		docPrd.setRConfigurazione(ordEsec.getIdConfigurazione());
		docPrd.setRCommessa(ordEsec.getIdCommessa());
		docPrd.setRReparto(atvEsec.getIdReparto());
		docPrd.setCentroCosto(atvEsec.getCentroCosto());
		docPrd.setRCentroLavoro(atvEsec.getIdCentroLavoro());
		docPrd.setNote(null);
		docPrd.setSaldo(DocumentoProduzione.SALDO);
		docPrd.setQuantita(quantita);
		docPrd.setQtaScarto(quantitaScarto);
		if (idUMSec != null) {
			docPrd.setQtaSec(qtaSec);
			docPrd.setQtaScartoSec(qtaScartoSec);
			if (docPrd.getQtaSec() == null || docPrd.getQtaScartoSec() == null)
				docPrd.setRicalcoloQtaFattoreConv(true);
		}
		return docPrd;
	}

	//	@SuppressWarnings("rawtypes") 72320 rem
	public DocumentoProduzione esisteDocumentoProd(String idCausale, String bollaLavorazione, String pezzaGreggia) {
		DocumentoProduzione docPrd = null;
		//72320 inzio rem <
		//		String where = " "+DocumentoProduzioneTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND "+DocumentoProduzioneTM.R_CAU_DOC_PRD+" = '"+idCausale+"' ";
		//		where += " AND "+DocumentoProduzioneTM.NUM_RITORNO+" = '"+bollaLavorazione+"' ";
		//		try {
		//			Vector v = DocumentoProduzione.retrieveList(DocumentoProduzione.class, where, "", false);
		//			if(!v.isEmpty()) {
		//				docPrd = (DocumentoProduzione) v.get(0);
		//			}
		//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
		//			e.printStackTrace(Trace.excStream);
		//		}
		//72320 fine rem >
		//72320 nuovo <
		ResultSet rs = null;
		try{
			PreparedStatement ps = cPezzaGreggiaScaricata.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, Azienda.getAziendaCorrente());
			db.setString(ps, 2, bollaLavorazione);
			db.setString(ps, 3, idCausale);
			db.setString(ps, 4, pezzaGreggia);
			rs = ps.executeQuery();
			if (rs.next()){
				docPrd = (DocumentoProduzione) DocumentoProduzione.elementWithKey(DocumentoProduzione.class, KeyHelper.buildObjectKey(new String[] {
						rs.getString(DocumentoProduzioneTM.ID_AZIENDA),
						rs.getString(DocumentoProduzioneTM.ID_ANNO_DOC),
						rs.getString(DocumentoProduzioneTM.ID_NUMERO_DOC),
				}), PersistentObject.NO_LOCK);
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally{
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace(Trace.excStream);
			}
		}
		//72320 fine nuovo >
		return docPrd;
	}
}
