package nz.unitec.ballgame;

import android.app.Activity;
import android.widget.Toast;

public class AndroidDB implements NativeDB{

    private Activity context;

    public AndroidDB(Activity context){
        this.context = context;
    }


    @Override
    public void initDB() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // check if there has a SQLite db otherwise create a new one
                Toast.makeText(context, "SQLite interface", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int[] getScores() {
        return new int[0];
    }

    @Override
    public void setScore(int score) {

    }
}
