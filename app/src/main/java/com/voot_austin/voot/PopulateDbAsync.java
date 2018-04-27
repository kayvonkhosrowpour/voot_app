package com.voot_austin.voot;

import android.os.AsyncTask;

public class PopulateDbAsync extends AsyncTask<VootUser, Void, Void> {

    private final UserDao mDao;

    PopulateDbAsync(AppDatabase db) {
        mDao = db.userDao();
    }

    @Override
    protected Void doInBackground(final VootUser ... vootUsers) {
        if (vootUsers != null && vootUsers.length > 0) {
            mDao.insertAll(vootUsers);
        }
        return null;
    }

}