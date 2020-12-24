package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.search.db.entity.SearchHistoryEntity;

@Dao
public interface SearchHistoryDao {
    @Query("SELECT * FROM search_history")
    LiveData<List<SearchHistoryEntity>> getAll();

    @Query("SELECT * FROM search_history  "
            + "WHERE search_history.search_query LIKE :query")
    LiveData<List<SearchHistoryEntity>> searchHistory(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(SearchHistoryEntity... searchHistoryEntities);

    @Delete
    public void delete(SearchHistoryEntity... searchHistoryEntities);
}
