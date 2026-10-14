package com.smartpantry.manager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.adapters.PantryAdapter;
import com.smartpantry.manager.database.PantryDatabaseHelper;

public class PantryActivity extends AppCompatActivity {

    private PantryDatabaseHelper databaseHelper;
    private RecyclerView rvIngredients;
    private TextView tvPantryCount;
    private TextView tvPantryEmpty;
    private PantryAdapter adapter;

    private final ActivityResultLauncher<Intent> ingredientLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> loadIngredients()
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        databaseHelper = new PantryDatabaseHelper(this);

        rvIngredients = findViewById(R.id.rvPantryIngredients);
        tvPantryCount = findViewById(R.id.tvPantryCount);
        tvPantryEmpty = findViewById(R.id.tvPantryEmpty);

        Button btnAdd = findViewById(R.id.btnAddIngredient);
        Button btnHome = findViewById(R.id.btnPantryHome);

        rvIngredients.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddIngredientActivity.class);
            ingredientLauncher.launch(intent);
        });

        btnHome.setOnClickListener(view -> finish());

        loadIngredients();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    private void loadIngredients() {
        if (databaseHelper == null || rvIngredients == null) {
            return;
        }

        Cursor cursor = databaseHelper.getAllIngredients();

        if (adapter != null) {
            rvIngredients.setAdapter(null);
            adapter.swapCursor(null);
        }

        adapter = new PantryAdapter(cursor,
                new PantryAdapter.OnIngredientActionListener() {
                    @Override
                    public void onEdit(int id, String name, double quantity,
                                       String unit, String expiryDate) {
                        Intent intent = new Intent(
                                PantryActivity.this,
                                AddIngredientActivity.class
                        );

                        intent.putExtra(AddIngredientActivity.EXTRA_ID, id);
                        intent.putExtra(AddIngredientActivity.EXTRA_NAME, name);
                        intent.putExtra(AddIngredientActivity.EXTRA_QUANTITY, quantity);
                        intent.putExtra(AddIngredientActivity.EXTRA_UNIT, unit);
                        intent.putExtra(AddIngredientActivity.EXTRA_EXPIRY, expiryDate);

                        ingredientLauncher.launch(intent);
                    }

                    @Override
                    public void onDelete(int id, String name) {
                        confirmDelete(id, name);
                    }
                });

        rvIngredients.setAdapter(adapter);

        int count = cursor.getCount();
        tvPantryCount.setText(count == 1
                ? "1 ingredient in your pantry"
                : count + " ingredients in your pantry");

        tvPantryEmpty.setVisibility(
                count == 0 ? TextView.VISIBLE : TextView.GONE);

        rvIngredients.setVisibility(
                count == 0 ? RecyclerView.GONE : RecyclerView.VISIBLE);
    }

    private void confirmDelete(int id, String name) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + name + " from your pantry?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Delete", (dialog, which) -> {
                    int deleted = databaseHelper.deleteIngredient(id);

                    if (deleted > 0) {
                        Toast.makeText(this, "Ingredient deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Could not delete ingredient",
                                Toast.LENGTH_SHORT).show();
                    }

                    loadIngredients();
                })
                .show();
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
