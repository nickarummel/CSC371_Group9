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
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

/**
 * The GUI class for Location connections
 * @author Aaron Gerber
 */
public class RelatesToGUI  extends JFrame implements ActionListener
{
	//The string fields needed.
	String [] curCHID;
	String [] crIDs;
	String curPK, crID;
	Boolean hate,like;
	
	//The GUI objects needed.
	protected JButton[] southButtons;
	protected JTextField curCHTF, relatedCrTF;
	protected JLabel tableName, imageLabel;
	protected JComboBox<String> dropBox, dropBox_next;
	protected JPanel northPanel,westPanel, southPanel, eastPanel;
	protected JButton selectButton;
	protected JRadioButton crHates, crLikes;
	
	
	public RelatesToGUI()
	{
		curPK = null;
		updateGUI(Runner.getDBConnection());
	}

	/**
	 * Updates the GUI with the correct information from the entrybase when called.
	 * @param m_dbConn
	 */
	public void updateGUI(Connection m_dbConn)
	{
		// Reset content pane before building it
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// Get primary keys and build the drop down box
		curCHID = queryDatabaseForPrimaryKeys(m_dbConn);
		
		tableName = new JLabel("RELATES TO", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(curCHID);
		
		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		northPanel = new JPanel(new GridLayout(2,1));
		northPanel.add(tableName);
		northPanel.add(selectButton);
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
		
		add("North",northPanel);
				
		dropBox_next = new JComboBox<String>(crIDs);
		
		westPanel = new JPanel(new GridLayout(2,2));
		westPanel.add(new JLabel("Current Character Name"));
		westPanel.add(dropBox);
		westPanel.add(new JLabel("Related Creature ID"));
		westPanel.add(dropBox_next);
		
		add("West",westPanel);

		eastPanel = new JPanel(new GridLayout(2,4));
		if(curPK!=null)
		{
			hate = false;
			like = false;
			
			if(!(curPK.equals("(new entry)"))&&!(crID.equals("(new entry)")))
			{
				ResultSet rs = null;
				String selectStmt1 = "SELECT Ch_Hates FROM RELATE_W_CH WHERE Ch_Name =\"" +curPK+ "\" AND Cr_ID = \"" + crID+"\"";
				String selectStmt2 = "SELECT Ch_Likes FROM RELATE_W_CH WHERE Ch_Name =\"" +curPK+ "\" AND Cr_ID = \"" + crID+"\"";
				
				try
				{
					// Retrieve the count of primary keys in the table
					Statement stmt = m_dbConn.createStatement();
					
					rs = stmt.executeQuery(selectStmt1);
					rs.next();
					hate = rs.getBoolean("Ch_Hates");
					rs = stmt.executeQuery(selectStmt2);
					rs.next();
					like = rs.getBoolean("Ch_Likes");
				} catch (SQLException e)
				{
					e.printStackTrace();
				}

			}
			eastPanel.add(new JLabel("Current Creature ID"));
			
			if(!(curPK.equals("(new entry)")))
				curCHTF = new JTextField(curPK);
			else
				curCHTF = new JTextField("");
			
			eastPanel.add(curCHTF);
			
			eastPanel.add(new JLabel("Current hates Related"));
			
			crHates = new JRadioButton();
	
			crHates.setSelected(hate);
			
			eastPanel.add(crHates);
			
			eastPanel.add(new JLabel("Related Creature ID"));
			
			if(!(crID.equals("(new entry)")))
				relatedCrTF = new JTextField(crID);
			else
				relatedCrTF = new JTextField("");
		
			eastPanel.add(relatedCrTF);
			
			eastPanel.add(new JLabel("Current likes Related"));
			
			crLikes =  new JRadioButton();
			
			crLikes.setSelected(like);
			
			eastPanel.add(crLikes);
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
		
		if (curPK != null && crID != null && crID.equals("(new entry)"))
		{
			
			// Only the insert button can be clicked if the user
			// wants to add a new entry to the table
			southButtons[0].setEnabled(false);
			southButtons[1].setEnabled(false);
			southButtons[2].setEnabled(true);
		} else if (curPK != null && crID != null)
		{
			// Only the delete and update buttons can be clicked
			// if the user wants to view a row of entry from the table
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
	 * Grabs the results of the query being made, with reference being what column in the entrybase is being accessed.
	 * @param m_dbconn
	 * @param col
	 * @return results of the query
	 */
	private ResultSet queryDatabaseForDataRow(Connection m_dbconn, String col) 
	{
		ResultSet rs = null;
		String selectStmt = "SELECT * FROM RELATE_W_CH WHERE Ch_Name=\"" + col + "\"";

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
	 * Grabs the primary keys that will be used to find information in the entrybase
	 * @param m_dbconn
	 * @return set of strings for the drop down menu
	 */
	public String[] queryDatabaseForPrimaryKeys(Connection m_dbconn)
	{
		ResultSet rs = null;
		String selectStmt = "SELECT Ch_Name FROM RELATE_W_CH";
		String selectCount = "SELECT COUNT(*) FROM RELATE_W_CH";
		String[] entry = null;
		
		if(curPK!=null&&!(curPK.equals("(new entry)")))
		{
			String selectStmt1 = "SELECT Cr_ID FROM RELATE_W_CH WHERE Ch_Name =\"" +curPK+ "\"";
			String selectCount1 = "SELECT COUNT(*) FROM RELATE_W_CH WHERE Ch_Name = \""+curPK+"\"";
			crIDs = null;
			
			
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
				crIDs = new String[count + 1];
				crIDs[0] = "(new entry)";

				// Retrieve the primary keys from the table
				// and store each one in an array of Strings
				rs = stmt.executeQuery(selectStmt1);
				int i = 0;
				while (rs.next() && i < crIDs.length)
				{
					crIDs[i + 1] = rs.getString("Cr_ID");
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
					entry[i + 2] = rs.getString("Ch_Name");
					i++;
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			crIDs = new String [1];
			crIDs[0] = "(new entry)";
			
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
					entry[i + 1] = rs.getString("Ch_Name");
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
			curPK = curCHID[index];
			index = dropBox_next.getSelectedIndex();
			crID = crIDs[index];
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(index);
		}

		// The delete button was pressed, so delete the current entry
		if (e.getSource() == southButtons[0])
		{
			int index = dropBox.getSelectedIndex();
			curPK = curCHID[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("DELETE FROM RELATE_W_CH WHERE Ch_Name=?");
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
			curPK = curCHID[index];
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement(
						"UPDATE RELATE_W_CH SET Ch_Name=?, Cr_ID=?, Ch_Hates=?, Ch_Likes=? WHERE Ch_Name=?");
				System.out.println(curCHTF.getText().trim());
				stmt.setString(1, curCHTF.getText().trim());
				stmt.setString(2, relatedCrTF.getText().trim());
				stmt.setBoolean(3, crHates.isSelected());
				stmt.setBoolean(4, crLikes.isSelected());
				stmt.setString(5, curCHTF.getText().trim());
				stmt.executeUpdate();
			} catch (SQLException e2)
			{
				e2.printStackTrace();
			}
			curPK = null;
			updateGUI(Runner.getDBConnection());
			dropBox.setSelectedIndex(0);
		}

		// The insert button was pressed, so add the entry to the table
		if (e.getSource() == southButtons[2])
		{
			PreparedStatement stmt;
			try
			{
				stmt = Runner.getDBConnection().prepareStatement("INSERT INTO RELATE_W_CH VALUES (?,?,?,?)");
				stmt.setString(1, curCHTF.getText().trim());
				stmt.setString(2, relatedCrTF.getText().trim());
				stmt.setBoolean(3, crHates.isSelected());
				stmt.setBoolean(4, crLikes.isSelected());
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
