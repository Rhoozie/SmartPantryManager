package com.smartpantry.manager.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryDatabaseHelper;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.IngredientViewHolder> {

    public interface OnIngredientActionListener {
        void onEdit(int id, String name, double quantity,
                    String unit, String expiryDate);

        void onDelete(int id, String name);
    }

    private Cursor cursor;
    private final OnIngredientActionListener listener;

    public PantryAdapter(Cursor cursor, OnIngredientActionListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor == newCursor) {
            return;
        }

        Cursor oldCursor = cursor;
        cursor = newCursor;
        notifyDataSetChanged();

        if (oldCursor != null && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder, int position) {

        if (cursor == null || cursor.isClosed()
                || !cursor.moveToPosition(position)) {
            return;
        }

        int id = cursor.getInt(
                cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_ID));

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_NAME));

        double quantity = cursor.getDouble(
                cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_QUANTITY));

        String unit = cursor.getString(
                cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_UNIT));

        String expiryDate = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        PantryDatabaseHelper.COL_EXPIRY_DATE));

        holder.tvName.setText(name);
        holder.tvQuantity.setText(formatQuantity(quantity) + " " + unit);

        holder.tvExpiry.setText(
                expiryDate == null || expiryDate.trim().isEmpty()
                        ? "Expiry: Not provided"
                        : "Expiry: " + expiryDate);

        holder.btnEdit.setOnClickListener(view ->
                listener.onEdit(id, name, quantity, unit, expiryDate));

        holder.btnDelete.setOnClickListener(view ->
                listener.onDelete(id, name));
    }

    @Override
    public int getItemCount() {
        return cursor == null || cursor.isClosed() ? 0 : cursor.getCount();
    }

    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)
                && quantity >= Long.MIN_VALUE
                && quantity <= Long.MAX_VALUE) {
            return String.valueOf((long) quantity);
        }

        return String.valueOf(quantity);
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvQuantity;
        TextView tvExpiry;
        Button btnEdit;
        Button btnDelete;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
            tvExpiry = itemView.findViewById(R.id.tvIngredientExpiry);
            btnEdit = itemView.findViewById(R.id.btnEditIngredient);
            btnDelete = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
