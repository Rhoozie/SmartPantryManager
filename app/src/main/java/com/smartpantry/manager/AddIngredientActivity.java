package com.smartpantry.manager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smartpantry.manager.database.PantryDatabaseHelper;

public class AddIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "ingredient_id";
    public static final String EXTRA_NAME = "ingredient_name";
    public static final String EXTRA_QUANTITY = "ingredient_quantity";
    public static final String EXTRA_UNIT = "ingredient_unit";
    public static final String EXTRA_EXPIRY = "ingredient_expiry";

    private EditText etName;
    private EditText etQuantity;
    private EditText etExpiry;
    private Spinner spUnit;
    private PantryDatabaseHelper databaseHelper;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        databaseHelper = new PantryDatabaseHelper(this);

        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etIngredientQuantity);
        etExpiry = findViewById(R.id.etIngredientExpiry);
        spUnit = findViewById(R.id.spIngredientUnit);

        Button btnSave = findViewById(R.id.btnSaveIngredient);
        Button btnCancel = findViewById(R.id.btnCancelIngredient);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.ingredient_units,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spUnit.setAdapter(adapter);

        ingredientId = getIntent().getIntExtra(EXTRA_ID, -1);

        if (ingredientId != -1) {
            setTitle("Edit Ingredient");
            btnSave.setText("Update Ingredient");
            loadIngredientDetails();
        } else {
            setTitle("Add Ingredient");
        }

        btnSave.setOnClickListener(view -> saveIngredient());
        btnCancel.setOnClickListener(view -> finish());
    }

    private void loadIngredientDetails() {
        etName.setText(getIntent().getStringExtra(EXTRA_NAME));

        double quantity = getIntent().getDoubleExtra(EXTRA_QUANTITY, 0);
        etQuantity.setText(String.valueOf(quantity));

        String expiry = getIntent().getStringExtra(EXTRA_EXPIRY);
        etExpiry.setText(expiry == null ? "" : expiry);

        String unit = getIntent().getStringExtra(EXTRA_UNIT);
        if (unit != null) {
            for (int i = 0; i < spUnit.getCount(); i++) {
                if (spUnit.getItemAtPosition(i).toString().equalsIgnoreCase(unit)) {
                    spUnit.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spUnit.getSelectedItem().toString();
        String expiryDate = etExpiry.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            etName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {
            etQuantity.setError("Quantity must be greater than zero");
            etQuantity.requestFocus();
            return;
        }

        if (!expiryDate.isEmpty()
                && !expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etExpiry.setError("Use the format YYYY-MM-DD");
            etExpiry.requestFocus();
            return;
        }

        try {
            if (ingredientId == -1) {
                long result = databaseHelper.addIngredient(
                        name, quantity, unit, expiryDate
                );

                if (result != -1) {
                    showSuccess("Ingredient added successfully");
                } else {
                    showFailure();
                }
            } else {
                int result = databaseHelper.updateIngredient(
                        ingredientId, name, quantity, unit, expiryDate
                );

                if (result > 0) {
                    showSuccess("Ingredient updated successfully");
                } else {
                    Toast.makeText(this,
                            "No changes were saved. Please check the pantry.",
                            Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this,
                    "Database error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void showFailure() {
        Toast.makeText(this,
                "Could not save the ingredient. Please try again.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}
