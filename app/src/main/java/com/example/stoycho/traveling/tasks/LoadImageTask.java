package com.example.stoycho.traveling.tasks;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by stoycho on 7/12/16.
 */
public class LoadImageTask extends AsyncTask<Void,Void,Drawable> {

    public interface OnImageDownload{
        void onDownload(String url,Drawable drawable);
    }
    private Context context;
    private String url;
    private File image;
    private OnImageDownload listener;
    private String checkUrl;
    private String directoryName;

    public LoadImageTask(Context context,String url, File image, OnImageDownload listener,String directoryName)
    {
        this.context = context;
        this.url = url;
        this.image = image;
        this.listener = listener;
        this.directoryName = directoryName;
    }


    @Override
    protected Drawable doInBackground(Void... params) {
        try {
            if (url != null) {
                checkUrl = url;
                url = url.replaceAll(" ", "%20");
                InputStream is = (InputStream) new URL(url).getContent();
                FileOutputStream fileOutputStream = new FileOutputStream(image);
                byte[] buffer = new byte[1024];
                int size = is.read(buffer);
                while (size > 0) {
                    fileOutputStream.write(buffer, 0, size);
                    size = is.read(buffer);
                }
                is.close();
                fileOutputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Drawable imageDrawable = new BitmapDrawable(context.getResources(),image.getAbsolutePath());
        return imageDrawable;
    }

    private static long dirSize(File dir) {

        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += dirSize(fileList [i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Drawable result)
    {
        if(listener != null)
            listener.onDownload(checkUrl,result);
    }

    public static Drawable laodImage(Context context, String directoryName,String imageName, String urlImage, OnImageDownload listener)
    {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        if(imageName == null)
            imageName =  urlImage.substring(urlImage.lastIndexOf("/")+1);
        File file = new File(directory,imageName);
        if(file.exists())
            return  new BitmapDrawable(context.getResources(),file.getAbsolutePath());
        else {
            LoadImageTask loadImageTask = new LoadImageTask(context,urlImage,file,listener,directoryName);
            loadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return null;
        }
    }

    public static Drawable laodImageSync(Context context, String directoryName,String imageName, String urlImage)
    {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        if(imageName == null)
            imageName =  urlImage.substring(urlImage.lastIndexOf("/")+1);
        File file = new File(directory,imageName);
        if(file.exists())
            return  new BitmapDrawable(context.getResources(),file.getAbsolutePath());
        else {
            LoadImageTask loadImageTask = new LoadImageTask(context,urlImage,file,null,directoryName);
            try {
                return loadImageTask.execute().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
