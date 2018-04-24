package part2;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Part 2: Build the statements to insert the data into the tables created in
 * @author Austin Smale
 */

public class SQLInserts
{
	/**
	 * Create all the rows of data needed for part 2
	 * @param m_dbConn the connection to the database
	 */
	public void createRows(Connection m_dbConn)
	{
		try
		{
			PreparedStatement stmt;
			//PLAYER insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO PLAYER VALUES (?, ?, ?)");
			stmt.setString(1, "RummelSmale");
			stmt.setString(2, "rummelsmale@ship.edu");
			stmt.setString(3, "ILovEThIsClass321");
			stmt.executeUpdate();

			//MODERATOR insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO MODERATOR VALUES (?, ?, ?)");
			stmt.setString(1, "Girard");
			stmt.setString(2, "cdgira@cs.ship.edu");
			stmt.setString(3, "PopQuiz1234");
			stmt.executeUpdate();

			//MANAGER insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO MANAGER VALUES (?, ?, ?)");
			stmt.setString(1, "Wellington");
			stmt.setString(2, "merlin@cs.ship.edu");
			stmt.setString(3, "DepartmentCHair!");
			stmt.executeUpdate();

			//MAN_PERMS insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO MAN_PERMS VALUES (?, ?)");
			stmt.setString(1, "Wellington");
			stmt.setString(2, "Delete Moderator");
			stmt.executeUpdate();

			//MOD_PERMS insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO MOD_PERMS VALUES (?, ?)");
			stmt.setString(1, "Girard");
			stmt.setString(2, "Unban Player");
			stmt.executeUpdate();

			//LOCATION insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO LOCATION VALUES (?, ?, ?), (?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, "Jungle");
			stmt.setInt(3, 7);
			//second location
			stmt.setInt(4, 1);
			stmt.setString(5, "City");
			stmt.setInt(6, 4);
			stmt.executeUpdate();

			//LEADS_TO insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO LEADS_TO VALUES (?, ?)");
			stmt.setInt(1, 0);
			stmt.setInt(2, 1);
			stmt.executeUpdate();

			//PLAYER_CHAR insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO PLAYER_CHAR VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, "Avatar");
			stmt.setInt(2, 100);
			stmt.setInt(3, 1);
			stmt.setInt(4, 5);
			stmt.setInt(5, 10);
			stmt.setString(6, "RummelSmale");
			stmt.setInt(7, 1);
			stmt.executeUpdate();

			//CONTAINER insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO CONTAINER VALUES (?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setInt(2, 600);
			stmt.setInt(3, 40);
			stmt.setString(4, "Avatar");
			stmt.setInt(5, 1);
			stmt.executeUpdate();
			//second container
			stmt.setInt(1, 1);
			stmt.setInt(2, 1000);
			stmt.setInt(3, 60);
			stmt.setString(4, "Avatar");
			stmt.setInt(5, 1);
			stmt.executeUpdate();

			//CREATURE insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO CREATURE VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, "Bigfoot");
			stmt.setInt(3, 200);
			stmt.setInt(4, 150);
			stmt.setInt(5, 50);
			stmt.setInt(6, 5);
			stmt.setInt(7, 2);
			stmt.setInt(8, 0);
			stmt.executeUpdate();
			// second creature
			stmt.setInt(1, 1);
			stmt.setString(2, "King Kong");
			stmt.setInt(3, 300);
			stmt.setInt(4, 200);
			stmt.setInt(5, 75);
			stmt.setInt(6, 25);
			stmt.setInt(7, 4);
			stmt.setInt(8, 1);
			stmt.executeUpdate();

			//ARMOR insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO ARMOR VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, "steelChest.png");
			stmt.setString(3, "Sturdy Steel Chestplate");
			stmt.setString(4, "Chest");
			stmt.setString(5, "Avatar");
			stmt.setInt(6, 1);
			stmt.setInt(7, 0);
			stmt.executeUpdate();

			//RELATE_W_CH insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO RELATE_W_CH VALUES (?, ?, ?, ?)");
			stmt.setString(1, "Avatar");
			stmt.setInt(2, 0);
			stmt.setInt(3, 1);
			stmt.setInt(4, 0);
			stmt.executeUpdate();

			//AREA insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO AREA VALUES (?, ?)");
			stmt.setInt(1, 1);
			stmt.setInt(2, 1);
			stmt.executeUpdate();

			//ITEM insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO ITEM VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, "Slay King Kong");
			stmt.setInt(3, 40);
			stmt.setInt(4, 10);
			stmt.setString(5, "kingKongHeart.png");
			stmt.setString(6, "King Kong's Heart");
			stmt.setString(7, "Avatar");
			stmt.setInt(8, 1);
			stmt.executeUpdate();

			//ABILITY insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO ABILITY VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setInt(2, 2);
			stmt.setInt(3, 50);
			stmt.setInt(4, 1);
			stmt.setInt(5, 500);
			stmt.setInt(6, 1);
			stmt.setString(7, "HP");
			stmt.executeUpdate();

			//SKILLED_W insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO SKILLED_W VALUES (?, ?)");
			stmt.setInt(1, 0);
			stmt.setInt(2, 0);
			stmt.executeUpdate();

			//RELATE_W_CR insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO RELATE_W_CR VALUES (?, ?, ?, ?)");
			stmt.setInt(1, 1);
			stmt.setInt(2, 0);
			stmt.setInt(3, 0);
			stmt.setInt(4, 1);
			stmt.executeUpdate();

			//CR_ITEMS insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO CR_ITEMS VALUES (?, ?)");
			stmt.setInt(1, 0);
			stmt.setInt(2, 0);
			stmt.execute();

			//CON_CONTAIN insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO CON_CONTAIN VALUES (?, ?)");
			stmt.setInt(1, 1);
			stmt.setInt(2, 0);
			stmt.executeUpdate();

			//WEAPON insertion
			stmt = m_dbConn.prepareStatement("INSERT INTO WEAPON VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, 0);
			stmt.setString(2, "sword.png");
			stmt.setString(3, "A big sword");
			stmt.setString(4, "Left Hand");
			stmt.setString(5, "Avatar");
			stmt.setInt(6, 0);
			stmt.setInt(7, 0);
			stmt.setInt(8, 1);
			stmt.executeUpdate();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
