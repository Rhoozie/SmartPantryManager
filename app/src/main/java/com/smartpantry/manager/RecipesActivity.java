package com.smartpantry.manager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.adapters.RecipeAdapter;
import com.smartpantry.manager.database.PantryDatabaseHelper;
import com.smartpantry.manager.recipes.RecipeMatcher;
import com.smartpantry.manager.recipes.RecipeSeeder;

public class RecipesActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;
    private RecyclerView rvRecipes;
    private TextView tvRecipeCount;
    private TextView tvRecipesEmpty;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        databaseHelper = new PantryDatabaseHelper(this);

        rvRecipes = findViewById(R.id.rvSuggestedRecipes);
        tvRecipeCount = findViewById(R.id.tvRecipeCount);
        tvRecipesEmpty = findViewById(R.id.tvRecipesEmpty);

        Button btnHome = findViewById(R.id.btnRecipesHome);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        btnHome.setOnClickListener(view -> finish());

        RecipeSeeder.seedIfEmpty(databaseHelper);
        loadAvailableRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && rvRecipes != null) {
            loadAvailableRecipes();
        }
    }

    private void loadAvailableRecipes() {
        Cursor cursor = new RecipeMatcher(databaseHelper)
                .getAvailableRecipes();

        if (adapter != null) {
            rvRecipes.setAdapter(null);
            adapter.swapCursor(null);
        }

        adapter = new RecipeAdapter(cursor, recipeId -> {
            Intent intent = new Intent(
                    RecipesActivity.this,
                    RecipeDetailActivity.class
            );
            intent.putExtra("recipe_id", recipeId);
            startActivity(intent);
        });

        rvRecipes.setAdapter(adapter);

        int count = cursor.getCount();

        tvRecipeCount.setText(count == 1
                ? "1 recipe you can make"
                : count + " recipes you can make");

        tvRecipesEmpty.setVisibility(
                count == 0 ? TextView.VISIBLE : TextView.GONE);

        rvRecipes.setVisibility(
                count == 0 ? RecyclerView.GONE : RecyclerView.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (adapter != null) {
            adapter.swapCursor(null);
        }

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}
