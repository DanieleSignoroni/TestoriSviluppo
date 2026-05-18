package it.testori.thip.base.cliente;

import com.thera.thermfw.persist.*;
import it.thera.thip.base.cliente.MotivoBloccoEvasione;
import it.thera.thip.base.cliente.MotivoBloccoOrdine;
import it.thera.thip.base.cliente.*;
import it.thera.thip.base.azienda.Azienda;

/**
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 18/05/2026
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72XXX    18/05/2026  DSSOF3   Prima stesura
 */

public class YModalitaPagamento extends ModalitaPagamentoPrimrose {

	protected Proxy iMotivoBloccoEvasione = new Proxy(it.thera.thip.base.cliente.MotivoBloccoEvasione.class);

	protected Proxy iMotivoBloccoPianif = new Proxy(it.thera.thip.base.cliente.MotivoBloccoOrdine.class);

	public YModalitaPagamento() {
		super();
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setMotivoBloccoEvasione(MotivoBloccoEvasione motivobloccoevasione) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (motivobloccoevasione != null) {
			idAzienda = KeyHelper.getTokenObjectKey(motivobloccoevasione.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iMotivoBloccoEvasione.setObject(motivobloccoevasione);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public MotivoBloccoEvasione getMotivoBloccoEvasione() {
		return (MotivoBloccoEvasione)iMotivoBloccoEvasione.getObject();
	}

	public void setMotivoBloccoEvasioneKey(String key) {
		String oldObjectKey = getKey();
		iMotivoBloccoEvasione.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getMotivoBloccoEvasioneKey() {
		return iMotivoBloccoEvasione.getKey();
	}

	public void setIdBloccoEvasione(String idBloccoEvasione) {
		String key = iMotivoBloccoEvasione.getKey();
		iMotivoBloccoEvasione.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idBloccoEvasione));
		setDirty();
	}

	public String getIdBloccoEvasione() {
		String key = iMotivoBloccoEvasione.getKey();
		String objIdBloccoEvasione = KeyHelper.getTokenObjectKey(key,2);
		return objIdBloccoEvasione;
	}

	public void setMotivoBloccoPianif(MotivoBloccoOrdine motivobloccopianif) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (motivobloccopianif != null) {
			idAzienda = KeyHelper.getTokenObjectKey(motivobloccopianif.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iMotivoBloccoPianif.setObject(motivobloccopianif);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public MotivoBloccoOrdine getMotivoBloccoPianif() {
		return (MotivoBloccoOrdine)iMotivoBloccoPianif.getObject();
	}

	public void setMotivoBloccoPianifKey(String key) {
		String oldObjectKey = getKey();
		iMotivoBloccoPianif.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getMotivoBloccoPianifKey() {
		return iMotivoBloccoPianif.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		setIdAziendaInternal(idAzienda);
	}

	public void setIdBloccoPianif(String idBloccoPianif) {
		String key = iMotivoBloccoPianif.getKey();
		iMotivoBloccoPianif.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idBloccoPianif));
		setDirty();
	}

	public String getIdBloccoPianif() {
		String key = iMotivoBloccoPianif.getKey();
		String objIdBloccoPianif = KeyHelper.getTokenObjectKey(key,2);
		return objIdBloccoPianif;
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YModalitaPagamento yModalitaPagamento = (YModalitaPagamento)obj;
		iMotivoBloccoEvasione.setEqual(yModalitaPagamento.iMotivoBloccoEvasione);
		iMotivoBloccoPianif.setEqual(yModalitaPagamento.iMotivoBloccoPianif);
	}

	protected void setIdAziendaInternal(String idAzienda) {
		if(iMotivoBloccoEvasione != null) {
			String key1 = iMotivoBloccoEvasione.getKey();
			iMotivoBloccoEvasione.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, idAzienda));
		}
		if(iMotivoBloccoPianif != null) {
			String key2 = iMotivoBloccoPianif.getKey();
			iMotivoBloccoPianif.setKey(KeyHelper.replaceTokenObjectKey(key2, 1, idAzienda));
		}
	}

}

