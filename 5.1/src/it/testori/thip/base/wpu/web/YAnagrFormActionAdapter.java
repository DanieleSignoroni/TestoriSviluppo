package it.testori.thip.base.wpu.web;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.base.IniFile;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.web.ServletEnvironment;

import it.testori.thip.produzione.pianifica.YInquiryPianificazioneIdx;
import it.thera.thip.base.wpu.web.AnagrFormActionAdapter;
import it.thera.thip.base.wpu.web.WPUParams;
import it.thera.thip.cs.ColonneFiltri;
import it.thera.thip.cs.CondizioniFiltri;
import it.thera.thip.cs.ThipException;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 01/04/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    01/04/2026  DSSOF3   Prima stesura
 */

public class YAnagrFormActionAdapter extends AnagrFormActionAdapter {

	public static final String INQ_PIANIFICAZIONE = "InquiryPianificaFab";

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	@Override
	protected void apriAltro(ServletEnvironment se, ClassADCollection cadc) throws ServletException, IOException {
		String classNameReq = getStringParameter(se.getRequest(), WPUParams.CLASS_NAME_REQ);
		if(classNameReq.equals(INQ_PIANIFICAZIONE)) {

			YInquiryPianificazioneIdx inq = (YInquiryPianificazioneIdx) Factory.createObject(YInquiryPianificazioneIdx.class);
			inq.inizializzaUtenteAmbiente();
			inq.getCondizioniFiltri().getColonneFiltro();

			Vector colonne = inq.getCondizioniFiltri().getColonneFiltro();
			for (int i = 0; i < colonne.size(); i++) {
				ColonneFiltri colFiltro = (ColonneFiltri) colonne.elementAt(i);
				if (colFiltro.getClassAdName().equals("IdArticolo")) {
					CondizioniFiltri.impostaFiltroLista(colFiltro, se.getRequest().getParameter("Articolo"));
				}
			}
			try {
				inq.interroga();
			} catch (ThipException e) {
				e.printStackTrace(Trace.excStream);
			}
			if(inq.getKeyLinkPublic() != null) {
				String param ="?thAction=DETTAGLIO&ObjectKey="+inq.getKeyLinkPublic()+"&NoNewWin=true";
				param+="&"+CLASS_NAME+"="+classNameReq;
				se.sendRequest(getServletContext(), se.getServletPath() + "it.thera.thip.produzione.pianifica.web.InquiryPianificaFormActionAdapter"+param, false);
			}else {
				se.addErrorMessage(new ErrorMessage("BAS0000078","L'interrogazione non ha prodotto risultati per l'articolo "+se.getRequest().getParameter("Articolo")));
				String errorPage = "/" + IniFile.getValue("thermfw.ini", "Web", "MessageActionHandler");
				se.sendRequest(getServletContext(), errorPage, false);
			}
		}else {
			super.apriAltro(se, cadc);
		}
	}
}
