package com.example.shoppinglistjava.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Ignore;

import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categoryList = new ArrayList<>();
    private OnItemClickListener listener;

    @Ignore
    public CategoryAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener{
        void onItemClick(Category category);
        void deleteCategory(Category category);
    }


    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category  =  categoryList.get(position);
        holder.bind(category, listener);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory;
        ImageView btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textCategory = itemView.findViewById(R.id.categoryName);
            btnDelete = itemView.findViewById(R.id.categoryDelete);

        }

        public void bind(final Category item, final OnItemClickListener listener){
            textCategory.setText(item.getCategoryName());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    Log.d("CategoryAdapter", "Category item clicked: " + item.getCategoryName());
                    listener.onItemClick(item); // Pass category object to listener
                }
            });
            btnDelete.setOnClickListener(v -> listener.deleteCategory(item));
        }
    }
}
