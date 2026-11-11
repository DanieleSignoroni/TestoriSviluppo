package it.testori.thip.base.generale;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;

import it.testori.thip.base.cliente.YClienteVendita;
import it.testori.thip.logis.bas.YCostantiTestori;
import it.testori.thip.logis.lgb.YRigaLista;
import it.testori.thip.magazzino.generalemag.YLotto;
import it.thera.thip.base.generale.CollegamentoLogisThip;
import it.thera.thip.base.generale.RigaLogisThip;
import it.thera.thip.base.generale.TestataLogisThip;
import it.thera.thip.base.generale.UnitaMisura;
import it.thera.thip.logis.lgb.RigaLista;
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
 * 72233	02/12/2025	DSSOF3	 Lock type in YCostantiTestori.documentoVenditaTestataLista
 * 72749	11/11/2026	DSSOF3	 Settaggio peso netto.
 */

public class YCollegamentoLogisThip extends CollegamentoLogisThip {

	@Override
	protected RigaLogisThip settaAttributiDaRigaLista(TestataLogisThip tlt, RigaLista rl) {
		RigaLogisThip rlt = super.settaAttributiDaRigaLista(tlt, rl);

		//..Provo a bonificare il lotto
		UnitaMisura umPezze = YCostantiTestori.getUnitaMisuraGestionePezze();
		if(umPezze != null && umPezze.getIdUnitaMisura().equals(rl.getUmDocumento())
				&& rl.getLotto1() != null) {
			DocumentoVendita dv = YCostantiTestori.documentoVenditaTestataLista(rl.getTestataLista(), PersistentObject.NO_LOCK); //72233
			if(dv != null) {
				YLotto lotto;
				try {
					lotto = (YLotto) YLotto.elementWithKey(YLotto.class, KeyHelper.buildObjectKey(new String[] {
							rl.getCodiceSocieta(),rl.getCodiceArticolo(),rl.getLotto1()
					}), PersistentObject.NO_LOCK);
					if(lotto != null && lotto.getQuantitaBonifico() != null && lotto.getQuantitaBonifico().compareTo(BigDecimal.ZERO) > 0) {
						YClienteVendita cliente = (YClienteVendita) dv.getCliente();
						if(cliente != null && cliente.isBonificoPezza()) {
							rlt.setQta1Evasa(rlt.getQta1Evasa().subtract(lotto.getQuantitaBonifico()));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		
		//72749 <
		if(rl != null
				&& rl instanceof YRigaLista
				&& rlt instanceof YRigaLogisThip)
			((YRigaLogisThip)rlt).setPesoNetto(((YRigaLista)rl).getPesoNetto());
		//72749 >

		return rlt;
	}
}