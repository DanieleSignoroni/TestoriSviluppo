package it.testori.thip.acquisti.ordineAC;

import java.math.BigDecimal;

import it.thera.thip.acquisti.ordineAC.DocEvaAcq;
import it.thera.thip.acquisti.ordineAC.DocEvaAcqRiga;
import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRiga;
import it.thera.thip.base.comuniVenAcq.AzioneMagazzino;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 19/05/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    19/05/2026  DSSOF3   Prima stesura
 */

public class YDocEvaAcqRiga extends DocEvaAcqRiga {

	@Override
	public boolean isAddable(DocEvaAcqRiga attRiga, OrdineAcquistoRiga rigaOrdine) {
		boolean isAddable = super.isAddable(attRiga, rigaOrdine);
		if(isAddable 
				&& attRiga.getRigaDoc() instanceof YDocEvaAcqRigaSec
				&& !attRiga.getRigaDoc().getArticolo().isArticLotto()
				&& attRiga.getRigaDoc().getLavEsterna()
				&& ((DocEvaAcq)attRiga.getRigaDoc().getTestata()).getCausale().getAzioneMagazzino() == AzioneMagazzino.ENTRATA) {
			BigDecimal giacenzaFornitore = getQtaGiacenzaSaldo(attRiga.getRigaDoc(), null);			
			if(giacenzaFornitore == null || giacenzaFornitore.compareTo(BigDecimal.ZERO) == 0) {
				isAddable = false;
			}
		}
		return isAddable;
	}
}
