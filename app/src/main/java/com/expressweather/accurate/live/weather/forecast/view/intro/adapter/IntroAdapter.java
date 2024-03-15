package com.expressweather.accurate.live.weather.forecast.view.intro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.expressweather.accurate.live.weather.forecast.R;

import java.util.List;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.SlideViewHolder> {
    private final List<Integer> listSlide;

    public IntroAdapter(List<Integer> imageList) {
        this.listSlide = imageList;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_image_slide, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.imageViewLayer1.setImageResource(listSlide.get(position));
    }

    @Override
    public int getItemCount() {
        return listSlide.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewLayer1;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLayer1 = itemView.findViewById(R.id.img_layer_1);
        }
    }
}
