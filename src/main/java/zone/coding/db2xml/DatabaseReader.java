package zone.coding.db2xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseReader {
	public static void read(Connection db, XmlWriter writer, Properties props) throws SQLException {
		for (String tableName: JdbcHelper.listTables(db)) {
			_writeTable(db, writer, tableName, props);
		}
		System.out.println("done...");
	}

	public static void read(Connection db, XmlWriter writer) throws SQLException {
		read(db, writer, new Properties());
	}

	private static void _writeTable(Connection db, XmlWriter writer, String tableName, Properties props) throws SQLException {
		System.out.println("Extracting table '"+tableName+"'");
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
