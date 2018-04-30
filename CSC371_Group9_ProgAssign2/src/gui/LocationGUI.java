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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * GUI for the LOCATION table.
 * @author Nick Rummel
 * CSC371
 */
public class LocationGUI extends JFrame implements ActionListener
{
	/**
	 * Instance variables that contain all of the necessary GUI features and
	 * connection to the database.
	 */
	protected JButton[] southButtons;
	protected JButton selectButton;
	protected JLabel tableName, imageLabel;
	protected JTextField locIDTF, areaTypeTF, sizeTF;
	protected JComboBox<String> dropBox;
	protected JPanel northPanel, southPanel, centerPanel;
	protected String[] locationUsernames;
	protected String curPK;

	/**
	 * Constructor of LOCATION table GUI.
	 */
	public LocationGUI()
	{
		curPK = null;
		updateGUI(Runner.getDBConnection());
	}

	/**
	 * Updates the North, South, and Center Panels of the GUI with the current
	 * data from the database.
	 * @param m_dbConn The connection to the database.
	 */
	public void updateGUI(Connection m_dbConn)
	{
		// Reset content pane before building it
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// Get primary keys and build the drop down box
		locationUsernames = queryDatabaseForPrimaryKeys(m_dbConn);

		tableName = new JLabel("LOCATION", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(locationUsernames);

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

		centerPanel = new JPanel(new GridLayout(6, 1));

		if (curPK != null && !(curPK.equals("(new entry)")))
		{
			// The user wants to view a current row of data from the table
			// So add in blank text fields to both west and east panels
			ResultSet rs = queryDatabaseForDataRow(m_dbConn, curPK);
			centerPanel.add(new JLabel("Location ID"));
			try
			{
				locIDTF = new JTextField(rs.getString("Loc_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			centerPanel.add(locIDTF);

			centerPanel.add(new JLabel("Area Type"));
			try
			{
				areaTypeTF = new JTextField(rs.getString("Area_Type"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			centerPanel.add(areaTypeTF);

			centerPanel.add(new JLabel("Size"));
			try
			{
				sizeTF = new JTextField(rs.getString("Size"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			centerPanel.add(sizeTF);

			add("Center", centerPanel);

		} else if (curPK != null && curPK.equals("(new entry)"))
		{
			// The user wants to add a new entry to the table
			locIDTF = new JTextField();
			areaTypeTF = new JTextField();
			sizeTF = new JTextField();
			centerPanel.add(new JLabel("Location ID"));
			centerPanel.add(locIDTF);
			centerPanel.add(new JLabel("Area Type"));
			centerPanel.add(areaTypeTF);
			centerPanel.add(new JLabel("Size"));
			centerPanel.add(sizeTF);
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
	 * Retrieves a list of primary keys from the LOCATION table.
	 * @param conn The connection to the database.
	 * @return The list of primary keys as a String array.
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection conn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT Loc_ID FROM LOCATION";
		String selectCount = "SELECT COUNT(*) FROM LOCATION";
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
				data[i + 1] = rs.getString("Loc_ID");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * Retrieves one row of data from the LOCATION table using the primary key.
	 * @param conn The connection to the database.
	 * @param pKey The value of the primary key that is used in the SELECT
	 *            statement.
	 * @return the Result Set that will contain that row of data.
	 */
	public ResultSet queryDatabaseForDataRow(Connection conn, String pKey)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM LOCATION WHERE Loc_ID=" + pKey;

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
			curPK = locationUsernames[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);

		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curPK = locationUsernames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM LOCATION WHERE Loc_ID=?");
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
			curPK = locationUsernames[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection()
						.prepareStatement("UPDATE LOCATION SET Loc_ID=?, Area_Type=?, Size=? WHERE Loc_ID=?");
				stmt.setInt(1, Integer.parseInt(locIDTF.getText().trim()));
				stmt.setString(2, areaTypeTF.getText().trim());
				stmt.setInt(3, Integer.parseInt(sizeTF.getText().trim()));
				stmt.setInt(4, Integer.parseInt(curPK));
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
				stmt = Runner.getDBConnection().prepareStatement("INSERT INTO LOCATION VALUES (?, ?, ?)");
				stmt.setInt(1, Integer.parseInt(locIDTF.getText().trim()));
				stmt.setString(2, areaTypeTF.getText().trim());
				stmt.setInt(3, Integer.parseInt(sizeTF.getText().trim()));
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
