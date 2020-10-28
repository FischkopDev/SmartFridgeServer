/**
 * @description
 *      A documentation about all events is given in a separate file.
 */
package server;

import com.blogspot.debukkitsblog.net.Datapackage;
import com.blogspot.debukkitsblog.net.Executable;
import com.blogspot.debukkitsblog.net.Server;
import de.extra.lib.Recipe;

import java.net.Socket;

/**
 *  This class is the "server" here you can setup requests for
 *  which the program will wait until the user sent them.
 */
public class SocketServer extends Server{


    public SocketServer(int port) {
        super(port);
    }

    @Override
    public void preStart() {
        /**
         *  Receiving registration requests.
         */
        registerMethod("REGISTER", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                //Check if user already exists
                if(!ServerManager.mysql.existUsername(datapackage.get(1).toString())) {
                    //add user to database
                    ServerManager.mysql.registerNewUser(datapackage.get(1).toString(), datapackage.get(2).toString(), datapackage.get(3).toString(), datapackage.get(4).toString());

                    //send message back
                    sendMessage(datapackage.getSenderID(), new Datapackage("REGISTERED"));
                }
                else{
                    sendMessage(datapackage.getSenderID(), new Datapackage("ERROR","Der Benutzername existiert bereits."));

                }
                sendReply(socket, "OK");
            }
        });

        //Request for login
        registerMethod("LOGIN", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                if(ServerManager.mysql.loginUser(datapackage.get(1).toString(), datapackage.get(2).toString())){
                    sendMessage(datapackage.getSenderID(), new Datapackage("KEY_REPLY", ServerManager.mysql.getKey(datapackage.get(1).toString())));
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler beim Login"));
                }

                sendReply(socket, "OK");
            }
        });

        //Request to insert something into the user fridge
        registerMethod("INSERT_INTO_FRIDGE", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                String username = datapackage.get(1).toString();

                //check key from user
                if(ServerManager.mysql.getKey(username).equals(datapackage.get(2).toString())){
                    ServerManager.mysql.insertIntoFridge(datapackage.get(3).toString(),datapackage.get(4).toString(),
                            datapackage.get(5).toString(),datapackage.get(6).toString(), username);
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler bitte neu einloggen."));
                }

                sendReply(socket, "OK");
            }
        });

        registerMethod("READ_FROM_FRIDGE", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                String username = datapackage.get(1).toString();
                if(ServerManager.mysql.getKey(username).equals(datapackage.get(2).toString())){
                    sendMessage(datapackage.getSenderID(), new Datapackage("READ_FROM_FRIDGE",
                            ServerManager.mysql.getFridgeContent(username)));
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler beim auslesen. Bitte neu einloggen."));
                }
                sendReply(socket, "OK");
            }
        });

        registerMethod("UPLOAD_RECIPE", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                String username = datapackage.get(1).toString();
                if(ServerManager.mysql.getKey(username).equals(datapackage.get(2).toString())){
                    ServerManager.mysql.writeRecipeToDatabase((Recipe)datapackage.get(3));
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler: Der Server hat die" +
                            " Anfrage nicht zugelassen. Bitte neu einloggen."));
                }
                sendReply(socket, "OK");
            }
        });

        registerMethod("READ_ALL_RECIPES", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                String username = datapackage.get(1).toString();
                if(ServerManager.mysql.getKey(username).equals(datapackage.get(2).toString())){
                    sendMessage(datapackage.getSenderID(), new Datapackage("READ_ALL_RECIPES", ServerManager.mysql.getRndRecipes()));
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler: Der Server hat die" +
                            " Anfrage nicht zugelassen. Bitte neu einloggen."));
                }

                sendReply(socket, "OK");
            }
        });

        registerMethod("READ_RECIPES_WITH_NEEDS", new Executable() {
            @Override
            public void run(Datapackage datapackage, Socket socket) {
                String username = datapackage.get(1).toString();
                if(ServerManager.mysql.getKey(username).equals(datapackage.get(2).toString())){
                    sendMessage(datapackage.getSenderID(), new Datapackage("READ_RECIPES_WITH_NEEDS", ServerManager.mysql.getRecipeFromDatabase(username)));
                }
                else{
                    sendMessage(datapackage.getSenderID(),new Datapackage("ERROR","Fehler: Der Server hat die" +
                            " Anfrage nicht zugelassen. Bitte neu einloggen."));
                }

                sendReply(socket, "OK");
            }
        });
    }
}
