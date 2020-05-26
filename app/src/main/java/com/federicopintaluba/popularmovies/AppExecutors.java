package com.federicopintaluba.popularmovies;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }

        return sInstance;
    }

    Executor diskIO() {
        return diskIO;
    }
}
