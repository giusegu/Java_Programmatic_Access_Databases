package panaderias;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBConnection {

	final static String NULL_SENTINEL_VARCHAR = "NULL";
	final static int NULL_SENTINEL_INT = Integer.MIN_VALUE;
	final static java.sql.Date NULL_SENTINEL_DATE = java.sql.Date.valueOf("1900-01-01");

	private Connection conn = null;
	private String user;
	private String pass;
	private String url;

	// Constructor de la clase DBConnection que recibe los parámetros necesarios
	// para establecer una conexión con la base de datos MySQL.

	public DBConnection(String server, int port, String user, String pass, String database) {
		// Asigna los parámetros recibidos a las variables de instancia
		// correspondientes.
		this.user = user;
		this.pass = pass;
		// Construye la URL de conexión utilizando los valores de server, port y
		// database.
		this.url = "jdbc:mysql://" + server + ":" + port + "/" + database;
	}

	// Método para establecer la conexión con la base de datos

	public boolean connect() {
		try {
			// Cargar el driver de la base de datos
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Verificar si ya se estableció la conexión previamente
			if (conn != null && !conn.isClosed()) {
				return true;
			}

			// Establecer la conexión con la base de datos
			conn = DriverManager.getConnection(url, user, pass);
			System.out.println("Base de datos conectada!");
			return true;

		} catch (SQLException | ClassNotFoundException e) {
			// En caso de error, imprimir el mensaje de error y retornar false
			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
			return false;
		}
	}

	// Este método intenta cerrar la conexión a la base de datos.
	// Devuelve true si la conexión ha sido cerrada correctamente, de lo contrario,
	// devuelve false.

	public boolean close() {
		try {
			// Verifica si la conexión es diferente de nula y no está cerrada
			if (conn != null && !conn.isClosed()) {
				conn.close(); // cierra la conexión
			}
		} catch (SQLException e) {
			e.printStackTrace(); // muestra información del error en la consola
			return false; // retorna false si hay un error al cerrar la conexión
		}
		return true; // retorna true si la conexión se cerró correctamente
	}

	// Este método actualiza la base de datos con la consulta SQL proporcionada
	// como parámetro y devuelve el número de filas afectadas.

	public int update(String sql) {
		int rowsAffected = -1;
		try {
			// Si la conexión con la base de datos es nula o está cerrada, se conecta de
			// nuevo
			if (conn == null || conn.isClosed()) {
				connect();
			}
			// Prepara la consulta SQL
			Statement stmt = conn.prepareStatement(sql);
			// Ejecuta la consulta y almacena el número de filas afectadas en rowsAffected
			rowsAffected = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Devuelve el número de filas afectadas
		return rowsAffected;
	}

	// Este método se encarga de ejecutar una sentencia SQL de tipo UPDATE y
	// devuelve el número de filas afectadas por la operación.

	public int update(String sql, ArrayList<Object> a) {
		int rowsAffected = -1;
		try {
			// Comprobamos si la conexión está cerrada o es nula, y la abrimos en caso
			// necesario
			if (conn == null || conn.isClosed()) {
				connect();
			}

			// Preparamos la sentencia SQL con los parámetros recibidos en el ArrayList
			PreparedStatement ps = conn.prepareStatement(sql);

			// Recorremos el ArrayList para establecer los valores de los parámetros en la
			// sentencia SQL
			for (int i = 0; i < a.size(); i++) {
				Object param = a.get(i);
				if (param == null) {
					// Si el parámetro es nulo, establecemos el valor centinela correspondiente
					if (param instanceof String) {
						ps.setString(i + 1, NULL_SENTINEL_VARCHAR);
					} else if (param instanceof Integer) {
						ps.setInt(i + 1, NULL_SENTINEL_INT);
					} else if (param instanceof Date) {
						ps.setDate(i + 1, NULL_SENTINEL_DATE);
					} else {
						throw new IllegalArgumentException(
								"Tipo de parámetro no soportado: " + param.getClass().getName());
					}
				} else {
					// Si el parámetro no es nulo, establecemos su valor correspondiente según su
					// tipo
					String className = param.getClass().getName();
					switch (className) {
					case "java.lang.Integer":
						ps.setInt(i + 1, (int) param);
						break;
					case "java.lang.Double":
						ps.setDouble(i + 1, (double) param);
						break;
					case "java.lang.Float":
						ps.setFloat(i + 1, (float) param);
						break;
					case "java.lang.Long":
						ps.setLong(i + 1, (long) param);
						break;
					case "java.lang.Short":
						ps.setShort(i + 1, (short) param);
						break;
					case "java.lang.Boolean":
						ps.setBoolean(i + 1, (boolean) param);
						break;
					case "java.lang.String":
						ps.setString(i + 1, (String) param);
						break;
					case "java.sql.Date":
						ps.setDate(i + 1, (java.sql.Date) param);
						break;
					default:
						throw new IllegalArgumentException("Tipo de parámetro no soportado: " + className);
					}
				}
			}
			// Ejecutamos la sentencia SQL y almacenamos el número de filas afectadas
			rowsAffected = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Devolvemos el número de filas afectadas por la operación
		return rowsAffected;
	}

	// Este método ejecuta una consulta SQL en la base de datos y devuelve un
	// ResultSet con los resultados obtenidos

	public ResultSet query(String sql) {
		try {
			// Si la conexión está cerrada o es nula, se abre una nueva conexión
			if (conn == null || conn.isClosed()) {
				connect();
			}
			// Se crea un objeto Statement para ejecutar la consulta
			Statement stmt = conn.createStatement();
			// Se devuelve el ResultSet obtenido al ejecutar la consulta
			return stmt.executeQuery(sql);
		} catch (SQLException e) {
			// Si se produce alguna excepción, se muestra un mensaje de error y se devuelve
			// null
			System.err.println("Error executing query: " + e.getMessage());
			return null;
		}
	}

	// Este método ejecuta una consulta SQL utilizando un objeto PreparedStatement y
	// los parámetros especificados en el ArrayList a

	public ResultSet query(String sql, ArrayList<Object> a) {
		ResultSet rs = null;

		try {

			// Comprobamos si la conexión está cerrada o nula, y la establecemos si es
			// necesario
			if (conn == null || conn.isClosed()) {
				connect();
			}

			// Creamos un objeto PreparedStatement a partir de la consulta SQL
			PreparedStatement ps = conn.prepareStatement(sql);

			// Iteramos sobre los parámetros de la consulta y los establecemos en el
			// PreparedStatement
			for (int i = 0; i < a.size(); i++) {
				Object param = a.get(i);
				if (param == null) {
					// Si el parametro es nulo, establecemos el valor centinela correspondiente
					if (param instanceof String) {
						ps.setString(i + 1, NULL_SENTINEL_VARCHAR);
					} else if (param instanceof Integer) {
						ps.setInt(i + 1, NULL_SENTINEL_INT);
					} else if (param instanceof Date) {
						ps.setDate(i + 1, NULL_SENTINEL_DATE);
					} else {
						throw new IllegalArgumentException(
								"Tipo de parámetro no soportado: " + param.getClass().getName());
					}
				} else {
					// Si el parametro no es nulo, establecemos su valor correspondiente
					String className = param.getClass().getName();
					switch (className) {
					case "java.lang.Integer":
						ps.setInt(i + 1, (int) param);
						break;
					case "java.lang.Double":
						ps.setDouble(i + 1, (double) param);
						break;
					case "java.lang.Float":
						ps.setFloat(i + 1, (float) param);
						break;
					case "java.lang.Long":
						ps.setLong(i + 1, (long) param);
						break;
					case "java.lang.Short":
						ps.setShort(i + 1, (short) param);
						break;
					case "java.lang.Boolean":
						ps.setBoolean(i + 1, (boolean) param);
						break;
					case "java.lang.String":
						ps.setString(i + 1, (String) param);
						break;
					case "java.sql.Date":
						ps.setDate(i + 1, (java.sql.Date) param);
						break;
					default:
						throw new IllegalArgumentException("Tipo de parámetro no soportado: " + className);
					}
				}
			}

			// Ejecutamos la consulta y obtenemos el ResultSet resultante
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Retornamos el ResultSet resultante
		return rs;
	}

	// Este método comprueba si una tabla con el nombre especificado existe en la
	// base de datos.

	public boolean tableExists(String tableName) {
		// Inicializa el indicador de existencia de la tabla como falso
		boolean existe = false;
		try {
			// Ejecuta una consulta "SHOW TABLES" para obtener los nombres de todas las
			// tablas en la base de datos
			ResultSet res = query("SHOW TABLES", new ArrayList<Object>());
			// Recorre los resultados obtenidos hasta encontrar la tabla buscada o hasta
			// agotar los resultados
			while (res.next() && !existe) {
				// Obtiene el nombre de la tabla actual
				String table = res.getString(1);
				// Compara el nombre de la tabla actual con el nombre buscado, ignorando
				// mayúsculas y minúsculas
				if (table.equalsIgnoreCase(tableName)) {
					// Si el nombre coincide, actualiza el indicador de existencia a verdadero
					existe = true;
				}
			}
			// Cierra el objeto ResultSet
			res.close();
		} catch (SQLException e) {
			// Si se produce una excepción al ejecutar la consulta, imprime un mensaje de
			// error
			System.err.println("Error con SQL query: " + e.getMessage());
		}
		// Devuelve el indicador de existencia de la tabla
		return existe;
	}
}
