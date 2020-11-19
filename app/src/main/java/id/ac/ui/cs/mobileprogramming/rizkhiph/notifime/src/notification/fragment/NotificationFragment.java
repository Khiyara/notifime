package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.NotificationFragmentBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.NotificationSettingViewModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.implementation.NotificationActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationFragment extends Fragment {
    private static final String KEY_NOTIFICATION_ID = "notification_id";

    private NotificationFragmentBinding binding;

    private NotificationSettingAdapter mNotificationSettingAdapter;

    private final NotificationClickCallback mNotificationClickCallback = notification -> {
        NotificationEntity notificationEntity;
        NotificationActivity activity = (NotificationActivity) requireActivity();
        if (notification.isNotify()) {
            activity.cancelNotification(notification);
            notificationEntity = activity.updateNotification(notification);
        } else {
            activity.onPushNotification(notification);
            notificationEntity = activity.updateNotification(notification);
        }
        mNotificationSettingAdapter.setNotification(notificationEntity);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        binding = NotificationFragmentBinding.inflate(inflater, container, false);

        mNotificationSettingAdapter = new NotificationSettingAdapter(mNotificationClickCallback);
        binding.notificationSetting.setAdapter(mNotificationSettingAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NotificationSettingViewModel.Factory factory = new NotificationSettingViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getInt(KEY_NOTIFICATION_ID)
        );

        final NotificationSettingViewModel model = new ViewModelProvider(this, factory).get(NotificationSettingViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setNotificationSettingModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final NotificationSettingViewModel model) {
        model.getNotification().observe(getViewLifecycleOwner(), notification -> {
           if (notification != null) {
               binding.setIsLoading(false);
               mNotificationSettingAdapter.setNotification(notification);
           } else {
               binding.setIsLoading(true);
           }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static NotificationFragment forNotification(int id) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_NOTIFICATION_ID, id);
        fragment.setArguments(args);

        return fragment;
    }
}
