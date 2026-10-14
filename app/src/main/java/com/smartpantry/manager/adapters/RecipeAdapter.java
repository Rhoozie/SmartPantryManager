package com.smartpantry.manager.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.database.PantryDatabaseHelper;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(int recipeId);
    }

    private Cursor cursor;
    private final OnRecipeClickListener listener;

    public RecipeAdapter(Cursor cursor, OnRecipeClickListener listener) {
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
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder, int position) {

        if (cursor == null || cursor.isClosed()
                || !cursor.moveToPosition(position)) {
            return;
        }

        int recipeId = cursor.getInt(
                cursor.getColumnIndexOrThrow(
                        PantryDatabaseHelper.COL_RECIPE_ID));

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        PantryDatabaseHelper.COL_RECIPE_NAME));

        String description = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        PantryDatabaseHelper.COL_DESCRIPTION));

        holder.tvName.setText(name);
        holder.tvDescription.setText(description);

        holder.itemView.setOnClickListener(
                view -> listener.onRecipeClick(recipeId));
    }

    @Override
    public int getItemCount() {
        return cursor == null || cursor.isClosed()
                ? 0 : cursor.getCount();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDescription;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvDescription = itemView.findViewById(R.id.tvRecipeDescription);
        }
    }
}
