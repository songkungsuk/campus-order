package com.example.campus;

import android.view.View;

public interface OnProductItemClickListener {
    void onItemClick(ProductAdapter.ViewHolder holder, View view, int position);
}