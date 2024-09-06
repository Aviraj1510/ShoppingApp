package com.example.shoppinglistjava.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<ShoppingItem> shoppingItems = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(ShoppingItem item);
        void onPlusClick(ShoppingItem item);
        void onMinusClick(ShoppingItem item);
    }

    public ShoppingListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        ShoppingItem currentItem = shoppingItems.get(position);
        holder.bind(currentItem, listener);

    }

    public int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        colorCode.add(R.color.color8);
        colorCode.add(R.color.color9);

        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);

    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }
    public void setShoppingItems(List<ShoppingItem> shoppingItems) {
        this.shoppingItems = shoppingItems;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvAmount;
        ImageView ivDelete,ivPlus, ivMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            ivMinus = itemView.findViewById(R.id.ivMinus);



        }

        public void bind(final ShoppingItem item, final OnItemClickListener listener) {
            tvName.setText(item.getName());
            tvAmount.setText(String.valueOf(item.getAmount()));

            ivDelete.setOnClickListener(v -> listener.onDeleteClick(item));
            ivPlus.setOnClickListener(v -> listener.onPlusClick(item));
            ivMinus.setOnClickListener(v -> listener.onMinusClick(item));
        }
    }
}
