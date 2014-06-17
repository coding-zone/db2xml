package zone.coding.db2xml.entity;

/**
 * Stores the meta data of a database table column
 */
public class Column {
	/**
	 * Constructor
	 * @param name Column name
	 * @param type Column data type (as reported by the database)
	 * @param standardType One of {@link java.sql.Types}
	 * @param size Datatype size
	 * @param nullable Nullability (true if nullable, false if not, null if undetermined)
	 * @param comment Column comment
	 * @param defaultValue Default value (plain strings are enclosed in single quotes, computed values + numbers aren't
	 */
	public Column(String name, String type, int standardType, Integer size, Boolean nullable, String comment, String defaultValue) {
		m_name = name;
		m_type = type;
		m_standardType = standardType;
		m_size = size;
		m_nullable = nullable;
		m_comment = comment;
		m_default = defaultValue;
	}

	/**
	 * Column name getter
	 * @return Column name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Column type getter
	 * @return Column type (might be DBMS specific)
	 */
	public String getType() {
		return m_type;
	}

	/**
	 * {@link java.sql.Types} getter
	 * @return One of the types defined in {@link java.sql.Types}
	 */
	public int getStandardType() {
		return m_standardType;
	}

	/**
	 * Column size getter (length for varchar, storage size for numbers, etc.)
	 * @return Column size
	 */
	public Integer getSize() {
		return m_size;
	}

	/**
	 * Returns whether or not the column is nullable
	 * @return true/false to indicate nullability of the column. <code>null</code> if undetermined
	 */
	public Boolean isNullable() {
		return m_nullable;
	}

	/**
	 * Returns the column's comment (or null if there isn't any)
	 * @return Column comment String
	 */
	public String getComment() {
		return m_comment;
	}

	/**
	 * Default value getter
	 * @return Column's default value (or null if there is none). String values are encased in single quotes.
	 */
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
