package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.model.BaseModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.model.SearchHistory;

@Entity(tableName = "search_history")
public class SearchHistoryEntity extends BaseModel implements SearchHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "search_query")
    private String query;

    public SearchHistoryEntity(String query) {
        this.query = query;
    }

    public int getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
