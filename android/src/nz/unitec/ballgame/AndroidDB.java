package nz.unitec.ballgame;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import android.database.Cursor;

public class AndroidDB implements NativeDB{

    private Activity context;
    private SQLiteDatabase db;

    public AndroidDB(Activity context){
        this.context = context;
    }


    @Override
    public void initDB() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // check if there has a SQLite db otherwise create a new one
                Toast.makeText(context, "Welcome to ballgame", Toast.LENGTH_SHORT).show();
                db = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir()+"/ballgame.db",null);
                String score_table = "create table if not exists score(_id integer primary key autoincrement,score integer)";
                db.execSQL(score_table);
            }
        });
    }

    @Override
    public int[] getScores() {
//        Cursor cursor = db.query ("score",null,null,null,null,null,null);
        int[] scores = new int[5];
//        if(cursor.moveToFirst()) {
//            for(int i=0;i<5;i++){
//                if(cursor.move(i)){
//                    scores[i] = cursor.getInt(1);
//                }
//            }
//        }
        for(int i=0; i<5; i++){
            Cursor cursor = db.query ("score",null,null,null,null,null,"score DESC");
            if(cursor.moveToFirst()){
                if(cursor.move(i)){
                    scores[i] = cursor.getInt(1);
                }
            }
            cursor.close();
        }

//        cursor.close();
        return scores;
    }

    @Override
    public void setScore(int score) {
        String set_score=String.format("insert into score(score) values(%s)",score);

        db.execSQL(set_score);
    }
}
