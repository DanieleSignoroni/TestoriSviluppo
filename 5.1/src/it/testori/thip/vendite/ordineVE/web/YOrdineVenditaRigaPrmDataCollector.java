package it.testori.thip.vendite.ordineVE.web;

import com.thera.thermfw.collector.EnhBOComponentManager;

//import it.testori.thip.produzione.rifornLinea.YPsnDatiRifornimentoLinea;
import it.testori.thip.vendite.ordineVE.YOrdineVenditaRigaPrm;
import it.thera.thip.base.comuniVenAcq.StatoEvasione;
import it.thera.thip.base.documenti.TipoGestione;
import it.thera.thip.vendite.ordineVE.OrdineVendita;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRiga;
import it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmDataCollector;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 13/11/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72214    13/11/2025  DSSOF3   Prima stesura
 * 72335	03/02/2026	DSSOF3	 Rimuovere obbligatorieta' per ordini interni.
 * 72391	09/03/2026	DSSOF3	 Se la riga e' saldata metto in sola visualizzazione le data consegna richiesta/confermata.
 * 72425	30/03/2026	DSSOF3	 Aggiunto parametro per lista di causali per le quali l'area applicativa non deve essere mandatory.
 */

public class YOrdineVenditaRigaPrmDataCollector extends OrdineVenditaRigaPrmDataCollector {

	@Override
	public void updateHandlingModeOnComponentManagers() {
		super.updateHandlingModeOnComponentManagers();

		OrdineVenditaRiga riga = (OrdineVenditaRiga)getBo();

		OrdineVendita testata = (OrdineVendita) riga.getTestata();
		if(testata != null && !testata.getIdValuta().equals("EUR")){
			impostaHandlingModeOnComponentManagers((EnhBOComponentManager)getComponentManager("PrezzoEuro"), TipoGestione.SOLO_VISUALIZZ);
		}
		//72335 <
		if(testata != null 
				/* && (YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea() != null 
				&& testata.getIdCliente().equals(YPsnDatiRifornimentoLinea.getCurrentYPsnDatiRifornimentoLinea().getIdClienteIntestatario())
				|| */ && (YOrdineVenditaRigaPrm.isCausaleOrdineGestioneIdAreaApplicativaNonObbligatoria(testata))) { //72425
			impostaHandlingModeOnComponentManagers((EnhBOComponentManager)getComponentManager("IdAreaApplicativa"), TipoGestione.GESTITO_NON_OBBLIG);
		}
		//72335 >

		//72391 <
		if(riga.getStatoEvasione() == StatoEvasione.SALDATO) {
			impostaHandlingModeOnComponentManagers((EnhBOComponentManager)getComponentManager("DataConsegnaConfermata"), TipoGestione.SOLO_VISUALIZZ);
			impostaHandlingModeOnComponentManagers((EnhBOComponentManager)getComponentManager("DataConsegnaRichiesta"), TipoGestione.SOLO_VISUALIZZ);
		}
		//72391 >
	}
}