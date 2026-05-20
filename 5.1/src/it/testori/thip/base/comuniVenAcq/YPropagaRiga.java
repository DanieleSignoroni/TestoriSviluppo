package it.testori.thip.base.comuniVenAcq;

import java.sql.Date;
import java.util.Calendar;

import it.thera.thip.base.comuniVenAcq.PropagaRiga;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 20/05/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    20/05/2026  DSSOF3   Gestione propagazione settimanale.
 */

public class YPropagaRiga extends PropagaRiga {

	public static final int SETTIMANALE = '3';

	@Override
	protected Date getProssimoMese(Date dataRiga, int nbr, char cadenza, boolean isDataFineMesse) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataRiga);

		// Nuova gestione settimanale
		if (cadenza == SETTIMANALE) {

			// Aggiunge nbr settimane
			calendar.add(Calendar.WEEK_OF_YEAR, nbr);

			return new Date(calendar.getTimeInMillis());
		}

		// Mantiene logica standard per mensile/bimestrale
		return super.getProssimoMese(dataRiga, nbr, cadenza, isDataFineMesse);
	}
}
