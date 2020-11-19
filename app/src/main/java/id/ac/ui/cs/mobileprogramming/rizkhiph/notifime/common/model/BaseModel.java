package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.common.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class BaseModel {
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    private String createdAt;

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    private String updatedAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
