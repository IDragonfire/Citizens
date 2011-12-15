package net.citizensnpcs.idragonfire.keys;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.citizensnpcs.idragonfire.values.DIntValue;
import net.citizensnpcs.idragonfire.values.DStringValue;
import net.citizensnpcs.idragonfire.values.DValue;

/**
 * Represent a key of a value/entry, used to identify entries into the HashMap,
 * MySQL and YML file
 * 
 * @author IDragonfire
 * 
 */
public abstract class DKey {

	public abstract DValue fetch(ResultSet rs) throws SQLException;

	public abstract String[] getYMLCall();

	public abstract String getSQL_SELECT();

	public static DIntValue getDIntValueFromResultSet(ResultSet rs)
			throws SQLException {
		return new DIntValue(rs.getInt(1));
	}

	public static DStringValue getDStringValueFromResultSet(ResultSet rs)
			throws SQLException {
		return new DStringValue(rs.getString(1));
	}

	@Override
	public String toString() {
		return getClass().toString();
	}
}
