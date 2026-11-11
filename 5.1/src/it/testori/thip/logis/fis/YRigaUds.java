package it.testori.thip.logis.fis;

import java.math.BigDecimal;

import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;

import it.thera.thip.logis.fis.RigaUds;

/**
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

public class YRigaUds extends RigaUds {

	protected BigDecimal iPesoNetto;

	public YRigaUds() {
		super();
	}

	public void setPesoNetto(BigDecimal pesoNetto) {
		this.iPesoNetto = pesoNetto;
		setDirty();
	}

	public BigDecimal getPesoNetto() {
		return iPesoNetto;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
	}

}
