package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.dao.ContentDao;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.content.db.entity.ContentEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.dao.NotificationDao;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.notification.db.entity.NotificationEntity;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.db.dao.SearchHistoryDao;
import id.ac.ui.cs.mobileprogramming.rizkhiph.notifime.src.search.db.entity.SearchHistoryEntity;

@Database(entities = {ContentEntity.class, NotificationEntity.class, SearchHistoryEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    public static final String DATABASE_NAME = "notifime";

    public abstract ContentDao contentDao();

    public abstract NotificationDao historyNotificationDao();

    public abstract SearchHistoryDao searchHistoryDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = buildDatabase(context.getApplicationContext(), executors);
                    appDatabase.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }

        return appDatabase;
    }

    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                addDelay();
                                AppDatabase database = AppDatabase.getInstance(appContext, executors);
                                database.setDatabaseCreated();
                            }
                        });
                    }
                })
                .addMigrations(MIGRATION_1_2, MIGRATION_1_3, MIGRATION_1_4)
                .allowMainThreadQueries()
                .build();
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notification ADD COLUMN notify BOOLEAN DEFAULT 1");
        }
    };

    static final Migration MIGRATION_1_3 = new Migration(1, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE notification ADD COLUMN text TEXT NOT NULL DEFAULT ''");
        }
    };

    static final Migration MIGRATION_1_4 = new Migration(1, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE searchhistory(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "search_query TEXT NOT NULL) ");
        }
    };

//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `productsFts` USING FTS4("
//                    + "`name` TEXT, `description` TEXT, content=`products`)");
//            database.execSQL("INSERT INTO productsFts (`rowid`, `name`, `description`) "
//                    + "SELECT `id`, `name`, `description` FROM products");
//
//        }
//    };
}
