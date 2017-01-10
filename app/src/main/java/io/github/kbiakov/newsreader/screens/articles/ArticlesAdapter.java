package io.github.kbiakov.newsreader.screens.articles;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.github.kbiakov.newsreader.R;
import io.github.kbiakov.newsreader.models.entities.Article;

class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder>{

    private List<Article> mArticles;
    private OnArticleClickListener mListener;

    ArticlesAdapter(List<Article> data, ArticlesAdapter.OnArticleClickListener listener) {
        this.mArticles = data;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Article item = mArticles.get(position);
        holder.mArticle = item;

        Uri uri = Uri.parse(item.urlToImage);
        holder.sdvCover.setImageURI(uri);
        holder.ivNoPhoto.setVisibility(View.GONE);

        holder.tvAuthor.setText(item.author);
        holder.tvAuthor.setVisibility(item.author == null ? View.GONE : View.VISIBLE);

        holder.tvTitle.setText(item.title);
        holder.tvDescription.setText(item.description);

        holder.mView.setOnClickListener(v ->
                mListener.onArticleClicked(item.url)
        );
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public List<Article> getData() {
        return mArticles;
    }

    public void setData(List<Article> data) {
        this.mArticles = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvCover;
        ImageView ivNoPhoto;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvDescription;

        final View mView;
        Article mArticle;

        ViewHolder(View view) {
            super(view);
            mView = view;
            sdvCover = (SimpleDraweeView) view.findViewById(R.id.sdv_cover);
            ivNoPhoto = (ImageView) view.findViewById(R.id.iv_no_photo);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvDescription = (TextView) view.findViewById(R.id.tv_description);
        }

        @Override
        public String toString() {
            return String.format("%s %s", super.toString(), tvTitle.getText());
        }
    }

    interface OnArticleClickListener {
        void onArticleClicked(String articleUrl);
    }
}