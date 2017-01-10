package io.github.kbiakov.newsreader.screens.home;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.github.kbiakov.newsreader.R;
import io.github.kbiakov.newsreader.models.entities.Source;

class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder>{

    private List<Source> mSources;
    private OnSourceClickListener mListener;

    SourcesAdapter(List<Source> data, OnSourceClickListener listener) {
        this.mSources = data;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_source, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Source item = mSources.get(position);
        holder.mItem = item;

        Uri uri = Uri.parse(item.getImageUrl());
        holder.sdvCover.setImageURI(uri);

        holder.tvName.setText(item.name);

        holder.mView.setOnClickListener(v ->
                mListener.onSourceClicked(item.id)
        );
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    public List<Source> getData() {
        return mSources;
    }

    public void setData(List<Source> data) {
        this.mSources = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvCover;
        TextView tvName;

        final View mView;
        Source mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            sdvCover = (SimpleDraweeView) view.findViewById(R.id.sdv_cover);
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }

        @Override
        public String toString() {
            return String.format("%s %s", super.toString(), tvName.getText());
        }
    }

    interface OnSourceClickListener {
        void onSourceClicked(String sourceId);
    }
}
