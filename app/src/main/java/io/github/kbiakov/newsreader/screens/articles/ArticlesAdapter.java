package io.github.kbiakov.newsreader.screens.articles;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kbiakov.newsreader.R;
import io.github.kbiakov.newsreader.models.Article;

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
        holder.mItem = item;

        Uri uri = Uri.parse(item.urlToImage);
        holder.sdvCover.setImageURI(uri);

        holder.tvTitle.setText(item.title);
        holder.tvAuthor.setText(item.author);
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
        @BindView(R.id.sdv_cover) SimpleDraweeView sdvCover;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_description) TextView tvDescription;

        final View mView;
        Article mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
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