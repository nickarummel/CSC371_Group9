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

public class ConContainsGUI extends JFrame implements ActionListener
{
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * GUI for the Container table
	 * @author Austin Smale
	 */

	private JComboBox<String> dropDownMenu;
	private int curIndex;
	private String[] contKeys;
	private Connection m_dbConn;
	private JButton selectBtn, insertBtn, deleteBtn, updateBtn;
	private JTextField containerTF, containeeTF;
	private JPanel top, middle, bottom;

	public ConContainsGUI()
	{
		// set the current index to 0
		curIndex = 0;

		// display
		display();
	}

	/**
	 * Display the columns of the table and all the other information that goes
	 * along with it
	 */
	public void display()
	{
		// remove content
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// create panels
		top = new JPanel(new GridLayout(9, 1));
		middle = new JPanel(new GridLayout(3, 2));
		bottom = new JPanel(new GridLayout(1, 3));
		// create buttons
		selectBtn = new JButton("Select");
		insertBtn = new JButton("Insert");
		deleteBtn = new JButton("Delete");
		updateBtn = new JButton("Update");
		// create connection
		m_dbConn = Runner.getDBConnection();

		// pull all primary keys and reconnect
		initKeys();
		// set top panel
		// table name label
		JLabel tableName = new JLabel("CON_CONTAIN", SwingConstants.CENTER);
		// set drop down menu
		dropDownMenu = new JComboBox<String>(contKeys);
		dropDownMenu.setSelectedIndex(curIndex);
		// select button
		selectBtn.addActionListener(this);
		// add 3 to top panel
		top.add(tableName);
		top.add(dropDownMenu);
		top.add(selectBtn);
		top.add(new JLabel("Use the menu to select an entry."));
		top.add(new JLabel("Clicking Select will populate the fields below."));
		top.add(new JLabel("You can either update the current entry and click"));
		top.add(new JLabel("update to save, or just simply delete the entry."));
		top.add(new JLabel("If new item, it will leave the fields empty."));
		top.add(new JLabel("Click insert will add the new item to the database."));
		add("North", top);
		// add(top);

		// set middle panel
		// if new entry row is selected
		if (dropDownMenu.getSelectedIndex() == 0)
		{
			// init text fields
			containerTF = new JTextField();
			containeeTF = new JTextField();
		}
		// if a row from the table was selected
		else
		{
			ResultSet row = queryRowFromDatabase();
			// init text fields
			try
			{
				containerTF = new JTextField(row.getString("Container_ID"));
				containeeTF = new JTextField(row.getString("Containee_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		middle.add(new JLabel("Container ID:"));
		middle.add(containerTF);
		middle.add(new JLabel("Containee ID:"));
		middle.add(containeeTF);
		add("West", middle);

		// set bottom panel
		// only allow insert to be enabled if new data is selected
		if (dropDownMenu.getSelectedIndex() == 0)
		{
			insertBtn.setEnabled(true);
			deleteBtn.setEnabled(false);
			updateBtn.setEnabled(false);
			// add an action listner to each button
			insertBtn.addActionListener(this);
			deleteBtn.addActionListener(this);
			updateBtn.addActionListener(this);
		}
		// if a row is selected only enable delete and update buttons
		else
		{
			insertBtn.setEnabled(false);
			deleteBtn.setEnabled(true);
			updateBtn.setEnabled(true);
			// add an action listner to each button
			insertBtn.addActionListener(this);
			deleteBtn.addActionListener(this);
			updateBtn.addActionListener(this);
		}
		bottom.add(deleteBtn);
		bottom.add(updateBtn);
		bottom.add(insertBtn);
		add("South", bottom);

		// pack everything and set visible
		pack();
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * Query a row from the CONTAINER table matching the current drop down menu
	 * item
	 * @return The row of the data the user selected
	 */
	private ResultSet queryRowFromDatabase()
	{
		ResultSet result = null;
		String selectString = "SELECT * FROM CON_CONTAIN WHERE Container_ID=?";
		try
		{
			PreparedStatement stmt = m_dbConn.prepareStatement(selectString);
			stmt.setString(1, dropDownMenu.getItemAt(curIndex));
			result = stmt.executeQuery();
			result.next();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Initialize the drop down menu whenever it is updated/called. Finds the
	 * total number of keys, then selects all of the ID's. It then stores it
	 * into an array and sets the contKeys array to all of the keys currently
	 * inside the database
	 * @author Nick Rummel
	 * @modified Austin Smale
	 */
	private void initKeys()
	{
		ResultSet result = null;
		String selectStmt = "SELECT Container_ID FROM CON_CONTAIN";
		String selectCount = "SELECT COUNT(*) FROM CON_CONTAIN";
		String[] data = null;

		try
		{
			// Retrieve the count of primary keys in the table
			Statement stmt = m_dbConn.createStatement();
			result = stmt.executeQuery(selectCount);
			int count = 1;
			while (result.next())
			{
				count = result.getInt(1);
			}

			data = new String[count + 1];
			data[0] = "(new entry)";

			// Retrieve the primary keys from the table
			// and store each one in an array of Strings
			result = stmt.executeQuery(selectStmt);
			int i = 0;
			while (result.next() && i < data.length)
			{
				data[i + 1] = result.getString("Container_ID");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		contKeys = data;
	}

	/**
	 * Override method for when a button is clicked
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// if select was pressed
		if (e.getSource() == selectBtn)
		{
			curIndex = dropDownMenu.getSelectedIndex();
		}
		// if insert was pressed
		else if (e.getSource() == insertBtn)
		{
			String insert = "INSERT INTO CON_CONTAIN VALUES(?,?)";
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(insert);
				stmt.setInt(1, Integer.parseInt(containerTF.getText().trim()));
				stmt.setInt(2, Integer.parseInt(containeeTF.getText().trim()));
				stmt.executeUpdate();
			} catch (SQLException error)
			{
				error.printStackTrace();
			}
		}
		// if delete was pressed
		else if (e.getSource() == deleteBtn)
		{
			String delete = "DELETE FROM CON_CONTAIN WHERE Container_ID=?";
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(delete);
				stmt.setInt(1, Integer.parseInt(containerTF.getText().trim()));
				stmt.executeUpdate();
			} catch (SQLException error)
			{
				error.printStackTrace();
			}
			curIndex = 0;
		}
		// if update was pressed
		else if (e.getSource() == updateBtn)
		{
			String update = "UPDATE CON_CONTAIN SET Containee_ID=? WHERE Container_ID=?";
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(update);
				stmt.setInt(1, Integer.parseInt(containeeTF.getText().trim()));
				stmt.setInt(2, Integer.parseInt(containerTF.getText().trim()));
				stmt.executeUpdate();
			} catch (SQLException error)
			{
				error.printStackTrace();
			}
		}

		// recall display
		display();
	}

	/**
	 * Allow the GUI to be toggled on and off
	 */
	public void toggle()
	{
		setVisible(!this.isShowing());
	}
}
