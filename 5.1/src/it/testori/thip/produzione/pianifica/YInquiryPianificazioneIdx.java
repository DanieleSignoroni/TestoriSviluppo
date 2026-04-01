package it.testori.thip.produzione.pianifica;

import it.thera.thip.produzione.pianifica.InquiryPianificazioneIdx;

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

public class YInquiryPianificazioneIdx extends InquiryPianificazioneIdx {

	public String getKeyLinkPublic() {
		if(this.keyPianificaIdx != null)
			return getKeyLink();
		else
			return null;
	}

	public String getKeyPianificaIdx() {
		return this.keyPianificaIdx;
	}
}
