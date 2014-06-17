package zone.coding.db2xml;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import zone.coding.db2xml.entity.Column;

import com.ociweb.xml.Version;
import com.ociweb.xml.WAX;


public class XmlWriter implements Writer {
	private WAX m_wax;

	public XmlWriter(OutputStream os, String rootName) {
		m_wax = new WAX(os, Version.V1_0);
		m_wax.start(rootName);
	}

	public void close() throws IOException {
		m_wax.close();
	}

	public void startTable(String tableName) {
		m_wax.start("table").attr("name", tableName);
	}

	public void endTable() {
		m_wax.end();
	}

	public void writeColumnInfo(List<Column> columns) {
		for (Column column: columns) {
			writeColumnInfo(column);
		}
	}

	public void writeColumnInfo(Column column) {
		m_wax.start("column");
		try {
			m_wax.attr("name", column.getName());
			m_wax.attr("type", column.getType());
	
			if (column.getSize() != null) m_wax.attr("size", column.getSize());
			if (column.isNullable() != null) m_wax.attr("nullable", column.isNullable());
			if (column.getDefault() != null) m_wax.attr("defaultValue", column.getDefault());
			//m_wax.attr("javaType", column.getStandardType());
			if (_isBinary(column.getStandardType())) {
				m_wax.attr("base64", true);
			}
		}
		finally {
			m_wax.end();
		}
	}

	public void writeRow(ResultSet rs) throws SQLException {
		m_wax.start("row");
		try {
			ResultSetMetaData meta = rs.getMetaData();
			int columns = meta.getColumnCount();

			for (int i = 1; i <= columns; i++) {
				String name = meta.getColumnName(i);
				String value = null;
				
				if (_isBinary(meta.getColumnType(i))) {
					value = Base64.encodeBase64String(rs.getBytes(i));
				}
				else {
					value = rs.getString(i);
				}

				m_wax.attr(name, value);
			}
		}
		finally {
			m_wax.end();
		}
	}

	private static boolean _isBinary(int sqlType) {
		boolean rc = false;

		switch (sqlType) {
		case Types.BINARY:
		case Types.LONGVARBINARY:
		case Types.VARBINARY:
			rc = true;
		}

		return rc;
	}
}
