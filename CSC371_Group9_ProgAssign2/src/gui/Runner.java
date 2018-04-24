package gui;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import part1.SQLTables;
import part2.SQLInserts;

/**
 * Class to run Programming Assignment 2
 * @author Aaron Gerber
 * @author Austin Smale
 * @author David Gautschi
 * @author Nick Rummel CSC371-01
 */
public class Runner
{
	/**
	 * The location of the database that I was assigned to.
	 */
	private static final String DB_LOCATION = "jdbc:mysql://db.cs.ship.edu:3306/csc371-09";

	/**
	 * The login username to access the database.
	 */
	private static final String LOGIN_NAME = "csc371-09";

	/**
	 * The login password to access the database.
	 */
	private static final String PASSWORD = "Password09";

	/**
	 * Instance variable to store the Connection object for the database.
	 */
	protected static Connection m_dbConn = null;

	/**
	 * Main method to run this project
	 * @param args arguments
	 */
	public static void main(String[] args)
	{
		activateJDBC();
		/**
		 * Creates a connection to the database that you can then send commands
		 * to.
		 */
		try
		{
			m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		/**
		 * To get the meta data for the DB.
		 */
		try
		{
			DatabaseMetaData meta = m_dbConn.getMetaData();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		// runPartOne();
		// runPartTwo();

		try
		{
			m_dbConn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * From Dr. Girard's sample code. This is the recommended way to activate
	 * the JDBC drivers, but is only setup to work with one specific driver.
	 * Setup to work with a MySQL JDBC driver.
	 * 
	 * @return Returns true if it successfully sets up the driver.
	 */
	public static boolean activateJDBC()
	{
		try
		{
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		} catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

		return true;
	}

	/**
	 * Executes Part 1's code, where the CREATE TABLE statements are executed on
	 * the MySQL Database.
	 */
	public static void runPartOne()
	{
		SQLTables st = new SQLTables();
		st.createTables(m_dbConn);
	}

	/**
	 * Executes Part 2's code, where the INSERT INTO * statements are executed
	 * on the database.
	 */
	public static void runPartTwo()
	{
		SQLInserts si = new SQLInserts();
		si.createRows(m_dbConn);
	}
}