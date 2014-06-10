package zone.coding.db2xml.entity;

public class Column {
	public Column(String name, String type, int standardType, Integer size, Boolean nullable, String comment, String defaultValue) {
		m_name = name;
		m_type = type;
		m_standardType = standardType;
		m_size = size;
		m_nullable = nullable;
		m_comment = comment;
		m_default = defaultValue;
	}

	public String getName() {
		return m_name;
	}

	public String getType() {
		return m_type;
	}

	public int getStandardType() {
		return m_standardType;
	}

	public Integer getSize() {
		return m_size;
	}

	public Boolean isNullable() {
		return m_nullable;
	}

	public String getComment() {
		return m_comment;
	}

	public String getDefault() {
		return m_default;
	}

	private final String m_name;
	private final String m_type;
	private final int m_standardType;
	private final Integer m_size;
	private final Boolean m_nullable;
	private final String m_comment;
	private final String m_default;
}
