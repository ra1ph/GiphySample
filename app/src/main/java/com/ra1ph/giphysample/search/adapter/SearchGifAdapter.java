package com.ra1ph.giphysample.search.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ra1ph.giphysample.R;
import com.ra1ph.giphysample.search.domain.GifModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mikhail Korshunov on 10.08.17.
 */

public class SearchGifAdapter extends RecyclerView.Adapter<SearchGifAdapter.GifViewholder> {
    private Picasso picasso;
    private OnGifClickListener listener;
    @Nullable
    private List<GifModel> items;

    public SearchGifAdapter(Picasso picasso, OnGifClickListener listener) {
        this.picasso = picasso;
        this.listener = listener;
    }

    @Override
    public SearchGifAdapter.GifViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchGifAdapter.GifViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_gif_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchGifAdapter.GifViewholder holder, int position) {
        if (items == null) {
            throw new NullPointerException("Unable create view from empty items list");
        }
        if (position < 0 || position >= items.size()) {
            throw new IndexOutOfBoundsException("Position must be in items list boundaries");
        }
        GifModel model = items.get(position);
        holder.setup(model, picasso, listener);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void replaceItems(@Nullable List<GifModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class GifViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_gif_item_image)
        ImageView previewImage;

        GifViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setup(GifModel gifModel, Picasso picasso, OnGifClickListener clickListener) {
            picasso.cancelRequest(previewImage);
            picasso.load(gifModel.getPreviewUrl())
                    .into(previewImage);

            if (clickListener != null) {
                itemView.setOnClickListener(__ -> clickListener.onGifClick(gifModel));
            }
        }
    }

    public interface OnGifClickListener {
        void onGifClick(GifModel gifModel);
    }
}
