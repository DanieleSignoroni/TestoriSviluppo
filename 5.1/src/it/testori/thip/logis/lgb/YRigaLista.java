package it.testori.thip.logis.lgb;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;

import it.testori.thip.base.cliente.YClienteVendita;
import it.testori.thip.logis.bas.YCostantiTestori;
import it.testori.thip.logis.fis.YRigaUdsTM;
import it.testori.thip.magazzino.generalemag.YLotto;
import it.testori.thip.vendite.documentoVE.YDocumentoVendita;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.logis.bas.ParametriLogis;
import it.thera.thip.logis.fis.Missione;
import it.thera.thip.logis.fis.Operatore;
import it.thera.thip.logis.fis.PianificazioneLista;
import it.thera.thip.logis.fis.Postazione;
import it.thera.thip.logis.fis.RigaUdsTM;
import it.thera.thip.logis.fis.SaldoQtaAssegnata;
import it.thera.thip.logis.fis.TestataUds;
import it.thera.thip.logis.fis.TestataUdsTM;
import it.thera.thip.logis.lgb.RigaLista;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.vendite.documentoVE.DocumentoVendita;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 20/10/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72178    20/10/2025  DSSOF3   Prima stesura
 * 72226	25/11/2025	DSSFO3   Gestione ubicazione inversa su multiple ubicazioni
 * 72233	02/12/2025	DSSOF3	 Lock type in YCostantiTestori.documentoVenditaTestataLista, escludere da logica i documenti interni
 * 72269	07/01/2026	DSSOF3	 Rimuovere controllo su articoli non gestiti a lotto
 */

public class YRigaLista extends RigaLista {

	protected static final String STMT_PESO_NETTO_COLLO_CONF = "SELECT SUM(R."+YRigaUdsTM.PESO_NETTO+")" +
			" FROM "+YRigaUdsTM.TABLE_NAME_EXT+" R " +
			" INNER JOIN "+RigaUdsTM.NOME_TABELLA+" RUS " +
			" ON R."+RigaUdsTM.COD_TESTATA+" = RUS."+RigaUdsTM.COD_TESTATA+" " +
			" AND R."+RigaUdsTM.CODICE+" = RUS."+RigaUdsTM.CODICE+" " +
			" INNER JOIN "+TestataUdsTM.NOME_TABELLA+" T " +
			" ON RUS."+RigaUdsTM.COD_TESTATA+" = T."+TestataUdsTM.CODICE+" " +
			" WHERE RUS."+RigaUdsTM.COD_SOCIETA+" = ? AND " +
			" RUS."+RigaUdsTM.COD_TESTATA_LISTA+" = ? AND " +
			" RUS."+RigaUdsTM.COD_RIGA_LISTA+" = ? AND " +
			" T."+TestataUdsTM.STATO_ALLESTIMENTO+" = '"+TestataUds.CHIUSO+"'";
	protected static final CachedStatement stmtPesoNettoColloConf = new CachedStatement(STMT_PESO_NETTO_COLLO_CONF);

	protected BigDecimal pesoNettoUdsConfermata = new BigDecimal(0);

	protected BigDecimal iPesoNetto;

	public BigDecimal getPesoNetto() {
		return iPesoNetto;
	}

	public void setPesoNetto(BigDecimal pesoNetto) {
		this.iPesoNetto = pesoNetto;
		setDirty();
	}

	@Override
	public boolean calcolaQtaUds() {
		boolean ret = super.calcolaQtaUds();
		calcolaPesoNettoConfermata();
		return ret;
	}

	@Override
	protected boolean chiudi(char fase) {
		if(fase == UDS) {
			setPesoNetto(pesoNettoUdsConfermata);
		}
		return super.chiudi(fase);
	}

