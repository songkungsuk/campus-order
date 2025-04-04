package com.example.campus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
        implements OnProductItemClickListener {
    ArrayList<Product> items = new ArrayList<Product>();

    OnProductItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.product_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Product item) {
        items.add(item);
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    public Product getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Product item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnProductItemClickListener listener) {
        this.listener = listener;
    }
    public void init_item(){
        items.clear();
    }
    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView2;
        TextView textView3;
        TextView textView4;

        ImageView imageView1;

        public ViewHolder(View itemView, final OnProductItemClickListener listener) {
            super(itemView);


            textView2 = itemView.findViewById(R.id.textView8);
            textView3 = itemView.findViewById(R.id.textView9);
            textView4 = itemView.findViewById(R.id.textView10);

            imageView1 = itemView.findViewById(R.id.imageView5);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(Product item) {
            textView2.setText(item.getName());
            textView4.setText(String.valueOf(item.getPrice()));
            imageView1.setImageResource(item.getImageRes());
        }

    }

}
