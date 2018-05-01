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
 * The GUI class for Location connections
 * @author Aaron Gerber
 */
public class LeadsToGUI  extends JFrame implements ActionListener
{
	//The string fields needed.
	String [] curLocations;
	String [] nextLocations;
	String curPK;
	String nextLoc;
	
	//The GUI objects needed.
	protected JButton[] southButtons;
	protected JTextField curLocTF, nextLocTF;
	protected JLabel tableName, imageLabel;
	protected JComboBox<String> dropBox, dropBox_next;
	protected JPanel northPanel,westPanel, southPanel, eastPanel;
	protected JButton selectButton;
	
	
	public LeadsToGUI()
	{
		curPK = null;
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
		curLocations = queryDatabaseForPrimaryKeys(m_dbConn);
		
		tableName = new JLabel("LEADS TO", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(curLocations);
		
		
		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		JPanel titlePanel = new JPanel(new GridLayout(2,1));
		titlePanel.add(tableName);
		titlePanel.add(selectButton);
		
		//Instructions on how to use the GUI
		northPanel.add(new JLabel("Use the drop down menu to select an entry."));
		northPanel.add(new JLabel("Once an entry is selected, press the Select button"));
		northPanel.add(new JLabel("to pull the information related to that entry."));
		northPanel.add(new JLabel("You may then select the related data to this entry from"));
		northPanel.add(new JLabel("the second drop down menu. Similarly to selecting the"));
		northPanel.add(new JLabel("first entry, press the Select button to pull the complete"));
		northPanel.add(new JLabel("information from this entry. At this point you may then"));
		northPanel.add(new JLabel("delete or update the info displayed. If either drop down"));
		northPanel.add(new JLabel("has (new entry) displayed, you may insert the input"));
		northPanel.add(new JLabel("data into the database as a new entry."));
		
		add("North",titlePanel);
		
		
		
		dropBox_next = new JComboBox<String>(nextLocations);
		
		westPanel = new JPanel(new GridLayout( 2,2));
		westPanel.add(new JLabel("Current Location"));
		westPanel.add(dropBox);
		westPanel.add(new JLabel("Next Location"));
		westPanel.add(dropBox_next);
		
		add("West",westPanel);

		eastPanel = new JPanel(new GridLayout(2,2));
		if(curPK!=null)
		{
			
			eastPanel.add(new JLabel("Current Location"));
			
			if(!(curPK.equals("(new entry)")))
				curLocTF = new JTextField(curPK);
			else
				curLocTF = new JTextField("");
			
			eastPanel.add(curLocTF);
			
			eastPanel.add(new JLabel("Next Location"));
			
			if(!(nextLoc.equals("(new entry)")))
				nextLocTF = new JTextField(nextLoc);
			else
				nextLocTF = new JTextField("");
		
			eastPanel.add(nextLocTF);
			
		}
		add("East", eastPanel);
		
		
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
		} else if (curPK != null && nextLoc != null)
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
	 * Grabs the results of the query being made, with reference being what column in the database is being accessed.
	 * @param m_dbconn
	 * @param col
	 * @return results of the query
	 */
	private ResultSet queryDatabaseForDataRow(Connection m_dbconn, String col) 
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM LEADS_TO WHERE Cur_Loc_ID=\"" + col + "\"";

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
		String selectStmt = "SELECT Cur_Loc_ID FROM LEADS_TO";
		String selectCount = "SELECT COUNT(*) FROM LEADS_TO";
		String[] entry = null;
		
		if(curPK!=null&&!(curPK.equals("(new entry)")))
		{
			String selectStmt1 = "SELECT Next_Loc_ID FROM LEADS_TO Cur_Loc_ID =\"" +curPK+ "\"";
			String selectCount1 = "SELECT COUNT(*) FROM LEADS_TO WHERE Cur_Loc_ID = \""+curPK+"\"";
			nextLoc = null;
			
			
			try
			{
				// Retrieve the count of primary keys in the table
				Statement stmt = m_dbconn.createStatement();
				rs = stmt.executeQuery(selectCount1);
				int count = 1;
				while (rs.next())
				{
					count = rs.getInt(1);
				}

				//Dynamically create the array so as to not worry about the number of items
				nextLocations = new String[count + 1];
				nextLocations[0] = "(new entry)";

				// Retrieve the primary keys from the table
				// and store each one in an array of Strings
				rs = stmt.executeQuery(selectStmt1);
				int i = 0;
				while (rs.next() && i < nextLocations.length)
				{
					nextLocations[i + 1] = rs.getString("Next_Loc_ID");
					i++;
				}
				
				// Retrieve the count of primary keys in the table
				rs = stmt.executeQuery(selectCount);
				count = 1;
				while (rs.next())
				{
					count = rs.getInt(1);
				}

				//Dynamically create the array so as to not worry about the number of items
				entry = new String[count + 2];
				entry[0] = curPK;
				entry[1] = "(new entry)";

				// Retrieve the primary keys from the table
				// and store each one in an array of Strings
				rs = stmt.executeQuery(selectStmt);
				i = 0;
				while (rs.next() && i < entry.length)
				{
					entry[i + 2] = rs.getString("Cur_Loc_ID");
					i++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			nextLocations = new String [1];
			nextLocations[0] = "(new entry)";
			
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
				entry = new String[count + 1];
				entry[0] = "(new entry)";

				// Retrieve the primary keys from the table
				// and store each one in an array of Strings
				rs = stmt.executeQuery(selectStmt);
				int i = 0;
				while (rs.next() && i < entry.length)
				{
					entry[i + 1] = rs.getString("Cur_Loc_ID");
					i++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		return entry;
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
			curPK = curLocations[index];
			index = dropBox_next.getSelectedIndex();
			nextLoc = nextLocations[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);

		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curPK = curLocations[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM LEADS_TO WHERE Cur_Loc_ID=?");
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
			curPK = curLocations[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement(
						"UPDATE LEADS_TO SET Cur_Loc_ID=?, Next_Loc_ID=? WHERE Cur_Loc_ID=?");
				stmt.setString(1, curLocTF.getText().trim());
				stmt.setString(2, nextLocTF.getText().trim());
				stmt.setString(3, curPK);
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
				stmt = Runner.getDBConnection().prepareStatement("INSERT INTO LEADS_TO VALUES (?, ?)");
				stmt.setString(1, curLocTF.getText().trim());
				stmt.setString(2, nextLocTF.getText().trim());
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
