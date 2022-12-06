package edu.northeastern.group33webapi.FinalProject.login;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class TopScore {
    private SharedPreferences sp;

    public TopScore(Context context) {
        sp = context.getSharedPreferences("TopScore",context.MODE_PRIVATE);
    }

    public int getTopScore(){
        int topScore = sp.getInt("TopScore", 0);
        return topScore;
    }

    public void setTopscore(int topScore){
        Editor editor = sp.edit();
        editor.putInt("TopScore",topScore);
        editor.commit();
    }
}
