package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.model.BaseModel;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.ui.content.model.Content;

@Entity(tableName = "content")
public class ContentEntity extends BaseModel implements Content {
    @PrimaryKey
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "content_title")
    private String title;

    @Nullable
    @ColumnInfo(name = "content_time")
    private String time;

    @Nullable
    @ColumnInfo(name = "content_date")
    private String date;

    @Nullable
    @ColumnInfo(name = "content_favorite")
    private boolean favorite;

    @Nullable
    @ColumnInfo(name = "content_airing")
    private boolean airing;

    public ContentEntity(int id, String title, String time, String date, boolean airing, boolean favorite) {
        super();
        this.id = id;
        this.title = title;
        this.time = time;
        this.date= date;
        this.airing = airing;
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "ID: " + this.id;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getDate() {
        return date;
    }

    @Nullable
    public String getTime() {
        return time;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public boolean getAiring() {
        return airing;
    }

    @Nullable
    public boolean getFavorite() {
        return favorite;
    }

    public void setAiring(boolean airing) {
        this.airing = airing;
    }

    public void setDate(@Nullable String date) {
        this.date = date;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(@Nullable String time) {
        this.time = time;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
}
