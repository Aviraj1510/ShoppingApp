package com.example.shoppinglistjava.Adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCategoryListAdapter extends RecyclerView.Adapter<ShoppingCategoryListAdapter.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;


    private List<ShoppingCList> shoppingCLists = new ArrayList<>();
    private OnItemClickListener listener;

    public ShoppingCategoryListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }


    // Interface to handle item clicks and delete functionality
    public interface OnItemClickListener {
        void onPlusClick(ShoppingCList shoppingCList);
        void onMinusClick(ShoppingCList shoppingCList);
        void onDeleteClick(ShoppingCList shoppingCList);
        void onTotalRupee(ShoppingCList shoppingCList);
    }

    

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCList shoppingCList = shoppingCLists.get(position);
        holder.bind(shoppingCList, listener);
    }

    @Override
    public int getItemCount() {
        return shoppingCLists.size();
    }

    public void setShoppingItemList(List<ShoppingCList> shoppingCLists) {
        this.shoppingCLists = shoppingCLists;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName, textItemQuantity, textItemRupee, textPerProductRupee;
        ImageView btDelete, btPlus, btMinus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textItemName = itemView.findViewById(R.id.tvCName);
            textItemQuantity = itemView.findViewById(R.id.tvCAmount);
            textItemRupee =  itemView.findViewById(R.id.tvCRupee);
            textPerProductRupee = itemView.findViewById(R.id.Cperproductrupee);
            btDelete =  itemView.findViewById(R.id.ivCDelete);
            btPlus = itemView.findViewById(R.id.ivCPlus);
            btMinus = itemView.findViewById(R.id.ivCMinus);

        }

        public void bind(final ShoppingCList shoppingCList, final OnItemClickListener listener) {
            textItemName.setText(shoppingCList.getCname());
            textItemQuantity.setText(String.valueOf(shoppingCList.getCamount()));
            textItemRupee.setText(String.valueOf(shoppingCList.getCtotalrupees()));
            textPerProductRupee.setText(String.valueOf(shoppingCList.getCrupees()));

            btDelete.setOnClickListener(v -> listener.onDeleteClick(shoppingCList));
            btPlus.setOnClickListener(v -> listener.onPlusClick(shoppingCList));
            btMinus.setOnClickListener(v -> listener.onMinusClick(shoppingCList));
        }
    }
}

