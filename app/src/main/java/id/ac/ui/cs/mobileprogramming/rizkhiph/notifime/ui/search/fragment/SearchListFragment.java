package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.fragment;

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

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.databinding.ListSearchHistoryFragmentBinding;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.ListSearchHistoryViewModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.SearchHistoryClickCallback;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.activity.implementation.HistoryActivity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.entity.SearchHistoryEntity;

public class SearchListFragment extends Fragment {
    public static final String TAG = "Search List Fragment";

    private SearchAdapter adapter;

    private ListSearchHistoryFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        binding = ListSearchHistoryFragmentBinding.inflate(inflater, container, false);

        adapter = new SearchAdapter(mSearchHistoryClickCallback);
        binding.searchHistoryList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListSearchHistoryViewModel viewModel = new ViewModelProvider(this).get(ListSearchHistoryViewModel.class);

        binding.searchHistorySearchBtn.setOnClickListener(v -> {
            Editable query = binding.searchHistorySearchBox.getText();
            viewModel.setQuery(query);
        });
        subscribe(viewModel.getSearchHistories());
    }

    private void subscribe(LiveData<List<SearchHistoryEntity>> liveData) {
        liveData.observe(getViewLifecycleOwner(), searchHistory -> {
            if (searchHistory != null) {
                binding.setIsLoading(false);
                adapter.setSearchHistoryList(searchHistory);
            }
        });
    }

    private final SearchHistoryClickCallback mSearchHistoryClickCallback = searchHistory -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((HistoryActivity) requireActivity()).removeHistory(searchHistory);
        }
    };
}
