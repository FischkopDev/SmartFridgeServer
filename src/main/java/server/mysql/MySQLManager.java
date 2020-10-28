package server.mysql;

import com.google.gson.Gson;
import de.extra.lib.Food;
import de.extra.lib.Recipe;
import server.utils.RecipeAlgorithm;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MySQLManager extends MySQL{

    /**
     *
     * @param HOST
     *      Host IP for the database
     * @param DATABASE
     *      The actual database on the server
     * @param USER
     *      The user who have the permission to change, read or add data
     *      to the tables.
     * @param PASSWORD
     *      The password of the user.
     */
    public MySQLManager(String HOST, String DATABASE, String USER, String PASSWORD) {
        super(HOST, DATABASE, USER, PASSWORD);

        //Creating tables
        update("CREATE TABLE IF NOT EXISTS user(name varchar(100), email varchar(100), password varchar(1000), tmpkey varchar(100));");
        update("CREATE TABLE IF NOT EXISTS food(name varchar(100), amount varchar(100), item varchar(100), category varchar(100), fridge varchar(100));");
        update("CREATE TABLE IF NOT EXISTS fridge(name varchar(100), username varchar(100));");
        update("CREATE TABLE IF NOT EXISTS recipes(name varchar(100), recipe varchar(5000), needs varchar(1000));");
    }

    /**
     *
     * @param name
     *      Name of the registering user
     * @param email
     *      Email of the new client
     * @param pw
     *      The blank password which will be encrypted with Argon2
     */
    public void registerNewUser(String name, String email, String pw, String fridge){
        //TODO encryption
        //update("INSERT INTO user(name, email, password, tmpkey) VALUES ('"+name+"','"+email+"','"+ Argon2Hashing.encrypt(pw) +  "','null');");
        update("INSERT INTO user(name, email, password, tmpkey) VALUES ('"+name+"','"+email+"','"+ pw +  "','null');");
        update("INSERT INTO fridge(name, username) VALUES ('"+fridge+"','"+name+"');");
    }

    /**
     *
     * @param name
     *      Name of the current client
     * @param pw
     *      Password of the client.
     * @return
     *      The result if the client is logged in or not.
     */
    public boolean loginUser(String name, String pw){
        try {
            ResultSet rs = query("SELECT password FROM user WHERE name='" + name + "';");
            rs.next();

            //if(Argon2Hashing.verify(rs.getString(0), pw)){
            if(rs.getString(1).equals(pw)){
                update("UPDATE user SET tmpkey='"+ UUID.randomUUID() + "' WHERE name='"+name+"';");
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param name
     *      the name of the user which will be checked.
     * @return
     *      returning if the user exist in database signaled by
     *      true or false
     */
    public boolean existUsername(String name){
        try {
           // ResultSet rs = query("SELECT EXISTS(SELECT * FROM user WHERE name='" + name + "');");
            ResultSet rs = query("SELECT * FROM user WHERE name='" + name + "';");
            rs.next();

             return rs.getString(1) != null;
        }catch(SQLException e){
           // e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param name
     *      Name of the current client
     * @return
     *      returning the key which allows next operations on the server
     */
    public String getKey(String name){
        try {
            ResultSet rs = query("SELECT tmpkey FROM user WHERE name='" + name + "';");
            rs.next();

            return rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param itemName
     *      Name of the food item you insert
     * @param amount
     *      Count of the food you insert
     * @param item
     *      The datatype of the item e.g. kg, ml, g,...
     * @param category
     *      A category to sort the items in different aspects.
     * @param username
     *      The name of the user which is locked with a
     *      single or multiple fridges
     */
    public void insertIntoFridge(String itemName, String amount, String item, String category, String username){
        if(!existItemInFridge(itemName, username))
             update("INSERT INTO food (name, amount, item,category, fridge) VALUES ('"+itemName+"','"+amount+"','"+item+"','"+category+"','"+getFridge(username)+"');");
        else {
            //read the existing data
            try {
                ResultSet rs = query("SELECT * FROM food WHERE name='" + itemName + "';");
                rs.next();

                //add to fridge
                update("UPDATE food SET amount='"+(Float.parseFloat(rs.getString(2))+Float.parseFloat(amount)) + "' WHERE name='"+itemName+"';");
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    //name varchar(100), amount varchar(100), item varchar(100), category varchar(100), fridge varchar(100)
    public ArrayList<Food> getFridgeContent(String username){
        try {
            ArrayList<Food> list = new ArrayList<>();

            ResultSet rs = query("SELECT name FROM fridge WHERE username='" + username + "';");
            rs.next();
            String fridge = rs.getString(1);

            ResultSet rsFood = query("SELECT * FROM food WHERE fridge='" + fridge + "';");

            //Saving the food into a list
            while(rsFood.next()){
                list.add(new Food(rsFood.getString(1), rsFood.getString(2), rsFood.getString(3), rsFood.getString(4)));
            }
            return list;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param itemName
     *      The food item you want to check
     * @param username
     *      The uesr and indirect the fridge of the user
     * @return
     *      Returns true or false depending of if
     *      the items exists.
     */
    public boolean existItemInFridge(String itemName, String username){
        try {
            ResultSet rs = query("SELECT name FROM food WHERE fridge='" + getFridge(username) + "';");

            while(rs.next()){
                if(rs.getString(1).equals(itemName))return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param name
     *      Name of the user
     * @return
     *      Returning the name of the fridge
     *      from the given user
     */
    public String getFridge(String name){
        try {
            ResultSet rs = query("SELECT name FROM fridge WHERE username='" + name + "';");
            rs.next();

            return rs.getString(1);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /*
        =====================================================================================
                Methods for the management of the different recipes
        =====================================================================================
     */


    private Gson gson = new Gson();

    /**
     *
     * @param recipe
     *      Add a recipe to the database. It will be
     *      parsed as Recipe json and contains all
     *      information.
     */
    public void writeRecipeToDatabase(Recipe recipe){
        update("INSERT INTO recipes(name, recipe, needs) VALUES ('"+recipe.getName()+"','"+gson.toJson(recipe) + "','"+recipe.getNeeds()+"');");
    }

    public ArrayList<Recipe> getRecipeFromDatabase(String username){
        ArrayList<Food> foods = getFridgeContent(username);
        //get rnd recipes from database
        ArrayList<Recipe> list = getRndRecipes();

        //return the list with the recipes
        return RecipeAlgorithm.getAllowedRecipes(foods, list);
    }

    /**
     *
     * @return
     *      returns random recipes from the database
     */
    public ArrayList<Recipe> getRndRecipes(){
        ArrayList<Recipe> list = new ArrayList<>();

        try{
            ResultSet rs = query("SELECT recipe FROM recipes ORDER BY RAND();");
            int count = 0;
            while(rs.next()){
                count++;
                list.add(gson.fromJson(rs.getString(1), Recipe.class));
                if(count == 100)//100 = 100 recipes
                    break;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return list;
    }
}
