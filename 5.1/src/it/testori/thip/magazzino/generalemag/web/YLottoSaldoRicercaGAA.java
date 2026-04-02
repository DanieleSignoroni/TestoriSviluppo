package it.testori.thip.magazzino.generalemag.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.persist.Column;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.WebMenuBar;
import com.thera.thermfw.web.WebSettingBar;
import com.thera.thermfw.web.WebSettingToolBarButton;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;
import com.thera.thermfw.web.servlet.GridActionAdapter;
import com.thera.thermfw.web.servlet.NavigationMapServlet;

import it.thera.thip.cs.ColonneFiltri;
import it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 06/02/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72349    06/02/2026  DSSOF3   Prima stesura
 */

public class YLottoSaldoRicercaGAA extends GridActionAdapter {

	private static final long serialVersionUID = 1L;

	public static final String PULISCI_FILTRO_MAGAZZINO = "PULISCI_FILTRO_MAGAZZINO";

	@Override
	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);

		ServletEnvironment se = toolBar.getServletEnvironment();
		if(isSelezioneMultiplaLottiEnvironment(se)) {
			toolBar.getButtons().clear();

			WebToolBarButton webToolBarButSelMult = SelezioneMultiplaLottoTestori.getButtonSelezioneMultipla();
			if (webToolBarButSelMult != null)
				toolBar.addButton(webToolBarButSelMult);
		}
	}

	@Override
	public void modifyMenuBar(WebMenuBar menuBar) {
		super.modifyMenuBar(menuBar);

		ServletEnvironment se = menuBar.getServletEnvironment();
		if(se != null 
				&& (se.getRequest().getParameter(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI) != null
				&& se.getRequest().getParameter(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI).equals("true"))) {
			menuBar.removeMenu("ViewSettingMenu");
			menuBar.removeMenu("SelectedMenu");
			menuBar.removeMenu("ListMenu");
		}
	}

	@Override
	public void modifySettingBar(WebSettingBar settingBar) {
		super.modifySettingBar(settingBar);
		String thRestrictConditions = settingBar.getServletEnvironment().getRequest().getParameter("thRestrictConditions");
		if(thRestrictConditions != null && thRestrictConditions.contains("YMostraBtnMostraTuttiLotti")
				|| isSelezioneMultiplaLottiEnvironment(settingBar.getServletEnvironment())) {
			int indexFrom = thRestrictConditions.indexOf("YMostraBtnMostraTuttiLotti") + "YMostraBtnMostraTuttiLotti=".length();
			String valore = thRestrictConditions.substring(indexFrom , indexFrom + 1);
			if(valore != null && valore.equals(String.valueOf(Column.TRUE_CHAR))
					|| isSelezioneMultiplaLottiEnvironment(settingBar.getServletEnvironment())) {
				WebSettingToolBarButton addToHomepage = new WebSettingToolBarButton(PULISCI_FILTRO_MAGAZZINO,
						"action_submit", "infoArea", "no", "it.testori.thip.magazzino.generalemag.resources.LottiTestori",
						PULISCI_FILTRO_MAGAZZINO, "thermweb/image/gui/cnr/delete.svg", PULISCI_FILTRO_MAGAZZINO, "none", false);
				settingBar.addButton(addToHomepage);
			}
		}
	}

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String azione = getStringParameter(se.getRequest(), ACTION);
		if(azione != null && azione.equals(PULISCI_FILTRO_MAGAZZINO)) {
			PrintWriter out = se.getResponse().getWriter();
			out.println("<script>");
			out.println("parent.document.forms[0].thSpecificDOList.value = 'it.testori.thip.magazzino.generalemag.web.YLottoSearchDOListNoMagazzino';");
			out.println("parent.runAction('REFRESH_GRID','none','infoArea','no');");
			out.println("</script>");
			out.close();
		}else if(azione != null && azione.equals(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI)) {
			selezioneMultiplaLottiTestori(cadc,se);
		}else {
			super.otherActions(cadc, se);
		}
	}

	@Override
	protected void requireNewPage(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		if(isSelezioneMultiplaLottiEnvironment(se)) {
			callShowGridSelezioneMultiplaLottiEnvironment(se);
		}else {
			super.requireNewPage(cadc, se);
		}
	}

	protected void callShowGridSelezioneMultiplaLottiEnvironment(ServletEnvironment se) throws ServletException, IOException {
		String np = se.getRequest().getParameter(NEW_PAGE);
		String cn = se.getRequest().getParameter(CLASS_NAME);
		String sk = se.getRequest().getParameter(TH_SETTING_KEY);
		String url = se.getServletPath() + "com.thera.thermfw.web.servlet.Execute?Page=" + np + "&ClassName=" + cn + "&SettingKey=" + sk;
		url += "&ShowGridName=" + "it.thera.thip.vendite.generaleVE.web.SelezioneMultiplaLottoTestori";
		String navigationMapName = se.getRequest().getParameter(NavigationMapServlet.NAV_MAP_NAME_PARAM);
		String navigationMapBoxName = se.getRequest().getParameter(NavigationMapServlet.NAV_MAP_BOX_NAME_PARAM);
		String navigationMapUserKey = se.getRequest().getParameter(NavigationMapServlet.NAV_MAP_BOX_USER_KEY_PARAM);

		if (navigationMapName != null){
			url += "&" + NavigationMapServlet.NAV_MAP_NAME_PARAM + "=" + navigationMapName;
		}
		if (navigationMapBoxName != null){
			url += "&" + NavigationMapServlet.NAV_MAP_BOX_NAME_PARAM + "=" + navigationMapBoxName;
		}
		if (navigationMapUserKey != null){
			url += "&" + NavigationMapServlet.NAV_MAP_BOX_USER_KEY_PARAM + "=" + navigationMapUserKey;
		}
		String newUrl = "";
		newUrl+="&"+SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI+"=true";
		url += newUrl;
		se.sendRequest(getServletContext(), url , false);
	}

	protected void selezioneMultiplaLottiTestori(ClassADCollection cadc, ServletEnvironment se) throws IOException {
		String[] selectedKeys = se.getRequest().getParameterValues(OBJECT_KEY);
		String ChiaviLottiMultSel = String.join(ColonneFiltri.LISTA_SEP, selectedKeys);
		PrintWriter out = se.getResponse().getWriter();
		out.println("<script>");
		out.println("parent.opener.document.forms[0].ChiaviLottiMultSel.value = '"+WebElement.formatStringForHTML(ChiaviLottiMultSel)+"';");
		out.println("parent.parent.opener.parent.onSearchBackSelezionaLottiMultipliTestori();");
		out.println("parent.window.close();");
		out.println("</script>");
		out.close();		
	}

	protected boolean isSelezioneMultiplaLottiEnvironment(ServletEnvironment se) {
		if(se != null 
				&& (se.getRequest().getParameter(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI) != null
				&& se.getRequest().getParameter(SelezioneMultiplaLottoTestori.SELEZIONE_MULTIPLA_LOTTI).equals("true"))) {
			return true;
		}
		return false;
	}
}
