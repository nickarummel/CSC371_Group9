package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

/**
 * The GUI class for Moderator Permissions
 * Most of this code is similar due to the intention of keeping the same style as the other classes
 * Changes are mostly made where needed, such as in the DB calls and to match the structure to the
 * mock up of the GUI made in homework 5. This is the same structure as ManagerPermsGUI, and as
 * such, the code was left the same aside from updating references to Manager with Moderator.
 * @author Aaron Gerber
 */
public class ModeratorPermsGUI  extends JFrame implements ActionListener
{
	//The string fields needed.
	String [] modUsernames;
	String curUsername;
	
	//The GUI objects needed.
	protected JButton[] southButtons;
	protected JTextField modUserTF, permTF;
	protected JLabel tableName, imageLabel;
	protected JComboBox<String> dropBox;
	protected JPanel northPanel, southPanel, centerPanel;
	protected JButton selectButton;
	
	
	public ModeratorPermsGUI()
	{
		curUsername = null;
		updateGUI(Runner.getDBConnection());
	}

	/**
	 * Updates the GUI with the correct information from the database when called.
	 * @param m_dbConn
	 */
	public void updateGUI(Connection m_dbConn)
	{
		// Reset content pane before building it
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// Get primary keys and build the drop down box
		modUsernames = queryDatabaseForPrimaryKeys(m_dbConn);
		
		tableName = new JLabel("MODERATOR PERMISSIONS", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(modUsernames);

		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		// Add the table name, drop down box, and select button
		// to the north panel
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);
		add("North", northPanel);
		
		centerPanel = new JPanel(new GridLayout(6, 1));
		
		if (curUsername != null && !(curUsername.equals("(new entry)")))
		{
			// The user wants to view a current row of data from the table
			// So add in blank text fields to both west and east panels
			ResultSet rs = queryDatabaseForDataRow(m_dbConn, curUsername);
			centerPanel.add(new JLabel("Moderator Username"));
			try
			{
				modUserTF = new JTextField(rs.getString("Mod_Username"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			centerPanel.add(modUserTF);
			
			centerPanel.add(new JLabel("Moderator Permissions"));
			try
			{
				permTF = new JTextField(rs.getString("Mod_Permission"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			centerPanel.add(permTF);

			add("Center", centerPanel);
		} 
		else if (curUsername != null && curUsername.equals("(new entry)"))
		{
			// The user wants to add a new entry to the table
			modUserTF = new JTextField();
			permTF = new JTextField();
			centerPanel.add(new JLabel("Moderator Username"));
			centerPanel.add(modUserTF);
			centerPanel.add(new JLabel("Moderator Permissions"));
			centerPanel.add(permTF);
			add("Center", centerPanel);
		}
		
		// Add the delete, update, and insert buttons to the bottom
		JPanel southPanel = new JPanel(new GridLayout(1, 3));
		southButtons = new JButton[3];
		southButtons[0] = new JButton("Delete");
		southButtons[1] = new JButton("Update");
		southButtons[2] = new JButton("Insert");
		for (int i = 0; i < southButtons.length; i++)
		{
			southButtons[i].addActionListener(this);
			southPanel.add(southButtons[i]);
		}
		
		if (curUsername != null && curUsername.equals("(new entry)"))
		{
			// Only the insert button can be clicked if the user
			// wants to add a new entry to the table
			southButtons[0].setEnabled(false);
			southButtons[1].setEnabled(false);
			southButtons[2].setEnabled(true);
		} else if (curUsername != null)
		{
			// Only the delete and update buttons can be clicked
			// if the user wants to view a row of data from the table
			southButtons[0].setEnabled(true);
			southButtons[1].setEnabled(true);
			southButtons[2].setEnabled(false);
		} else
		{
			// When selecting a primary key from the drop box,
			// all three buttons (delete, update, insert) are unavailable.
			southButtons[0].setEnabled(false);
			southButtons[1].setEnabled(false);
			southButtons[2].setEnabled(false);
		}
		add("South", southPanel);
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();
	}
	
	/**
	 * Grabs the results of the query being made, with reference being what column in the database is being accessed.
	 * @param m_dbconn
	 * @param col
	 * @return results of the query
	 */
	private ResultSet queryDatabaseForDataRow(Connection m_dbconn, String col) 
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM MOD_PERMS WHERE Mod_Username=\"" + col + "\"";

		try
		{
			Statement stmt = m_dbconn.createStatement();

			rs = stmt.executeQuery(selectStmt);
			rs.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return rs;
	}

	/**
	 * Grabs the primary keys that will be used to find information in the database
	 * @param m_dbconn
	 * @return set of strings for the drop down menu
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection m_dbconn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT Mod_Username FROM MOD_PERMS";
		String selectCount = "SELECT COUNT(*) FROM MOD_PERMS";
		String[] data = null;
		
		try
		{
			// Retrieve the count of primary keys in the table
			Statement stmt = m_dbconn.createStatement();
			rs = stmt.executeQuery(selectCount);
			int count = 1;
			while (rs.next())
			{
				count = rs.getInt(1);
			}

			//Dynamically create the array so as to not worry about the number of items
			data = new String[count + 1];
			data[0] = "(new entry)";

			// Retrieve the primary keys from the table
			// and store each one in an array of Strings
			rs = stmt.executeQuery(selectStmt);
			int i = 0;
			while (rs.next() && i < data.length)
			{
				data[i + 1] = rs.getString("Mod_Username");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * Tells the GUI when to update and how to update.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// The select button was pressed
		if (e.getSource() == selectButton)
		{
			int index = dropBox.getSelectedIndex();
			curUsername = modUsernames[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);

		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curUsername = modUsernames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM MOD_PERMS WHERE Mod_Username=?");
				stmt.setString(1, curUsername);
				stmt.executeUpdate();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			curUsername = null;
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);
		}

		// The update button was pressed, so update the values of each column
		if (e.getSource() == southButtons[1])
		{
			int index = dropBox.getSelectedIndex();
			curUsername = modUsernames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement(
						"UPDATE MOD_PERMS SET Mod_Username=?, Mod_Permission=? WHERE Mod_Username=?");
				stmt.setString(1, modUserTF.getText().trim());
				stmt.setString(2, permTF.getText().trim());
				stmt.setString(3, curUsername);
				stmt.executeUpdate();
			} catch (SQLException e2)
			{
				e2.printStackTrace();
			}
			curUsername = null;
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);
		}

		// The insert button was pressed, so add the data to the table
		if (e.getSource() == southButtons[2])
		{
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("INSERT INTO MOD_PERMS VALUES (?, ?)");
				stmt.setString(1, modUserTF.getText().trim());
				stmt.setString(2, permTF.getText().trim());
				stmt.executeUpdate();
			} catch (SQLException e3)
			{
				e3.printStackTrace();
			}
			curUsername = null;

			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);

		}
	}
	
}
