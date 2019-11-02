package in.roxfort.birthday;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by Rajnish yadav on 21 Aug 2019 at 14:56.
 */
public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer player;

    public IBinder onBind(Intent arg0) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setLooping(true); // Set looping
        try {
            AssetFileDescriptor afd = getAssets().openFd("audio1.mp3");
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
//            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setVolume(100, 100);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    public void onStart(Intent intent, int startId) {

        player.start();
        // TO DO
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }

    public void onPause() {

    }

    @Override
    public void onDestroy() {
        player.stop();
        player.start();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }

}
