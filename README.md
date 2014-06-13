db2xml
======

Connects to any JDBC-compatible database and extracts its schema+data into an XML file

Requires Apache Maven and JDBC drivers for the database you want to connect to (drivers for some of the more common databases are already included as runtime dependencies)
Usage
-----

mvn exec:java -DdbDriver=path/to/jdbcDriver.jar -Dexec.args="jdbc.properties db.xml"

* `dbDriver` is optional. It is only required if you want to load another JDBC driver .jar
* The arguments defined in `exec.args` are:
  * `jdbc.properties`: Path to your jdbc.properties file (look at `jdbc.properties.example` for an example config
  * `db.xml`: XML file to export to


