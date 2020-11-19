package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.ContentItemBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.activity.ContentClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.model.Content;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    List<? extends Content> mContentList;
    private final ContentClickCallback mContentClickCallback;

    public ContentAdapter(@Nullable ContentClickCallback contentClickCallback) {
        mContentClickCallback = contentClickCallback;
        setHasStableIds(true);
    }

    public void setContentList(final List<? extends Content> contentList) {
        if (mContentList == null) {
            mContentList = contentList;
            notifyItemRangeChanged(0, contentList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mContentList.size();
                }

                @Override
                public int getNewListSize() {
                    return contentList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mContentList.get(oldItemPosition).getId() == contentList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Content newContent = contentList.get(newItemPosition);
                    Content oldContent = mContentList.get(oldItemPosition);
                    return newContent.getId() == oldContent.getId()
                            && newContent.getDate().equals(oldContent.getDate())
                            && newContent.getTitle().equals(oldContent.getTitle())
                            && newContent.getTime().equals(oldContent.getTime());
                }
            });
            mContentList = contentList;
            result.dispatchUpdatesTo(this);
        }
    }
//
//    ContentAdapter(@Nullable ContentClickCallback contentClickCallback) {
//        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<ContentEntity>() {
//            @Override
//            public boolean areItemsTheSame(@NonNull ContentEntity oldItem, @NonNull ContentEntity newItem) {
//                return oldItem.getId() == newItem.getId();
//            }
//
//            @Override
//            public boolean areContentsTheSame(@NonNull ContentEntity oldItem, @NonNull ContentEntity newItem) {
//                return oldItem.getId() == newItem.getId()
//                        && oldItem.getTime().equals(newItem.getTime())
//                        && oldItem.getTitle().equals(newItem.getTitle())
//                        && oldItem.getDate().equals(newItem.getDate());
//            }
//        }).build());
//        mContentClickCallback = contentClickCallback;
//    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.content_item, parent, false);
        binding.setCallback(mContentClickCallback);

        return new ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.ContentViewHolder holder, int position) {
        holder.binding.setContent(mContentList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mContentList == null ? 0 : mContentList.size();
    }

    @Override
    public long getItemId(int position) {
        return mContentList.get(position).getId();
    }

    public Content getContent(int position) {
        return mContentList.get(position);
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        final ContentItemBinding binding;

        ContentViewHolder(ContentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
