package it.testori.thip.base.generale;

import java.math.BigDecimal;

import it.testori.thip.base.cliente.YClienteVendita;
import it.testori.thip.logis.bas.YCostantiTestori;
import it.testori.thip.magazzino.generalemag.YLotto;
import it.thera.thip.base.generale.RigaLogisThip;
import it.thera.thip.base.generale.TestataLogisThip;
import it.thera.thip.base.generale.UnitaMisura;
import it.thera.thip.vendite.documentoVE.DocumentoVenRigaLottoPrm;
import it.thera.thip.vendite.documentoVE.DocumentoVenRigaPrm;
import it.thera.thip.vendite.documentoVE.DocumentoVendita;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 21/10/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72178    21/10/2025  DSSOF3   Prima stesura
 * 72749	11/11/2026	DSSOF3	 Riportare peso netto su thip.
 */

public class YTestataLogisThip extends TestataLogisThip {

	@Override
	public DocumentoVenRigaLottoPrm trascodificaDatiPersonalizzati(RigaLogisThip rlt,
			DocumentoVenRigaLottoPrm dvr1otto) {
		dvr1otto = super.trascodificaDatiPersonalizzati(rlt, dvr1otto);
		DocumentoVenRigaPrm dvr = (DocumentoVenRigaPrm)dvr1otto.getDocumentoRiga();
		DocumentoVendita dv = (DocumentoVendita) dvr.getTestata();
		UnitaMisura umGesPezze = YCostantiTestori.getUnitaMisuraGestionePezze();
		if(dv != null 
				&& (umGesPezze != null && umGesPezze.getIdUnitaMisura().equals(dvr.getIdUMRif()))) {
			YLotto lotto = (YLotto) dvr1otto.getLotto();
			if(lotto != null && lotto.getQuantitaBonifico() != null && lotto.getQuantitaBonifico().compareTo(BigDecimal.ZERO) > 0) {
				YClienteVendita cliente = (YClienteVendita) dv.getCliente();
				if(cliente != null && cliente.isBonificoPezza()) {
					if (dvr.getStatoAvanzamento() == it.thera.thip.base.documenti.StatoAvanzamento.DEFINITIVO) {
						dvr1otto.getQtaAttesaEvasione().setQuantitaInUMRif(dvr1otto.getQtaAttesaEvasione().getQuantitaInUMRif().subtract(lotto.getQuantitaBonifico()));
					}
					if (dvr.getStatoAvanzamento() == it.thera.thip.base.documenti.StatoAvanzamento.PROVVISORIO) {
						dvr1otto.getQtaPropostaEvasione().setQuantitaInUMRif(dvr1otto.getQtaPropostaEvasione().getQuantitaInUMRif().subtract(lotto.getQuantitaBonifico()));
					}
				}
			}
		}
		return dvr1otto;
	}

	@Override
	public DocumentoVenRigaPrm trascodificaDatiPersonalizzati(RigaLogisThip rlt, DocumentoVenRigaPrm dvr) {
		dvr = super.trascodificaDatiPersonalizzati(rlt, dvr);
		//72749 <
		if(rlt instanceof YRigaLogisThip) {
			dvr.setPesoNetto(((YRigaLogisThip)rlt).getPesoNetto());
		}
		//72749 >
		return dvr;
	}
}