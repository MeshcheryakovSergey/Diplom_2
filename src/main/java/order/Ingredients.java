package order;

import java.util.ArrayList;

public class Ingredients {
    private  ArrayList <String> ingredients;

    public Ingredients(ArrayList ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList ingredients) {
        this.ingredients = ingredients;
    }
}
