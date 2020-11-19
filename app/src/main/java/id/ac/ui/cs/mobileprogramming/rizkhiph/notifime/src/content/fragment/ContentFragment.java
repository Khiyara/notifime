package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.ContentFragmentBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.ContentViewModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.activity.NotificationClickCallback;

public class ContentFragment extends Fragment {
    private static final String KEY_CONTENT_ID = "content_id";

    private ContentFragmentBinding binding;

    private NotificationContentAdapter notificationAdapter;

    private final NotificationClickCallback mNotificationClickCallback = notification -> {
        // no-op
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ContentFragmentBinding.inflate(inflater, container, false);

        notificationAdapter = new NotificationContentAdapter(mNotificationClickCallback);
        binding.contentNotification.setAdapter(notificationAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ContentViewModel.Factory factory = new ContentViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getInt(KEY_CONTENT_ID)
        );

        final ContentViewModel model = new ViewModelProvider(this, factory).get(ContentViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setContentViewModel(model);

        subscribeToModel(model);
    }

    public void subscribeToModel(final ContentViewModel model) {
        model.getHistoryNotification().observe(getViewLifecycleOwner(), notification -> {
            if (notification != null) {
                notificationAdapter.setNotification(notification);
            }
        });
        model.getContents().observe(getViewLifecycleOwner(), content -> {
            if (content != null) {
                binding.setIsLoading(false);
                notificationAdapter.setContent(content);
            } else {
                binding.setIsLoading(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static ContentFragment forContent(int id) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CONTENT_ID, id);
        fragment.setArguments(args);

        return fragment;
    }
}
