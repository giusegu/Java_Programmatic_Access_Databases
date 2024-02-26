package panaderias;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Producto extends DBTable {

	private int id_producto;
	private String nombre;
	private String descripcion;

	// Constructor de la clase Producto con la clave primaria y la conexión a la
	// base de datos
	public Producto(int id_producto, DBConnection conn, boolean DBSync) {

		// Llamada al constructor de la clase padre con la conexión y la sincronización
		super(conn, DBSync);

		// Asignación de la clave primaria
		this.id_producto = id_producto;

		// Asignación de valores nulos a nombre y descripción utilizando constantes de
		// la clase DBConnection
		this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
		this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;

		// Si se solicita sincronización con la base de datos
		if (DBSync) {

			// Creación de la tabla en la base de datos si no existe
			createTable();

			// Inserción de la entrada en la base de datos y verificación de que se realizó
			// correctamente
			if (insertEntry() == false) {

				// Si la inserción falló, se asignan valores nulos a la clave primaria y a los
				// campos de la instancia
				this.id_producto = DBConnection.NULL_SENTINEL_INT;
				this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
				this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;

				// Desactivación de la sincronización con la base de datos
				setSync(false);
			}
		}
	}

	// Constructor de la clase Producto con la clave primaria, el nombre, la
	// descripción y la conexión a la base de datos
	public Producto(int id_producto, String nombre, String descripcion, DBConnection conn, boolean DBSync) {
		// Llamada al constructor de la clase padre con la conexión y la sincronización
		super(conn, DBSync);

		// Asignación de la clave primaria, el nombre y la descripción
		this.id_producto = id_producto;
		this.nombre = nombre;
		this.descripcion = descripcion;

		// Si se solicita sincronización con la base de datos
		if (DBSync) {

			// Creación de la tabla en la base de datos si no existe
			createTable();

			// Inserción de la entrada en la base de datos y verificación de que se realizó
			// correctamente
			if (insertEntry() == false) {

				// Si la inserción falló, se asignan valores nulos al nombre y a la descripción
				// de la instancia
				this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
				this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;

				// Desactivación de la sincronización con la base de datos
				setSync(false);
			}
		}

	}

	// Este método devuelve el valor del campo id_producto
	public int getId_producto() {
		// Si la sincronización está activada, se obtienen los cambios de la entrada en
		// la base de datos
		if (DBSync) {
			getEntryChanges();
		}
		// Se devuelve el valor del campo id_producto
		return id_producto;
	}

	// Este método devuelve el valor del campo nombre
	public String getNombre() {
		// Si la sincronización está activada, se obtienen los cambios de la entrada en
		// la base de datos
		if (DBSync) {
			getEntryChanges();
		}
		// Se devuelve el valor del campo nombre
		return nombre;
	}

	// Este método establece el valor del campo nombre
	public void setNombre(String nombre) {
		// Se establece el valor del campo nombre
		this.nombre = nombre;
		// Si la sincronización está activada, se actualiza la entrada en la base de
		// datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Este método devuelve el valor del campo descripcion
	public String getDescripcion() {
		// Si la sincronización está activada, se obtienen los cambios de la entrada en
		// la base de datos
		if (DBSync) {
			getEntryChanges();
		}
		// Se devuelve el valor del campo descripcion
		return descripcion;
	}

	// Este método establece el valor del campo descripcion
	public void setDescripcion(String descripcion) {
		// Se establece el valor del campo descripcion
		this.descripcion = descripcion;
		// Si la sincronización está activada, se actualiza la entrada en la base de
		// datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Este método borra el registro de la base de datos correspondiente al objeto
	// Producto actual
	public void destroy() {
		// Si el objeto Producto actual está sincronizado con la base de datos, procede
		// a borrar el registro
		if (DBSync) {
			String query = "DELETE FROM Producto WHERE id_producto = ?";
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_producto);
			conn.update(query, params); // Ejecuta la consulta de eliminación utilizando el id_producto como parámetro
		}

		// Se establece los valores nulos para los atributos del objeto Producto
		this.id_producto = DBConnection.NULL_SENTINEL_INT;
		this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
		this.descripcion = DBConnection.NULL_SENTINEL_VARCHAR;

		// Se establece la sincronización del objeto Producto con la base de datos en
		// falso
		setSync(false);
	}

	// El siguiente método crea una nueva tabla en la base de datos llamada
	// "Producto"
	boolean createTable() {
		try {
			// Construir la consulta SQL para crear la tabla Producto
			String query = "CREATE TABLE Producto (" + "id_producto INT PRIMARY KEY, "
					+ "nombre VARCHAR(100) NOT NULL, " + "descripcion VARCHAR(100) NOT NULL)";
			// Verificar si la tabla Producto ya existe
			if (conn.tableExists("Producto")) {
				// Si la tabla ya existe, no se puede crear, se desactiva la sincronización y se
				// retorna false
				setSync(false);
				return false;
			}

			// Si la tabla no existe, se ejecuta la consulta para crearla
			if (conn.update(query) != -1) {
				// Si la consulta fue exitosa, se activa la sincronización y se retorna true
				setSync(true);
				return true;
			} else {
				// Si la consulta no fue exitosa, se desactiva la sincronización y se retorna
				// false
				setSync(false);
				return false;
			}
		} catch (Exception e) {
			// Si ocurre alguna excepción, se imprime la traza y se retorna false
			e.printStackTrace();
			return false;
		}
	}

	// Este método se encarga de insertar un nuevo registro en la tabla "Producto".
	boolean insertEntry() {
		boolean success = false;
		// Consulta SQL para la inserción de un nuevo registro.
		String query = "INSERT INTO Producto (id_producto, nombre, descripcion) VALUES (?, ?, ?)";

		try {
			// Se preparan los parámetros para la consulta SQL.
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_producto);
			params.add(nombre);
			params.add(descripcion);

			// Se ejecuta la consulta SQL.
			int rowsUpdated = conn.update(query, params);

			// Si se han actualizado más de 0 filas, se considera que la inserción ha sido
			// satisfactoria.
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				setSync(false);
			}
		} catch (Exception e) {
			// Se imprime la excepción en caso de que haya ocurrido algún error.
			e.printStackTrace();
		}

		// Se devuelve el valor booleano que indica si la inserción ha tenido éxito o
		// no.
		return success;
	}

	// Este método actualiza los datos de un producto en la base de datos.
	boolean updateEntry() {
		boolean success = false;
		// Query SQL para actualizar los datos del producto en la base de datos.
		String query = "UPDATE Producto SET nombre=?, descripcion=? WHERE id_producto=?";

		try {

			// Se crea un ArrayList para almacenar los parámetros de la consulta.
			ArrayList<Object> params = new ArrayList<>();
			params.add(nombre);
			params.add(descripcion);
			params.add(id_producto);

			// Se ejecuta la consulta con los parámetros correspondientes.
			int rowsUpdated = conn.update(query, params);

			// Si se actualizó al menos una fila, se actualiza el estado de sincronización
			// del objeto y se establece
			// la variable success en true.
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// Si no se actualizó ninguna fila, se establece el estado de sincronización del
				// objeto en false.
				setSync(false);
			}
		} catch (Exception e) {
			// Si se produce una excepción, se imprime el stack trace.
			e.printStackTrace();
		}

		// Se retorna el valor de la variable success.
		return success;
	}

	// El método deleteEntry() elimina una entrada de la tabla "Producto" utilizando
	// la clave primaria "id_producto".
	boolean deleteEntry() {
		boolean success = false;
		String query = "DELETE FROM Producto WHERE id_producto = ?";
		try {
			// Se prepara una lista para los parámetros de la consulta SQL
			ArrayList<Object> params = new ArrayList<>();
			// Se añade el valor de la clave primaria a la lista
			params.add(id_producto);

			// Se ejecuta la consulta SQL de eliminación y se recoge el número de filas
			// afectadas
			int rowUpdated = conn.update(query, params);

			// Si se ha eliminado alguna fila, se indica que se ha sincronizado la entrada y
			// se devuelve true
			if (rowUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// Si no se ha eliminado ninguna fila, se indica que no se ha sincronizado la
				// entrada y se devuelve false
				setSync(false);
			}
		} catch (Exception e) {
			// Se imprime la traza del error y se devuelve false
			e.printStackTrace();
		}
		return success;
	}

	// Este método se encarga de obtener los cambios de una entrada en la tabla
	// "Producto" de la base de datos.
	void getEntryChanges() {
		try {

			// Se prepara la consulta SQL
			String query = "SELECT * FROM Producto WHERE id_producto = ?";

			// Se preparan los parámetros de la consulta
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_producto);

			// Se ejecuta la consulta y se recupera el resultado
			ResultSet rs = conn.query(query, params);

			// Si se encontró la entrada correspondiente al ID especificado...
			if (rs.next()) {

				// Se actualiza el nombre y la descripción de la entrada con los valores
				// obtenidos de la consulta
				nombre = rs.getString("nombre");
				descripcion = rs.getString("descripcion");
			}

			// Se cierra el resultado de la consulta
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
