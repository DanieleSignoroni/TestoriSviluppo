package it.testori.thip.vendite.ordineVE.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.thera.thermfw.ad.ClassAD;
import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.ad.SimpleClassAD;
import com.thera.thermfw.gui.cnr.DOList;
import com.thera.thermfw.persist.PersistentObject;

import it.thera.thip.base.comuniVenAcq.DocumentoOrdineRigaLotto;
import it.thera.thip.base.documenti.DocumentoBaseRiga;
import it.thera.thip.magazzino.generalemag.LottiSaldiTM;
import it.thera.thip.magazzino.generalemag.Lotto;

/**
 *
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 21/01/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    21/01/2026  DSSOF3   Prima stesura
 */

public class YSelezioneMultiplaLottoDOList extends DOList {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setRestrictCondition(ClassAD[] attributes, String[] values) {
		String className = null;
		String docRigaKey = null;
		List<ClassAD> attrList = new ArrayList();
		List<String> valList = new ArrayList<>();
		for (int i = 0; i < attributes.length; i++) {
			SimpleClassAD attribute = (SimpleClassAD) attributes[i];
			if (attribute != null) {
				String attrName = attribute.getAttributeName();
				if ("ChiaveLottoProdotto2".equals(attrName)) {
					docRigaKey = values[i];
				} 
				else if ("ChiaveLottoProdotto1".equals(attrName)) {
					className = values[i];
				} 
				else {
					attrList.add(attributes[i]);
					valList.add(values[i]);
				}
			}
		}

		ClassAD[] attributesOk = attrList.toArray(new ClassAD[0]);
		String[] valuesOk = valList.toArray(new String[0]);

		super.setRestrictCondition(attributesOk, valuesOk);

		ClassADCollection cadc;
		try {
			cadc = ClassADCollectionManager.collectionWithName(className);
			if(cadc != null) {
				DocumentoBaseRiga docRiga = (DocumentoBaseRiga) PersistentObject.elementWithKey(cadc.getBOClassName(), docRigaKey, PersistentObject.NO_LOCK);
				if(docRiga != null) {
					Iterator iterLotti = docRiga.getRigheLotto().iterator();
					while(iterLotti.hasNext()) {
						DocumentoOrdineRigaLotto docRigaLotto = (DocumentoOrdineRigaLotto) iterLotti.next();

						specificWhereClause += " AND ("+LottiSaldiTM.TABLE_NAME+"."+LottiSaldiTM.ID_LOTTO+" <> '"+docRigaLotto.getIdLotto()+"')  ";

					}
				}
			}
		} catch (NoSuchElementException | NoSuchFieldException | SQLException e) {
			e.printStackTrace();
		}

		specificWhereClause += " AND ("+LottiSaldiTM.TABLE_NAME+"."+LottiSaldiTM.ID_LOTTO+" <> '"+Lotto.LOTTO_DUMMY+"')  ";
	}
}
