package panaderias;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Empleado extends DBTable {

	private int id_empleado;
	private String n_ss;
	private String nombre;
	private String apellido1;
	private String apellido2;

	// Constructor para crear un objeto de la clase Empleado con valores de clave
	// primaria y sincronización con la base de datos

	public Empleado(int id_empleado, DBConnection conn, boolean DBSync) {
		// Llamar al constructor de la clase padre para inicializar la conexión con la
		// base de datos y la sincronización
		super(conn, DBSync);

		// Asignar el valor del identificador del empleado
		this.id_empleado = id_empleado;

		// Inicializar los campos nombre, apellido1, apellido2 y n_ss con valores nulos
		this.n_ss = DBConnection.NULL_SENTINEL_VARCHAR;
		this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
		this.apellido1 = DBConnection.NULL_SENTINEL_VARCHAR;
		this.apellido2 = DBConnection.NULL_SENTINEL_VARCHAR;

		// Si la sincronización está activada
		if (DBSync) {
			// Crear la tabla correspondiente en la base de datos
			createTable();
			// Insertar una entrada con los valores del objeto en la tabla correspondiente
			if (insertEntry() == false) {
				// Si la inserción falla, asignar el valor nulo al identificador del empleado
				this.id_empleado = DBConnection.NULL_SENTINEL_INT;
				// Desactivar la sincronización con la base de datos
				setSync(false);
			}

		}
	}

	// Constructor de la clase Empleado que crea un objeto con los valores de los
	// parámetros correspondientes.

	public Empleado(int id_empleado, String n_ss, String nombre, String apellido1, String apellido2, DBConnection conn,
			boolean DBSync) {
		// Llamada al constructor de la clase padre
		super(conn, DBSync);
		// Asignación de valores a las variables de instancia
		this.id_empleado = id_empleado;
		this.n_ss = n_ss;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;

		// Si se desea sincronizar el objeto con la base de datos
		if (DBSync) {
			// Creación de la tabla correspondiente
			createTable();

			// Inserción de los valores en la tabla
			if (insertEntry() == false) {
				// En caso de que la inserción falle, se asignan valores nulos a las variables
				// de instancia
				this.n_ss = DBConnection.NULL_SENTINEL_VARCHAR;
				this.nombre = DBConnection.NULL_SENTINEL_VARCHAR;
				this.apellido1 = DBConnection.NULL_SENTINEL_VARCHAR;
				this.apellido2 = DBConnection.NULL_SENTINEL_VARCHAR;

				// Desactivación de la sincronización
				setSync(false);
			}
		}
	}

	// Método que devuelve el ID del empleado
	public int getId_empleado() {
		// Si está activada la sincronización con la base de datos, se actualizan los
		// cambios en la entrada
		if (DBSync) {
			getEntryChanges();
		}
		return id_empleado;
	}

	// Método que devuelve el número de seguridad social del empleado
	public String getN_ss() {
		// Si está activada la sincronización con la base de datos, se actualizan los
		// cambios en la entrada
		if (DBSync) {
			getEntryChanges();
		}
		return n_ss;
	}

	// Método que establece el número de seguridad social del empleado
	public void setN_ss(String nss) {
		// Se establece el número de seguridad social del empleado
		this.n_ss = nss;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada en la base de datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Método que devuelve el nombre del empleado
	public String getNombre() {
		// Si está activada la sincronización con la base de datos, se actualizan los
		// cambios en la entrada
		if (DBSync) {
			getEntryChanges();
		}
		return nombre;
	}

	// Método que establece el nombre del empleado
	public void setNombre(String nombre) {
		// Se establece el nombre del empleado
		this.nombre = nombre;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada en la base de datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Método que devuelve el primer apellido del empleado
	public String getApellido1() {
		// Si está activada la sincronización con la base de datos, se actualizan los
		// cambios en la entrada
		if (DBSync) {
			getEntryChanges();
		}
		return apellido1;
	}

	// Método que establece el primer apellido del empleado
	public void setApellido1(String apellido1) {
		// Se establece el primer apellido del empleado
		this.apellido1 = apellido1;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada en la base de datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Método que devuelve el segundo apellido del empleado
	public String getApellido2() {
		// Si está activada la sincronización con la base de datos, se actualizan los
		// cambios en la entrada
		if (DBSync) {
			getEntryChanges();
		}
		return apellido2;
	}

	// Método que establece el segundo apellido del empleado
	public void setApellido2(String apellido2) {
		// Se establece el segundo apellido del empleado
		this.apellido2 = apellido2;
		// Si está activada la sincronización con la base de datos, se actualiza la
		// entrada en la base de datos
		if (DBSync) {
			updateEntry();
		}
	}

	// Este método elimina un registro de la tabla "Empleado" en la base de datos,
	// si la sincronización está activada.

	public void destroy() {
		// Si la sincronización está activada, se construye la consulta SQL y se ejecuta
		// en la base de datos.
		if (DBSync) {
			String query = "DELETE FROM Empleado WHERE id_empleado = ?";
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_empleado);
			conn.update(query, params);
		}

		// Se establecen los valores de los atributos de instancia a sus valores nulos.
		id_empleado = DBConnection.NULL_SENTINEL_INT;
		n_ss = DBConnection.NULL_SENTINEL_VARCHAR;
		nombre = DBConnection.NULL_SENTINEL_VARCHAR;
		apellido1 = DBConnection.NULL_SENTINEL_VARCHAR;
		apellido2 = DBConnection.NULL_SENTINEL_VARCHAR;

		// Se desactiva la sincronización.
		setSync(false);
	}

	// Método para crear una tabla llamada Empleado en la base de datos
	boolean createTable() {
		try {
			// Definición de la consulta SQL para crear la tabla Empleado
			String query = "CREATE TABLE Empleado (" + "id_empleado INT PRIMARY KEY, " + "n_ss VARCHAR(100) NOT NULL, "
					+ "nombre VARCHAR(100) NOT NULL, " + "apellido1 VARCHAR(100) NOT NULL, "
					+ "apellido2 VARCHAR(100) NOT NULL)";
			// Si la tabla Empleado ya existe, desactivar la sincronización y retornar false
			if (conn.tableExists("Empleado")) {
				setSync(false);
				return false;
			}

			// Ejecutar la consulta SQL para crear la tabla Empleado
			if (conn.update(query) != -1) {
				// Si la ejecución de la consulta fue exitosa, activar la sincronización y
				// retornar true
				setSync(true);
				return true;
			} else {
				// Si la ejecución de la consulta falló, desactivar la sincronización y retornar
				// false
				setSync(false);
				return false;
			}

		} catch (Exception e) {
			// Imprimir la excepción en caso de que ocurra algún error y retornar false
			e.printStackTrace();
			return false;
		}
	}

	// Este método inserta una nueva entrada en la tabla Empleado

	boolean insertEntry() {
		boolean success = false;
		// Definimos la consulta SQL para insertar en la tabla Empleado
		String query = "INSERT INTO Empleado (id_empleado, n_ss, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?, ?)";

		try {
			// Creamos un ArrayList con los parámetros que vamos a insertar
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_empleado);
			params.add(n_ss);
			params.add(nombre);
			params.add(apellido1);
			params.add(apellido2);

			// Ejecutamos la consulta SQL usando el método update de la conexión a la base
			// de datos
			int rowsUpdated = conn.update(query, params);

			// Si se actualizó al menos una fila, la inserción fue exitosa
			if (rowsUpdated > 0) {
				// Activamos la sincronización con la base de datos para este objeto Empleado
				setSync(true);
				success = true;
			} else {
				// La inserción no fue exitosa, desactivamos la sincronización
				setSync(false);
			}
		} catch (Exception e) {
			// Si hubo alguna excepción, la imprimimos en la consola
			e.printStackTrace();
		}

		// Devolvemos el valor de éxito de la inserción
		return success;
	}

	// Método para actualizar una entrada en la tabla Empleado con los valores
	// actuales de las variables de instancia de la clase.
	public boolean updateEntry() {

		boolean success = false;

		// Definir la consulta SQL para actualizar la entrada en la tabla Empleado con
		// los valores actuales de las variables de instancia de la clase
		String query = "UPDATE Empleado SET n_ss=?, nombre=?, apellido1=?, apellido2=? WHERE id_empleado=?";

		try {
			// Crear un ArrayList para almacenar los parámetros de la consulta SQL
			ArrayList<Object> params = new ArrayList<>();
			params.add(n_ss);
			params.add(nombre);
			params.add(apellido1);
			params.add(apellido2);
			params.add(id_empleado);

			// Ejecutar la consulta SQL y guardar el número de filas afectadas en
			// rowsUpdated
			int rowsUpdated = conn.update(query, params);

			// Si rowsUpdated es mayor que cero, la entrada fue actualizada exitosamente
			if (rowsUpdated > 0) {
				// Activar la sincronización con la base de datos y establecer success como true
				setSync(true);
				success = true;
			} else {
				// Desactivar la sincronización con la base de datos
				setSync(false);
			}

		} catch (Exception e) {
			// Imprimir la traza de la excepción
			e.printStackTrace();
		}
		// Devolver success
		return success;
	}

	// El método "deleteEntry" elimina la entrada correspondiente al objeto
	// "Empleado" actual en la base de datos.
	boolean deleteEntry() {
		boolean success = false;
		String query = "DELETE FROM Empleado WHERE id_empleado = ?";
		try {

			ArrayList<Object> params = new ArrayList<>();

			// Añadir el id_empleado del objeto actual como parámetro para la consulta
			params.add(id_empleado);

			// Ejecutar la consulta de eliminación y obtener el número de filas actualizadas
			int rowUpdated = conn.update(query, params);

			if (rowUpdated > 0) {
				// Si se actualizó al menos una fila, la eliminación fue exitosa
				setSync(true);
				success = true;
			} else {
				// Si no se actualizó ninguna fila, la eliminación no fue exitosa
				setSync(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Devolver el valor booleano que indica si la eliminación fue exitosa o no
		return success;
	}

	// El siguiente método obtiene los cambios en una entrada de la tabla Empleado
	// con un ID de empleado específico.
	void getEntryChanges() {
		try {

			// Consulta SQL para obtener los datos de la entrada con el ID de empleado
			// correspondiente
			String query = "SELECT * FROM Empleado WHERE id_empleado = ?";

			// Parámetros para la consulta preparada
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_empleado);

			// Ejecutar la consulta y recoger el resultado en un ResultSet
			ResultSet rs = conn.query(query, params);

			// Si hay una fila en el resultado, se actualizan los valores de los atributos
			// correspondientes
			if (rs.next()) {

				n_ss = rs.getString("n_ss");
				nombre = rs.getString("nombre");
				apellido1 = rs.getString("apellido1");
				apellido2 = rs.getString("apellido2");
			}

			// Cerrar el ResultSet
			rs.close();
		} catch (Exception e) {
			// Si se produce alguna excepción, se imprime la traza de la pila
			e.printStackTrace();
		}
	}

}
