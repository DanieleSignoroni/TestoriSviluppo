package it.testori.thip.base.cliente;

import java.sql.*;
import it.thera.thip.base.cliente.*;
import com.thera.thermfw.base.*;

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

public class YModalitaPagamentoTM extends ModalitaPagamentoPrimroseTM {

	public static final String R_BLOCCO_EVASIONE = "R_BLOCCO_EVASIONE";

	public static final String R_BLOCCO_PIANIFICAZIONE = "R_BLOCCO_PIANIFICAZIONE";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YMODALITA_PAGAMENTO";

	private static final String CLASS_NAME = it.testori.thip.base.cliente.YModalitaPagamento.class.getName();

	public YModalitaPagamentoTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("IdBloccoEvasione", R_BLOCCO_EVASIONE, TABLE_NAME_EXT);
		addAttributeOnTable("IdBloccoPianif", R_BLOCCO_PIANIFICAZIONE, TABLE_NAME_EXT);

	}

}