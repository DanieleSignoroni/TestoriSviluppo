package it.thera.thip.vendite.generaleVE.web;

import java.io.IOException;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.URLUtils;
import com.thera.thermfw.web.servlet.ShowGrid;

import it.thera.thip.base.documenti.DocumentoBaseRiga;

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

	public static String getParamsShowGrid(String className, DocumentoBaseRiga docRiga) {
		String params = "?ClassName="+className;
		params += "&IdArticolo="+docRiga.getIdArticolo();
		params += "&IdCommessa="+docRiga.getIdCommessa();
		params += "&IdMagazzino="+docRiga.getIdMagazzino();
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
}
