package edu.doane.sudoku.persistence;

import edu.doane.sudoku.model.Game;
import edu.doane.sudoku.model.GameGrid;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * Singleton class implementing the persistence functionality used by SuDoKu
 * applications. The instance of this class serves as a connection to the cloud,
 * where the large repository of games is stored, and a manager for a local
 * Apache Derby database holding games, high scores, etc.
 *
 * @author Mark M. Meysenburg
 * @version 01/11/2018
 */
public class Persistence {

    /**
     * Reference to the single instance of the class that's allowed to exist.
     */
    private static Persistence instance = null;

    /**
     * URL of JSON array holding games. Value is read from the .ini file
     * when the class is instantiated.
     */
    private String sJSON_URL;

    /**
     * Private default constructor. Prevents the class from being instantiated
     * from the outside.
     */
    private Persistence() {
        loadSettings();
        loadDatabaseDriver();
        if (!databaseExists()) {
            createDatabaseTables();
            fetchFromCloud();
        }
    }

    /**
     * Get a reference to the Persistence instance.
     *
     * @return Reference to the single instance of the Persistence object that's
     * allowed to exist.
     */
    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }

        return instance;
    }

    /**
     * Load the driver required to use the embedded Apache Derby local
     * database. This method only needs to be called one time, before any
     * connections to the database are attempted.
     */
    private void loadDatabaseDriver() {
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            System.err.println("Cannot load embedded Derby driver! Exiting!");
            System.err.println(ex.toString());
            System.exit(-1);
        }
    }

    /**
     * Determine if the local Derby database already exists.
     *
     * @return True if the database exists, false if it does not.
     */
    private boolean databaseExists() {
        boolean databaseExists = false;

        try {
            // connect to database, creating it if need be
            Properties props = new Properties();
            Connection conn = DriverManager.getConnection("jdbc:derby:"
                    + "SuDoKuDB;create=true", props);

            // see if the GAME table already exists; if it does, the database
            // was already in existence
            DatabaseMetaData metadata = conn.getMetaData();
            String[] names = {"TABLE"};
            ResultSet tableNames = metadata.getTables(null, null, null, names);
            while (tableNames.next()) {
                String tab = tableNames.getString("TABLE_NAME");
                if (tab.equalsIgnoreCase("GAME")) {
                    databaseExists = true;
                }
            }

            // close database connection
            conn.close();

        } catch (SQLException ex) {
            System.err.println("Can't verify existence of Derby database! "
                    + "Exiting!");
            System.err.println(ex.toString());
            System.exit(-1);
        }

        return databaseExists;
    }

    /**
     * Create tables in a newly created, empty local Derby database.
     */
    private void createDatabaseTables() {
        try {
            // connect to the database
            Properties props = new Properties();
            Connection conn = DriverManager.getConnection("jdbc:derby:"
                    + "SuDoKuDB", props);

            // make the game table
            Statement s = conn.createStatement();
            s.execute("CREATE TABLE GAME(GAME_ID INTEGER, "
                    + "GAME_DATA CHAR(81) NOT NULL UNIQUE, "
                    + "SOLVED_GAME_DATA CHAR(81) NOT NULL UNIQUE, "
                    + "PRIMARY KEY(GAME_ID))");

            // close database connection
            conn.close();

        } catch (SQLException ex) {
            System.err.println("Can't create database tables! Exiting!");
            System.err.println(ex.toString());
            System.exit(-1);
        }

    }

    /**
     * Determine if the GAME table of the local Derby database is empty
     * or not.
     *
     * @return True if the GAME table is empty, false otherwise.
     */
    public boolean gameTableEmpty() {
        boolean isEmpty = false;

        try {
            // connect to the database
            Properties props = new Properties();
            Connection conn = DriverManager.getConnection("jdbc:derby:SuDoKuDB", props);

            // query to determine number of database elements
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM GAME");
            rs.next();
            if (rs.getInt(1) == 0) {
                isEmpty = true;
            }

            // close database connection
            conn.close();

        } catch (SQLException ex) {
            System.err.println("Cannot determine if GAME table is empty!");
        }

        // send back the result
        return isEmpty;
    }

    /**
     * Get the next game that hasn't been played yet.
     *
     * @return Game object representing the next un-played game
     */
    public Game getNextGame() {
        Game g = null;

        try {
            // connect to the database
            Properties props = new Properties();
            Connection conn = DriverManager.getConnection("jdbc:derby:SuDoKuDB", props);

            // fetch next game
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM GAME FETCH FIRST ROW ONLY");
            rs.next();
            g = new Game(rs.getInt(1),
                    new GameGrid(rs.getString(2)),
                    new GameGrid(rs.getString(3)));

            // remove the game we're fixing to return
            s.execute("DELETE FROM GAME WHERE GAME_ID = " + g.getID());

            // close database connection
            conn.close();

            // have we exhausted all the games?
            if (gameTableEmpty()) {
                // if so, restock
                fetchFromCloud();
            }

        } catch (SQLException ex) {
            System.err.println("Cannot connect to local Derby database in getNextGame()!");
        }
        return g;
    }

    /**
     * Retrieve a large set of SuDoKu games from the cloud. The games are
     * read as a JSON array and then placed into the local database GAME
     * table.
     */
    private void fetchFromCloud() {

        try {
            // read JSON array containing games from the cloud
            JSONArray jarr = JsonReader.readJsonArrFromUrl(sJSON_URL);

            // connect to the database
            Properties props = new Properties();
            Connection conn = DriverManager.getConnection("jdbc:derby:"
                    + "SuDoKuDB", props);

            // create a prepared statement that inserts into the game table
            PreparedStatement ps = conn.prepareStatement("INSERT INTO GAME "
                    + "VALUES(?, ?, ?)");

            // iterate through all of the objects in the JSON array
            for (Object o : jarr) {
                JSONObject j = (JSONObject) o;

                // set parameters in the prepared statement
                ps.setInt(1, j.getInt("GAME_ID"));
                ps.setString(2, j.getString("GAME_DATA"));
                ps.setString(3, j.getString("SOLVED_GAME_DATA"));

                // add the row!
                ps.executeUpdate();
            }

            // close database connection
            conn.close();

        } catch (IOException | JSONException | SQLException ex) {
            System.err.println("Cannot access JSON object from cloud! Exiting!");
            System.err.println(ex.toString());
            System.exit(-1);
        }
    }

    /**
     * Load persistence data from the DoaneSuDoKu.ini file.
     */
    private void loadSettings() {
        try {
            try (Scanner inFile = new Scanner(new File("DoaneSuDoKu.ini"))) {
                // read label / value pairs (one pair per line) from the 
                // ini file
                while (inFile.hasNext()) {

                    // get label and value
                    String label = inFile.next();
                    String value = inFile.next();

                    // make assignments based on label 
                    if (label.equals("url")) {
                        sJSON_URL = value;
                    }
                } // while

                // close .ini file
                inFile.close();

            } // try with resources
        } catch (FileNotFoundException ex) {
            System.err.println("Can't open DoaneSuDoKu.ini! Exiting!");
            System.err.println(ex.toString());
            System.exit(-1);
        }
    }
}
