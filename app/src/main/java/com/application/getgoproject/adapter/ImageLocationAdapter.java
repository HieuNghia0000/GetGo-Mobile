package com.application.getgoproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.getgoproject.R;
import com.application.getgoproject.models.ImageLocation;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageLocationAdapter extends RecyclerView.Adapter<ImageLocationAdapter.ViewHolder> {
    private Context context;
    private int layout;
    private List<ImageLocation> imageList;


    public ImageLocationAdapter(Context context, int layout, List<ImageLocation> imageList) {
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_img_location, parent, false);
        return new ImageLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageLocation image = imageList.get(position);
        String images = image.getListImage();

        if (images != null && !images.isEmpty()) {
            Glide.with(context)
                    .load(images)
                    .placeholder(R.drawable.sapa) // Default image while loading
                    .error(R.drawable.sapa) // Default image if error occurs
                    .into(holder.imgLocation);
        } else {
            holder.imgLocation.setImageResource(R.drawable.sapa);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các view trong item layout
        ImageView imgLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
            imgLocation = itemView.findViewById(R.id.imgLocation);
        }
    }
}