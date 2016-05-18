package com.bambora.nativepayment.storage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bambora.nativepayment.models.Credentials;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This is a class responsible for saving objects tot disc.
 *
 * Created by oskarhenriksson on 15/10/15.
 */
public class FileStorage {

    private static final String CREDENTIALS_FILE_NAME = "credentials";

    public interface IOnObjectRead {
        void notifyOnObjectRead(Serializable object);
    }

    public interface IOnObjectSaved {
        void notifyOnObjectSaved();
    }

    /**
     * A method for saving an {@link Credentials} to disk
     * @param context The context to be used
     * @param credentials The {@link Credentials} to be saved
     */
    public void saveCredentials(Context context, Credentials credentials) {
        saveObjectToDisk(context, CREDENTIALS_FILE_NAME, credentials);
    }

    /**
     * A method for retrieving an {@link Credentials} from disk. This method return null
     * if no Credentials are found.
     * @param context The context to be used
     * @param listener The result listener
     */
    public void getCredentials(Context context, IOnObjectRead listener) {
        getObjectFromDisk(context, CREDENTIALS_FILE_NAME, listener);
    }

    protected void saveObjectToDisk(final Context context, final String fileName, final Serializable object) {
        saveObjectToDisk(context, fileName, object, null);
    }

    protected void saveObjectToDisk(final Context context, final String fileName, final Serializable object, final IOnObjectSaved listener) {
        AsyncTask writeToFileTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(object);
                    objectOutputStream.close();
                    fileOutputStream.close();
                    postResultToUiThread(listener);
                    return null;
                }
                catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Failed to save object to disk. " + e);
                    return null;
                }
            }
        };
        executeAsyncTask(writeToFileTask);
    }

    protected void getObjectFromDisk(final Context context, final String fileName, final IOnObjectRead listener) {
        AsyncTask<Void, Void, Serializable> readFromFileTask = new AsyncTask<Void, Void, Serializable>() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Serializable object = null;
                try {
                    FileInputStream fileInputStream = context.openFileInput(fileName);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    object = (Serializable) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                    postResultToUiThread(listener, object);
                }
                catch (IOException | ClassNotFoundException e) {
                    Log.e(getClass().getSimpleName(), "Failed to read object from disk. " + e);
                    postResultToUiThread(listener, null);
                }
                return object;
            }
        };
        executeAsyncTask(readFromFileTask);
    }

    private void executeAsyncTask(AsyncTask task){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
        else
            task.execute();
    }

    private void postResultToUiThread(final IOnObjectRead listener, final Serializable object) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.notifyOnObjectRead(object);
                }
            }
        });
    }

    private void postResultToUiThread(final IOnObjectSaved listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.notifyOnObjectSaved();
                }
            }
        });
    }
}
