package panaderias;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class Correccion_S2 {
	
	private Connection c;
	
	public void openConnection() {
		String addr = "localhost:3306";
		String user = "panaderia_user";
		String pass = "panaderia_pass";
		String db= "panaderias";
		String url = "jdbc:mysql://" + addr + "/" + db;
		
		try {
			if(c == null || c.isClosed()) {
				c = DriverManager.getConnection(url, user, pass);
			}
		} catch (SQLException e) { }
	}

	void executeScript(String filename) {
		File f = null;
		FileReader fr = null;
		BufferedReader br = null;
		String line = null, text = "";
		Statement st = null;

		f = new File(filename);
		this.openConnection();
		try {
			st = c.createStatement();
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			
			while ((line = br.readLine()) != null) {
				if (line.length() > 0) {
					text += line;
					if (text.charAt(text.length()-1)==';') {
						st.executeUpdate(text);
						text = "";
					} else {
						text += " ";
					}
				}
			}

			if (c != null) c.close();
			if (st != null) st.close();
			if (br != null) br.close();
			if (fr != null) fr.close();
		} catch (SQLException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	// //////////////////////
	// createTable
	// /////////////////////

	@Test
	void test_createTable_empleado() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e = new Empleado(-1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(e.createTable());
		
		Statement st;
		ResultSet rs;
		boolean found = false;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				found = found || rs.getString(1).equalsIgnoreCase("empleado");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		assertFalse(e.createTable());
		
		conn.close();
	}
	
	@Test
	void test_createTable_local() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l = new Local(-1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(l.createTable());
		
		Statement st;
		ResultSet rs;
		boolean found = false;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				found = found || rs.getString(1).equalsIgnoreCase("local");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		assertFalse(l.createTable());
		
		conn.close();
	}
	
	@Test
	void test_createTable_producto() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p = new Producto(-1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(p.createTable());
		
		Statement st;
		ResultSet rs;
		boolean found = false;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				found = found || rs.getString(1).equalsIgnoreCase("producto");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		assertFalse(p.createTable());
		
		conn.close();
	}
	
	

	// //////////////////////
	// insertEntry
	// /////////////////////
	
	@Test
	void test_insertEntry_empleado() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "1111-1111", "Jorge", "Sol", "Fuertes", conn, false);
		Empleado e2 = new Empleado(2, "2222-2222", "Maria", "Perez", "Ruiz", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(e1.insertEntry());
		assertFalse(e1.insertEntry());
		assertTrue(e2.insertEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado ORDER BY id_empleado;");
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getString("n_ss").equalsIgnoreCase("1111-1111");
				found = found && rs.getString("nombre").equalsIgnoreCase("Jorge");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Sol");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Fuertes");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 2;
				found = found && rs.getString("n_ss").equalsIgnoreCase("2222-2222");
				found = found && rs.getString("nombre").equalsIgnoreCase("Maria");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Perez");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Ruiz");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_insertEntry_local() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Calle Nueva 1", "Nada", conn, false);
		Local l2 = new Local(2, false, "Calle Vieja 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(l1.insertEntry());
		assertFalse(l1.insertEntry());
		assertTrue(l2.insertEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local ORDER BY id_local;");
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getInt("tiene_cafeteria") == 1;
				found = found && rs.getString("direccion").equalsIgnoreCase("Calle Nueva 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Nada");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 2;
				found = found && rs.getInt("tiene_cafeteria") == 0;
				found = found && rs.getString("direccion").equalsIgnoreCase("Calle Vieja 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_insertEntry_producto() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Producto 1", "Descr", conn, false);
		Producto p2 = new Producto(2, "Producto 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(p1.insertEntry());
		assertFalse(p1.insertEntry());
		assertTrue(p2.insertEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto ORDER BY id_producto;");
			if (rs.next()) {
				found = found && rs.getInt("id_producto") == 1;
				found = found && rs.getString("nombre").equalsIgnoreCase("Producto 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Descr");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_producto") == 2;
				found = found && rs.getString("nombre").equalsIgnoreCase("Producto 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	

	// //////////////////////
	// updateEntry
	// /////////////////////
	
	@Test
	void test_updateEntry_empleado() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "Actualizado 1", "Jorge", "Sol", "Fuertes", conn, false);
		Empleado e2 = new Empleado(3, "Actualizado 2", "Maria", "Perez", "Ruiz", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(e1.updateEntry());
		assertFalse(e2.updateEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado ORDER BY id_empleado;");
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getString("n_ss").equalsIgnoreCase("Actualizado 1");
				found = found && rs.getString("nombre").equalsIgnoreCase("Jorge");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Sol");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Fuertes");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 2;
				found = found && rs.getString("n_ss").equalsIgnoreCase("2222-2222");
				found = found && rs.getString("nombre").equalsIgnoreCase("Maria");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Perez");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Ruiz");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_updateEntry_local() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Actualizado 1", "Nada", conn, false);
		Local l2 = new Local(3, false, "Actualizado 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(l1.updateEntry());
		assertFalse(l2.updateEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local ORDER BY id_local;");
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getInt("tiene_cafeteria") == 1;
				found = found && rs.getString("direccion").equalsIgnoreCase("Actualizado 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Nada");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 2;
				found = found && rs.getInt("tiene_cafeteria") == 0;
				found = found && rs.getString("direccion").equalsIgnoreCase("Calle Vieja 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_updateEntry_producto() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Actualizado 1", "Descr", conn, false);
		Producto p2 = new Producto(3, "Actualizado 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(p1.updateEntry());
		assertFalse(p2.updateEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto ORDER BY id_producto;");
			if (rs.next()) {
				found = found && rs.getInt("id_producto") == 1;
				found = found && rs.getString("nombre").equalsIgnoreCase("Actualizado 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Descr");
			}
			if (rs.next()) {
				found = found && rs.getInt("id_producto") == 2;
				found = found && rs.getString("nombre").equalsIgnoreCase("Producto 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	

	// //////////////////////
	// deleteEntry
	// /////////////////////
	
	@Test
	void test_deleteEntry_empleado() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "Actualizado 1", "Jorge", "Sol", "Fuertes", conn, false);
		Empleado e2 = new Empleado(3, "Actualizado 2", "Maria", "Perez", "Ruiz", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(e1.deleteEntry());
		assertFalse(e2.deleteEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado ORDER BY id_empleado;");
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 2;
				found = found && rs.getString("n_ss").equalsIgnoreCase("2222-2222");
				found = found && rs.getString("nombre").equalsIgnoreCase("Maria");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Perez");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Ruiz");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}

	@Test
	void test_deleteEntry_local() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Actualizado 1", "Nada", conn, false);
		Local l2 = new Local(3, false, "Actualizado 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(l1.deleteEntry());
		assertFalse(l2.deleteEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local ORDER BY id_local;");
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 2;
				found = found && rs.getInt("tiene_cafeteria") == 0;
				found = found && rs.getString("direccion").equalsIgnoreCase("Calle Vieja 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_deleteEntry_producto() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Actualizado 1", "Descr", conn, false);
		Producto p2 = new Producto(3, "Actualizado 2", "", conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(p1.deleteEntry());
		assertFalse(p2.deleteEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto ORDER BY id_producto;");
			if (rs.next()) {
				found = found && rs.getInt("id_producto") == 2;
				found = found && rs.getString("nombre").equalsIgnoreCase("Producto 2");
				found = found && rs.getString("descripcion").equalsIgnoreCase("");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}


	// //////////////////////
	// getEntryChanges
	// /////////////////////
	
	@Test
	void test_getEntryChanges_empleado() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, conn, false);
		Empleado e2 = new Empleado(3, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		e1.getEntryChanges();
		e2.getEntryChanges();
		
		assertEquals("1111-1111", e1.getN_ss());
		assertEquals("Jorge", e1.getNombre());
		assertEquals("Sol", e1.getApellido1());
		assertEquals("Fuertes", e1.getApellido2());

		assertTrue(e2.getN_ss()==null || e2.getN_ss().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		assertTrue(e2.getNombre()==null || e2.getNombre().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		assertTrue(e2.getApellido1()==null || e2.getApellido1().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		assertTrue(e2.getApellido2()==null || e2.getApellido2().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		
		conn.close();
	}
	
	@Test
	void test_getEntryChanges_local() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, conn, false);
		Local l2 = new Local(3, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		l1.getEntryChanges();
		l2.getEntryChanges();
		
		assertTrue(l1.getTiene_cafeteria());
		assertEquals("Calle Nueva 1", l1.getDireccion());
		assertEquals("Nada", l1.getDescripcion());

		assertTrue(l2.getDireccion()==null || l2.getDireccion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		assertTrue(l2.getDescripcion()==null || l2.getDescripcion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		
		conn.close();
	}
	
	@Test
	void test_getEntryChanges_producto() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, conn, false);
		Producto p2 = new Producto(3, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		p1.getEntryChanges();
		p2.getEntryChanges();
		
		assertEquals("Producto 1", p1.getNombre());
		assertEquals("Descr", p1.getDescripcion());

		assertTrue(p2.getNombre()==null || p2.getNombre().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		assertTrue(p2.getDescripcion()==null || p2.getDescripcion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
		
		conn.close();
	}


	// //////////////////////
	// Constructores
	// /////////////////////
	
	@Test
	void test_constructores_empleado() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "1111-1111", "Jorge", "Sol", "Fuertes", conn, true);
		Empleado e2 = new Empleado(2, conn, true);
		Empleado e3 = new Empleado(1, conn, true);
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado ORDER BY id_empleado;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getString("n_ss").equalsIgnoreCase("1111-1111");
				found = found && rs.getString("nombre").equalsIgnoreCase("Jorge");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Sol");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Fuertes");
			}
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_empleado") == 2;
				found = found && (e2.getN_ss()==null || e2.getN_ss().equals(DBConnection.NULL_SENTINEL_VARCHAR));
				found = found && (e2.getNombre()==null || e2.getNombre().equals(DBConnection.NULL_SENTINEL_VARCHAR));
				found = found && (e2.getApellido1()==null || e2.getApellido1().equals(DBConnection.NULL_SENTINEL_VARCHAR));
				found = found && (e2.getApellido2()==null || e2.getApellido2().equals(DBConnection.NULL_SENTINEL_VARCHAR));
			}
			assertEquals(DBConnection.NULL_SENTINEL_INT, e3.getId_empleado());
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(2, n);
		
		conn.close();
	}
	
	@Test
	void test_constructores_local() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Calle Nueva 1", "Nada", conn, true);
		Local l2 = new Local(2, conn, true);
		Local l3 = new Local(1, conn, true);
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local ORDER BY id_local;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getInt("tiene_cafeteria") == 1;
				found = found && rs.getString("direccion").equalsIgnoreCase("Calle Nueva 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Nada");
			}
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_empleado") == 2;
				found = found && (l2.getDireccion()==null || l2.getDireccion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
				found = found && (l2.getDescripcion()==null || l2.getDescripcion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
			}
			assertEquals(DBConnection.NULL_SENTINEL_INT, l3.getId_local());
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(2, n);
		
		conn.close();
	}
	
	@Test
	void test_constructores_producto() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Producto 1", "Descr", conn, true);
		Producto p2 = new Producto(2, conn, true);
		Producto p3 = new Producto(1, conn, true);
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto ORDER BY id_producto;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_producto") == 1;
				found = found && rs.getString("nombre").equalsIgnoreCase("Producto 1");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Descr");
			}
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_producto") == 2;
				found = found && (p2.getNombre()==null || p2.getNombre().equals(DBConnection.NULL_SENTINEL_VARCHAR));
				found = found && (p2.getDescripcion()==null || p2.getDescripcion().equals(DBConnection.NULL_SENTINEL_VARCHAR));
			}
			assertEquals(DBConnection.NULL_SENTINEL_INT, p3.getId_producto());
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(2, n);
		
		conn.close();
	}

	

	// //////////////////////
	// Getters
	// /////////////////////
	
	@Test
	void test_getters_empleado() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, conn, false);
		e1.DBSync = true;
		assertEquals("1111-1111", e1.getN_ss());
		assertEquals("Jorge", e1.getNombre());
		assertEquals("Sol", e1.getApellido1());
		assertEquals("Fuertes", e1.getApellido2());
				
		conn.close();
	}
	
	@Test
	void test_getters_local() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, conn, false);
		l1.DBSync = true;
		assertTrue(l1.getTiene_cafeteria());
		assertEquals("Calle Nueva 1", l1.getDireccion());
		assertEquals("Nada", l1.getDescripcion());
				
		conn.close();
	}
	
	@Test
	void test_getters_producto() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, conn, false);
		p1.DBSync = true;
		assertEquals("Producto 1", p1.getNombre());
		assertEquals("Descr", p1.getDescripcion());
				
		conn.close();
	}
	

	// //////////////////////
	// Setters
	// /////////////////////
	
	@Test
	void test_setters_empleado() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "1111-1111", "Jorge", "Sol", "Fuertes", conn, false);
		e1.DBSync = true;
		e1.setN_ss("Nuevo N_ss");
		e1.setNombre("Nuevo nombre");
		e1.setApellido1("Nuevo apellido 1");
		e1.setApellido2("Nuevo apellido 2");
				
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado ORDER BY id_empleado;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getString("n_ss").equalsIgnoreCase("Nuevo N_ss");
				found = found && rs.getString("nombre").equalsIgnoreCase("Nuevo nombre");
				found = found && rs.getString("apellido1").equalsIgnoreCase("Nuevo apellido 1");
				found = found && rs.getString("apellido2").equalsIgnoreCase("Nuevo apellido 2");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(1, n);
		
		conn.close();
	}
	
	@Test
	void test_setters_local() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Calle Nueva 1", "Nada", conn, false);
		l1.DBSync = true;
		l1.setTiene_cafeteria(false);
		l1.setDireccion("Nueva direccion");
		l1.setDescripcion("Nueva descripcion");
				
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local ORDER BY id_local;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getInt("tiene_cafeteria") == 0;
				found = found && rs.getString("direccion").equalsIgnoreCase("Nueva direccion");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Nueva descripcion");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(1, n);
		
		conn.close();
	}
	
	@Test
	void test_setters_producto() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Producto 1", "Descr", conn, false);
		p1.DBSync = true;
		p1.setNombre("Nuevo nombre");
		p1.setDescripcion("Nueva descripcion");
				
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto ORDER BY id_producto;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_producto") == 1;
				found = found && rs.getString("nombre").equalsIgnoreCase("Nuevo nombre");
				found = found && rs.getString("descripcion").equalsIgnoreCase("Nueva descripcion");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(1, n);
		
		conn.close();
	}
	

	// //////////////////////
	// destroy
	// /////////////////////
	
	@Test
	void test_destroy_empleado() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Empleado e1 = new Empleado(1, "1111-1111", "Jorge", "Sol", "Fuertes", conn, false);
		e1.DBSync = true;
		e1.destroy();
				
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado WHERE id_empleado=1;");
			if (rs.next()) n++;
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(0, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, e1.getId_empleado());
		
		conn.close();
	}
	
	@Test
	void test_destroy_local() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Local l1 = new Local(1, true, "Calle Nueva 1", "Nada", conn, false);
		l1.DBSync = true;
		l1.destroy();
				
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local WHERE id_local=1;");
			if (rs.next()) n++;
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(0, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, l1.getId_local());
		
		conn.close();
	}
	
	@Test
	void test_destroy_producto() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Producto p1 = new Producto(1, "Producto 1", "Descr", conn, false);
		p1.DBSync = true;
		p1.destroy();
				
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto WHERE id_producto=1;");
			if (rs.next()) n++;
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(0, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, p1.getId_producto());
		
		conn.close();
	}
	
}
