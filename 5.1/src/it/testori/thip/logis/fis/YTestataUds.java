package it.testori.thip.logis.fis;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import it.thera.thip.logis.fis.TestataUds;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 11/11/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    11/11/2026  DSSOF3   Prima stesura
 */

public class YTestataUds extends TestataUds {

	@Override
	public void ricalcoloPesi() {
		super.ricalcoloPesi();

		//..lo std fa peso lordo anagr articoli delle righe uds + tara, pers fa solo peso lordo indicato - tara
		setPesoNetto(getPesoLordo().subtract(getTipoUds().getTara()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Vector chiudiUds() {
		BigDecimal pesoNetto = getPesoNetto();
		BigDecimal pesoTotale = BigDecimal.ZERO;

		for (int i = 0; i < getRigheUds().size(); i++) {
			YRigaUds ru = (YRigaUds) getRigheUds().get(i);
			if (ru.getArticolo() != null && ru.getArticolo().getPesoUnitario() != null && ru.getQta1Confermata() != null)
				pesoTotale = pesoTotale.add(ru.getQta1Confermata().multiply(ru.getArticolo().getPesoUnitario()));
		}

		if (pesoNetto != null && pesoTotale.compareTo(BigDecimal.ZERO) > 0) {
			for (int i = 0; i < getRigheUds().size(); i++) {
				YRigaUds ru = (YRigaUds) getRigheUds().get(i);
				BigDecimal pesoRipartito = BigDecimal.ZERO;
				if (ru.getArticolo() != null && ru.getArticolo().getPesoUnitario() != null && ru.getQta1Confermata() != null)
					pesoRipartito = ru.getQta1Confermata().multiply(ru.getArticolo().getPesoUnitario()).multiply(pesoNetto).divide(pesoTotale, 6, RoundingMode.HALF_UP);
				ru.setPesoNetto(pesoRipartito);
			}
		}

		return super.chiudiUds();
	}
}
