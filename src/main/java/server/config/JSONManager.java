package server.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.mysql.MySQLManager;

import javax.net.ssl.HostnameVerifier;
import java.io.*;
/*
    A reader for json files
 */
public class JSONManager {

    //declaring the files and the directory
    private File fridge = new File("config/mysql.json");
    private File path = new File("config/");

    //needed data, read from file
    public static String HOST, DATABASE, USERNAME, PASSWORD;

    //constructing the json reader
    public JSONManager(){
        path.mkdir();
        try {
            //if file is not existing -> create and write into
            if (!fridge.exists()){
                fridge.createNewFile();
                writeInJSON();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //writing the pre defined layout
    public void writeInJSON(){
        //First Employee
        JSONObject employeeDetails = new JSONObject();
        employeeDetails.put("Host", "localhost");
        employeeDetails.put("Database", "Test");
        employeeDetails.put("username", "Test");
        employeeDetails.put("password", "Test");

        JSONObject employeeObject = new JSONObject();
        employeeObject.put("SQL-Data", employeeDetails);

        //Add employees to list
        JSONArray employeeList = new JSONArray();
        employeeList.add(employeeObject);

        //Write JSON file
        try (FileWriter file = new FileWriter(fridge)) {

            file.write(employeeList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read the vvalues from json file. Including the changes made by the user
    public void readValueFromJSon() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fridge)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;
            employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONArray readFromJSON() {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fridge)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray employeeList = (JSONArray) obj;

            return employeeList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseEmployeeObject(JSONObject employee)
    {
        //Get employee object within list
        JSONObject employeeObject = (JSONObject) employee.get("SQL-Data");

        //Get employee first name
        HOST = (String) employeeObject.get("Host");

        //Get employee last name
        DATABASE = (String) employeeObject.get("Database");

        //Get employee website name
        USERNAME = (String) employeeObject.get("username");

        //Get employee website name
        PASSWORD = (String) employeeObject.get("password");

    }
}