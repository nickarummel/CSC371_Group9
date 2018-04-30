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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class WeaponGUI extends JFrame implements ActionListener
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
	private String[] weapKeys;
	private Connection m_dbConn;
	private JButton selectBtn, insertBtn, deleteBtn, updateBtn;
	private JTextField idTF, imageTF, wornTF, nameTF, contTF, abilTF, locTF;
	private JTextArea descTA;
	private JPanel top, middle, bottom;

	public WeaponGUI()
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
		top = new JPanel(new GridLayout(3, 1));
		middle = new JPanel(new GridLayout(8, 2));
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
		JLabel tableName = new JLabel("WEAPON", SwingConstants.CENTER);
		// set drop down menu
		dropDownMenu = new JComboBox<String>(weapKeys);
		dropDownMenu.setSelectedIndex(curIndex);
		// select button
		selectBtn.addActionListener(this);
		// add 3 to top panel
		top.add(tableName);
		top.add(dropDownMenu);
		top.add(selectBtn);
		add("North", top);
		// add(top);

		// set middle panel
		// if new entry row is selected
		if (dropDownMenu.getSelectedIndex() == 0)
		{
			// init text fields
			idTF = new JTextField();
			imageTF = new JTextField();
			descTA = new JTextArea("", 2, 1);
			nameTF = new JTextField();
			wornTF = new JTextField();
			nameTF = new JTextField();
			contTF = new JTextField();
			abilTF = new JTextField();
			locTF = new JTextField();
		}
		// if a row from the table was selected
		else
		{
			ResultSet row = queryRowFromDatabase();
			// init text fields
			try
			{
				idTF = new JTextField(row.getString("W_ID"));
				imageTF = new JTextField(row.getString("W_Image"));
				descTA = new JTextArea(row.getString("W_Description"), 2, 1);
				wornTF = new JTextField(row.getString("W_Location"));
				nameTF = new JTextField(row.getString("C_Name"));
				contTF = new JTextField(row.getString("Cont_ID"));
				abilTF = new JTextField(row.getString("Ab_ID"));
				locTF = new JTextField(row.getString("L_ID"));
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		middle.add(new JLabel("Weapon ID:"));
		middle.add(idTF);
		middle.add(new JLabel("Image:"));
		middle.add(imageTF);
		middle.add(new JLabel("Description:"));
		middle.add(descTA);
		middle.add(new JLabel("Location Worn:"));
		middle.add(wornTF);
		middle.add(new JLabel("Character Name:"));
		middle.add(nameTF);
		middle.add(new JLabel("Container ID:"));
		middle.add(contTF);
		middle.add(new JLabel("Ability ID:"));
		middle.add(abilTF);
		middle.add(new JLabel("Location ID:"));
		middle.add(locTF);
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
		String selectString = "SELECT * FROM WEAPON WHERE W_ID=?";
		try
		{
			PreparedStatement stmt = m_dbConn.prepareStatement(selectString);
			stmt.setInt(1, Integer.parseInt(dropDownMenu.getItemAt(curIndex)));
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
	 * into an array and sets the weapKeys array to all of the keys currently
	 * inside the database
	 * @author Nick Rummel
	 * @modified Austin Smale
	 */
	private void initKeys()
	{
		ResultSet result = null;
		String selectStmt = "SELECT W_ID FROM WEAPON";
		String selectCount = "SELECT COUNT(*) FROM WEAPON";
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
				data[i + 1] = result.getString("W_ID");
				i++;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		weapKeys = data;
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
			String insert = "INSERT INTO WEAPON VALUES(?,?,?,?,?,?,?,?)";
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(insert);
				stmt.setInt(1, Integer.parseInt(idTF.getText().trim()));
				stmt.setString(2, imageTF.getText().trim());
				stmt.setString(3, descTA.getText().trim());
				stmt.setString(4, wornTF.getText().trim());
				stmt.setString(5, nameTF.getText().trim());
				stmt.setInt(6, Integer.parseInt(contTF.getText().trim()));
				stmt.setInt(7, Integer.parseInt(abilTF.getText().trim()));
				stmt.setInt(8, Integer.parseInt(locTF.getText().trim()));
				stmt.executeUpdate();
			} catch (SQLException error)
			{
				error.printStackTrace();
			}
		}
		// if delete was pressed
		else if (e.getSource() == deleteBtn)
		{
			String delete = "DELETE FROM WEAPON WHERE W_ID=?";
			int curID = Integer.parseInt(idTF.getText().trim());
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(delete);
				stmt.setInt(1, curID);
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
			String update = "UPDATE WEAPON SET W_Image=?, W_Description=?, W_Location=?, C_Name=?, Cont_ID=?, Ab_ID=?, L_ID=? WHERE W_ID=?";
			PreparedStatement stmt;
			try
			{
				m_dbConn = Runner.getDBConnection();
				stmt = m_dbConn.prepareStatement(update);
				stmt.setString(1, imageTF.getText().trim());
				stmt.setString(2, descTA.getText().trim());
				stmt.setString(3, wornTF.getText().trim());
				stmt.setString(4, nameTF.getText().trim());
				stmt.setInt(5, Integer.parseInt(contTF.getText().trim()));
				stmt.setInt(6, Integer.parseInt(abilTF.getText().trim()));
				stmt.setInt(7, Integer.parseInt(locTF.getText().trim()));
				stmt.setInt(8, Integer.parseInt(idTF.getText().trim()));
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
