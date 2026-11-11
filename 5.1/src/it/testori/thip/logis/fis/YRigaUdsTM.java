package it.testori.thip.logis.fis;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;

import it.thera.thip.logis.fis.RigaUdsTM;

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

public class YRigaUdsTM extends RigaUdsTM {

	public static final String PESO_NETTO = "PESO_NETTO";

	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIPPERS") + "YLUDS_RIGA";

	private static final String CLASS_NAME = it.testori.thip.logis.fis.YRigaUds.class.getName();

	public YRigaUdsTM() throws SQLException {
		super();
	}

	public void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	public void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("PesoNetto", PESO_NETTO, TABLE_NAME_EXT);

	}

}
