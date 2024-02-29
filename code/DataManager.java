package panaderias;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DataManager {

	// Método para obtener una lista de objetos Empleado desde la base de datos
	public static ArrayList<Empleado> getEmpleadosFromDB(DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los empleados
		ArrayList<Empleado> empleados = new ArrayList<>();
		// Nombre de la tabla que se va a consultar
		String tabla = "empleado";
		try {
			// Conectar a la base de datos
			conn.connect();
			// Comprobar si la tabla existe
			if (conn.tableExists(tabla)) {
				// Crear la consulta SQL para obtener todos los registros de la tabla
				String query = "SELECT * FROM " + tabla;
				// Ejecutar la consulta y recoger el resultado en un ResultSet
				ResultSet res = conn.query(query);
				// Recorrer el ResultSet y crear un objeto Empleado por cada fila
				while (res.next()) {
					int id = res.getInt("id_empleado");
					String n_ss = res.getString("n_ss");
					String nombre = res.getString("nombre");
					String apellido1 = res.getString("apellido1");
					String apellido2 = res.getString("apellido2");
					// Crear el objeto Empleado con los datos de la fila y la conexión a la base de
					// datos
					Empleado empleado = new Empleado(id, n_ss, nombre, apellido1, apellido2, conn, sync);
					// Activar la sincronización si se ha especificado
					empleado.setSync(sync);
					// Añadir el objeto Empleado a la lista
					empleados.add(empleado);
				}
			}
		} catch (Exception e) {
			// Mostrar información sobre cualquier excepción que se produzca
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Devolver la lista de empleados
		return empleados;
	}

	// Método que lee un archivo CSV con los datos de los empleados y crea un objeto
	// Empleado por cada fila, añadiéndolos a un ArrayList que se retorna.

	public static ArrayList<Empleado> getEmpleadosFromCSV(String filename, DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los empleados
		ArrayList<Empleado> empleados = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			// Leer la primera línea del archivo CSV (cabecera) y descartarla
			String lineas = reader.readLine();
			// Leer las demás líneas del archivo CSV y crear un objeto Empleado por cada una
			while ((lineas = reader.readLine()) != null) {
				// Dividir la línea en partes utilizando el carácter ';' como separador. El -1
				// es para que el split no descarte los campos vacios
				String[] partes = lineas.split(";", -1);
				// Obtener los valores de cada columna y crear un objeto Empleado con ellos
				int id = Integer.parseInt(partes[0]);
				String n_ss = (partes.length > 1) ? partes[1] : DBConnection.NULL_SENTINEL_VARCHAR;
				String nombre = (partes.length > 2) ? partes[2] : DBConnection.NULL_SENTINEL_VARCHAR;
				String apellido1 = (partes.length > 3) ? partes[3] : DBConnection.NULL_SENTINEL_VARCHAR;
				String apellido2 = (partes.length > 4) ? partes[4] : DBConnection.NULL_SENTINEL_VARCHAR;
				Empleado empleado = new Empleado(id, n_ss, nombre, apellido1, apellido2, conn, sync);
				// Añadir el objeto Empleado creado a la lista de empleados
				empleados.add(empleado);
			}
		} catch (Exception e) {
			// Imprimir el stack trace en caso de que se produzca una excepción
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Retornar la lista de empleados creados
		return empleados;
	}

	// Obtiene la lista de locales desde la base de datos.
	public static ArrayList<Local> getLocalesFromDB(DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los locales
		ArrayList<Local> locales = new ArrayList<>();
		// Nombre de la tabla que se va a consultar
		String tabla = "local";
		try {
			// Conectar a la base de datos
			conn.connect();
			// Comprobar si la tabla existe
			if (conn.tableExists(tabla)) {
				// Construir la consulta SQL
				String query = "SELECT * FROM " + tabla;
				// Ejecutar la consulta y obtener el resultado
				ResultSet res = conn.query(query);
				// Recorrer el resultado y crear un objeto Local por cada fila
				while (res.next()) {
					int id = res.getInt("id_local");
					Boolean tiene_cafeteria = res.getBoolean("tiene_cafeteria");
					String direccion = res.getString("direccion");
					String descripcion = res.getString("descripcion");
					Local local = new Local(id, tiene_cafeteria, direccion, descripcion, conn, sync);
					// Activar la sincronización si es necesario
					local.setSync(sync);
					// Añadir el objeto Local a la lista
					locales.add(local);
				}
			}
		} catch (Exception e) {
			// Imprimir la traza de la excepción
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Devolver la lista de locales
		return locales;
	}

	// Obtiene una lista de locales a partir de un archivo CSV.
	public static ArrayList<Local> getLocalesFromCSV(String filename, DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los locales
		ArrayList<Local> locales = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			// Leer la primera línea del archivo (cabecera)
			String lineas = reader.readLine();
			// Recorrer el archivo línea por línea
			while ((lineas = reader.readLine()) != null) {
				// Dividir la línea en partes separadas por el carácter ';'
				String[] partes = lineas.split(";");
				// Obtener el id del local
				int id = Integer.parseInt(partes[0]);
				// Obtener el valor booleano de la columna "tiene_cafeteria"
				boolean tiene_cafeteria = Integer.parseInt(partes[1]) == 0 ? false : true;
				// Obtener la dirección del local, si existe
				String direccion = (partes.length > 2) ? partes[2] : DBConnection.NULL_SENTINEL_VARCHAR;
				// Obtener la descripción del local, si existe
				String descripcion = (partes.length > 3) ? partes[3] : DBConnection.NULL_SENTINEL_VARCHAR;
				// Crear un objeto Local con los valores obtenidos y la conexión a la base de
				// datos
				Local local = new Local(id, tiene_cafeteria, direccion, descripcion, conn, sync);
				// Añadir el objeto Local a la lista
				locales.add(local);
			}
		} catch (Exception e) {
			// Imprimir la traza del error en caso de que ocurra una excepción
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Devolver la lista de locales creados a partir del archivo CSV
		return locales;
	}

	// Este método obtiene los productos de la base de datos y los devuelve en una
	// lista ArrayList.
	public static ArrayList<Producto> getProductosFromDB(DBConnection conn, boolean sync) {
		ArrayList<Producto> productos = new ArrayList<>();
		String tabla = "producto";
		try {
			// Conectarse a la base de datos
			conn.connect();
			// Verificar si la tabla existe
			if (conn.tableExists(tabla)) {
				// Crear la consulta SQL para seleccionar todos los registros de la tabla
				String query = "SELECT * FROM " + tabla;
				// Ejecutar la consulta SQL y obtener el resultado
				ResultSet res = conn.query(query);
				// Recorrer el resultado y crear un objeto Producto por cada fila
				while (res.next()) {
					// Obtener los valores de las columnas para cada fila
					int id = res.getInt("id_producto");
					String nombre = res.getString("nombre");
					String descripcion = res.getString("descripcion");
					// Crear un nuevo objeto Producto con los valores obtenidos
					Producto producto = new Producto(id, nombre, descripcion, conn, sync);
					// Establecer la sincronización del objeto Producto
					producto.setSync(sync);
					// Agregar el objeto Producto a la lista ArrayList
					productos.add(producto);
				}
			}
		} catch (Exception e) {
			// Imprimir la excepción si ocurre
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Devolver la lista ArrayList de objetos Producto
		return productos;
	}

	// Método para obtener una lista de objetos Producto a partir de un archivo CSV
	public static ArrayList<Producto> getProductosFromCSV(String filename, DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los productos
		ArrayList<Producto> productos = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			// Leer la primera línea del archivo, que contiene los encabezados de las
			// columnas (no se usa)
			String lineas = reader.readLine();
			// Recorrer el archivo línea por línea
			while ((lineas = reader.readLine()) != null) {
				// Dividir cada línea en sus partes correspondientes
				String[] partes = lineas.split(";");
				// Obtener los valores de los campos
				int id = Integer.parseInt(partes[0]);
				String nombre = (partes.length > 1) ? partes[1] : DBConnection.NULL_SENTINEL_VARCHAR;
				String descripcion = (partes.length > 2) ? partes[2] : DBConnection.NULL_SENTINEL_VARCHAR;
				// Crear un objeto Producto con los valores obtenidos
				Producto producto = new Producto(id, nombre, descripcion, conn, sync);
				// Añadir el objeto Producto a la lista de productos
				productos.add(producto);
			}
		} catch (Exception e) {
			// Si ocurre alguna excepción, imprimir el mensaje de error
			e.printStackTrace();
		} finally {
			// Cerrar la conexión con la base de datos
			conn.close();
		}
		// Retornar la lista de productos creada
		return productos;
	}

	// Método que retorna un ArrayList de objetos Trabaja obtenidos desde la base de
	// datos utilizando la conexión proporcionada.

	public static ArrayList<Trabaja> getTrabajaFromDB(DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los objetos Trabaja
		ArrayList<Trabaja> trabajadores = new ArrayList<>();
		// Nombre de la tabla a consultar
		String tabla = "trabaja";
		try {
			// Conectar a la base de datos
			conn.connect();
			// Verificar que la tabla existe
			if (conn.tableExists(tabla)) {
				// Crear la consulta SQL
				String query = "SELECT * FROM " + tabla;
				// Ejecutar la consulta y recoger el resultado
				ResultSet res = conn.query(query);
				// Recorrer el resultado y crear un objeto Trabaja por cada fila
				while (res.next()) {
					int id_empleado = res.getInt("id_empleado");
					int id_local = res.getInt("id_local");
					java.sql.Date fecha_inicio = res.getDate("fecha_inicio");
					java.sql.Date fecha_fin = res.getDate("fecha_fin");
					// Crear un objeto Trabaja sin sincronización con la base de datos en primera
					// instancia
					Trabaja trabaja = new Trabaja(id_empleado, id_local, fecha_inicio, fecha_fin, conn, sync);
					// Activar la sincronización con la base de datos
					trabaja.setSync(sync);
					// Añadir el objeto Trabaja creado a la lista de trabajadores
					trabajadores.add(trabaja);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos
			conn.close();
		}
		// Devolver la lista de objetos Trabaja
		return trabajadores;
	}

	// Método para obtener los trabajadores de un archivo CSV y añadirlos a un
	// ArrayList
	public static ArrayList<Trabaja> getTrabajaFromCSV(String filename, DBConnection conn, boolean sync) {
		// Crear una lista vacía para almacenar los trabajadores
		ArrayList<Trabaja> trabajadores = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			// Leer la primera línea del archivo (cabecera) y descartarla
			String lineas = reader.readLine();
			// Leer las líneas restantes
			while ((lineas = reader.readLine()) != null) {
				// Dividir la línea en partes separadas por ";"
				String[] partes = lineas.split(";");
				// Obtener los valores de cada campo y convertirlos al tipo correspondiente
				int id_empleado = Integer.parseInt(partes[0]);
				int id_local = (partes.length > 1) ? Integer.parseInt(partes[1]) : DBConnection.NULL_SENTINEL_INT;
				java.sql.Date fecha_inicio = (partes.length > 2) ? java.sql.Date.valueOf(partes[2])
						: DBConnection.NULL_SENTINEL_DATE;
				java.sql.Date fecha_fin = (partes.length > 3) ? java.sql.Date.valueOf(partes[3])
						: DBConnection.NULL_SENTINEL_DATE;
				// Crear un objeto Trabaja con los valores obtenidos y añadirlo a la lista de
				// trabajadores
				Trabaja trabaja = new Trabaja(id_empleado, id_local, fecha_inicio, fecha_fin, conn, sync);
				trabajadores.add(trabaja);
			}
		} catch (Exception e) {
			// Imprimir la excepción en caso de error y retornar null
			e.printStackTrace();
			return null;
		} finally {
			// Cerrar la conexión con la base de datos
			conn.close();
		}
		// Retornar la lista de trabajadores
		return trabajadores;
	}

	// Este método obtiene los datos de la tabla "vende" de la base de datos usando
	// la conexión proporcionada.
	public static ArrayList<Vende> getVendeFromDB(DBConnection conn, boolean sync) {
		// Crear una nueva lista vacía para almacenar los objetos Vende.
		ArrayList<Vende> vendidos = new ArrayList<>();
		// Nombre de la tabla de la base de datos.
		String tabla = "vende";
		try {
			// Conectar a la base de datos.
			conn.connect();
			// Comprobar si la tabla existe en la base de datos.
			if (conn.tableExists(tabla)) {
				// Crear la consulta SQL.
				String query = "SELECT * FROM " + tabla;
				// Ejecutar la consulta y obtener el resultado en un objeto ResultSet.
				ResultSet res = conn.query(query);
				// Recorrer cada fila del resultado y crear un objeto Vende a partir de ella.
				while (res.next()) {
					// Obtener los valores de los campos "id_local" e "id_producto" de la fila
					// actual.
					int id_local = res.getInt("id_local");
					int id_producto = res.getInt("id_producto");
					// Crear un objeto Vende usando los valores de los campos y la conexión
					// proporcionada.
					Vende vende = new Vende(id_local, id_producto, conn, sync);
					// Establecer el valor de "sync" en el objeto Vende.
					vende.setSync(sync);
					// Agregar el objeto Vende creado a la lista de objetos Vende.
					vendidos.add(vende);
				}
			}
		} catch (Exception e) {
			// Imprimir la pila de errores en caso de excepción.
			e.printStackTrace();
		} finally {
			// Cerrar la conexión a la base de datos.
			conn.close();
		}
		// Retornar la lista de objetos Vende creados.
		return vendidos;
	}

	// El siguiente método obtiene los datos de ventas desde un archivo CSV.
	public static ArrayList<Vende> getVendeFromCSV(String filename, DBConnection conn, boolean sync) {
		// Crea una nueva lista para almacenar los objetos Vende creados a partir de los
		// datos del archivo CSV.
		ArrayList<Vende> vendidos = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

			// Lee la primera línea del archivo y la descarta ya que es la cabecera.
			String lineas = reader.readLine();

			// Lee cada línea del archivo, crea un objeto Vende a partir de los datos y lo
			// añade a la lista.
			while ((lineas = reader.readLine()) != null) {
				String[] partes = lineas.split(";");
				int id_local = Integer.parseInt(partes[0]);
				int id_producto = (partes.length > 1) ? Integer.parseInt(partes[1]) : DBConnection.NULL_SENTINEL_INT;
				Vende vende = new Vende(id_local, id_producto, conn, sync);
				vendidos.add(vende);
			}

		} catch (Exception e) {
			// En caso de error, se imprime el stack trace.
			e.printStackTrace();
		} finally {
			// Cierra la conexión a la base de datos.
			conn.close();
		}

		// Devuelve la lista de objetos Vende.
		return vendidos;

	}

}
