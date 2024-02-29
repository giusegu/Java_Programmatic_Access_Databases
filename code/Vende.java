package panaderias;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Vende extends DBTable {

	private int id_local;
	private int id_producto;

	// Constructor de la clase Vende que recibe el id del local, el id del producto,
	// una conexión a la base de datos y un booleano para indicar si se desea
	// sincronizar con la base de datos

	public Vende(int id_local, int id_producto, DBConnection conn, boolean DBSync) {
		// Llamada al constructor de la clase padre para inicializar la conexión y
		// sincronización con la base de datos
		super(conn, DBSync);
		// Asignación de los valores de id_local e id_producto
		this.id_local = id_local;
		this.id_producto = id_producto;
		// Si se desea sincronizar con la base de datos
		if (DBSync) {
			// Se crea la tabla si no existe
			createTable();
			// Se inserta una nueva entrada en la tabla, si no se pudo insertar
			// se asigna el valor sentinela NULL_SENTINEL_INT y se desactiva la
			// sincronización
			if (insertEntry() == false) {
				this.id_local = DBConnection.NULL_SENTINEL_INT;
				this.id_producto = DBConnection.NULL_SENTINEL_INT;
				setSync(false);
			}
		}
	}

	// Obtener el ID del local
	public int getId_local() {
		// Si el objeto está sincronizado con la base de datos, obtener los cambios de
		// entrada
		if (DBSync) {
			getEntryChanges();
		}
		// Devolver el ID del local
		return id_local;
	}

	// Obtener el ID del producto
	public int getId_producto() {
		// Si el objeto está sincronizado con la base de datos, obtener los cambios de
		// entrada
		if (DBSync) {
			getEntryChanges();
		}
		// Devolver el ID del producto
		return id_producto;
	}

	// Este método elimina una entrada de la tabla Vende correspondiente a la
	// relación entre un local y un producto.
	public void destroy() {
		// Si DBSync es verdadero, la eliminación se realizará en la base de datos.
		if (DBSync) {
			String query = "DELETE FROM Vende WHERE id_local = ? AND id_producto = ?";
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			params.add(id_producto);
			conn.update(query, params);
		}

		// Reiniciar los valores de las claves primarias a los valores centinelas
		this.id_local = DBConnection.NULL_SENTINEL_INT;
		this.id_producto = DBConnection.NULL_SENTINEL_INT;

		// Desactivar la sincronización con la base de datos.
		setSync(false);

	}

	// Método que crea la tabla "Vende" en la base de datos si ésta no existe.
	boolean createTable() {
		try {
			// Crear la consulta SQL para crear la tabla "Vende" con sus columnas y llaves
			// foráneas
			String query = "CREATE TABLE Vende (" + "id_local INT NOT NULL," + "id_producto INT NOT NULL,"
					+ "PRIMARY KEY (id_local, id_producto)," + "FOREIGN KEY (id_local) REFERENCES Local (id_local),"
					+ "FOREIGN KEY (id_producto) REFERENCES Producto (id_producto)" + ")";
			// Verificar si la tabla "Vende" ya existe en la base de datos
			if (conn.tableExists("Vende")) {
				// Si ya existe, se cambia el estado de sincronización a false y se devuelve
				// false
				setSync(false);
				return false;
			}

			// Si la tabla no existe, se ejecuta la consulta para crearla
			if (conn.update(query) != -1) {
				// Si la consulta se ejecuta exitosamente, se cambia el estado de sincronización
				// a true y se devuelve true
				setSync(true);
				return true;
			} else {
				// Si ocurre un error al ejecutar la consulta, se cambia el estado de
				// sincronización a false y se devuelve false
				setSync(false);
				return false;
			}

		} catch (Exception e) {
			// Si ocurre una excepción, se imprime el stacktrace y se devuelve false
			e.printStackTrace();
			return false;
		}
	}

	// Método para insertar una nueva entrada en la tabla "Vende"
	boolean insertEntry() {
		// Inicializamos la variable de éxito a "false"
		boolean success = false;
		// Creamos la consulta SQL para insertar una nueva entrada
		String query = "INSERT INTO Vende (id_local, id_producto) VALUES (?, ?)";
		try {

			// Creamos un ArrayList para almacenar los parámetros de la consulta
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			params.add(id_producto);

			// Ejecutamos la consulta de inserción y recogemos el número de filas
			// actualizadas
			int rowsUpdated = conn.update(query, params);

			// Si se ha actualizado al menos una fila, establecemos la sincronización a
			// "true" y la operación es un éxito
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			}
			// Si no se ha actualizado ninguna fila, establecemos la sincronización a
			// "false"
			else {
				setSync(false);
			}
		} catch (Exception e) {
			// Imprimimos la traza de la excepción si se produce algún error
			e.printStackTrace();
		}
		// Devolvemos el valor de éxito de la operación
		return success;
	}

	// Método que actualiza una entrada en la tabla Vende con los valores de los
	// atributos id_local e id_producto
	boolean updateEntry() {
		boolean success = false;
		// Consulta SQL para actualizar los valores de id_local e id_producto
		String query = "UPDATE Vende SET id_local=?, id_producto=? WHERE id_local=? AND id_producto=?";

		try {

			// Lista para almacenar los parámetros que se van a pasar en la consulta SQL
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			params.add(id_producto);

			// Ejecutar la consulta SQL para actualizar la entrada y obtener el número de
			// filas actualizadas
			int rowsUpdated = conn.update(query, params);

			// Si se actualizó al menos una fila, se establece la sincronización en true y
			// se devuelve true
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// Si no se actualizó ninguna fila, se establece la sincronización en false
				setSync(false);
			}
		} catch (Exception e) {
			// Si se produce una excepción, se imprime el stack trace
			e.printStackTrace();
		}
		// Devuelve true si se actualizó al menos una fila, false en caso contrario
		return success;

	}

	// Este método intenta eliminar una entrada de la tabla "Vende" utilizando los
	// valores de las claves primarias "id_local" e "id_producto".
	boolean deleteEntry() {
		boolean success = false;
		// Consulta SQL para eliminar una entrada
		String query = "DELETE FROM Vende WHERE id_local=? AND id_producto=?";
		try {

			// Lista de parámetros para la consulta
			ArrayList<Object> params = new ArrayList<>();

			// Añadir los valores de las claves primarias a la lista de parámetros
			params.add(id_local);
			params.add(id_producto);

			// Ejecutar la consulta y recoger el número de filas actualizadas
			int rowUpdated = conn.update(query, params);

			// Si se ha eliminado la entrada, se activa la sincronización y se retorna true
			if (rowUpdated > 0) {

				setSync(true);
				success = true;
			} else {

				// Si no se ha eliminado la entrada, se desactiva la sincronización y se retorna
				// false
				setSync(false);
			}
		} catch (Exception e) {
			// En caso de excepción, se muestra el mensaje de error
			e.printStackTrace();
		}
		// Retorna un valor booleano que indica si se ha eliminado o no la entrada
		return success;
	}

	// El método busca cambios en la tabla Vende para una entrada específica,
	// identificada por su id_local e id_producto.

	void getEntryChanges() {
		try {
			// Consulta para seleccionar los datos de la entrada actual en la tabla Vende
			String query = "SELECT * FROM Vende WHERE id_local = ? AND id_producto = ?";
			// Lista de parámetros para añadir a la consulta preparada
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_local);
			params.add(id_producto);

			// Ejecutar la consulta y obtener el conjunto de resultados
			ResultSet rs = conn.query(query, params);

			// Si la consulta devuelve algún resultado, actualizar los atributos de la
			// entrada actual
			if (rs.next()) {
				// No se necesitan actualizar los atributos de la clave primaria, ya que no
				// cambian
				// Actualizar atributos que no forman parte de la clave primaria
				// En este caso, no hay atributos no-clave primaria
			}

			// Cerrar el conjunto de resultados
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
