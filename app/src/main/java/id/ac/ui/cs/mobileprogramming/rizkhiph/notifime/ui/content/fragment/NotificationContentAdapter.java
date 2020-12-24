package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.NotificationItemBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.model.Content;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.activity.NotificationClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.model.Notification;

public class NotificationContentAdapter extends RecyclerView.Adapter<NotificationContentAdapter.NotificationViewHolder> {

    Notification notificationModel;
    Content contentModel;

    private NotificationClickCallback mNotificationClickCallback;

    public NotificationContentAdapter(@Nullable NotificationClickCallback notificationClickCallback) {
        mNotificationClickCallback = notificationClickCallback;
    }

    public void setNotification(final Notification notification) {
        if (notificationModel == null) {
            notificationModel = notification;
            notifyItemRangeChanged(0, 1);
        } else {
            notificationModel = notification;
        }
    }

    public void setContent(final Content content) {
        if (contentModel == null) {
            contentModel = content;
            notifyItemRangeChanged(0, 1);
        } else {
            contentModel = content;
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotificationItemBinding binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        binding.setCallback(mNotificationClickCallback);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.binding.setNotification(notificationModel);
        holder.binding.setContent(contentModel);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

//    public void setModel(DataRepository repository, Content content) {
//        LiveData<NotificationEntity> notification = repository.loadHistoryNotification(content.getId());
//        model =  notification;
//    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        final NotificationItemBinding binding;

        NotificationViewHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
