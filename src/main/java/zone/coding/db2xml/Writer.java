package zone.coding.db2xml;

import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import zone.coding.db2xml.entity.Column;

public interface Writer extends Closeable {
	public void startTable(String tableName);
	public void endTable();

	public void writeColumnInfo(List<Column> columns);
	public void writeRow(ResultSet rs) throws SQLException;
}
