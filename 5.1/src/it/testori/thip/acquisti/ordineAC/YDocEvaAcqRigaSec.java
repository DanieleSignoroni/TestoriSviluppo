package it.testori.thip.acquisti.ordineAC;

import java.math.BigDecimal;

import it.thera.thip.acquisti.documentoAC.DocumentoAcquisto;
import it.thera.thip.acquisti.ordineAC.DocEvaAcqRigaSec;
import it.thera.thip.base.comuniVenAcq.AzioneMagazzino;
import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRiga;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.documenti.StatoAvanzamento;

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
 * 72494    19/05/2026  DSSOF3   Prima stesura
 */

public class YDocEvaAcqRigaSec extends DocEvaAcqRigaSec {

	@Override
	protected void aggiornaQtaEvasione(char stato, boolean forzaStato) {
		super.aggiornaQtaEvasione(stato, forzaStato);
		if(!getArticolo().isArticLotto()
				&& getLavEsterna()
				&& ((DocumentoAcquisto) this.getTestata()).getCausale().getAzioneMagazzino() == AzioneMagazzino.ENTRATA) {
			BigDecimal giacenzaFornitore = this.getDocEvaAcqRiga().getQtaGiacenzaSaldo(this, null);
			if(giacenzaFornitore == null)
				giacenzaFornitore = BigDecimal.ZERO;
			if(giacenzaFornitore.compareTo(this.getDocEvaAcqRiga().getRigaDoc().getQtaInUMAcq()) < 0) {
				if(stato == StatoAvanzamento.PROVVISORIO) {
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMAcq(giacenzaFornitore);
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMPrm(giacenzaFornitore);
				}else if (stato == StatoAvanzamento.DEFINITIVO) {
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMAcq(giacenzaFornitore);
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMPrm(giacenzaFornitore);
				}
			}
		}
	}

	@Override
	public QuantitaInUMRif ricolcoloQuantitaDaRicalcolare(DocumentoOrdineRiga rigaPrm,
			QuantitaInUMRif qtaRigaPrimaria) {
		QuantitaInUMRif sup = super.ricolcoloQuantitaDaRicalcolare(rigaPrm, qtaRigaPrimaria);
		if(!getArticolo().isArticLotto()
				&& getLavEsterna()
				&& ((DocumentoAcquisto) this.getTestata()).getCausale().getAzioneMagazzino() == AzioneMagazzino.ENTRATA) {
			BigDecimal giacenzaFornitore = this.getDocEvaAcqRiga().getQtaGiacenzaSaldo(this, null);
			if(giacenzaFornitore == null)
				giacenzaFornitore = BigDecimal.ZERO;
			if(giacenzaFornitore.compareTo(this.getDocEvaAcqRiga().getRigaDoc().getQtaInUMAcq()) < 0) {
				if(this.getDocEvaAcqRiga().getRigaDoc().getStatoAvanzamento() == StatoAvanzamento.PROVVISORIO) {
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMAcq(giacenzaFornitore);
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMPrm(giacenzaFornitore);
				}else if (this.getDocEvaAcqRiga().getRigaDoc().getStatoAvanzamento() == StatoAvanzamento.DEFINITIVO) {
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMAcq(giacenzaFornitore);
					this.getDocEvaAcqRiga().getRigaDoc().setQtaInUMPrm(giacenzaFornitore);
				}
			}
		}
		return sup;
	}

}
