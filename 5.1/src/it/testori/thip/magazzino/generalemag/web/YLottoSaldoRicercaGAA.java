package it.testori.thip.magazzino.generalemag.web;

import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.servlet.GridActionAdapter;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 21/01/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    21/01/2026  DSSOF3   Prima stesura
 */

public class YLottoSaldoRicercaGAA extends GridActionAdapter {

	private static final long serialVersionUID = 1L;

	@Override
	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);

		ServletEnvironment se = toolBar.getServletEnvironment();
		if(se != null) {
			 boolean s = true;
		}
	}
}
