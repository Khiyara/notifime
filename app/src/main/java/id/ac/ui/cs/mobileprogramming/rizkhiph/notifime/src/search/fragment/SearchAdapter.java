package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.SearchHistoryDetailBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.activity.SearchHistoryClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.model.SearchHistory;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.HistoryViewHolder> {

    List<? extends SearchHistory> mSearchHistoryList;
    private final SearchHistoryClickCallback mSearchHistoryClickCallback;

    public SearchAdapter(@Nullable SearchHistoryClickCallback clickCallback) {
        this.mSearchHistoryClickCallback = clickCallback;
    }

    public void setSearchHistoryList(final List<? extends SearchHistory> searchHistories) {
        if (mSearchHistoryList == null) {
            mSearchHistoryList = searchHistories;
            notifyItemRangeChanged(0, searchHistories.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return searchHistories.size();
                }

                @Override
                public int getNewListSize() {
                    return searchHistories.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return searchHistories.get(oldItemPosition).getId() == searchHistories.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    SearchHistory newSearchHistories = searchHistories.get(newItemPosition);
                    SearchHistory oldSearchHistories = searchHistories.get(oldItemPosition);
                    return newSearchHistories.getId() == oldSearchHistories.getId()
                            && newSearchHistories.getQuery().equals(oldSearchHistories.getQuery());
                }
            });
            mSearchHistoryList = searchHistories;
            result.dispatchUpdatesTo(this);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchHistoryDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.search_history_detail, parent, false);
        binding.setCallback(mSearchHistoryClickCallback);

        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.binding.setSearchHistory(mSearchHistoryList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSearchHistoryList == null ? 0 : mSearchHistoryList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        final SearchHistoryDetailBinding binding;

        HistoryViewHolder(SearchHistoryDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
