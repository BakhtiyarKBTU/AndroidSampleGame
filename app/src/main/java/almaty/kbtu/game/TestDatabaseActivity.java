package almaty.kbtu.game;


import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

/**
 * Created by RIA on 28.05.2015.
 */

public class TestDatabaseActivity extends ListActivity {
    private PlayerDataSource datasource;
   // public EditText editText, editText2, name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_layout);
        /*editText = (EditText) findViewById(R.id.takename);
        editText2 = (EditText) findViewById(R.id.takescore);
        name = (EditText) findViewById(R.id.edit_message);*/
        datasource = new PlayerDataSource(this);
        datasource.open();
        List<Player> values = datasource.getAllPlayers();
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Player> adapter = new ArrayAdapter<Player>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }
   /*public void onClick(View view) {
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
    }*/
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