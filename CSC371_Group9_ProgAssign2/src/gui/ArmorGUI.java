package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * GUI for the ARMOR table.
 * @author Nick Rummel
 * CSC371
 */
public class ArmorGUI extends JFrame implements ActionListener
{
	/**
	 * Instance variables that contain all of the necessary GUI features and
	 * connection to the database.
	 */
	protected JButton[] southButtons;
	protected JButton selectButton;
	protected JLabel tableName, instructionLabel;
	protected JTextField arIDTF, imageTF, locWornTF, cNameTF, contIDTF, locIDTF;
	protected JTextArea arDescTA;
	protected JComboBox<String> dropBox;
	protected JPanel northPanel, southPanel, eastPanel, westPanel;
	protected String[] armorID;
	protected String curPK;

	/**
	 * Constructor of ARMOR table GUI.
	 */
	public ArmorGUI()
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
		armorID = queryDatabaseForPrimaryKeys(m_dbConn);

		tableName = new JLabel("ARMOR", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(armorID);

		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		// Add the table name, drop down box, and select button
		// to the north panel
		JPanel northPanel = new JPanel(new GridLayout(9, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);

		northPanel.add(new JLabel("Choose which entry to view or edit. Then click \"Select\"."));
		northPanel.add(new JLabel("Once an entry is selected, change any details and click"));
		northPanel.add(new JLabel("\"Update\" to save the changes."));
		northPanel.add(new JLabel("To remove the the chosen entry, click \"Delete\"."));
		northPanel.add(new JLabel("By choosing (new entry), a new entry of data can be added"));
		northPanel.add(new JLabel("to the table. Fill out the form and click \"Insert\"."));
		
		add("North", northPanel);

		westPanel = new JPanel(new GridLayout(8, 1));
		eastPanel = new JPanel(new GridLayout(8, 1));

		if (curPK != null && !(curPK.equals("(new entry)")))
		{
			// The user wants to view a current row of data from the table
			// So add in blank text fields to both west and east panels
			ResultSet rs = queryDatabaseForDataRow(m_dbConn, curPK);
			westPanel.add(new JLabel("Armor ID"));
			try
			{
				arIDTF = new JTextField(rs.getString("Ar_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(arIDTF);

			westPanel.add(new JLabel("Image File Name"));
			try
			{
				imageTF = new JTextField(rs.getString("Image"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(imageTF);

			westPanel.add(new JLabel("Location ID"));
			try
			{
				locIDTF = new JTextField(rs.getString("L_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(locIDTF);

			westPanel.add(new JLabel("Location Worn"));
			try
			{
				locWornTF = new JTextField(rs.getString("Location_Worn"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			westPanel.add(locWornTF);

			add("West", westPanel);

			eastPanel.add(new JLabel("Character Name"));
			try
			{
				cNameTF = new JTextField(rs.getString("C_Name"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(cNameTF);

			eastPanel.add(new JLabel("Container ID"));
			try
			{
				contIDTF = new JTextField(rs.getString("Cont_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(contIDTF);

			eastPanel.add(new JLabel("Description"));
			try
			{
				arDescTA = new JTextArea(rs.getString("Ar_Description"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			eastPanel.add(arDescTA);

			add("East", eastPanel);
		} else if (curPK != null && curPK.equals("(new entry)"))
		{
			// The user wants to add a new entry to the table
			arIDTF = new JTextField();
			imageTF = new JTextField();
			locWornTF = new JTextField();
			cNameTF = new JTextField();
			contIDTF = new JTextField();
			locIDTF = new JTextField();
			arDescTA = new JTextArea();
			westPanel.add(new JLabel("Armor ID"));
			westPanel.add(arIDTF);
			westPanel.add(new JLabel("Image File Name"));
			westPanel.add(imageTF);
			westPanel.add(new JLabel("Location ID"));
			westPanel.add(locIDTF);
			westPanel.add(new JLabel("Location Worn"));
			westPanel.add(locWornTF);

			add("West", westPanel);

			eastPanel.add(new JLabel("Character Name"));
			eastPanel.add(cNameTF);
			eastPanel.add(new JLabel("Container ID"));
			eastPanel.add(contIDTF);
			eastPanel.add(new JLabel("Description"));
			eastPanel.add(arDescTA);

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
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * Retrieves a list of primary keys from the ARMOR table.
	 * @param conn The connection to the database.
	 * @return The list of primary keys as a String array.
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection conn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT Ar_ID FROM ARMOR";
		String selectCount = "SELECT COUNT(*) FROM ARMOR";
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
				data[i + 1] = rs.getString("Ar_ID");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Retrieves one row of data from the ARMOR table using the primary key.
	 * @param conn The connection to the database.
	 * @param pKey The value of the primary key that is used in the SELECT
	 *            statement.
	 * @return the Result Set that will contain that row of data.
	 */
	public ResultSet queryDatabaseForDataRow(Connection conn, String pKey)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM ARMOR WHERE Ar_ID=" + pKey;

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
			curPK = armorID[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);

		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curPK = armorID[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM ARMOR WHERE Ar_ID=?");
				stmt.setInt(1, Integer.parseInt(curPK));
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
			curPK = armorID[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement(
						"UPDATE ARMOR SET Ar_ID=?, Image=?, Ar_Description=?, Location_Worn=?, C_Name=?, Cont_ID=?, L_ID=? WHERE Ar_ID=?");
				stmt.setInt(1, Integer.parseInt(arIDTF.getText()));
				stmt.setString(2, imageTF.getText().trim());
				stmt.setString(3, arDescTA.getText().trim());
				stmt.setString(4, locWornTF.getText().trim());
				stmt.setString(5, cNameTF.getText().trim());
				stmt.setInt(6, Integer.parseInt(contIDTF.getText()));
				stmt.setInt(7, Integer.parseInt(locIDTF.getText().trim()));
				stmt.setInt(8, Integer.parseInt(curPK));
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
				stmt = Runner.getDBConnection().prepareStatement("INSERT INTO ARMOR VALUES (?, ?, ?, ?, ?, ?, ?)");
				stmt.setInt(1, Integer.parseInt(arIDTF.getText()));
				stmt.setString(2, imageTF.getText().trim());
				stmt.setString(3, arDescTA.getText().trim());
				stmt.setString(4, locWornTF.getText().trim());
				stmt.setString(5, cNameTF.getText().trim());
				stmt.setInt(6, Integer.parseInt(contIDTF.getText()));
				stmt.setInt(7, Integer.parseInt(locIDTF.getText().trim()));
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

	/**
	 * Allow the GUI to be toggled on and off
	 */
	public void toggle()
	{
		setVisible(!this.isShowing());
	}

}
