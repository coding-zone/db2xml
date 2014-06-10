package zone.coding.db2xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
		if (args.length != 2) {
			System.err.println("USAGE: java ... <jdbc.properties> <out.sql>");
			System.exit(1);
		}

		Properties props = new Properties();
		props.load(new FileInputStream(args[0]));

		String driverClass = props.getProperty("driverClass");
		String dsn = props.getProperty("url");
		String user = props.getProperty("username");
		String password = props.getProperty("password");
		String outPath = args[1];

		if (driverClass != null) {
			Class.forName(driverClass);
		}

		OutputStream os = new FileOutputStream(outPath);
		Connection db = DriverManager.getConnection(dsn, user, password);
		XmlWriter writer = new XmlWriter(os, "db");

		try {
			DatabaseReader.read(db, writer, props);
		}
		finally {
			writer.close();
		}
	}

}
