package it.testori.thip.base.generale;

import java.math.BigDecimal;

import it.thera.thip.base.generale.RigaLogisThip;

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
 * 72749    11/11/2026  DSSOF3   Prima stesura
 */

public class YRigaLogisThip extends RigaLogisThip {

	protected BigDecimal pesoNetto = new BigDecimal(0);

	public BigDecimal getPesoNetto() {
		return pesoNetto;
	}

	public void setPesoNetto(BigDecimal pesoNetto) {
		this.pesoNetto = pesoNetto;
	}
	
}
