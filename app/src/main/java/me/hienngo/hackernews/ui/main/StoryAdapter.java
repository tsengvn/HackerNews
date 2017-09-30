package me.hienngo.hackernews.ui.main;

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
import me.hienngo.hackernews.model.StoryModel;
import me.hienngo.hackernews.ui.base.ItemClickListener;
import me.hienngo.hackernews.ui.base.LoadMoreListener;

/**
 * @author hienngo
 * @since 9/30/17
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private List<StoryModel> dataList;
    private LayoutInflater layoutInflater;
    private LoadMoreListener listener;
    private ItemClickListener clickListener;

    public StoryAdapter(Context context) {
        this.dataList = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listener = listener;
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setDataList(List<StoryModel> dataList, boolean isLoadingMore) {
        if (!isLoadingMore) this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_story, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final StoryModel item = dataList.get(position);
        holder.titleView.setText(item.getTitle());
        holder.infoView.setText(item.getInfo());
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null)
                clickListener.onItemClicked(item.getItemId(), item.getTitle());
        });

        if (position == dataList.size()-1) {
            loadMore();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void loadMore() {
        if (listener != null) {
            listener.loadMore();
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView, infoView;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = ButterKnife.findById(itemView, R.id.tvTitle);
            infoView = ButterKnife.findById(itemView, R.id.tvInfo);

        }
    }

}
