package server.utils;

import de.extra.lib.Food;
import de.extra.lib.Recipe;

import java.util.ArrayList;

public class RecipeAlgorithm {

    /**
     *
     * @param userFood
     *      An arraylist of the food the user has in his fridge
     * @param listOfRecipes
     *      An list of random selected recipes
     * @return
     *      Returning the allowed recipes
     */
    public static ArrayList<Recipe> getAllowedRecipes(ArrayList<Food> userFood, ArrayList<Recipe> listOfRecipes) {
        String food = getAsString(userFood);
        System.out.println(food);
        //check for same amount of food
        for(int i = 0; i< listOfRecipes.size(); i++){
            //if not remove from list
            if(userFood.size() >= listOfRecipes.get(i).getFoodList().size() ){
                listOfRecipes.remove(i);
            }
        }

        //final check
        return checkFood(food, listOfRecipes);
    }

    private static ArrayList<Recipe> checkFood(String food, ArrayList<Recipe> fromRecipe){
        for(int k = 0; k< fromRecipe.size(); k++) {
            if(!food.contains(fromRecipe.get(k).getNeeds())){
                fromRecipe.remove(k);
            }
            else{
                System.out.println(fromRecipe.get(k).getNeeds());
            }
        }
        return fromRecipe;
    }


    //sorting the food
    private static void sortFood(ArrayList<Food> list) {
        int n = list.size();
        Food temp;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (list.get(j - 1).getName().charAt(0) > list.get(j).getName().charAt(0)) {
                    //swap elements
                    temp = list.get(j - 1);
                    list.set(j - 1, list.get(j));
                    list.set(j, temp);
                }

            }
        }
    }

    //format the food as String
    private static String getAsString(ArrayList<Food> foodList){
        String needs = "";

        sortFood(foodList);

        for(int i = 0; i < foodList.size(); i++){
            needs+=foodList.get(i).getName()+",";
        }
        return needs;
    }
}
