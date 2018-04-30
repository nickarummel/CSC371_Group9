package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The main GUI that allows each table's GUI to be opened.
 * @author Nick Rummel CSC371
 */
public class MainMenuGUI extends JFrame implements ActionListener
{
	/**
	 * Instance variables that contain all of the necessary GUI features and
	 * connection to the database.
	 */
	protected JButton selectButton;
	protected JLabel tableName, instructionLabel;
	protected JComboBox<String> dropBox;
	protected JPanel northPanel;
	protected List<? extends JFrame> guis;
	protected final String[] guiNames =
	{ "ABILITY", "AREA", "ARMOR", "CHARACTER", "CONTAINER", "CON_CONTAIN", "CREATURE", "CR_ITEMS", "ITEMS", "LEADS_TO",
			"LOCATION", "MANAGER", "MANAGER PERMISSIONS", "MODERATOR", "MODERATOR PERMISSIONS", "PLAYER",
			"RELATE WITH CHARACTER", "RELATE WITH CREATURE", "SKILLED WITH", "WEAPON" };
	protected AbilityGUI abGUI;
	protected ArmorGUI arGUI;
	protected CharacterGUI chGUI;
	protected ConContainsGUI concGUI;
	protected ContainerGUI contGUI;
	protected CreatureGUI crGUI;
	protected LeadsToGUI ltGUI;
	protected LocationGUI locGUI;
	protected ManagerGUI manGUI;
	protected ManagerPermsGUI manpGUI;
	protected ModeratorGUI modGUI;
	protected ModeratorPermsGUI modpGUI;
	protected PlayerGUI pGUI;
	protected RelatesToGUI rtGUI;
	protected WeaponGUI wGUI;

	/**
	 * Constructor of Main Menu GUI.
	 */
	public MainMenuGUI()
	{
		initializeGUIS();
		updateGUI();
	}

	/**
	 * Updates the North, South, and Center Panels of the GUI with the current
	 * data from the database.
	 * @param m_dbConn The connection to the database.
	 */
	public void updateGUI()
	{
		// Reset content pane before building it
		getContentPane().removeAll();
		setLayout(new BorderLayout());

		// Get primary keys and build the drop down box

		tableName = new JLabel("MAIN MENU", SwingConstants.CENTER);

		dropBox = new JComboBox<String>(guiNames);

		selectButton = new JButton("Select");
		selectButton.addActionListener(this);

		instructionLabel = new JLabel("Choose a table from the drop-down menu and click \"Select\".",
				SwingConstants.CENTER);

		// Add the table name, drop down box, and select button
		// to the north panel
		JPanel northPanel = new JPanel(new GridLayout(4, 1));
		northPanel.add(tableName);
		northPanel.add(dropBox);
		northPanel.add(selectButton);
		northPanel.add(instructionLabel);

		add("North", northPanel);

		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * Retrieves a list of primary keys from the MANAGER table.
	 * @param conn The connection to the database.
	 */
	public void initializeGUIS()
	{
		abGUI = new AbilityGUI();
		abGUI.toggle();
		arGUI = new ArmorGUI();
		arGUI.toggle();
		chGUI = new CharacterGUI();
		chGUI.toggle();
		concGUI = new ConContainsGUI();
		concGUI.toggle();
		contGUI = new ContainerGUI();
		contGUI.toggle();
		crGUI = new CreatureGUI();
		crGUI.toggle();
		ltGUI = new LeadsToGUI();
		ltGUI.toggle();
		locGUI = new LocationGUI();
		locGUI.toggle();
		manGUI = new ManagerGUI();
		manGUI.toggle();
		manpGUI = new ManagerPermsGUI();
		manpGUI.toggle();
		modGUI = new ModeratorGUI();
		modGUI.toggle();
		modpGUI = new ModeratorPermsGUI();
		modpGUI.toggle();
		pGUI = new PlayerGUI();
		pGUI.toggle();
		rtGUI = new RelatesToGUI();
		rtGUI.toggle();
		wGUI = new WeaponGUI();
		wGUI.toggle();
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
			switch (index)
			{
			case 0:
				// Ability GUI
				abGUI.toggle();
				break;
			case 1:
				// add in AREA GUI
				break;
			case 2:
				// Armor GUI
				arGUI.toggle();
				break;
			case 3:
				// Character GUI
				chGUI.toggle();
				break;
			case 4:
				// Container GUI
				contGUI.toggle();
				break;
			case 5:
				// Con_Contains GUI
				concGUI.toggle();
				break;
			case 6:
				// Creature GUI
				crGUI.toggle();
				break;
			case 7:
				// Add in CR_ITEMS GUI
				break;
			case 8:
				// Add in ITEMS GUI
				break;
			case 9:
				// Leads To GUI
				ltGUI.toggle();
				break;
			case 10:
				// Location GUI
				locGUI.toggle();
				break;
			case 11:
				// Manager GUI
				manGUI.toggle();
				break;
			case 12:
				// Manager Permissions GUI
				manpGUI.toggle();
				break;
			case 13:
				// Moderator GUI
				modGUI.toggle();
				break;
			case 14:
				// Moderator Permissions GUI
				modpGUI.toggle();
				break;
			case 15:
				// Player GUI
				pGUI.toggle();
				break;
			case 16:
				// Relate With Character GUI
				rtGUI.toggle();
				break;
			case 17:
				// Add Relate With Creature GUI
				break;
			case 18:
				// Add Skilled With GUI
				break;
			case 19:
				// Weapon GUI
				wGUI.toggle();
				break;
			default:
				break;
			}
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
