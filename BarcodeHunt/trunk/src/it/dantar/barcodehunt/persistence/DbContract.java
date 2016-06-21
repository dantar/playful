package it.dantar.barcodehunt.persistence;

import android.provider.BaseColumns;

public final class DbContract {

	public DbContract() {};
	
	// TABLES
	public static abstract class EquipmentEntry implements BaseColumns {
		public static final String TABLE_NAME = "equipment";
		public static final String C_NAME = "name";
		public static final String SQL_CREATE = String.format(
				"CREATE TABLE %s (%s TEXT)", TABLE_NAME, C_NAME); 
		public static final String SQL_DELETE = String.format(
				"DROP TABLE %s", TABLE_NAME); 
	}

	// SQL
	
}
