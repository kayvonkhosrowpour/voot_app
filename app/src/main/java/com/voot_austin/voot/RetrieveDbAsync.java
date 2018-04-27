package com.voot_austin.voot;

import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.Callable;

public class RetrieveDbAsync extends AsyncTask<Void, Void, List<VootUser>> {

    private final UserDao mDao;
    private final Callable<Void> call;

    RetrieveDbAsync(AppDatabase db, Callable<Void> call) {
        mDao = db.userDao();
        this.call = call;
    }

    @Override
    protected List<VootUser> doInBackground(Void... voids) {
        return mDao.getAll();
    }

    @Override
    protected void onPostExecute(List<VootUser> result) {
        try {
            this.call.call();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't run onPostExecute()");
        }
    }


    public static class DbCallable<VootUser> implements Callable<VootUser> {

        public VootUser vootUser;

        @Override
        public VootUser call() throws Exception {

            return null;
        }
    }

}