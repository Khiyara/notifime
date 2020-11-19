package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.R;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.NotificationDetailBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    List<? extends Notification> mNotificationList;
    private final NotificationClickCallback mNotificationClickCallback;

    public NotificationAdapter(@Nullable NotificationClickCallback notificationClickCallback) {
        this.mNotificationClickCallback = notificationClickCallback;
        setHasStableIds(false);
    }

    public void setNotificationList(final List<? extends Notification> notificationList) {
        if (mNotificationList == null) {
            mNotificationList = notificationList;
            notifyItemRangeChanged(0, notificationList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mNotificationList.size();
                }

                @Override
                public int getNewListSize() {
                    return notificationList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mNotificationList.get(oldItemPosition).getId() == notificationList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Notification newNotification = notificationList.get(newItemPosition);
                    Notification oldNotification = mNotificationList.get(oldItemPosition);
                    return newNotification.getId() == oldNotification.getId()
                            && newNotification.getMessage().equals(oldNotification.getMessage())
                            && newNotification.getTitle().equals(oldNotification.getTitle())
                            && newNotification.isNotify() == oldNotification.isNotify();
                }
            });
            mNotificationList = notificationList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.notification_detail, parent, false);
        binding.setCallback(mNotificationClickCallback);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewHolder holder, int position) {
        holder.binding.setNotification(mNotificationList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mNotificationList == null ? 0 : mNotificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        final NotificationDetailBinding binding;

        NotificationViewHolder(NotificationDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
