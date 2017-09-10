package org.deepanshu.jakhar.pokeapi;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 05/07/2017.
 */

public class Pokemon {
    String name,weight,height,id,base_experience;
    Sprites sprites;
    ArrayList<Types> types;

    public Pokemon(String name, String weight, String height, String id, String base_experience, Sprites sprites,ArrayList<Types> typesArrayList) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.id = id;
        this.base_experience = base_experience;
        this.sprites = sprites;
        this.types = typesArrayList;
    }

    public ArrayList<Types> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Types> types) {
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(String base_experience) {
        this.base_experience = base_experience;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }
}
