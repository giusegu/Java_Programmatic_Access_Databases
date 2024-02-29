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

class Correccion_S3 {
	
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
	void test_createTable_trabaja() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t = new Trabaja(-1, -1, java.sql.Date.valueOf("2023-04-01"), java.sql.Date.valueOf("2023-04-01"), conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(t.createTable());
		
		Statement st;
		ResultSet rs;
		boolean found = false;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				found = found || rs.getString(1).equalsIgnoreCase("trabaja");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		assertFalse(t.createTable());
		
		conn.close();
	}
	
	@Test
	void test_createTable_vende() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v = new Vende(-1, -1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(v.createTable());
		
		Statement st;
		ResultSet rs;
		boolean found = false;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SHOW TABLES;");
			while (rs.next()) {
				found = found || rs.getString(1).equalsIgnoreCase("vende");
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		assertFalse(v.createTable());
		
		conn.close();
	}
	
	

	// //////////////////////
	// insertEntry
	// /////////////////////
	
	@Test
	void test_insertEntry_trabaja() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-04-01"), conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(t.insertEntry());
		assertFalse(t.insertEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			if (rs.next()) {
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getDate("fecha_inicio").equals(java.sql.Date.valueOf("2021-03-01"));
				found = found && rs.getDate("feha_fin").equals(java.sql.Date.valueOf("2023-04-01"));
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_insertEntry_vende() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v = new Vende(1, 1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(v.insertEntry());
		assertFalse(v.insertEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM vende;");
			if (rs.next()) {
				found = found && rs.getInt("id_local") == 1;
				found = found && rs.getInt("id_producto") == 1;
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
	void test_updateEntry_trabaja() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-04-01"), conn, false);
		Trabaja t2 = new Trabaja(1, 2, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-04-01"), conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(t1.updateEntry());
		assertFalse(t2.updateEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			if (rs.next()) {
				found = found && rs.getDate("fecha_inicio").equals(java.sql.Date.valueOf("2021-03-01"));
				found = found && rs.getDate("feha_fin").equals(java.sql.Date.valueOf("2023-04-01"));
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		
		conn.close();
	}
	
	@Test
	void test_updateEntry_vende() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		Vende v2 = new Vende(1, 2, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		v1.updateEntry();
		v2.updateEntry();
		
		conn.close();
	}
	

	// //////////////////////
	// deleteEntry
	// /////////////////////
	
	@Test
	void test_deleteEntry_trabaja() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-04-01"), conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(t1.deleteEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			found = rs.next();
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertFalse(found);
		
		conn.close();
	}

	@Test
	void test_deleteEntry_vende() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		assertTrue(v1.deleteEntry());
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM vende;");
			found = rs.next();
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertFalse(found);
		
		conn.close();
	}


	// //////////////////////
	// getEntryChanges
	// /////////////////////
	
	@Test
	void test_getEntryChanges_trabaja() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), conn, false);
		Trabaja t2 = new Trabaja(1, 2, java.sql.Date.valueOf("2021-03-01"), conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		t1.getEntryChanges();
		t2.getEntryChanges();
		
		assertEquals(java.sql.Date.valueOf("2023-03-15"), t1.getFecha_fin());
		assertTrue(t2.getFecha_fin()==null || t2.getFecha_fin().equals(DBConnection.NULL_SENTINEL_DATE));
		
		conn.close();
	}
	
	@Test
	void test_getEntryChanges_vende() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		Vende v2 = new Vende(1, 2, conn, false);
		
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		v1.getEntryChanges();
		v2.getEntryChanges();
		
		conn.close();
	}
	
	
	// //////////////////////
	// Constructores
	// /////////////////////
	
	@Test
	void test_constructores_trabaja() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), conn, true);
		Trabaja t2 = new Trabaja(1, 3, java.sql.Date.valueOf("2021-03-01"), conn, true);
		
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			if (rs.next()) {
				n++;
				found = found && rs.getInt("id_empleado") == 1;
				found = found && rs.getDate("fecha_inicio").equals(java.sql.Date.valueOf("2021-03-01"));
			}
			assertEquals(DBConnection.NULL_SENTINEL_INT, t2.getId_empleado());
			assertEquals(DBConnection.NULL_SENTINEL_INT, t2.getId_local());
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertTrue(found);
		assertEquals(1, n);
		
		conn.close();
	}
	