	protected void calcolaPesoNettoConfermata() {
		CachedStatement stmt = stmtPesoNettoColloConf;
		synchronized(stmt) {
			ResultSet rs = null;
			try {
				Database db = ConnectionManager.getCurrentDatabase();
				PreparedStatement ps = stmt.getStatement();
				db.setString(ps, 1, getCodiceSocieta());
				db.setString(ps, 2, getCodiceTestataLista());
				ps.setInt(3, codice.intValue());
				rs = stmt.executeQuery();
				if (rs.next()) {
					String res = rs.getString(1);
					if (res != null)
						pesoNettoUdsConfermata = new BigDecimal(res.replace(',','.'));
					else
						pesoNettoUdsConfermata = new BigDecimal(0);
				}
			} catch(SQLException e) {
				Trace.excStream.println("RigaLista "+getKey());
				e.printStackTrace(Trace.excStream);
			}
			finally {
				try {
					if (rs != null)
						rs.close();
				}
				catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Vector generaMissioniSuElencoSaldi(Vector eS, PianificazioneLista pl, Postazione pt, Operatore op) {
		if (getCodiceArticolo().startsWith("5") || getCodiceArticolo().startsWith("6")
				&& YCostantiTestori.unitaMisuraVenditaRientraGestionePezze(YCostantiTestori.getArticoloThip(getArticolo()))) {
			//72269
			Articolo artThip = YCostantiTestori.getArticoloThip(getArticolo());
			if(artThip == null || !artThip.isArticLotto()) {
				return super.generaMissioniSuElencoSaldi(eS, pl, pt, op);
			}
			//72269

			//.Se la riga lista ha il lotto gia' assegnato allora propongo lo standard altimenti faccio controlli su difettosita'
			if(getLotto1() != null && !getLotto1().isEmpty()) {
				return super.generaMissioniSuElencoSaldi(eS, pl, pt, op);
			}else {
				DocumentoVendita dv = YCostantiTestori.documentoVenditaTestataLista(getTestataLista(), PersistentObject.NO_LOCK); //72233
				if (dv != null) {
					//72233 Inizio
					if(dv instanceof YDocumentoVendita
							&& (((YDocumentoVendita) dv).isSerieCausaleTrasferimentoPianoRifornimento() 
									|| ((YDocumentoVendita) dv).isSerieCausaleTrasferimentoUdcTraMagazzini()
									|| ((YDocumentoVendita) dv).isSerieCausaleTrasferimentoUdsTraMagazzini())) {
						return super.generaMissioniSuElencoSaldi(eS, pl, pt, op);
					}
					//72233 Fine
					YClienteVendita cliente = (YClienteVendita) dv.getCliente();
					String idMaxDif = null;
					if (getCodiceArticolo().startsWith("5"))
						idMaxDif = cliente != null ? cliente.getIdMaxDifAccTessuti() : null;
					else if(getCodiceArticolo().startsWith("6"))
						idMaxDif = cliente != null ? cliente.getIdMaxDifAccFeltri() : null;

					// Se il cliente non ha un massimo impostato, usa una policy di default (qui: 2)
					Integer defaultMax = 2;
					Integer maxAccettato = parseIntOrNull(idMaxDif) != null
							? parseIntOrNull(idMaxDif)
									: defaultMax;

					for (Iterator iterEs = eS.iterator(); iterEs.hasNext(); ) {
						SaldoQtaAssegnata sqa = (SaldoQtaAssegnata) iterEs.next();

						// niente lotto -> non posso validare, scarto
						if (sqa.getLotto1() == null) {
							iterEs.remove();
							continue;
						}

						try {
							Lotto lotto = (Lotto) Lotto.elementWithKey(
									Lotto.class,
									KeyHelper.buildObjectKey(new String[]{getCodiceSocieta(), getCodiceArticolo(), sqa.getLotto1()}),
									PersistentObject.NO_LOCK
									);

							if (lotto == null) {
								iterEs.remove();
								continue;
							}

							String difettositaLotto = ((YLotto) lotto).getIdDifettosita();

							// Regole:
							// - null o "0" -> scarto
							// - non numerico (es. "N", "P") -> scarto
							// - numerico: accetto solo se 1 <= valore <= maxAccettato
							if (!isDifettositaAccettata(difettositaLotto, maxAccettato)) {
								iterEs.remove();
							}

						} catch (SQLException ex) {
							ex.printStackTrace(Trace.excStream);
						}
					}
				}
			}
		}

		return super.generaMissioniSuElencoSaldi(eS, pl, pt, op);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Vector settaAttributiMissione(Missione m) {
		Vector v = super.settaAttributiMissione(m);
		if(m != null
				&& m.getCodiceUbicazione() != null
				&& trasferimentoUdcPadernoCodUbicazioneMissioneCambioInversa().contains(m.getCodiceUbicazione())) { //72226
			m.setCodiceUbicazioneInv(trasferimentoUdcPadernoCodUbicazioneInversaMissione());
		}
		return v;
	}

	//72226 Inizio rem
	//	public static String trasferimentoUdcPadernoCodUbicazioneMissioneCambioInversa() {
	//		String p = ParametriLogis.valoreDiSezioneChiave("YTrasfUdcPaderno", "CodiceUbicazMissione");
	//		return p != null ? p : "";
	//	}
	//72226 Fine rem

	//72226
	public static List<String> trasferimentoUdcPadernoCodUbicazioneMissioneCambioInversa() {
		String p = ParametriLogis.valoreDiSezioneChiave("YTrasfUdcPaderno", "CodiceUbicazMissione");

		if (p == null || p.trim().isEmpty()) {
			return Collections.emptyList();
		}

		return Arrays.stream(p.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());
	}
	//72226

	public static String trasferimentoUdcPadernoCodUbicazioneInversaMissione() {
		String p = ParametriLogis.valoreDiSezioneChiave("YTrasfUdcPaderno", "CodiceUbicazInvMissi");
		return p != null ? p : "";
	}

	/** Ritorna true se la difettosità del lotto è accettata rispetto al massimo. */
	public static boolean isDifettositaAccettata(String difLotto, Integer maxAccettato) {
		if (difLotto == null || "0".equals(difLotto)) return false;

		Integer val = parseIntOrNull(difLotto);
		if (val == null) return false; // valori non numerici non sono accettati

		return val >= 1 && val <= maxAccettato;
	}

	/** Tenta il parse di un intero, ritorna null se non numerico. */
	public static Integer parseIntOrNull(String s) {
		if (s == null) return null;
		try {
			return Integer.valueOf(s.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

}