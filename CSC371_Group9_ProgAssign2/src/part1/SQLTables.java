package part1;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Part 1: Build the SQL statements for creating tables.
 * @author Nick Rummel
 *
 */
public class SQLTables
{
	/**
	 * Array of strings that will store all of the CREATE TABLE SQL statements.
	 */
	protected String[] createStmts = new String[20];

	/**
	 * Constructor that creates the CREATE TABLE statements for MySQL.
	 */
	public SQLTables()
	{

		createStmts[0] = "CREATE TABLE PLAYER (P_Username VARCHAR(25) NOT NULL, P_Email VARCHAR(40) NOT NULL, P_Password VARCHAR(25) NOT NULL, PRIMARY KEY (P_Username)) ";

		createStmts[1] = "CREATE TABLE MODERATOR (Mo_Username VARCHAR(25) NOT NULL, Mo_Email VARCHAR(40) NOT NULL, Mo_Password VARCHAR(25) NOT NULL, PRIMARY KEY (Mo_Username)) ";

		createStmts[2] = "CREATE TABLE MANAGER (Ma_Username VARCHAR(25) NOT NULL, Ma_Email VARCHAR(40) NOT NULL, Ma_Password VARCHAR(25) NOT NULL, PRIMARY KEY (Ma_Username)) ";

		createStmts[3] = "CREATE TABLE MAN_PERMS (Man_Username VARCHAR(25) NOT NULL, Man_Permission VARCHAR(25) NOT NULL, PRIMARY KEY(Man_Username, Man_Permission), FOREIGN KEY(Man_Username) REFERENCES MANAGER(Ma_Username)) ";

		createStmts[4] = "CREATE TABLE MOD_PERMS (Mod_Username VARCHAR(25) NOT NULL, Mod_Permission VARCHAR(25) NOT NULL, PRIMARY KEY(Mod_Username, Mod_Permission), FOREIGN KEY(Mod_Username) REFERENCES MODERATOR(Mo_Username)) ";

		createStmts[5] = "CREATE TABLE LOCATION (Loc_ID INT  NOT NULL , Area_Type VARCHAR(30), Size  INT, PRIMARY KEY (Loc_ID), CHECK (Loc_ID > -1 AND Loc_ID < 1001), CHECK (Size > 0 AND Size < 101)) ";

		createStmts[6] = "CREATE TABLE LEADS_TO (Cur_Loc_ID INT  NOT NULL, Next_Loc_ID INT  NOT NULL, PRIMARY KEY (Cur_Loc_ID, Next_Loc_ID), FOREIGN KEY (Cur_Loc_ID) REFERENCES LOCATION(Loc_ID), FOREIGN KEY (Next_Loc_ID) REFERENCES LOCATION(Loc_ID), CHECK (Cur_Loc_ID > -1 AND Cur_Loc_ID < 1001), CHECK (Next_Loc_ID > -1 AND Next_Loc_ID < 1001)) ";

		createStmts[7] = "CREATE TABLE PLAYER_CHAR (P_Name VARCHAR(25) NOT NULL, P_Max_HP INT  NOT NULL, P_Cur_HP INT  NOT NULL, P_Strength INT  NOT NULL, P_Stamina INT  NOT NULL, P_Username VARCHAR(25) NOT NULL, L_ID  INT  NOT NULL, PRIMARY KEY (P_Name), FOREIGN KEY (P_Username) REFERENCES PLAYER(P_Username), FOREIGN KEY (L_ID) REFERENCES LOCATION(Loc_ID), CHECK (P_Max_HP > -1 AND P_Max_HP < 10001), CHECK (P_Cur_HP > -1 AND P_Cur_HP < 10001), CHECK (P_Strength > -1 AND P_Strength < 10001), CHECK (P_Stamina > -1 AND P_Stamina < 10001), CHECK (L_ID > -1 AND L_ID < 1001)) ";

		createStmts[8] = "CREATE TABLE CONTAINER  (Co_ID  INT  NOT NULL, Weight_Limit INT  NOT NULL, Total_Vol INT  NOT NULL, C_Name VARCHAR(25), L_ID  INT, PRIMARY KEY (Co_ID), FOREIGN KEY (C_Name) REFERENCES PLAYER_CHAR (P_Name), FOREIGN KEY (L_ID) REFERENCES LOCATION (Loc_ID), CHECK (Co_ID > -1 AND Co_ID < 10001), CHECK (Weight_Limit > -1 AND Weight_Limit < 1001), CHECK (Total_Vol > -1 AND Total_Vol < 1001)) ";

		createStmts[9] = "CREATE TABLE CREATURE (Cr_ID  INT  NOT NULL, Cr_Name VARCHAR(25) NOT NULL, Cr_Max_HP INT  NOT NULL, Cr_Cur_HP INT  NOT NULL, Cr_Strength INT  NOT NULL, Cr_Stamina INT  NOT NULL, Cr_Protection INT  NOT NULL, L_ID  INT  NOT NULL, PRIMARY KEY (Cr_ID), FOREIGN KEY (L_ID) REFERENCES LOCATION(Loc_ID), CHECK (Cr_ID > -1 AND Cr_ID < 10001), CHECK (Cr_Max_HP > -1 AND Cr_Max_HP < 10001), CHECK (Cr_Cur_HP > -1 AND Cr_Cur_HP < 10001), CHECK (Cr_Strength > -1 AND Cr_Strength < 10001), CHECK (Cr_Stamina > -1 AND Cr_Stamina < 10001), CHECK (Cr_Protection > -1 AND Cr_Protection < 10001), CHECK (L_ID > -1 AND L_ID < 1001)) ";

		createStmts[10] = "CREATE TABLE ARMOR (Ar_ID INT NOT NULL, Image VARCHAR(35), Ar_Description VARCHAR(50), Location_Worn VARCHAR(20), C_Name VARCHAR(25), Cont_ID INT, L_ID  INT,  PRIMARY KEY (Ar_ID), FOREIGN KEY (C_Name) REFERENCES PLAYER_CHAR(P_Name), FOREIGN KEY (Cont_ID) REFERENCES CONTAINER(Co_ID), FOREIGN KEY (L_ID) REFERENCES LOCATION(Loc_ID), CHECK (Ar_ID > -1 AND Ar_ID < 10001), CHECK (Ar_Protection > -1 AND Ar_Protection < 101), CHECK (Cont_Id > -1 AND Cont_ID < 10001), CHECK (L_ID > -1 AND L_ID < 1001)) ";

		createStmts[11] = "CREATE TABLE RELATE_W_CH (Ch_Name VARCHAR(25) NOT NULL, Cr_ID  INT  NOT NULL, Ch_Hates BOOLEAN, Ch_Likes BOOLEAN, PRIMARY KEY (Ch_Name, Cr_ID), FOREIGN KEY (Ch_Name) REFERENCES PLAYER_CHAR(P_Name), FOREIGN KEY (Cr_ID) REFERENCES CREATURE(CR_ID), CHECK (Cr_ID > -1 AND Cr_ID < 10001), CHECK (Ch_Hates >= 0 AND Ch_Hates <= 1), CHECK (Ch_Likes >= 0 AND Ch_Likes <= 1)) ";

		createStmts[12] = "CREATE TABLE AREA (C_ID  INT  NOT NULL, A_Area INT  NOT NULL, PRIMARY KEY (C_ID, A_Area), FOREIGN KEY (C_ID) REFERENCES CREATURE(Cr_ID), FOREIGN KEY (A_Area) REFERENCES LOCATION(Loc_ID), CHECK (C_ID > -1 AND C_ID < 10001), CHECK (A_Area > -1 AND A_Area < 1001)) ";

		createStmts[13] = "CREATE TABLE ITEM (I_ID  INT  NOT NULL, Quest  VARCHAR(50), Weight  INT, Volume INT, I_Image VARCHAR(35), I_Description VARCHAR(50), C_Name VARCHAR(25), Cont_ID INT, PRIMARY KEY (I_ID), FOREIGN KEY (C_Name) REFERENCES PLAYER_CHAR (P_Name), FOREIGN KEY (Cont_ID) REFERENCES CONTAINER (Co_ID), CHECK (I_ID > -1 AND I_ID < 10001), CHECK (Weight > -1 AND Weight < 1001), CHECK (Volume > -1 AND Volume < 1001), CHECK (Cont_ID > -1 AND Cont_ID < 10001))";

		createStmts[14] = "CREATE TABLE ABILITY (Ab_ID  INT  NOT NULL, Repeat_Time INT  NOT NULL, Rate  INT  NOT NULL, Exec_Time INT  NOT NULL, Amount INT  NOT NULL, Is_Attack BOOLEAN NOT NULL, Stat_Target VARCHAR(8) NOT NULL, PRIMARY KEY (Ab_ID), CHECK (Ab_ID >= 0 AND Ab_ID <= 1000), CHECK (Repeat_Time >= 0 AND Repeat_Time <= 10), CHECK (Rate >= 0 AND Rate <= 100), CHECK (Exec_Time >= 0 AND Exec_Time <= 50), CHECK (Amount >= 0 AND Amount <= 10000), CHECK (Is_Attack >=0 AND Is_Attack <= 1)) ";

		createStmts[15] = "CREATE TABLE SKILLED_W (Ab_ID  INT  NOT NULL, Cr_ID  INT  NOT NULL, PRIMARY KEY (Ab_ID, Cr_ID), FOREIGN KEY (Ab_ID) REFERENCES ABILITY(Ab_ID), FOREIGN KEY (Cr_ID) REFERENCES CREATURE (Cr_ID), CHECK (Ab_ID > -1 AND Ab_ID < 10001), CHECK (Cr_ID > -1 AND Cr_ID < 10001))  ";

		createStmts[16] = "CREATE TABLE RELATE_W_CR (Cur_Cr_ID INT  NOT NULL, Cr_ID  INT  NOT NULL, Cr_Hates BOOLEAN, Cr_Likes BOOLEAN, PRIMARY KEY (Cur_Cr_ID, Cr_ID), FOREIGN KEY (Cur_Cr_ID) REFERENCES CREATURE(Cr_ID), FOREIGN KEY (Cr_ID) REFERENCES CREATURE(Cr_ID), CHECK (Cur_Cr_ID > -1 AND Cur_Cr_ID < 10001), CHECK (Cr_ID > -1 AND Cr_ID < 10001), CHECK (Cr_Hates >= 0 AND Cr_Hates <= 1), CHECK (Cr_Likes >= 0 AND Cr_Likes <= 1)) ";

		createStmts[17] = "CREATE TABLE CR_ITEMS (C_ID  INT  NOT NULL, C_Item  INT  NOT NULL, PRIMARY KEY (C_ID, C_Item), FOREIGN KEY (C_ID) REFERENCES CREATURE (Cr_ID), FOREIGN KEY (C_Item) REFERENCES ITEM(I_ID), CHECK (C_ID > -1 AND C_ID < 10001), CHECK (C_Item > -1 AND C_Item < 10001)) ";

		createStmts[18] = "CREATE TABLE CON_CONTAIN (Container_ID INT  NOT NULL, Containee_ID INT  NOT NULL, PRIMARY KEY (Container_ID, Containee_ID), FOREIGN KEY (Container_ID) REFERENCES CONTAINER(Co_ID), FOREIGN KEY (Containee_ID) REFERENCES CONTAINER(Co_ID), CHECK (Container_ID > -1 AND Container_ID < 10001), CHECK (Containee_ID > -1 AND Containee_ID < 10001)) ";

		createStmts[19] = "CREATE TABLE WEAPON (W_ID  INT  NOT NULL, W_Image VARCHAR(35), W_Description VARCHAR(50), W_Location VARCHAR(20) NOT NULL, C_Name VARCHAR(25) NOT NULL, Cont_ID INT, Ab_ID  INT  NOT NULL, L_ID  INT, PRIMARY KEY (W_ID), FOREIGN KEY (C_Name) REFERENCES PLAYER_CHAR(P_Name), FOREIGN KEY (Cont_ID) REFERENCES CONTAINER(Co_ID), FOREIGN KEY (Ab_ID) REFERENCES ABILITY(Ab_ID), FOREIGN KEY (L_ID) REFERENCES LOCATION (Loc_ID), CHECK (W_ID > -1 AND W_ID < 10001), CHECK (Cont_ID > -1 AND Cont_ID < 10001), CHECK (Ab_ID > -1 AND Ab_ID < 10001), CHECK (L_ID > -1 AND L_ID < 10001)) ";

	}

	/**
	 * Creates the required CREATE TABLE statement for the MySQL database.
	 * @param m_dbConn The Connection object to the database.
	 */
	public void createTables(Connection m_dbConn)
	{
		for (int i = 0; i < createStmts.length; i++)
		{
			String data = createStmts[i];
			try
			{
				Statement stmt = m_dbConn.createStatement();
				stmt.executeUpdate(data);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
