package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.model.BaseModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.notification.model.Notification;

@Entity(tableName = "notification", foreignKeys = @ForeignKey(entity = ContentEntity.class, parentColumns = "id", childColumns = "contentId"))
public class NotificationEntity extends BaseModel implements Notification {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo
    public int contentId;

    @ColumnInfo(name = "notify")
    public boolean notify;

    @ColumnInfo(name = " text")
    public String text;

    @Ignore
    public NotificationEntity(String title, String message, int contentId, String text) {
        this(title, message, contentId, text,true);
    }

    @Ignore
    public NotificationEntity(int id, String title, String message, int contentId, String text, boolean notify) {
        this(title, message, contentId, text, notify);
        this.id = id;
    }

    public NotificationEntity(String title, String message, int contentId, String text, boolean notify) {
        super();
        this.title = title;
        this.message = message;
        this.contentId = contentId;
        this.text = text;
        this.notify = notify;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getContentId() {
        return contentId;
    }

    public String getText() {
        return text;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + " " + getTitle() + " " + getMessage() + " " + getContentId() + " " + isNotify();
    }
}
