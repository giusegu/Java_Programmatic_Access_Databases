package panaderias;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Trabaja extends DBTable {

	private int id_empleado;
	private int id_local;
	private java.sql.Date fecha_inicio;
	private java.sql.Date fecha_fin;

	// Constructor que crea una instancia de la clase Trabaja con los valores de los
	// parámetros y una conexión a la base de datos

	public Trabaja(int id_empleado, int id_local, java.sql.Date fecha_inicio, DBConnection conn, boolean DBSync) {
		// Llama al constructor de la clase padre, con la conexión a la base de datos y
		// un booleano que indica si se desea sincronizar con la base de datos
		super(conn, DBSync);
		// Asigna los valores de los parámetros a los campos de la clase
		this.id_empleado = id_empleado;
		this.id_local = id_local;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = DBConnection.NULL_SENTINEL_DATE;
		// Si se desea sincronizar con la base de datos, crea la tabla correspondiente
		// y, si no se puede insertar el objeto en la base de datos, establece los
		// valores de la clave primaria a valores nulos y desactiva la sincronización
		if (DBSync) {
			createTable();
			if (insertEntry() == false) {
				this.id_empleado = DBConnection.NULL_SENTINEL_INT;
				this.id_local = DBConnection.NULL_SENTINEL_INT;
				this.fecha_inicio = DBConnection.NULL_SENTINEL_DATE;
				setSync(false);
			}
		}
	}

	// Constructor que crea una instancia de la clase Trabaja con los valores de los
	// parámetros y una conexión a la base de datos

	public Trabaja(int id_empleado, int id_local, java.sql.Date fecha_inicio, java.sql.Date fecha_fin,
			DBConnection conn, boolean DBSync) {
		// Llama al constructor de la clase padre, con la conexión a la base de datos y
		// un booleano que indica si se desea sincronizar con la base de datos
		super(conn, DBSync);
		// Asigna los valores de los parámetros a los campos de la clase
		this.id_empleado = id_empleado;
		this.id_local = id_local;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		// Si se desea sincronizar con la base de datos, crea la tabla correspondiente
		// y, si no se puede insertar el objeto en la base de datos, establece el valor
		// de la fecha_fin a un valor nulo y desactiva la sincronización
		if (DBSync) {
			createTable();
			if (insertEntry() == false) {
				this.fecha_fin = DBConnection.NULL_SENTINEL_DATE;
				setSync(false);
			}
		}
	}

	// Devuelve el id del empleado
	public int getId_empleado() {
		// Si el objeto está sincronizado con la base de datos, se obtienen los cambios
		if (DBSync) {
			getEntryChanges();
		}
		return id_empleado;
	}

	// Devuelve el id del local
	public int getId_local() {
		// Si el objeto está sincronizado con la base de datos, se obtienen los cambios
		if (DBSync) {
			getEntryChanges();
		}
		return id_local;
	}

	// Devuelve la fecha de inicio
	public java.sql.Date getFecha_inicio() {
		// Si el objeto está sincronizado con la base de datos, se obtienen los cambios
		if (DBSync) {
			getEntryChanges();
		}
		return fecha_inicio;
	}

	// Devuelve la fecha de fin
	public java.sql.Date getFecha_fin() {
		// Si el objeto está sincronizado con la base de datos, se obtienen los cambios
		if (DBSync) {
			getEntryChanges();
		}
		return fecha_fin;
	}

	// Establece la fecha de fin y actualiza la entrada en la base de datos si el
	// objeto está sincronizado
	public void setFecha_fin(java.sql.Date fecha_fin) {
		this.fecha_fin = fecha_fin;
		if (DBSync) {
			updateEntry();
		}
	}

	// Este método elimina una entrada de la tabla "Trabaja" utilizando los valores
	// de los atributos de la instancia actual.
	public void destroy() {
		if (DBSync) {
			// Se construye una consulta DELETE utilizando los valores de la instancia
			// actual
			String query = "DELETE FROM Trabaja WHERE id_empleado = ? AND id_local = ? AND fecha_inicio = ?";
			// Se agregan los valores a una lista de parámetros
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_empleado);
			params.add(id_local);
			params.add(fecha_inicio);
			// Se ejecuta la consulta utilizando la conexión a la base de datos
			conn.update(query, params);
		}

		// Se establecen los valores de los atributos de la instancia actual como nulos
		this.id_empleado = DBConnection.NULL_SENTINEL_INT;
		this.id_local = DBConnection.NULL_SENTINEL_INT;
		this.fecha_inicio = DBConnection.NULL_SENTINEL_DATE;
		this.fecha_fin = DBConnection.NULL_SENTINEL_DATE;

		// Se establece el estado de sincronización de la instancia actual como no
		// sincronizado
		setSync(false);

	}

	// Método que crea una nueva tabla en la base de datos llamada "Trabaja", la
	// cual relaciona los empleados con los locales donde trabajan,
	// y registra las fechas de inicio y fin de cada período laboral.
	boolean createTable() {
		try {
			// Construcción de la consulta SQL para crear la tabla Trabaja
			String query = "CREATE TABLE Trabaja (" + "id_empleado INT NOT NULL, " + "id_local INT NOT NULL, "
					+ "fecha_inicio DATE NOT NULL, " + "fecha_fin DATE NOT NULL, "
					+ "PRIMARY KEY (id_empleado, id_local, fecha_inicio), "
					+ "FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado), "
					+ "FOREIGN KEY (id_local) REFERENCES Local(id_local))";
			// Comprobar si la tabla Trabaja ya existe
			if (conn.tableExists("Trabaja")) {
				setSync(false);
				return false; // Si existe, se desactiva la sincronización y se devuelve false
			}

			// Si la tabla no existe, se ejecuta la consulta SQL para crearla y se activa la
			// sincronización
			if (conn.update(query) != -1) {
				setSync(true);
				return true; // Si la consulta se ejecuta correctamente, se activa la sincronización y se
								// devuelve true
			} else {
				setSync(false);
				return false; // Si ocurre un error en la ejecución de la consulta, se desactiva la
								// sincronización y se devuelve false
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false; // Si ocurre una excepción, se imprime la traza del error y se devuelve false
		}
	}

	// Este método inserta una nueva entrada en la tabla "Trabaja" con los valores
	// de los atributos del objeto actual.
	boolean insertEntry() {
		boolean success = false;

		String query = "INSERT INTO Trabaja (id_empleado, id_local, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
		try {
			// Crear un ArrayList para almacenar los valores de los parámetros de la
			// consulta
			ArrayList<Object> params = new ArrayList<>();
			// Agregar los valores de los atributos del objeto actual al ArrayList de
			// parámetros
			params.add(id_empleado);
			params.add(id_local);
			params.add(fecha_inicio);
			params.add(fecha_fin);
			// Ejecutar la consulta de actualización de la base de datos y obtener el número
			// de filas actualizadas
			int rowsUpdated = conn.update(query, params);

			// Si se actualizó al menos una fila, establecer la sincronización del objeto
			// actual con la base de datos y establecer el valor de success a true
			if (rowsUpdated > 0) {
				setSync(true);
				success = true;
			} else {
				// Si no se actualizó ninguna fila, establecer la sincronización del objeto
				// actual con la base de datos a false
				setSync(false);
			}
		} catch (Exception e) {
			// Imprimir la traza del error en caso de que se produzca una excepción
			e.printStackTrace();
		}
		// Devolver el valor de success
		return success;
	}

	// Este método actualiza una entrada en la tabla "Trabaja" con una fecha de
	// finalización.
	boolean updateEntry() {
		// Se inicializa el valor de éxito de la actualización a false.
		boolean success = false;
		// Se define la consulta SQL de actualización.
		String query = "UPDATE Trabaja SET fecha_fin=? WHERE id_empleado=? AND id_local=? AND fecha_inicio=?";

		try {
			// Se crean los parámetros necesarios para la consulta.
			ArrayList<Object> params = new ArrayList<>();
			params.add(fecha_fin);
			params.add(id_empleado);
			params.add(id_local);
			params.add(fecha_inicio);

			// Se ejecuta la consulta de actualización.
			int rowsUpdated = conn.update(query, params);

			// Si la actualización afectó a más de una fila...
			if (rowsUpdated > 0) {
				// Se activa la sincronización del objeto con la base de datos.
				setSync(true);
				// Se cambia el valor de éxito de la actualización a true.
				success = true;
			} else {
				// Se desactiva la sincronización del objeto con la base de datos.
				setSync(false);
			}
		} catch (Exception e) {
			// Si ocurre una excepción, se imprime su traza.
			e.printStackTrace();
		}

		// Se devuelve el valor de éxito de la actualización.
		return success;

	}

	// Método para eliminar una entrada en la tabla Trabaja
	boolean deleteEntry() {
		// Variable para indicar si la eliminación fue exitosa o no
		boolean success = false;
		// Consulta SQL para eliminar la entrada correspondiente a la clave primaria del
		// objeto actual
		String query = "DELETE FROM Trabaja WHERE id_empleado = ? AND id_local = ? AND fecha_inicio = ?";
		try {
			// Crear una lista de parámetros para la consulta preparada
			ArrayList<Object> params = new ArrayList<>();

			// Agregar los valores de clave primaria del objeto actual a la lista de
			// parámetros
			params.add(id_empleado);
			params.add(id_local);
			params.add(fecha_inicio);

			// Ejecutar la consulta preparada y obtener el número de filas actualizadas
			int rowUpdated = conn.update(query, params);

			// Si se actualizó al menos una fila, la eliminación fue exitosa
			if (rowUpdated > 0) {
				// Marcar el objeto actual como sincronizado con la base de datos
				setSync(true);
				success = true;
			} else {
				// Marcar el objeto actual como no sincronizado con la base de datos
				setSync(false);
			}
		} catch (Exception e) {
			// Imprimir la traza de la excepción en caso de error
			e.printStackTrace();
		}
		// Devolver el resultado de la eliminación
		return success;

	}

	// Método para obtener los cambios en una entrada en la tabla Trabaja
	void getEntryChanges() {
		try {
			// Se define la consulta SQL con parámetros
			String query = "SELECT * FROM Trabaja WHERE id_empleado = ? AND id_local = ? AND fecha_inicio = ?";
			// Se crea una lista de parámetros y se agregan los valores necesarios
			ArrayList<Object> params = new ArrayList<>();
			params.add(id_empleado);
			params.add(id_local);
			params.add(fecha_inicio);

			// Se ejecuta la consulta con los parámetros y se guarda el resultado en un
			// ResultSet
			ResultSet rs = conn.query(query, params);

			// Si se encuentra una fila que coincide con los valores de los parámetros
			if (rs.next()) {
				// Se obtiene el valor de la columna "fecha_fin" y se asigna a la variable
				// fecha_fin
				fecha_fin = rs.getDate("fecha_fin");
			}

			// Se cierra el ResultSet
			rs.close();
		} catch (Exception e) {
			// Si ocurre una excepción, se imprime el error
			e.printStackTrace();
		}
	}

}
