package com.smartpantry.manager.recipes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smartpantry.manager.database.PantryDatabaseHelper;

public class RecipeSeeder {

    private RecipeSeeder() {
    }

    public static void seedIfEmpty(PantryDatabaseHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.beginTransaction();
        try {
            addRecipe(db, "Tomato Egg Scramble",
                    "A quick egg dish with fresh tomato.",
                    "1. Chop the tomato. 2. Beat the eggs. 3. Cook tomato and eggs in a pan until set.",
                    new String[]{"eggs", "tomato"},
                    new double[]{2, 1},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Cheese Omelette",
                    "A simple omelette with cheese.",
                    "1. Beat the eggs. 2. Cook in a lightly oiled pan. 3. Add cheese, fold and cook until done.",
                    new String[]{"eggs", "cheese"},
                    new double[]{2, 30},
                    new String[]{"pieces", "grams"});

            addRecipe(db, "Banana Oatmeal",
                    "Warm oats topped with banana.",
                    "1. Simmer oats in milk or water. 2. Slice banana and serve on top.",
                    new String[]{"oats", "banana"},
                    new double[]{50, 1},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Garlic Pasta",
                    "Pasta flavoured with garlic and oil.",
                    "1. Boil pasta. 2. Cook chopped garlic in oil. 3. Toss together.",
                    new String[]{"pasta", "garlic", "olive oil"},
                    new double[]{100, 2, 15},
                    new String[]{"grams", "pieces", "millilitres"});

            addRecipe(db, "Tomato Pasta",
                    "Pasta with a quick tomato sauce.",
                    "1. Boil pasta. 2. Cook chopped tomatoes until soft. 3. Mix with pasta.",
                    new String[]{"pasta", "tomato"},
                    new double[]{100, 2},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Cheese Sandwich",
                    "A toasted cheese sandwich.",
                    "1. Place cheese between bread slices. 2. Toast until golden and cheese melts.",
                    new String[]{"bread", "cheese"},
                    new double[]{2, 30},
                    new String[]{"pieces", "grams"});

            addRecipe(db, "Peanut Butter Toast",
                    "Toast spread with peanut butter.",
                    "1. Toast the bread. 2. Spread peanut butter evenly.",
                    new String[]{"bread", "peanut butter"},
                    new double[]{2, 20},
                    new String[]{"pieces", "grams"});

            addRecipe(db, "Apple Oat Bowl",
                    "Oats served with chopped apple.",
                    "1. Prepare oats with water or milk. 2. Chop the apple and add on top.",
                    new String[]{"oats", "apple"},
                    new double[]{50, 1},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Potato Egg Hash",
                    "Pan-cooked potato with egg.",
                    "1. Dice and cook potato until tender. 2. Add egg and cook thoroughly.",
                    new String[]{"potato", "eggs"},
                    new double[]{2, 1},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Carrot Salad",
                    "A crisp carrot and lemon salad.",
                    "1. Grate the carrot. 2. Add lemon juice and mix.",
                    new String[]{"carrot", "lemon"},
                    new double[]{2, 1},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Garlic Rice",
                    "Rice with garlic flavour.",
                    "1. Cook rice until tender. 2. Gently fry chopped garlic. 3. Mix together.",
                    new String[]{"rice", "garlic"},
                    new double[]{100, 1},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Spinach Omelette",
                    "Eggs cooked with spinach.",
                    "1. Beat the eggs. 2. Add chopped spinach. 3. Cook until the eggs are fully set.",
                    new String[]{"eggs", "spinach"},
                    new double[]{2, 50},
                    new String[]{"pieces", "grams"});

            addRecipe(db, "Yoghurt Banana Bowl",
                    "A quick banana and yoghurt snack.",
                    "1. Slice the banana. 2. Mix with yoghurt and serve.",
                    new String[]{"banana", "yoghurt"},
                    new double[]{1, 100},
                    new String[]{"pieces", "grams"});

            addRecipe(db, "Corn Salad",
                    "A simple corn and tomato salad.",
                    "1. Drain the corn. 2. Dice tomato. 3. Combine and serve.",
                    new String[]{"corn", "tomato"},
                    new double[]{100, 1},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Lemon Rice",
                    "Rice with fresh lemon.",
                    "1. Cook rice. 2. Add lemon juice and stir before serving.",
                    new String[]{"rice", "lemon"},
                    new double[]{100, 1},
                    new String[]{"grams", "pieces"});

            addRecipe(db, "Carrot Soup",
                    "A simple carrot soup.",
                    "1. Chop carrots. 2. Simmer in water until soft. 3. Blend or mash.",
                    new String[]{"carrot", "onion"},
                    new double[]{3, 1},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Tomato Cheese Toast",
                    "Toast topped with tomato and cheese.",
                    "1. Place sliced tomato and cheese on bread. 2. Toast until cheese melts.",
                    new String[]{"bread", "tomato", "cheese"},
                    new double[]{2, 1, 20},
                    new String[]{"pieces", "pieces", "grams"});

            addRecipe(db, "Onion Omelette",
                    "An omelette with chopped onion.",
                    "1. Chop the onion. 2. Beat eggs and mix in onion. 3. Cook thoroughly.",
                    new String[]{"eggs", "onion"},
                    new double[]{2, 1},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Cucumber Tomato Salad",
                    "A refreshing chopped salad.",
                    "1. Chop cucumber and tomato. 2. Mix and serve.",
                    new String[]{"cucumber", "tomato"},
                    new double[]{1, 2},
                    new String[]{"pieces", "pieces"});

            addRecipe(db, "Flour Pancakes",
                    "Basic pancakes made from flour and milk.",
                    "1. Mix flour, milk and egg into a batter. 2. Cook small portions in a pan until golden.",
                    new String[]{"flour", "milk", "eggs"},
                    new double[]{100, 100, 1},
                    new String[]{"grams", "millilitres", "pieces"});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void addRecipe(
            SQLiteDatabase db,
            String name,
            String description,
            String instructions,
            String[] ingredients,
            double[] quantities,
            String[] units) {

        ContentValues recipe = new ContentValues();
        recipe.put(PantryDatabaseHelper.COL_RECIPE_NAME, name);
        recipe.put(PantryDatabaseHelper.COL_DESCRIPTION, description);
        recipe.put(PantryDatabaseHelper.COL_INSTRUCTIONS, instructions);

        long recipeId = db.insertWithOnConflict(
                PantryDatabaseHelper.TABLE_RECIPES,
                null,
                recipe,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        // If the recipe already exists, do not insert its ingredients again.
        if (recipeId == -1) {
            return;
        }

        for (int i = 0; i < ingredients.length; i++) {
            ContentValues item = new ContentValues();
            item.put(PantryDatabaseHelper.COL_RECIPE_ID, recipeId);
            item.put(PantryDatabaseHelper.COL_REQUIRED_INGREDIENT, ingredients[i]);
            item.put(PantryDatabaseHelper.COL_REQUIRED_QUANTITY, quantities[i]);
            item.put(PantryDatabaseHelper.COL_REQUIRED_UNIT, units[i]);

            db.insert(PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                    null, item);
        }
    }
}
