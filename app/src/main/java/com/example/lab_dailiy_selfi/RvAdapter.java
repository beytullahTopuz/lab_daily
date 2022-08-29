package com.example.lab_dailiy_selfi;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    private List<Bitmap> imagelist;
    private List<String> imageTextList;

    public RvAdapter(List<Bitmap> imagelist, List<String> imageTextList) {
        this.imagelist = imagelist;
        this.imageTextList = imageTextList;
    }

    @NonNull
    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RvAdapter.ViewHolder holder, int position) {
        Bitmap currentBitmap = imagelist.get(position);

        String currentText= imageTextList.get(position);

        if (currentBitmap != null){
            holder.imageView.setImageBitmap(currentBitmap);
            holder.textView.setText(currentText.toString());
        }

    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image_view);
            textView = itemView.findViewById(R.id.item_text_view);
        }
    }
}
