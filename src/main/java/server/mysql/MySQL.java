package server.mysql;
/**
 * Project: MySQL library
 * Author: Timo Skrobanek
 * Last Changes:
 * 		date: 29.3.2020
 * 		version: 1.2
 *
 *
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @MySQL
 *  Diese Klasse beinhaltet Methoden zur vereinfachten Benutzung einer Datenbank.
 *  Der Code bassiert auf die JDBC API.
 *  Damit der Code funktioniert wird die Library mysql-connector-java gebraucht,
 *  die über Gradlew hier im Projekt mit drin ist. Sollte die Klasse ohne Gradlew
 *  verwendet werden muss die Jar extra hinzugefügt werden!
 */
public class MySQL {

    private String HOST = "";
    private String DATABASE = "";
    private String USER = "";
    private String PASSWORD = "";
    private int PORT = 3306;

    private boolean reconnect = true;
    private boolean mute = false;

    private Connection con;

    /**
       @param HOST
            Legt einen Server fest, zu den eine Verbindung hergestellt werden soll

        @param DATABASE
            Legt die Datenbank fest, die angesprochen werden soll.

        @param USER
            Gibt den Benutzer an die Datenbank weiter.

        @param PASSWORD
            Das Passwort soll die Datenbank schützen. Zur sicheren Übertragung muss eine SSL
            Verbindung bestehen oder Die Datenbank darf nach außen nicht erreichbar sein!
     */
    public MySQL(String HOST, String DATABASE, String USER, String PASSWORD) {
        this.HOST = HOST;
        this.DATABASE = DATABASE;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

        if(!mute) {
            System.out.println("Connecting to Database");
        }

        connect();
    }

    public MySQL(String HOST, String DATABASE, String USER, String PASSWORD, boolean autoConnect, boolean thread) {
        this.HOST = HOST;
        this.DATABASE = DATABASE;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

        if(!mute) {
            System.out.println("[Information]: Connecting to Database");
        }

        if(autoConnect && thread) {
            new Thread( new Runnable(){
                @Override
                public void run() {
                    connect();

                }}).start();
        }
        else if(autoConnect && !thread) {
            connect();
        }
    }

    /**
     * Stellt eine Verbindung zur Datenbank her.
     */
    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":"+PORT+"/" + DATABASE + "?autoReconnect="+reconnect, USER, PASSWORD);

            if(!mute) {
                System.out.println("===========================");
                System.out.println("Connected to MySQL database");
                System.out.println("===========================");
            }

        }catch(SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Beendet eine bestehende Verbindung
     */
    public void cancelConnection() {
        try {
            if(con != null) {
                con.close();
            }
            else {
                if(!mute) {
                    System.out.println("[Information]: Can't disconnect if no connection available");
                }
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param query
     *  Ermöglicht es Befehle an die Datenbank zu schicken. Hier können zbs
     *  Tabellen erstellt und Daten eingetragen werden. Im Parameter wird
     *  SQL-Code erwartet.
     */
    public void update(String query) {
        try {
            java.sql.Statement st = con.createStatement();
            st.execute(query);
            st.close();

        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param query
     *  Mit dieser Methode können Daten aus der Datenbank ausgelesen werden.
     *  Der Parameter nimmt SQL-Code entgegen.
     *
     * @return
     * Die Daten werden als @ResultSet zurückgegeben und müssen
     * dementsprechend noch in das richtige Format gecasted werden.
     */
    public ResultSet query(String query) {
        ResultSet rs = null;

        try {
            java.sql.Statement st = con.createStatement();
            rs = st.executeQuery(query);
        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }

        return rs;
    }

    /**
     *
     * @param query
     *  Mit dieser Methode können Daten aus der Datenbank ausgelesen werden.
     *  Der Parameter nimmt SQL-Code entgegen.
     *
     * @return
     * Die Daten werden als @Object zurückgegeben und müssen
     * dementsprechend noch in das richtige Format gecasted werden.
     */
    public Object queryObject(String query) {
        ResultSet rs = null;

        try {
            java.sql.Statement st = con.createStatement();
            rs = st.executeQuery(query);
            return rs.getObject(1);
        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public void setPort(int PORT){
        this.PORT = PORT;
    }

    public int getPort(){
        return PORT;
    }

}
