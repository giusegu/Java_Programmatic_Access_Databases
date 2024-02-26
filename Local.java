package panaderias;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Local extends DBTable {

	private int id_local;
	private int tiene_cafeteria;
	private String direccion;
	private String descripcion;

	// Constructor de la clase Local que recibe el ID, la conexión a la base de
	// datos y un booleano que indica si se debe sincronizar con la base de datos
	public Local(int id_local, DBConnection conn, boolean DBSync) {
		// Llamada al constructor de la clase padre con la conexión a la base de datos y
		// el booleano de sincronización
		super(conn, DBSync);
		// Asignación del ID local y de los valores centinela a los otros atributos
		this.id_local = id_local;
		this.tiene_cafeteria = DBConnection.NULL_SENTINEL_INT;
		this.direccion = DBConnection.NULL_SENTINEL_VARCHAR;
		this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;
		// Si se debe sincronizar con la base de datos
		if (DBSync) {
			// Creación de la tabla
			createTable();
			// Si no se puede insertar la entrada en la tabla, se asignan valores
			// centinela a los atributos correspondientes
			if (insertEntry() == false) {
				this.id_local = DBConnection.NULL_SENTINEL_INT;
				setSync(false);
			}
		}
	}

	// Constructor de la clase Local que recibe todos los atributos, la conexión a
	// la base de datos y un booleano que indica si se debe sincronizar con la base
	// de datos
	public Local(int id_local, boolean tiene_cafeteria, String direccion, String descripcion, DBConnection conn,
			boolean DBSync) {
		// Llamada al constructor de la clase padre con la conexión a la base de datos y
		// el booleano de sincronización
		super(conn, DBSync);
		// Asignación de los atributos correspondientes
		this.id_local = id_local;
		this.tiene_cafeteria = tiene_cafeteria ? 1 : 0;
		this.direccion = direccion;
		this.descripcion = descripcion;
		// Si se debe sincronizar con la base de datos
		if (DBSync) {
			// Creación de la tabla
			createTable();
			// Si no se puede insertar la entrada en la tabla, se asignan valores
			// centinela a los atributos correspondientes
			if (insertEntry() == false) {
				this.tiene_cafeteria = DBConnection.NULL_SENTINEL_INT;
				this.direccion = DBConnection.NULL_SENTINEL_VARCHAR;
				this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;
				setSync(false);
			}
		}
	}

	// Obtener el id_local
	public int getId_local() {
		// Si está activada la sincronización con la base de datos, se obtienen los
		// cambios
		if (DBSync) {
			getEntryChanges();
		}
		return id_local;
	}

	// Obtener si tiene cafetería
	public boolean getTiene_cafeteria() {
		// Si está activada la sincronización con la base de datos, se obtienen los
		// cambios
		if (DBSync) {
			getEntryChanges();
		}
		// Si tiene_cafeteria es igual a 1, retorna true, de lo contrario false
		return tiene_cafeteria == 1 ? true : false;
	}

	// Establecer si tiene cafetería
	public void setTiene_cafeteria(boolean tiene_cafeteria) {
		// Si tiene_cafeteria es true, se establece 1, de lo contrario 0
		this.tiene_cafeteria = tiene_cafeteria ? 1 : 0;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada
		if (DBSync) {
			updateEntry();
		}
	}

	// Obtener la dirección
	public String getDireccion() {
		// Si está activada la sincronización con la base de datos, se obtienen los
		// cambios
		if (DBSync) {
			getEntryChanges();
		}
		return direccion;
	}

	// Establecer la dirección
	public void setDireccion(String direccion) {
		this.direccion = direccion;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada
		if (DBSync) {
			updateEntry();
		}
	}

	// Obtener la descripción
	public String getDescripcion() {
		// Si está activada la sincronización con la base de datos, se obtienen los
		// cambios
		if (DBSync) {
			getEntryChanges();
		}
		return descripcion;
	}

	// Establecer la descripción
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada
		if (DBSync) {
			updateEntry();
		}
	}

	// Este método se encarga de eliminar un registro de la tabla Local
	// correspondiente al objeto actual.
	public void destroy() {
		// Si DBSync es true, se debe sincronizar la eliminación con la base de datos
		if (DBSync) {
			// Crear la consulta SQL para eliminar el registro correspondiente al objeto
			// actual
			String query = "DELETE FROM Local WHERE id_local = ?";
			// Crear una lista de parámetros con el id_local del objeto actual
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			// Ejecutar la consulta SQL utilizando el método update de DBConnection
			conn.update(query, params);
		}
		// Establecer los valores de los atributos a NULL_SENTINEL_INT o
		// NULL_SENTINEL_VARCHAR
		this.id_local = DBConnection.NULL_SENTINEL_INT;
		this.tiene_cafeteria = DBConnection.NULL_SENTINEL_INT;
		this.direccion = DBConnection.NULL_SENTINEL_VARCHAR;
		this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;
		// Establecer la sincronización con la base de datos a false
		setSync(false);
	}

	// Este método se encarga de crear la tabla "Local" en la base de datos

	boolean createTable() {
		try {
			// Definición de la consulta SQL para crear la tabla "Local"
			String query = "CREATE TABLE Local (" + "id_local INT PRIMARY KEY, " + "tiene_cafeteria INT NOT NULL, "
					+ "direccion VARCHAR(100) NOT NULL, " + "descripcion VARCHAR(100) NOT NULL)";

			// Verificar si la tabla ya existe en la base de datos
			if (conn.tableExists("Local")) {
				// Si ya existe, se desactiva la sincronización y se retorna false
				setSync(false);
				return false;
			}

			// Si la tabla no existe, se ejecuta la consulta para crearla
			if (conn.update(query) != -1) {
				// Si la operación fue exitosa, se activa la sincronización y se retorna true
				setSync(true);
				return true;
			} else {
				// Si hubo algún problema en la operación, se desactiva la sincronización y se
				// retorna false
				setSync(false);
				return false;
			}

		} catch (Exception e) {
			// Si se produce una excepción, se imprime en consola y se retorna false
			e.printStackTrace();
			return false;
		}
	}

	// Método que inserta una entrada en la tabla Local de la base de datos
	boolean insertEntry() {
		// Inicialización de variable de éxito en false
		boolean success = false;

		// Definición de la consulta SQL con parámetros
		String query = "INSERT INTO Local (id_local, tiene_cafeteria, direccion, descripcion) VALUES (?, ?, ?, ?)";
		try {

			// Creación de una lista de parámetros para la consulta SQL
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			params.add(tiene_cafeteria);
			params.add(direccion);
			params.add(descripcion);

			// Ejecución de la consulta SQL con los parámetros y recogida del número de
			// filas afectadas
			int rowsUpdated = conn.update(query, params);

			// Si la consulta actualizó al menos una fila, se establece el objeto como
			// sincronizado y se devuelve éxito
			if (rowsUpdated > 0) {

				setSync(true);
				success = true;
			} else {

				// Si no se actualizó ninguna fila, se establece el objeto como no sincronizado
				setSync(false);
			}
		} catch (Exception e) {
			// Si se produce una excepción, se imprime el error en la consola y se devuelve
			// null
			e.printStackTrace();
		}
		// Devuelve el valor de éxito
		return success;

	}

	// Método encargado de actualizar una entrada en la tabla Local
	boolean updateEntry() {
		boolean success = false;
		String query = "UPDATE Local SET tiene_cafeteria=?, direccion=?, descripcion=? WHERE id_local=?";
		try {
			// Creamos un ArrayList para guardar los parámetros de la consulta
			ArrayList<Object> params = new ArrayList<>();
			params.add(tiene_cafeteria);
			params.add(direccion);
			params.add(descripcion);
			params.add(id_local);

			// Ejecutamos la consulta de actualización y guardamos el número de filas
			// afectadas
			int rowsUpdated = conn.update(query, params);

			// Si al menos una fila ha sido actualizada, marcamos la entrada como
			// sincronizada
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// En caso contrario, la entrada no ha sido actualizada correctamente
				setSync(false);
			}
		} catch (Exception e) {
			// Si se produce alguna excepción, la imprimimos y devolvemos false
			e.printStackTrace();
		}
		return success;

	}

	// Este método elimina una entrada de la tabla "Local" usando la clave primaria
	// "id_local".
	boolean deleteEntry() {
		// Se inicializa la variable booleana 'success' como falsa.
		boolean success = false;
		// Se define la consulta SQL para eliminar la entrada.
		String query = "DELETE FROM Local WHERE id_local = ?";

		try {
			// Se crea una lista 'params' para almacenar los parámetros de la consulta.
			ArrayList<Object> params = new ArrayList<>();
			// Se añade el valor de 'id_local' a la lista de parámetros.
			params.add(id_local);

			// Se ejecuta la consulta usando el método 'update' de la clase 'DBConnection'.
			// El método retorna el número de filas actualizadas en la tabla.
			int rowUpdated = conn.update(query, params);

			// Si la consulta actualizó alguna fila, se establece el valor de 'sync' como
			// verdadero y se establece 'success' como verdadero.
			if (rowUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// Si la consulta no actualizó ninguna fila, se establece 'sync' como falso.
				setSync(false);
			}
		} catch (Exception e) {
			// Si se produce alguna excepción, se imprime el mensaje de error.
			e.printStackTrace();
		}
		// Se retorna el valor de 'success'.
		return success;
	}

	// Este método obtiene los cambios de una entrada de la tabla "Local"
	// correspondiente al objeto actual de la clase.
	void getEntryChanges() {
		try {
			// Consulta SQL para obtener los datos de la entrada correspondiente al objeto
			// actual
			String query = "SELECT * FROM Local WHERE id_local = ?";
			// Parámetros para la consulta
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);

			// Ejecutar la consulta y obtener los resultados
			ResultSet rs = conn.query(query, params);

			// Si hay resultados, actualizar los atributos correspondientes del objeto
			// actual
			if (rs.next()) {
				tiene_cafeteria = rs.getInt("tiene_cafeteria");
				direccion = rs.getString("direccion");
				descripcion = rs.getString("descripcion");
			}

			// Cerrar el ResultSet
			rs.close();
		} catch (Exception e) {
			// Imprimir la traza del error si se produce alguna excepción
			e.printStackTrace();
		}
	}

}
