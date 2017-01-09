package almaty.kbtu.game;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Start(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.start_btn:
                intent = new Intent(this, PlayerName.class);
                startActivity(intent);
                break;
            case R.id.score_btn:
                intent = new Intent(this, TestDatabaseActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}