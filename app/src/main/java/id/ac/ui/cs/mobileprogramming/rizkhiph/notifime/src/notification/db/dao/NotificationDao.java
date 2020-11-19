package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;

@Dao
public interface NotificationDao {
    // Expected to be one row only
    @Query("SELECT * FROM notification")
    LiveData<List<NotificationEntity>> getAll();

    @Query("SELECT * FROM notification WHERE contentId = :contentId")
    LiveData<NotificationEntity> findOneByContent(final int contentId);

    @Query("SELECT * FROM notification WHERE contentId = :contentId")
    NotificationEntity loadHistoryNotificationSync(final int contentId);

    @Query("SELECT * FROM notification WHERE id = :notificationId")
    LiveData<NotificationEntity> findOne(final int notificationId);

    @Query("SELECT * FROM notification  "
            + "WHERE notification.title LIKE :query")
    LiveData<List<NotificationEntity>> searchNotification(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(NotificationEntity... notificationEntities);

    @Update
    public void update(NotificationEntity... notificationEntities);
}
