package de.extra.lib;

import java.io.Serializable;
import java.util.ArrayList;

/*
    You have a single recipe with multiple steps to do.
    Furthermore there are some variables to specialize the
    recipe for all needs.
 */
public class Recipe implements Serializable {

    private static final long serialVersionUID = 3935968302671731981L;

    private String name; //Name of the recipe

    //The needed food to add for the recipe
    private ArrayList<Food> foodList;

    //List of the needed steps to cook the recipe
    private ArrayList<Step> stepList;

    public Recipe(ArrayList<Food> foodList, ArrayList<Step> stepList){
        this.foodList = foodList;
        this.stepList = stepList;
    }

    public void setName(String name) {
        this.name = name;
    }

    //sorting the food
    private void sortFood(ArrayList<Food> list) {
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

    public String getNeeds(){
        String needs = "";

        sortFood(foodList);

        for(int i = 0; i < foodList.size(); i++){
            needs+=foodList.get(i).getName()+",";
        }
        return needs;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

}
