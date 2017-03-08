package com.defaultapps.producthuntviewer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.defaultapps.producthuntviewer.R;
import com.defaultapps.producthuntviewer.data.model.post.Post;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context context;
    private List<Post> postList;


    private Listener listener;


    public interface Listener {
        void onProductClick(int position);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.productItem)
        RelativeLayout productItem;

        @BindView(R.id.productName)
        TextView productName;

        @BindView(R.id.productDescription)
        TextView productDescription;

        @BindView(R.id.productUpVotes)
        IconTextView productUpVotes;

        @BindView(R.id.productThumbnail)
        ImageView productThumbnail;


        ProductViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Inject
    public ProductsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        Post post = postList.get(holder.getAdapterPosition());
        holder.productItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onProductClick(holder.getAdapterPosition());
            }
        });
        Glide
                .with(context)
                .load(post.getThumbnail().getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.place_holder)
                .animate(android.R.anim.fade_in)
                .fitCenter()
                .into(holder.productThumbnail);
        holder.productName.setText(post.getName());
        holder.productDescription.setText(post.getTagline());
        holder.productUpVotes.setText("{md-thumb-up} " + post.getVotesCount().toString());
    }

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    public void setData(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
