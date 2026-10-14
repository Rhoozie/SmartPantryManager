package com.smartpantry.manager.recipes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smartpantry.manager.database.PantryDatabaseHelper;

public class RecipeMatcher {

    private final PantryDatabaseHelper helper;

    public RecipeMatcher(PantryDatabaseHelper helper) {
        this.helper = helper;
    }

    public Cursor getAvailableRecipes() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql =
                "SELECT r." + PantryDatabaseHelper.COL_RECIPE_ID
                + ", r." + PantryDatabaseHelper.COL_RECIPE_NAME
                + ", r." + PantryDatabaseHelper.COL_DESCRIPTION
                + " FROM " + PantryDatabaseHelper.TABLE_RECIPES + " r"
                + " WHERE NOT EXISTS ("
                + " SELECT 1 FROM "
                + PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS + " ri"
                + " WHERE ri." + PantryDatabaseHelper.COL_RECIPE_ID
                + " = r." + PantryDatabaseHelper.COL_RECIPE_ID
                + " AND NOT EXISTS ("
                + " SELECT 1 FROM "
                + PantryDatabaseHelper.TABLE_INGREDIENTS + " p"
                + " WHERE " + normalizedNameSql(
                        "p." + PantryDatabaseHelper.COL_NAME)
                + " = " + normalizedNameSql(
                        "ri." + PantryDatabaseHelper.COL_REQUIRED_INGREDIENT)
                + " AND " + quantityAvailableSql()
                + " )"
                + " )"
                + " ORDER BY r." + PantryDatabaseHelper.COL_RECIPE_NAME
                + " COLLATE NOCASE ASC";

        return db.rawQuery(sql, null);
    }

    private String quantityAvailableSql() {
        String pantryUnit = normalizedUnitSql(
                "p." + PantryDatabaseHelper.COL_UNIT);

        String requiredUnit = normalizedUnitSql(
                "ri." + PantryDatabaseHelper.COL_REQUIRED_UNIT);

        String pantryQty = "p." + PantryDatabaseHelper.COL_QUANTITY;
        String requiredQty =
                "ri." + PantryDatabaseHelper.COL_REQUIRED_QUANTITY;

        return "("
                // Same standardised unit
                + "(" + pantryUnit + " = " + requiredUnit
                + " AND " + pantryQty + " >= " + requiredQty + ")"

                // Kilograms in the pantry satisfy gram requirements
                + " OR ((" + pantryUnit + ") = 'kg'"
                + " AND (" + requiredUnit + ") = 'g'"
                + " AND " + pantryQty + " * 1000 >= " + requiredQty + ")"

                // Grams in the pantry satisfy kilogram requirements
                + " OR ((" + pantryUnit + ") = 'g'"
                + " AND (" + requiredUnit + ") = 'kg'"
                + " AND " + pantryQty + " >= " + requiredQty + " * 1000)"

                // Litres in the pantry satisfy millilitre requirements
                + " OR ((" + pantryUnit + ") = 'l'"
                + " AND (" + requiredUnit + ") = 'ml'"
                + " AND " + pantryQty + " * 1000 >= " + requiredQty + ")"

                // Millilitres in the pantry satisfy litre requirements
                + " OR ((" + pantryUnit + ") = 'ml'"
                + " AND (" + requiredUnit + ") = 'l'"
                + " AND " + pantryQty + " >= " + requiredQty + " * 1000)"
                + ")";
    }

    private String normalizedUnitSql(String column) {
        String value = "LOWER(TRIM(" + column + "))";

        return "CASE "
                + "WHEN " + value
                + " IN ('piece','pieces','pc','pcs') THEN 'pieces' "
                + "WHEN " + value
                + " IN ('gram','grams','g') THEN 'g' "
                + "WHEN " + value
                + " IN ('kilogram','kilograms','kg') THEN 'kg' "
                + "WHEN " + value
                + " IN ('millilitre','millilitres','milliliter',"
                + "'milliliters','ml') THEN 'ml' "
                + "WHEN " + value
                + " IN ('litre','litres','liter','liters','l') THEN 'l' "
                + "WHEN " + value
                + " IN ('packet','packets') THEN 'packets' "
                + "WHEN " + value
                + " IN ('can','cans') THEN 'cans' "
                + "WHEN " + value
                + " IN ('tablespoon','tablespoons','tbsp') THEN 'tablespoons' "
                + "WHEN " + value
                + " IN ('teaspoon','teaspoons','tsp') THEN 'teaspoons' "
                + "ELSE " + value + " END";
    }

    private String normalizedNameSql(String column) {
        String value = "LOWER(TRIM(" + column + "))";

        return "CASE "
                + "WHEN " + value + " IN ('egg','eggs') THEN 'egg' "
                + "WHEN " + value + " IN ('tomato','tomatoes') THEN 'tomato' "
                + "WHEN " + value + " IN ('potato','potatoes') THEN 'potato' "
                + "WHEN " + value + " IN ('berry','berries') THEN 'berry' "
                + "WHEN " + value + " IN ('loaf','loaves') THEN 'loaf' "
                + "WHEN " + value + " IN ('leaf','leaves') THEN 'leaf' "
                + "WHEN " + value + " IN ('apple','apples') THEN 'apple' "
                + "WHEN " + value + " IN ('banana','bananas') THEN 'banana' "
                + "WHEN " + value + " IN ('carrot','carrots') THEN 'carrot' "
                + "WHEN " + value + " IN ('onion','onions') THEN 'onion' "
                + "WHEN " + value + " IN ('lemon','lemons') THEN 'lemon' "
                + "WHEN " + value + " IN ('cucumber','cucumbers') THEN 'cucumber' "
                + "WHEN " + value + " IN ('bean','beans') THEN 'bean' "
                + "WHEN " + value + " IN ('olive','olives') THEN 'olive' "
                + "WHEN " + value
                + " IN ('garlic clove','garlic cloves') THEN 'garlic clove' "
                + "ELSE " + value + " END";
    }
}
