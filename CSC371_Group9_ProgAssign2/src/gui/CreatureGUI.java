package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * GUI for the Creature
 * @author Aaron Gerber
 */
public class CreatureGUI extends JFrame implements ActionListener
{
	/**
	 * Instance variables that contain all of the necessary GUI features and
	 * connection to the database.
	 */
	protected JButton[] southButtons;
	protected JButton selectButton;
	protected JLabel tableName, imageLabel;
	protected JTextField nameTF, idTF, locIDTF, maxHPTF, curHPTF, strengthTF, staminaTF, protTF;
	protected JComboBox<String> dropBox;
	protected JPanel northPanel, southPanel, eastPanel, westPanel;
	protected String[] creatureNames;
	protected String curPK;

	/**
	 * Constructor of PLAYER_CHAR table GUI.
	 * @param m_dbConn The database connection to query data.
	 */
	public CreatureGUI()
	{
		curPK = null;
		updateGUI(Runner.getDBConnection());
	}

	/**
	 * Updates the North, South, East, and West Panels of the GUI with the
	 * current data from the database.
	 * @param m_dbConn The connection to the database.
	 */
	public void updateGUI(Connection m_dbConn)
	{
		// Reset content pane before building it
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// Get primary keys and build the drop down box
		creatureNames = queryDatabaseForPrimaryKeys(m_dbConn);

		tableName = new JLabel("CREATURE", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(creatureNames);

		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		// Add the table name, drop down box, and select button
		// to the north panel
		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);

		add("North", northPanel);

		westPanel = new JPanel(new GridLayout(8, 1));
		eastPanel = new JPanel(new GridLayout(8, 1));

		if (curPK != null && !(curPK.equals("(new entry)")))
		{
			// The user wants to view a current row of data from the table
			// So add in blank text fields to both west and east panels
			ResultSet rs = queryDatabaseForDataRow(m_dbConn, curPK);
			
			westPanel.add(new JLabel("ID"));
			
			idTF = new JTextField(curPK);
			westPanel.add(idTF);
			
			westPanel.add(new JLabel("Creature Name"));
			try
			{
				nameTF = new JTextField(rs.getString("Cr_Name"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(nameTF);

			westPanel.add(new JLabel("Max HP"));
			try
			{
				maxHPTF = new JTextField(rs.getString("Cr_Max_HP"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(maxHPTF);	
			
			westPanel.add(new JLabel("Current HP"));
			try
			{
				curHPTF = new JTextField(rs.getString("Cr_Cur_HP"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(curHPTF);
		
			add("West", westPanel);
			
			eastPanel.add(new JLabel("Protection"));
			try
			{
				protTF = new JTextField(rs.getString("Cr_Protection"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(protTF);
			

			eastPanel.add(new JLabel("Strength"));
			try
			{
				strengthTF = new JTextField(rs.getString("Cr_Strength"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(strengthTF);

			eastPanel.add(new JLabel("Stamina"));
			try
			{
				staminaTF = new JTextField(rs.getString("Cr_Stamina"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(staminaTF);
			
			eastPanel.add(new JLabel("Location ID"));
			try
			{
				locIDTF = new JTextField(rs.getString("L_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(locIDTF);

			add("East", eastPanel);
		} else if (curPK != null && curPK.equals("(new entry)"))
		{
			// The user wants to add a new entry to the table
			nameTF = new JTextField();
			idTF = new JTextField();
			locIDTF = new JTextField();
			maxHPTF = new JTextField();
			curHPTF = new JTextField();
			strengthTF = new JTextField();
			staminaTF = new JTextField();
			protTF = new JTextField();
			
			westPanel.add(new JLabel("ID"));
			westPanel.add(idTF);
			westPanel.add(new JLabel("Creature Name"));
			westPanel.add(nameTF);
			westPanel.add(new JLabel("Max HP"));
			westPanel.add(maxHPTF);
			westPanel.add(new JLabel("Current HP"));
			westPanel.add(curHPTF);

			add("West", westPanel);

			eastPanel.add(new JLabel("Protection"));
			eastPanel.add(protTF);
			eastPanel.add(new JLabel("Strength"));
			eastPanel.add(strengthTF);
			eastPanel.add(new JLabel("Stamina"));
			eastPanel.add(staminaTF);
			eastPanel.add(new JLabel("Location ID"));
			eastPanel.add(locIDTF);

			add("East", eastPanel);

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

		if (curPK != null && curPK.equals("(new entry)"))
		{
			// Only the insert button can be clicked if the user
			// wants to add a new entry to the table
			southButtons[0].setEnabled(false);
			southButtons[1].setEnabled(false);
			southButtons[2].setEnabled(true);
		} else if (curPK != null)
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
	 * Retrieves a list of primary keys from the PLAYER_CHAR table.
	 * @param conn The connection to the database.
	 * @return The list of primary keys as a String array.
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection conn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT Cr_ID FROM CREATURE";
		String selectCount = "SELECT COUNT(*) FROM CREATURE";
		String[] data = null;

		try
		{
			// Retrieve the count of primary keys in the table
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(selectCount);
			int count = 1;
			while (rs.next())
			{
				count = rs.getInt(1);
			}

			data = new String[count + 1];
			data[0] = "(new entry)";

			// Retrieve the primary keys from the table
			// and store each one in an array of Strings
			rs = stmt.executeQuery(selectStmt);
			int i = 0;
			while (rs.next() && i < data.length)
			{
				data[i + 1] = rs.getString("Cr_ID");
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
		String selectStmt = "SELECT * FROM CREATURE WHERE Cr_ID=\"" + pKey + "\"";

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

	/**
	 * Method that is invoked when an action occurs.
	 * @param e The action that occurred. 
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// The select button was pressed
		if (e.getSource() == selectButton)
		{
			int index = dropBox.getSelectedIndex();
			curPK = creatureNames[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);

		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curPK = creatureNames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM CREATURE WHERE Cr_ID=?");
				stmt.setString(1, curPK);
				stmt.executeUpdate();
			} catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			curPK = null;
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);
		}

		// The update button was pressed, so update the values of each column
		if (e.getSource() == southButtons[1])
		{
			int index = dropBox.getSelectedIndex();
			curPK = creatureNames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement(
						"UPDATE CREATURE SET Cr_ID=?, Cr_Name=?, Cr_Max_HP=?, Cr_Cur_HP=?, Cr_Protection=?, Cr_Strength=?, Cr_Stamina=?, L_ID=? WHERE Cr_ID=?");
				stmt.setString(1, idTF.getText().trim());
				stmt.setString(2, nameTF.getText().trim());
				stmt.setInt(3, Integer.parseInt(maxHPTF.getText().trim()));
				stmt.setInt(4, Integer.parseInt(curHPTF.getText().trim()));
				stmt.setInt(5, Integer.parseInt(protTF.getText().trim()));
				stmt.setInt(6, Integer.parseInt(strengthTF.getText().trim()));
				stmt.setInt(7, Integer.parseInt(staminaTF.getText().trim()));
				stmt.setInt(8, Integer.parseInt(locIDTF.getText().trim()));
				stmt.setString(9, curPK);
				stmt.executeUpdate();
			} catch (SQLException e2)
			{
				e2.printStackTrace();
			}
			curPK = null;
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);
		}

		// The insert button was pressed, so add the data to the table
		if (e.getSource() == southButtons[2])
		{
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection()
						.prepareStatement("INSERT INTO CREATURE VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				stmt.setString(1, idTF.getText().trim());
				stmt.setString(2, nameTF.getText().trim());
				stmt.setInt(3, Integer.parseInt(maxHPTF.getText().trim()));
				stmt.setInt(4, Integer.parseInt(curHPTF.getText().trim()));
				stmt.setInt(5, Integer.parseInt(strengthTF.getText().trim()));
				stmt.setInt(6, Integer.parseInt(staminaTF.getText().trim()));
				stmt.setInt(7, Integer.parseInt(protTF.getText().trim()));
				stmt.setInt(8, Integer.parseInt(locIDTF.getText().trim()));
				stmt.executeUpdate();
			} catch (SQLException e3)
			{
				e3.printStackTrace();
			}
			curPK = null;

			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);

		}
	}

}
