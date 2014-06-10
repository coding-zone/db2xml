package zone.coding.db2xml;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zone.coding.db2xml.entity.Column;


public class JdbcHelper {
	public static List<String> listTables(Connection db) throws SQLException {
		DatabaseMetaData meta = db.getMetaData();
		List<String> rc = new ArrayList<String>();
		ResultSet rs = meta.getTables(db.getCatalog(), null, "%", null);

		try {
			while (rs.next()) {
				String name = rs.getString("TABLE_NAME");
				String type = rs.getString("TABLE_TYPE");
				if ("table".equalsIgnoreCase(type)) {
					rc.add(name);
				}
			}
		}
		finally {
			rs.close();
		}

		return rc;
	}

	public static List<Column> getColumns(Connection db, String tableName) throws SQLException {
		DatabaseMetaData meta = db.getMetaData();
		List<Column> rc = new ArrayList<Column>();

		ResultSet rs = meta.getColumns(db.getCatalog(), null, tableName, null);
		try {
			while (rs.next()) {
				String name = rs.getString("COLUMN_NAME");
				String type = rs.getString("TYPE_NAME");
				int standardType = rs.getInt("DATA_TYPE");
				Integer size = rs.getInt("COLUMN_SIZE");
				if (rs.wasNull()) size = null;
				Boolean nullable = _parseNullable(rs.getString("IS_NULLABLE"));
				String comment = rs.getString("REMARKS");
				String defaultValue = rs.getString("COLUMN_DEF");

				rc.add(new Column(name, type, standardType, size, nullable, comment, defaultValue));
			}
		}
		finally {
			rs.close();
		}

		return rc;
	}

	public Map<String, Column> getColumnMap(Connection db, String tableName) throws SQLException {
		Map<String, Column> rc = new HashMap<String, Column>();

		for (Column column: getColumns(db, tableName)) {
			rc.put(column.getName(), column);
		}

		return rc;
	}

	private static Boolean _parseNullable(String nullable) {
		Boolean rc = null;

		if ("YES".equalsIgnoreCase(nullable)) {
			rc = true;
		}
		else if ("NO".equalsIgnoreCase(nullable)) {
			rc = false;
		}

		return rc;
	}
}
