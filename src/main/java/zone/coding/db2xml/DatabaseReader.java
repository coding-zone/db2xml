package zone.coding.db2xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Reads the database schema and data and calls {@link Writer} methods accordingly
 */
public class DatabaseReader {
	/**
	 * Reads the database schema and data and calls writer's methods accordingly.
	 *
	 * With props you can specify whether or not you want to include the schema
	 * and/or data in the output and which of the tables to exclude
	 * (see jdbc.properties.example for further details)
	 *
	 * @param db Database {@link Connection}
	 * @param writer {@link Writer} implementation (e.g. XmlWriter)
	 * @param props Configuration (see jdbc.properties.example for details)
	 * @throws SQLException
	 */
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

	/**
	 * Reads the database schema and data and calls writer's methods accordingly.
	 *
	 * This overloaded method calls the other read() with empty Properties
	 * @param db Database connection
	 * @param writer Writer implementation (e.g. XmlWriter)
	 * @throws SQLException
	 */
	public static void read(Connection db, XmlWriter writer) throws SQLException {
		read(db, writer, new Properties());
	}

	/**
	 * Read the list of excluded tables from the {@link Properties} file (key: "excluded")
	 * @param props Configuration to use
	 * @return String Set with table names to exclude
	 */
	private static Set<String> _getExcludedList(Properties props) {
		Set<String> rc = new TreeSet<String>();
		String list[] = props.getProperty("excluded", "").split(",");

		for (String tableName: list) {
			rc.add(tableName);
		}

		return rc;
	}

	/**
	 * Call the Writer with the given table's information.
	 *
	 * This method is called by read() for each table to extract
	 * @param db Database {@link Connection}
	 * @param writer {@link Writer} implementation (e.g. XmlWriter)
	 * @param tableName Table to extract
	 * @param props Configuration (to specify whether to include the schema/data)
	 * @throws SQLException
	 */
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
}
