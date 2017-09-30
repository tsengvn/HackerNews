package me.hienngo.hackernews.ui.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import me.hienngo.hackernews.R;
import me.hienngo.hackernews.model.CommentModel;
import me.hienngo.hackernews.ui.base.LoadMoreListener;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> dataList;
    private LayoutInflater layoutInflater;
    private LoadMoreListener listener;
    private int padding;

    public CommentAdapter(Context context) {
        this.dataList = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
        this.padding = context.getResources().getDimensionPixelSize(R.dimen.item_padding);
    }

    public void setDataList(List<CommentModel> dataList, boolean loadMore) {
        if (!loadMore) this.dataList.clear();
        this.dataList.addAll(dataList);
        this.notifyDataSetChanged();
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentModel commentModel = dataList.get(position);
        holder.infoText.setText(commentModel.getInfo());
        holder.commentText.setText(commentModel.getComment());

        holder.itemView.setPadding(
                padding*commentModel.getLevel(),
                padding,
                padding,
                padding
        );

        if (position == dataList.size()-1 && listener != null) {
            listener.loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView infoText, commentText;

        protected ViewHolder(View itemView) {
            super(itemView);
            infoText = ButterKnife.findById(itemView, R.id.tvInfo);
            commentText = ButterKnife.findById(itemView, R.id.tvText);
        }
    }
}
