package zone.coding.db2xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseReader {
	public static void read(Connection db, Writer writer, Properties props) throws SQLException {
		Set<String> excludedTables = _getExcludedList(props);
		System.out.println("Extracting tables:");
		for (String tableName: JdbcHelper.listTables(db)) {
			if (!excludedTables.contains(tableName)) {
				_writeTable(db, writer, tableName, props);
			}
			else {
				System.out.println("\t'"+tableName+"' (excluded)");
			}
		}
		System.out.println("done...");
	}

	public static void read(Connection db, XmlWriter writer) throws SQLException {
		read(db, writer, new Properties());
	}

	private static Set<String> _getExcludedList(Properties props) {
		Set<String> rc = new TreeSet<String>();
		String list[] = props.getProperty("excluded", "").split(",");

		for (String tableName: list) {
			rc.add(tableName);
		}

		return rc;
	}

	private static void _writeTable(Connection db, Writer writer, String tableName, Properties props) throws SQLException {
		System.out.println("\t'"+tableName+"'");
		boolean includeSchema = new Boolean(props.getProperty("includeSchema", "true"));
		boolean includeData = new Boolean(props.getProperty("includeData", "true"));

		writer.startTable(tableName);
		try {
			if (includeSchema) {
				// first the schema
				writer.writeColumnInfo(JdbcHelper.getColumns(db, tableName));
			}
			if (includeData) {
				// then the data
				PreparedStatement pstmt = db.prepareStatement("SELECT * FROM "+tableName);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					writer.writeRow(rs);
				}
			}
		}
		finally {
			writer.endTable();
		}
	}

	public static void _readData(Connection db, XmlWriter writer) {
		
	}
}
