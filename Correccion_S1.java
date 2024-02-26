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

class Correccion_S1 {
	
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

	@Test
	void test_connect() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertTrue(conn.connect());
		assertTrue(conn.connect());
		DBConnection conn2 = new DBConnection("localhost", 3306, "other_user", "other_pass", "panaderias");
		assertFalse(conn2.connect());
	}

	@Test
	void test_close() {
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertTrue(conn.close());
		conn.connect();
		assertTrue(conn.close());
		assertTrue(conn.close());
	}

	@Test
	void test_update() {
		try {
			this.executeScript("scripts/panaderias_drop_table_prueba.sql");
		} catch (Exception e) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertEquals(-1, conn.update("CREATE TBLE IF NOT EXISTS prueba (id_prueba INT, columna1 INT, columna2 VARCHAR(100), columna3 VARCHAR(100), columna4 DATE, PRIMARY KEY (id_prueba));"));
		assertEquals(0, conn.update("CREATE TABLE IF NOT EXISTS prueba (id_prueba INT, columna1 INT, columna2 VARCHAR(100), columna3 VARCHAR(100), columna4 DATE, PRIMARY KEY (id_prueba));"));
		assertEquals(1, conn.update("INSERT INTO prueba VALUES (1, 0, 'texto1', 'texto2', '2023-03-20');"));
		conn.close();
		
		Statement st;
		ResultSet rs;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='prueba';");
			boolean columnas[] = {false, false, false, false, false};
			int n_columnas = 0;
			while (rs.next()) {
				String columna = rs.getString("COLUMN_NAME");
				String data_type = rs.getString("DATA_TYPE");
				if (columna.equals("id_prueba") && data_type.equals("int")) columnas[0]=true;
				if (columna.equals("columna1") && data_type.equals("int")) columnas[1]=true;
				if (columna.equals("columna2") && data_type.equals("varchar")) columnas[2]=true;
				if (columna.equals("columna3") && data_type.equals("varchar")) columnas[3]=true;
				if (columna.equals("columna4") && data_type.equals("date")) columnas[4]=true;
				n_columnas++;
			}
			assertArrayEquals(new boolean[] {true, true, true, true, true}, columnas);
			assertEquals(columnas.length, n_columnas);
			
			if (c!=null) c.close();
			if (rs!=null) rs.close();
			if (st!=null) st.close();
		} catch (SQLException e) { }
	}

	@Test
	void test_update_params() {
		try {
			this.executeScript("scripts/panaderias_drop_table_prueba.sql");
			this.executeScript("scripts/panaderias_create_table_prueba.sql");
		} catch (Exception e) { }
		
		ArrayList<Object> params = new ArrayList<Object>();
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		params.add(2); params.add(0); params.add("texto3"); params.add("texto4"); params.add(java.sql.Date.valueOf("2023-01-01"));
		assertEquals(1, conn.update("INSERT INTO prueba VALUES (?, ?, ?, ?, ?);", params));
		params.removeAll(params);
		params.add(3); params.add(-1); params.add("texto5"); params.add("texto6"); params.add(java.sql.Date.valueOf("2023-02-01"));
		assertEquals(1, conn.update("INSERT INTO prueba VALUES (?, ?, ?, ?, ?);", params));
		conn.close();
		
		Statement st;
		ResultSet rs;
		this.openConnection();
		try {
			st = c.createStatement();
			rs = st.executeQuery("SELECT * FROM prueba WHERE id_prueba IN (2,3) ORDER BY id_prueba;");
			if (rs.next()) {
				assertEquals(2, rs.getInt("id_prueba"));
				assertEquals(0, rs.getInt("columna1"));
				assertEquals("texto3", rs.getString("columna2"));
				assertEquals("texto4", rs.getString("columna3"));
				assertEquals(java.sql.Date.valueOf("2023-01-01"), rs.getDate("columna4"));
			}
			if (rs.next()) {
				assertEquals(3, rs.getInt("id_prueba"));
				assertEquals(-1, rs.getInt("columna1"));
				assertEquals("texto5", rs.getString("columna2"));
				assertEquals("texto6", rs.getString("columna3"));
				assertEquals(java.sql.Date.valueOf("2023-02-01"), rs.getDate("columna4"));
			}
			if (rs!=null) rs.close();
			if (st!=null) st.close();
			if (c!=null) c.close();
		} catch (SQLException e) { }
	}
	
	
	@Test
	void test_query() {
		try {
			this.executeScript("scripts/panaderias_drop_table_prueba.sql");
			this.executeScript("scripts/panaderias_create_table_prueba.sql");
			this.executeScript("scripts/panaderias_insert_prueba.sql");
		} catch (Exception e) { }
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertEquals(null, conn.query("SELEC * FROM prueba ORDER BY id_prueba;"));
		ResultSet rs1 = conn.query("SELECT * FROM prueba ORDER BY id_prueba;");
		
		Statement st;
		ResultSet rs2;
		this.openConnection();
		try {
			st = c.createStatement();
			rs2 = st.executeQuery("SELECT * FROM prueba ORDER BY id_prueba;");
			while (rs2.next()) {
				assertTrue(rs1.next());
				assertEquals(rs2.getInt("id_prueba"), rs1.getInt("id_prueba"));
				assertEquals(rs2.getInt("columna1"), rs1.getInt("columna1"));
				assertEquals(rs2.getString("columna2"), rs1.getString("columna2"));
				assertEquals(rs2.getString("columna3"), rs1.getString("columna3"));
				assertEquals(rs2.getDate("columna4"), rs1.getDate("columna4"));
			}
			assertFalse(rs1.next());
			
			conn.close();
			if (rs1!=null) rs1.close();
			if (rs2!=null) rs2.close();
			if (st!=null) st.close();
			if (c!=null) c.close();
		} catch (SQLException e) { }
	}
	
	
	@Test
	void test_query_params() {
		try {
			this.executeScript("scripts/panaderias_drop_table_prueba.sql");
			this.executeScript("scripts/panaderias_create_table_prueba.sql");
			this.executeScript("scripts/panaderias_insert_prueba.sql");
		} catch (Exception e) { }

		ArrayList<Object> params = new ArrayList<Object>();
		
		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertEquals(null, conn.query("SELECT * FROM prueba WHERE id_prueba=? ORDER BY id_prueba;", params));
		
		params.add(1); params.add("Texto7");
		ResultSet rs1 = conn.query("SELECT * FROM prueba WHERE id_prueba=? OR columna2=? ORDER BY id_prueba;", params);
		
		Statement st;
		ResultSet rs2;
		this.openConnection();
		try {
			st = c.createStatement();
			rs2 = st.executeQuery("SELECT * FROM prueba WHERE id_prueba=1 OR columna2='Texto7' ORDER BY id_prueba;");
			while (rs2.next()) {
				assertTrue(rs1.next());
				assertEquals(rs2.getInt("id_prueba"), rs1.getInt("id_prueba"));
				assertEquals(rs2.getInt("columna1"), rs1.getInt("columna1"));
				assertEquals(rs2.getString("columna2"), rs1.getString("columna2"));
				assertEquals(rs2.getString("columna3"), rs1.getString("columna3"));
				assertEquals(rs2.getDate("columna4"), rs1.getDate("columna4"));
			}
			assertFalse(rs1.next());
			
			conn.close();
			if (rs1!=null) rs1.close();
			if (rs2!=null) rs2.close();
			if (st!=null) st.close();
			if (c!=null) c.close();
		} catch (SQLException e) { }
	}
	
	
	@Test
	void test_table_exists() {
		try {
			this.executeScript("scripts/panaderias_drop_table_prueba.sql");
			this.executeScript("scripts/panaderias_create_table_prueba.sql");
		} catch (Exception e) { }

		DBConnection conn = new DBConnection("localhost", 3306, "panaderia_user", "panaderia_pass", "panaderias");
		assertTrue(conn.tableExists("prueba"));
		assertTrue(conn.tableExists("prueba"));
		assertFalse(conn.tableExists("otra"));
	}

}
