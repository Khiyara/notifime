package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.entity.ContentEntity;

@Dao
public interface ContentDao  {
    @Query("SELECT * FROM content")
    LiveData<List<ContentEntity>> getAll();

    @Query("SELECT content.id, content_title, content_time, content_date, " +
            "content_airing, content_favorite, " +
            "content.created_at, content.updated_at " +
            "FROM content INNER JOIN notification ON notification.contentId = content.id")
    LiveData<List<ContentEntity>> fetchNotification();

    @Query("SELECT * FROM content where id = :contentId")
    LiveData<ContentEntity> findOne(int contentId);

    @Query("SELECT * FROM content where id = :contentId")
    ContentEntity loadContentSync(int contentId);

    @Query("SELECT * FROM content  "
            + "WHERE content.content_title LIKE :query")
    LiveData<List<ContentEntity>> searchContent(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(ContentEntity... contents);

}
