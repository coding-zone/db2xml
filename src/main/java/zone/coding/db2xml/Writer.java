package zone.coding.db2xml;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import zone.coding.db2xml.entity.Column;

/**
 * Writes database schema+data to some target (e.g. an XML file in XmlWriter)
 *
 */
public interface Writer extends Closeable {
	/**
	 * Start writing a new table
	 * @param tableName Table name
	 */
	public void startTable(String tableName);

	/**
	 * Done writing the table
	 */
	public void endTable();

	/**
	 * Got meta information about the current table's columns
	 * @param columns List of columns and their details
	 */
	public void writeColumnInfo(List<Column> columns);

	/**
	 * Got a table row (the actual table data)
	 *
	 * Don't call rs.next(), that's been done by DatabaseReader
	 * TODO Don't use a ResultSet here. We shouldn't rely on database stuff on the writer side.
	 * @param rs ResultSet pointed on the row to write
	 * @throws SQLException
	 */
	public void writeRow(ResultSet rs) throws SQLException;
}
