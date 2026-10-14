package com.smartpantry.manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartpantry.manager.database.PantryDatabaseHelper;

public class RecipeDetailActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        databaseHelper = new PantryDatabaseHelper(this);

        TextView tvName = findViewById(R.id.tvDetailRecipeName);
        TextView tvDescription =
                findViewById(R.id.tvDetailRecipeDescription);
        TextView tvIngredients =
                findViewById(R.id.tvDetailRecipeIngredients);
        TextView tvInstructions =
                findViewById(R.id.tvDetailRecipeInstructions);
        Button btnBack = findViewById(R.id.btnRecipeDetailBack);

        btnBack.setOnClickListener(view -> finish());

        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadRecipe(recipeId, tvName, tvDescription,
                tvIngredients, tvInstructions);
    }

    private void loadRecipe(
            int recipeId,
            TextView tvName,
            TextView tvDescription,
            TextView tvIngredients,
            TextView tvInstructions) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try (Cursor recipeCursor = db.query(
                PantryDatabaseHelper.TABLE_RECIPES,
                new String[]{
                        PantryDatabaseHelper.COL_RECIPE_NAME,
                        PantryDatabaseHelper.COL_DESCRIPTION,
                        PantryDatabaseHelper.COL_INSTRUCTIONS
                },
                PantryDatabaseHelper.COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null, null)) {

            if (!recipeCursor.moveToFirst()) {
                Toast.makeText(this, "Recipe not found",
                        Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            tvName.setText(recipeCursor.getString(0));
            tvDescription.setText(recipeCursor.getString(1));
            tvInstructions.setText(recipeCursor.getString(2));
        }

        StringBuilder ingredients = new StringBuilder();

        try (Cursor ingredientCursor = db.query(
                PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                new String[]{
                        PantryDatabaseHelper.COL_REQUIRED_INGREDIENT,
                        PantryDatabaseHelper.COL_REQUIRED_QUANTITY,
                        PantryDatabaseHelper.COL_REQUIRED_UNIT
                },
                PantryDatabaseHelper.COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null,
                PantryDatabaseHelper.COL_REQUIRED_INGREDIENT
                        + " COLLATE NOCASE ASC")) {

            while (ingredientCursor.moveToNext()) {
                String name = ingredientCursor.getString(0);
                double quantity = ingredientCursor.getDouble(1);
                String unit = ingredientCursor.getString(2);

                ingredients.append("\u2022 ")
                        .append(formatQuantity(quantity))
                        .append(" ")
                        .append(unit)
                        .append(" ")
                        .append(name)
                        .append("\n");
            }
        }

        tvIngredients.setText(
                ingredients.length() == 0
                        ? "No ingredients listed."
                        : ingredients.toString().trim());
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
