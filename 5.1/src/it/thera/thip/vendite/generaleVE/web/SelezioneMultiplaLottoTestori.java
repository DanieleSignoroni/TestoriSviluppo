package it.thera.thip.vendite.generaleVE.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.URLUtils;
import com.thera.thermfw.web.WebToolBarButton;
import com.thera.thermfw.web.servlet.ShowGrid;

import it.thera.thip.base.documenti.DocumentoBaseRiga;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.cs.ColonneFiltri;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 16/01/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    16/01/2026  DSSOF3   Prima stesura
 */

public class SelezioneMultiplaLottoTestori extends ShowGrid {

	private static final long serialVersionUID = 1L;

	public static final String SELEZIONE_MULTIPLA_LOTTI = "SEL_MULTIPLA_LOTTI_TESTORI";
	public static final String SEL_MULTIPLA_LOTTI_TESTORI_CONFERMA = "SEL_MULTIPLA_LOTTI_TESTORI_CONFERMA";

	public static final String RES_FILE = "it.testori.thip.magazzino.generalemag.resources.LottiTestori";

	public static String getParamsShowGrid(String className, DocumentoBaseRiga docRiga) {
		String params = "?ClassName="+className;
		params += "&IdArticolo="+docRiga.getIdArticolo();
		params += "&IdCommessa="+docRiga.getIdCommessa();
		params += "&IdMagazzino="+docRiga.getIdMagazzino();
		params += "&"+SELEZIONE_MULTIPLA_LOTTI+"=true";
		return params;
	}

	public static String getRestrictConditionsShowGrid(String className, DocumentoBaseRiga docRiga) {
		String filtri = "";
		if(docRiga == null)
			return filtri;
		filtri += "CodiceAzienda=" + docRiga.getIdAzienda() + ";";
		filtri += "CodiceArticolo=" + docRiga.getIdArticolo() + ";";
		filtri += "CodiceMagazzino=" + docRiga.getIdMagazzino() + ";";
		filtri += "IdConfigurazione=" + (docRiga.getIdConfigurazione() != null ? docRiga.getIdConfigurazione() : "0") + ";";
		filtri += "CodiceAzienda=" + docRiga.getIdAzienda() + ";";
		filtri += "ChiaveLottoProdotto2=" + docRiga.getKey() + ";";
		filtri += "ChiaveLottoProdotto1=" + className + ";";
		return "thRestrictConditions=" + URLUtils.get().encode(filtri) + "&thSpecificDOList=it.testori.thip.vendite.ordineVE.web.YSelezioneMultiplaLottoDOList";
	}

	public static WebToolBarButton getButtonSelezioneMultipla() {
		return new WebToolBarButton("SelezioneMultiplaLottoTestori", "action_submit", "same", "no", RES_FILE, "btnSelLottiMultiplo", getSelezioneMultiplaLottoTestoriImmagine(), SELEZIONE_MULTIPLA_LOTTI, "multiple", false, true, getSelezioneMultiplaLottoTestoriImmagine());
	}

	public static String getSelezioneMultiplaLottoTestoriImmagine() {
		return "it/testori/thip/datiTecnici/modpro/images/InterrogValMod.svg";
	}

	public static List<String> getListLottiSelezioneMultipla(String chiaviLottiMultSel) {
		List<String> listKeys = new ArrayList<String>();
		String listaKeys = chiaviLottiMultSel;
		if(listaKeys == null || listaKeys.equals(""))
			return listKeys;
		java.util.StringTokenizer st = new java.util.StringTokenizer(listaKeys, ColonneFiltri.LISTA_SEP);
		while (st.hasMoreTokens()) {
			String stt = st.nextToken();
			listKeys.add(stt);
		}
		return listKeys;
	}

	public static boolean isCausaliTestataAdattaQuantitaLottiRiga(String idCauTes) {
		if(getListaCausaliTestataAdattaQuantitaLottiRiga().contains(idCauTes)) {
			return true;
		}
		return false;
	}

	public static List<String> getListaCausaliTestataAdattaQuantitaLottiRiga(){
		List<String> list = new ArrayList<String>();
		String val = getValoreParametroCausaliTestataAdattaQuantitaLottiRiga();
		if(val != null && !val.isEmpty()) {
			if(val.contains("@")) {
				list.addAll(Arrays.asList(val.split("@")));
			}else {
				list.add(val.trim());
			}
		}
		return list;
	}

	private static String getValoreParametroCausaliTestataAdattaQuantitaLottiRiga() {
		return ParametroPsn.getValoreParametroPsn("pers.base.comuniVenAcq.SelezioneMultiplaLotto", "CausaliAdattaQta");
	}

	@Override
	protected String getGridName(ServletEnvironment se, ClassADCollection cadc, boolean searchGrid) {
		return "it/testori/thip/magazzino/generalemag/SelezioneMultiplaLottoTestoriGrid.jsp";
	}
}
