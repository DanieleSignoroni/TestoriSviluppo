package it.testori.thip.vendite.ordineVE;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.thera.thermfw.base.Utils;
import com.thera.thermfw.persist.*;
import it.thera.thip.vendite.ordineVE.*;
import it.testori.thip.base.cliente.YClienteFinale;
import it.testori.thip.base.cliente.YModalitaPagamento;

/**
 * <p></p>
 *
 * <p>
 * Company: Softre Solutions<br>
 * Author: Daniele Signoroni<br>
 * Date: 12/10/2025
 * </p>
 */

/*
 * Revisions:
 * Number   Date        Owner    Description
 * 72166    12/10/2025  DSSOF3   Prima stesura
 * 72272	07/01/2026	DSSOF3	 Aggiunta valore totale ordinato in valuta a db.
 */

public class YOrdineVendita extends OrdineVendita {

	//72272
	private static final String STMT_UPDATE_VLR_ORD_VA = "UPDATE "+YOrdineVenditaTM.TABLE_NAME_EXT+"  "
			+ "SET "+YOrdineVenditaTM.YVLR_TOT_ORD_VA+" = ? "
			+ "WHERE "+YOrdineVenditaTM.ID_AZIENDA+" = ? "
			+ "AND "+YOrdineVenditaTM.ID_ANNO_ORDINE+" = ? "
			+ "AND "+YOrdineVenditaTM.ID_NUMERO_ORD+" = ? ";
	public static CachedStatement cUpdateValoreOrdinatoVA = new CachedStatement(STMT_UPDATE_VLR_ORD_VA);
	//72272

	protected Proxy iClienteFinale = new Proxy(YClienteFinale.class);

	protected BigDecimal iYValoreTotOrdVA; //72272

	public YOrdineVendita() {
		super();
	}

	public void setClienteFinale(YClienteFinale Cliente){
		this.iClienteFinale.setObject(Cliente);
		setDirty();
	}

	public YClienteFinale getClienteFinale(){
		return (YClienteFinale) iClienteFinale.getObject();
	}

	public void setClienteFinaleKey(String key){
		iClienteFinale.setKey(key);
		setDirty();
	}

	public String getClienteFinaleKey(){
		return iClienteFinale.getKey();
	}

	public void setIdCodiceClienteFinale(Integer rCliente){
		String key = iClienteFinale.getKey();
		iClienteFinale.setKey(KeyHelper.replaceTokenObjectKey(key, 3, rCliente));
		setDirty();
	}

	public Integer getIdCodiceClienteFinale(){
		String key = iClienteFinale.getKey();
		Integer objRCliente = KeyHelper.stringToIntegerObj(KeyHelper.getTokenObjectKey(key, 3));
		return objRCliente;
	}

	//72272
	public BigDecimal getYValoreTotOrdVA() {
		return iYValoreTotOrdVA;
	}

	public void setYValoreTotOrdVA(BigDecimal iYValoreTotOrdVA) {
		this.iYValoreTotOrdVA = iYValoreTotOrdVA;
		setDirty();
	}
	//72272

	@Override
	public void setIdCliente(String rCliente) {
		super.setIdCliente(rCliente);
		String key = iClienteFinale.getKey();
		iClienteFinale.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rCliente));
	}

	@Override
	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		if(iClienteFinale != null) {
			iClienteFinale.setKey(KeyHelper.replaceTokenObjectKey(iClienteFinale.getKey(), 1, idAzienda));
		}
	}

	@Override
	public void completaBO() {
		super.completaBO();
		recuperaBlocchiModalitaPagamento();
	}
	
	public void recuperaBlocchiModalitaPagamento() {
		if(getModalitaPagamento() != null
				&& getModalitaPagamento() instanceof YModalitaPagamento) {
			YModalitaPagamento ymod = (YModalitaPagamento) getModalitaPagamento();
			if(ymod.getMotivoBloccoEvasione() != null)
				setMotivoBloccoEvasione(ymod.getMotivoBloccoEvasione());
			if(ymod.getMotivoBloccoPianif() != null)
				setMotivoBloccoPianif(ymod.getMotivoBloccoPianif());
		}
	}

	@Override
	public int save() throws SQLException {
		if(isOnDB() 
				&& getOldTestata() != null
				&& !Utils.areEqual(getIdModPagamento(), getOldTestata().getIdModPagamento())) {
			recuperaBlocchiModalitaPagamento();
		}
		int rc = super.save();
		if(rc > 0 && isOnDB()) {
			//72272
			BigDecimal valoreOrdinatoVA = getValoreImponibileVA();
			if(valoreOrdinatoVA != null) {
				aggiornaValoreOrdinatoVA(valoreOrdinatoVA);
			}
			//72272
		}
		return rc;
	}

	//72272
	public int aggiornaValoreOrdinatoVA(BigDecimal val) throws SQLException {
		PreparedStatement ps = cUpdateValoreOrdinatoVA.getStatement();
		Database db = ConnectionManager.getCurrentDatabase();
		ps.setBigDecimal(1, val);
		db.setString(ps, 2, getIdAzienda());
		db.setString(ps, 3, getAnnoDocumento());
		db.setString(ps, 4, getNumeroDocumento());
		if (ps.executeUpdate() >= 0)
			return 1;
		return -100;
	}
	//72272

	@Override
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YOrdineVendita ordineVendita = (YOrdineVendita) obj;
		iClienteFinale.setEqual(ordineVendita.iClienteFinale);
	}

}