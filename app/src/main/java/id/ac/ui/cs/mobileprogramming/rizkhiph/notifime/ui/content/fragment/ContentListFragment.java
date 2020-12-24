package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.ListContentFragmentBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.ListContentViewModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.activity.ContentClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.activity.implementation.ContentListActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.entity.ContentEntity;

public class ContentListFragment extends Fragment {
    public static final String TAG = "Content List Fragment";

    private ContentAdapter adapter;

    private ListContentFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.list_content_fragment, container, false);
        binding = ListContentFragmentBinding.inflate(inflater, container, false);

        adapter = new ContentAdapter(mContentClickCallback);
        binding.contentList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListContentViewModel viewModel = new ViewModelProvider(this).get(ListContentViewModel.class);
//        final ListContentViewModel viewModel = new ListContentViewModel(new App(), sav)
        binding.contentsSearchBtn.setOnClickListener(v -> {
            Editable query = binding.contentsSearchBox.getText();
            viewModel.setQuery(query);
        });
        subscribe(viewModel.getContents());
    }

    private void subscribe(LiveData<List<ContentEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), content -> {
            if (content != null) {
                binding.setIsLoading(false);
                adapter.setContentList(content);
            }
        });
    }
    private final ContentClickCallback mContentClickCallback = content -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((ContentListActivity) requireActivity()).show(content);
        }
    };
}
