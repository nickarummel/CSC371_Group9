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
 * @author Nick Rummel
 * CSC371-01
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
	 * Instance variable to store the Main Menu GUI
	 */
	protected static MainMenuGUI mmgui;
	
	
	/**
	 * Main method to run this project
	 * @param args arguments
	 */
	public static void main(String[] args)
	{
		activateJDBC();
		createDBConnection();

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
		runGUI();

		try
		{
			m_dbConn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates a connection to the database that you can then send commands to.
	 */
	public static void createDBConnection()
	{

		try
		{
			m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Getter for the database connection instance variable. If the connection
	 * is null or closed, the connection will be recreated.
	 * @return the Connection instance to the database.
	 */
	public static Connection getDBConnection()
	{
		try
		{
			if (m_dbConn == null || m_dbConn.isClosed())
			{
				createDBConnection();
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return m_dbConn;
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

	/**
	 * Method that calls the Main Menu GUI.
	 */
	public static void runGUI()
	{
		mmgui = new MainMenuGUI();
	}
	
	public static MainMenuGUI getMainMenuGUI()
	{
		return mmgui;
	}
}