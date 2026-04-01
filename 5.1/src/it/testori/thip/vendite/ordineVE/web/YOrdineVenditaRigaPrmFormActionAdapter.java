package it.testori.thip.vendite.ordineVE.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebForm;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;

import it.testori.thip.vendite.generaleVE.GestioneCalcoloCosti;
import it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrm;
import it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmFormActionAdapter;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 22/09/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72141    22/09/2025  DSSOF3   Prima stesura
 */

public class YOrdineVenditaRigaPrmFormActionAdapter extends OrdineVenditaRigaPrmFormActionAdapter {

	private static final long serialVersionUID = 1L;

	public static final String SELEZIONE_MULTIPLA_LOTTI = "SEL_MULTIPLA_LOTTI_TESTORI";

	@Override
	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);
		int mode = toolBar.getOwnerForm().getMode();
		if (mode != WebForm.NEW && mode != WebForm.COPY) {
			WebToolBarButton webToolBarButGestioneCosti = GestioneCalcoloCosti.getInstance().getButtonInterrogazioneModProCos();
			if (webToolBarButGestioneCosti != null)
				toolBar.addButton(webToolBarButGestioneCosti);
		}
	}

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String azione = getAzione(se);
		if(azione.equals(GestioneCalcoloCosti.INT_VAL_MOD_PROD_COS)) {
			interrogazioneValorizzazioneModProCosto(cadc,se);
		}else if(azione != null && azione.equals(SELEZIONE_MULTIPLA_LOTTI)){
			selezioneMultiplaLotti(cadc,se);
		}else {
			super.otherActions(cadc, se);
		}
	}

	protected void selezioneMultiplaLotti(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String key = getStringParameter(se.getRequest(), "thKey");
		OrdineVenditaRigaPrm riga;
		try {
			riga = (OrdineVenditaRigaPrm)OrdineVenditaRigaPrm.elementWithKey(OrdineVenditaRigaPrm.class, key, PersistentObject.OPTIMISTIC_LOCK);
			if(riga != null){
				String url = se.getServletPath() + "it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori";
				url += SelezioneMultiplaLottoTestori.getParamsShowGrid("LottoSaldoRicerca", riga);
				url += "&" + SelezioneMultiplaLottoTestori.getRestrictConditionsShowGrid(getStringParameter(se.getRequest(), CLASS_NAME),riga);
				se.sendRequest(getServletContext(), url, false);
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
	}

	protected void interrogazioneValorizzazioneModProCosto(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		OrdineVenditaRigaPrm offCliRigaPrm = getObject(getStringParameter(se.getRequest(), "thKey"));

		se.sendRequest(getServletContext(), se.getServletPath() + "it.thera.thip.datiTecnici.modpro.web.InterrogValorizFormActionAdapter"
				+ GestioneCalcoloCosti.getInstance().getParamsInterrogazioneModProCos(offCliRigaPrm), false);
	}
}