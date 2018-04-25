package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;

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
	JButton[] southButtons;
	JButton selectButton;
	JLabel tableName, imageLabel;
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

		String[] characterNames =
		{ "Avatar", "Gandalf", "Merlin" };
		dropBox = new JComboBox<String>(characterNames);

		selectButton = new JButton("Select");

		JPanel northPanel = new JPanel(new GridLayout(3, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);

		add("North", northPanel);

		JPanel westPanel = new JPanel(new GridLayout(10, 1));

		westPanel.add(new JLabel("Name"));
		westPanel.add(new JTextField("Text"));
		westPanel.add(new JLabel("Player Username"));
		westPanel.add(new JTextField("Text"));
		westPanel.add(new JLabel("Location ID"));
		westPanel.add(new JTextField("Text"));
		westPanel.add(new JLabel("Bag"));
		westPanel.add(new JTextField("Text"));
		westPanel.add(new JLabel("Max HP"));
		westPanel.add(new JTextField("Text"));

		add("West", westPanel);

		JPanel eastPanel = new JPanel(new GridLayout(6, 1));
		eastPanel.add(new JLabel("Current HP"));
		eastPanel.add(new JTextField("Text"));
		eastPanel.add(new JLabel("Strength"));
		eastPanel.add(new JTextField("Text"));
		eastPanel.add(new JLabel("Stamina"));
		eastPanel.add(new JTextField("Text"));

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

}
