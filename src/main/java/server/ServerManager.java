package server;

import server.config.JSONManager;
import server.mysql.MySQLManager;
import server.utils.Argon2Hashing;

import java.util.Scanner;

public class ServerManager {

    //Static variables for global access
    public static JSONManager fileManager;
    public static MySQLManager mysql;
    public static SocketServer server;

    public static void main(String[]args){
        System.out.print("Starting server");
        //System.out.println(Argon2Hashing.encrypt("test"));

        //loading configs
        fileManager = new JSONManager();
        fileManager.readValueFromJSon();

        //loading mysql
        mysql = new MySQLManager(JSONManager.HOST, JSONManager.DATABASE, JSONManager.USERNAME, JSONManager.PASSWORD);
        //loading server
        server = new SocketServer(8000);
        server.setMuted(true);

        readFromConsole();
    }

    private static void readFromConsole(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Scanner sc = new Scanner(System.in);

                while(true){
                    if(sc.nextLine().equalsIgnoreCase("/stop")){
                        System.out.println("Stopping server");
                        System.exit(0);
                    }
                    else if(sc.nextLine().equalsIgnoreCase("/help")){
                        System.out.println("Existing commands:");
                        System.out.println("/stop  | stop the server safely");
                        System.out.println("/help  | list all commands");
                    }
                    else{
                        System.out.println("This command does not exist.");
                    }
                }
            }
        }).start();
    }

}
