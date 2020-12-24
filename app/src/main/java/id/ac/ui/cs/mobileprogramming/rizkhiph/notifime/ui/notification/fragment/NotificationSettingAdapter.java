package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.NotificationSettingItemBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.activity.NotificationClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.model.Notification;

public class NotificationSettingAdapter extends RecyclerView.Adapter<NotificationSettingAdapter.NotificationViewHolder> {

    Notification notificationModel;

    private NotificationClickCallback mNotificationClickCallback;

    public NotificationSettingAdapter(@Nullable NotificationClickCallback notificationClickCallback) {
        mNotificationClickCallback = notificationClickCallback;
    }

    public void setNotification(final Notification notification) {
        if (notificationModel == null) {
            notificationModel = notification;
        } else {
            notificationModel = notification;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationSettingItemBinding binding = NotificationSettingItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        binding.setCallback(mNotificationClickCallback);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.binding.setNotification(notificationModel);
        holder.binding.notifyChange();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        final NotificationSettingItemBinding binding;

        NotificationViewHolder(NotificationSettingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
