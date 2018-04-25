package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * GUI for the Character table
 * @author Nick Rummel CSC371
 */
public class CharacterGUI extends JFrame
{
	/**
	 * Instance variables that contain all of the necessary GUI features and
	 * connection to the database.
	 */
	JButton[] southButtons;
	JButton selectButton;
	JLabel tableName, imageLabel;
	JTextField nameTF, userTF, locIDTF, maxHPTF, curHPTF, strengthTF, staminaTF;
	JComboBox<String> dropBox;
	Connection m_dbConn;

	/**
	 * Constructor of CHARACTER table GUI.
	 * @param m_dbConn The database connection to query data.
	 */
	public CharacterGUI(Connection m_dbConn)
	{
		this.m_dbConn = m_dbConn;

		setLayout(new BorderLayout());

		tableName = new JLabel("CHARACTER", SwingConstants.CENTER);

		String[] characterNames = queryDatabaseForPrimaryKeys(m_dbConn);

		dropBox = new JComboBox<String>(characterNames);

		selectButton = new JButton("Select");

		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);

		add("North", northPanel);

		JPanel westPanel = new JPanel(new GridLayout(8, 1));
		ResultSet rs = queryDatabaseForDataRow(m_dbConn, characterNames[0]);
		westPanel.add(new JLabel("Name"));
		try
		{
			nameTF = new JTextField(rs.getString("P_Name"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		westPanel.add(nameTF);

		westPanel.add(new JLabel("Player Username"));
		try
		{
			userTF = new JTextField(rs.getString("P_Username"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		westPanel.add(userTF);

		westPanel.add(new JLabel("Location ID"));
		try
		{
			locIDTF = new JTextField(rs.getString("L_ID"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		westPanel.add(locIDTF);

		westPanel.add(new JLabel("Max HP"));
		try
		{
			maxHPTF = new JTextField(rs.getString("P_Max_HP"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		westPanel.add(maxHPTF);

		add("West", westPanel);

		JPanel eastPanel = new JPanel(new GridLayout(8, 1));
		eastPanel.add(new JLabel("Current HP"));
		try
		{
			curHPTF = new JTextField(rs.getString("P_Cur_HP"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		eastPanel.add(curHPTF);

		eastPanel.add(new JLabel("Strength"));
		try
		{
			strengthTF = new JTextField(rs.getString("P_Strength"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		eastPanel.add(strengthTF);

		eastPanel.add(new JLabel("Stamina"));
		try
		{
			staminaTF = new JTextField(rs.getString("P_Stamina"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		eastPanel.add(staminaTF);

		add("East", eastPanel);

		JPanel southPanel = new JPanel(new GridLayout(1, 3));
		southButtons = new JButton[3];
		southButtons[0] = new JButton("Delete");
		southButtons[1] = new JButton("Update");
		southButtons[2] = new JButton("Insert");
		for (int i = 0; i < southButtons.length; i++)
		{
			southPanel.add(southButtons[i]);
		}
		add("South", southPanel);

		pack();
		setVisible(true);
	}

	/**
	 * Retrieves a list of primary keys from the PLAYER_CHAR table.
	 * @param conn The connection to the database.
	 * @return The list of primary keys as a String array.
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection conn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT P_Name FROM PLAYER_CHAR";
		String selectCount = "SELECT COUNT(*) FROM PLAYER_CHAR";
		String[] data = null;

		try
		{
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(selectCount);
			int count = 1;
			while (rs.next())
			{
				count = rs.getInt(1);
			}

			data = new String[count];

			rs = stmt.executeQuery(selectStmt);
			int i = 0;
			while (rs.next() && i < data.length)
			{
				data[i] = rs.getString("P_Name");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Retrieves one row of data from the PLAYER_CHAR table using the primary
	 * key.
	 * @param conn The connection to the database.
	 * @param pKey The value of the primary key that is used in the SELECT
	 *            statement.
	 * @return the Result Set that will contain that row of data.
	 */
	public ResultSet queryDatabaseForDataRow(Connection conn, String pKey)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM PLAYER_CHAR WHERE P_Name=\"" + pKey + "\"";

		try
		{
			Statement stmt = conn.createStatement();

			rs = stmt.executeQuery(selectStmt);
			rs.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return rs;
	}

}
