package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.ListNotificationFragmentBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.ListNotificationViewModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.implementation.NotificationActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class    NotificationListFragment extends Fragment {
    public static final String TAG = "Notification List Fragment";

    private NotificationAdapter adapter;

    private ListNotificationFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        binding = ListNotificationFragmentBinding.inflate(inflater, container, false);

        adapter = new NotificationAdapter(mNotificationClickCallback);
        binding.notificationList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListNotificationViewModel viewModel = new ViewModelProvider(this).get(ListNotificationViewModel.class);

        binding.notificationSearchBtn.setOnClickListener(v -> {
            Editable query = binding.notificationSearchBox.getText();
            viewModel.setQuery(query);
        });
        subscribe(viewModel.getNotifications());
    }

    private void subscribe(LiveData<List<NotificationEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), notification -> {
            if (notification != null) {
                binding.setIsLoading(false);
                adapter.setNotificationList(notification);
            }
        });
    }

    private final NotificationClickCallback mNotificationClickCallback = notification -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((NotificationActivity) requireActivity()).changeDetailFragment(notification);
        }
    };
}
