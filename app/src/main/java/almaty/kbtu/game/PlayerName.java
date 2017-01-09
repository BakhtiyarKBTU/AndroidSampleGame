package almaty.kbtu.game;


import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

/**
 * Created by RIA on 28.05.2015.
 */

public class PlayerName extends ListActivity {
    private PlayerDataSource datasource;
     public EditText editText, editText2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        editText = (EditText) findViewById(R.id.takename);
        editText2 = (EditText) findViewById(R.id.takescore);
        datasource = new PlayerDataSource(this);
        datasource.open();
        List<Player> values = datasource.getAllPlayers();
        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
    public void startGame(View v){
        Intent intent = new Intent(this, BreakoutGame.class);
        startActivity(intent);
    }
    public void onClick(View view) {
         @SuppressWarnings("unchecked")
         ArrayAdapter<Player> adapter = (ArrayAdapter<Player>) getListAdapter();
         Player player = null;
         switch (view.getId()) {
             case R.id.add:
                 player = datasource.createPlayer(editText.getText().toString(), Integer.parseInt(editText2.getText().toString()));
                 adapter.add(player);
                 break;
             case R.id.delete:
                 if (getListAdapter().getCount() > 0) {
                     player = (Player) getListAdapter().getItem(getListAdapter().getCount()-1);
                     datasource.deletePlayer(player);
                     adapter.remove(player);
                 }
                 break;
         }
         adapter.notifyDataSetChanged();
     }
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }
    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}