	@Test
	void test_constructores_vende() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, true);
		Vende v2 = new Vende(1, 3, conn, true);
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM vende;");
			if (rs.next()) {
				n++;
			}
			assertEquals(DBConnection.NULL_SENTINEL_INT, v2.getId_producto());
			assertEquals(DBConnection.NULL_SENTINEL_INT, v2.getId_local());
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(1, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, v2.getId_local());
		assertEquals(DBConnection.NULL_SENTINEL_INT, v2.getId_producto());
		
		conn.close();
	}
	
	

	// //////////////////////
	// Getters
	// /////////////////////
	
	@Test
	void test_getters_trabaja() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2022-03-14"), conn, false);
		t1.DBSync = true;
		assertEquals(1, t1.getId_empleado());
		assertEquals(java.sql.Date.valueOf("2023-03-15"), t1.getFecha_fin());
				
		conn.close();
	}
	
	@Test
	void test_getters_vende() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		v1.DBSync = true;
		assertEquals(1, v1.getId_local());
		assertEquals(1, v1.getId_producto());
				
		conn.close();
	}
	

	// //////////////////////
	// Setters
	// /////////////////////
	
	@Test
	void test_setters_trabaja() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-03-15"), conn, false);
		t1.DBSync = true;
		t1.setFecha_fin(java.sql.Date.valueOf("2022-03-01"));
				
		Statement st;
		ResultSet rs;
		boolean found = true;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			if (rs.next()) {
				n++;
				found = found && rs.getDate("fecha_fin").equals(java.sql.Date.valueOf("2022-03-01"));
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
	void test_setters_vende() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		v1.DBSync = true;
		conn.close();
	}
	

	// //////////////////////
	// destroy
	// /////////////////////
	
	@Test
	void test_destroy_trabaja() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Trabaja t1 = new Trabaja(1, 1, java.sql.Date.valueOf("2021-03-01"), java.sql.Date.valueOf("2023-03-15"), conn, false);
		t1.DBSync = true;
		t1.destroy();
				
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			if (rs.next()) n++;
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(0, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, t1.getId_empleado());
		
		conn.close();
	}
	
	@Test
	void test_destroy_vende() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		Vende v1 = new Vende(1, 1, conn, false);
		v1.DBSync = true;
		v1.destroy();
				
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM vende;");
			if (rs.next()) n++;
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(0, n);
		assertEquals(DBConnection.NULL_SENTINEL_INT, v1.getId_local());
		
		conn.close();
	}
	

	// //////////////////////
	// getFromCSV
	// /////////////////////
	
	@Test
	void test_getEmpleadosFromCSV() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Empleado> a = DataManager.getEmpleadosFromCSV("scripts/empleados.csv", conn, true);
		
		assertEquals(5, a.size());
		for (Empleado e: a) {
			if (e.getId_empleado() == 2) {
				assertEquals("2222-2222", e.getN_ss());
				assertEquals("Angeles", e.getNombre());
				assertEquals("Sanchez", e.getApellido1());
				assertEquals("Fernandez", e.getApellido2());
			} else if (e.getId_empleado() == 3) {
				assertEquals("3333-3333", e.getN_ss());
				assertEquals("Jose", e.getNombre());
				assertEquals("Ramirez", e.getApellido1());
				assertEquals("Bueno", e.getApellido2());
			}
			assertTrue(e.DBSync);
		}
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM empleado;");
			while (rs.next()) {
				n++;
				if (rs.getInt("id_empleado") == 1) {
					assertEquals("1111-1111", rs.getString("n_ss"));
					assertEquals("Jaime", rs.getString("nombre"));
					assertEquals("Ruiz", rs.getString("apellido1"));
					assertEquals("Barbate", rs.getString("apellido2"));
				} else if (rs.getInt("id_empleado") == 4) {
					assertEquals("4444-4444", rs.getString("n_ss"));
					assertEquals("Ascension", rs.getString("nombre"));
					assertEquals("del Rey", rs.getString("apellido1"));
					assertEquals("Olmo", rs.getString("apellido2"));
				}
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(5, n);
		
		conn.close();
	}
		
	@Test
	void test_getLocalesFromCSV() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Local> a = DataManager.getLocalesFromCSV("scripts/locales.csv", conn, true);
		
		assertEquals(5, a.size());
		for (Local l: a) {
			if (l.getId_local() == 2) {
				assertFalse(l.getTiene_cafeteria());
				assertEquals("Calle Baja, 2", l.getDireccion());
				assertEquals("Descripcion 2", l.getDescripcion());
			} else if (l.getId_local() == 3) {
				assertTrue(l.getTiene_cafeteria());
				assertEquals("Calle Media, 3", l.getDireccion());
				assertEquals("Descripcion 3", l.getDescripcion());
			}
			assertTrue(l.DBSync);
		}
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM local;");
			while (rs.next()) {
				n++;
				if (rs.getInt("id_local") == 1) {
					assertEquals(0, rs.getInt("tiene_cafeteria"));
					assertEquals("Calle Alta, 1", rs.getString("direccion"));
					assertEquals("Descripcion 1", rs.getString("descripcion"));
				} else if (rs.getInt("id_local") == 5) {
					assertEquals(1, rs.getInt("tiene_cafeteria"));
					assertEquals("Plaza del Rio, 5", rs.getString("direccion"));
					assertEquals("Descripcion 5", rs.getString("descripcion"));
				}
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(5, n);
		
		conn.close();
	}
	
	@Test
	void test_getProductosFromCSV() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Producto> a = DataManager.getProductosFromCSV("scripts/productos.csv", conn, true);
		
		assertEquals(4, a.size());
		for (Producto p: a) {
			if (p.getId_producto() == 2) {
				assertEquals("Magdalena", p.getNombre());
				assertEquals("Magdalena con azucar por encima", p.getDescripcion());
			} else if (p.getId_producto() == 3) {
				assertEquals("Palmera", p.getNombre());
				assertEquals("Palmera de hojaldre horneado", p.getDescripcion());
			}
			assertTrue(p.DBSync);
		}
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM producto;");
			while (rs.next()) {
				n++;
				if (rs.getInt("id_producto") == 1) {
					assertEquals("Barra de pan", rs.getString("nombre"));
				} else if (rs.getInt("id_producto") == 4) {
					assertEquals("Pan rustico", rs.getString("nombre"));
					assertEquals("Hecho con masa madre", rs.getString("descripcion"));
				}
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(4, n);
		
		conn.close();
	}
	
	@Test
	void test_getTrabajaFromCSV() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Trabaja> a = DataManager.getTrabajaFromCSV("scripts/trabaja_reduced.csv", conn, true);
		
		assertEquals(4, a.size());
		for (Trabaja t: a) {
			if (t.getId_empleado()==1 && t.getId_local()==1 && t.getFecha_inicio().equals(java.sql.Date.valueOf("2000-02-01"))) {
				assertEquals(java.sql.Date.valueOf("2005-04-30"), t.getFecha_fin());
			} else if (t.getId_empleado()==1 && t.getId_local()==1 && t.getFecha_inicio().equals(java.sql.Date.valueOf("2005-06-01"))) {
				assertEquals(java.sql.Date.valueOf("2019-07-15"), t.getFecha_fin());
			}
			assertTrue(t.DBSync);
		}
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM trabaja;");
			while (rs.next()) {
				n++;
				if (rs.getInt("id_empleado")==1 && rs.getInt("id_local")==2 && rs.getDate("fecha_inicio").equals(java.sql.Date.valueOf("2005-05-01"))) {
					assertEquals(java.sql.Date.valueOf("2005-05-31"), rs.getDate("fecha_fin"));
				} else if (rs.getInt("id_empleado")==2 && rs.getInt("id_local")==2 && rs.getDate("fecha_inicio").equals(java.sql.Date.valueOf("2005-05-01"))) {
					assertEquals(java.sql.Date.valueOf("2009-12-31"), rs.getDate("fecha_fin"));
				}
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(4, n);
		
		conn.close();
	}
	
	@Test
	void test_getVendeFromCSV() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Vende> a = DataManager.getVendeFromCSV("scripts/vende_reduced.csv", conn, true);
		
		assertEquals(3, a.size());
		for (Vende v: a) {
			assertTrue(v.DBSync);
		}
		
		Statement st;
		ResultSet rs;
		int n=0;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM vende;");
			while (rs.next()) {
				n++;
			}
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException ex) { }
		
		assertEquals(3, n);
		
		conn.close();
	}
	

	// //////////////////////
	// getFromDB
	// /////////////////////
	
	@Test
	void test_getEmpleadosFromDB() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Empleado> a = DataManager.getEmpleadosFromDB(conn, true);
		
		assertEquals(2, a.size());
		for (Empleado e: a) {
			if (e.getId_empleado() == 1) {
				assertEquals("1111-1111", e.getN_ss());
				assertEquals("Jorge", e.getNombre());
				assertEquals("Sol", e.getApellido1());
				assertEquals("Fuertes", e.getApellido2());
			} else if (e.getId_empleado() == 2) {
				assertEquals("2222-2222", e.getN_ss());
				assertEquals("Maria", e.getNombre());
				assertEquals("Perez", e.getApellido1());
				assertEquals("Ruiz", e.getApellido2());
			}
			assertTrue(e.DBSync);
		}
		conn.close();
	}
	
	@Test
	void test_getLocalesFromDB() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Local> a = DataManager.getLocalesFromDB(conn, true);
		
		assertEquals(2, a.size());
		for (Local l: a) {
			if (l.getId_local() == 1) {
				assertEquals("Calle Nueva 1", l.getDireccion());
				assertEquals("Nada", l.getDescripcion());
			} else if (l.getId_local() == 2) {
				assertEquals("Calle Vieja 2", l.getDireccion());
				assertEquals("", l.getDescripcion());
			}
			assertTrue(l.DBSync);
		}
		conn.close();
	}
	
	@Test
	void test_getProductosFromDB() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Producto> a = DataManager.getProductosFromDB(conn, true);
		
		assertEquals(2, a.size());
		for (Producto p: a) {
			if (p.getId_producto() == 1) {
				assertEquals("Producto 1", p.getNombre());
				assertEquals("Descr", p.getDescripcion());
			} else if (p.getId_producto() == 2) {
				assertEquals("Producto 2", p.getNombre());
				assertEquals("", p.getDescripcion());
			}
			assertTrue(p.DBSync);
		}
		conn.close();
	}
	
	@Test
	void test_getTrabajaFromDB() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Trabaja> a = DataManager.getTrabajaFromDB(conn, true);
		
		assertEquals(1, a.size());
		for (Trabaja t: a) {
			assertEquals(1, t.getId_empleado());
			assertEquals(1, t.getId_local());
			assertEquals(java.sql.Date.valueOf("2021-03-01"), t.getFecha_inicio());
			assertEquals(java.sql.Date.valueOf("2023-03-15"), t.getFecha_fin());
			assertTrue(t.DBSync);
		}
		conn.close();
	}
	
	@Test
	void test_getVendeFromDB() {
		try {
			this.executeScript("scripts/panaderias_drop_all_tables.sql");
			this.executeScript("scripts/panaderias_create_all_tables.sql");
			this.executeScript("scripts/panaderias_insert_all_tables.sql");
			this.executeScript("scripts/panaderias_create_s3_tables.sql");
			this.executeScript("scripts/panaderias_insert_s3_tables.sql");
		} catch (Exception ex) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		ArrayList<Vende> a = DataManager.getVendeFromDB(conn, true);
		
		assertEquals(1, a.size());
		for (Vende v: a) {
			assertEquals(1, v.getId_local());
			assertEquals(1, v.getId_producto());
			assertTrue(v.DBSync);
		}
		conn.close();
	}
	
}
