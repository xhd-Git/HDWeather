package com.example.reol.hdweather;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reol.hdweather.db.DatabaseHelper;


public class ChangeCityActivity extends AppCompatActivity {
    public static final String TAG = "HDLOGTAG";

    ListView lvCity;
    EditText etSearch;
    Button btnSearch;
    DatabaseHelper dbHelper;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_city);
        dbHelper = new DatabaseHelper(this, "CityInfo.db", null, 1);

        initView();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                cursor = db.rawQuery("select _id,cityname,province from CityInfo where cityname like '%"+etSearch.getText()
                        +"%';", null);
                if (cursor.getCount() == 0){
                    Toast.makeText(ChangeCityActivity.this, "查不到，检查输入", Toast.LENGTH_SHORT).show();
                }else{
                    ListAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.item_lv_city, cursor,
                            new String[]{"cityname","province"}, new int[]{R.id.item_tv_city,R.id.item_tv_prov},
                            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    lvCity.setAdapter(adapter);
                }
            }
        });

        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvCityName = (TextView) view.findViewById(R.id.item_tv_city);
//                Toast.makeText(ChangeCityActivity.this, tvCityName.getText(), Toast.LENGTH_SHORT).show();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                cursor = db.rawQuery("select _id,cityid,cityname from CityInfo where cityname='"+tvCityName.getText().toString()+"';",null);
                cursor.moveToFirst();
                String cityId = cursor.getString(cursor.getColumnIndex("cityid"));
                Intent intent = new Intent();
                intent.putExtra("cityId",cityId);
                ChangeCityActivity.this.setResult(0,intent);
                cursor.close();
                ChangeCityActivity.this.finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        ChangeCityActivity.this.setResult(-1);
        cursor.close();
        super.onBackPressed();
    }

    private void initView() {
        lvCity = (ListView) findViewById(R.id.lv_city);
        btnSearch = (Button) findViewById(R.id.btn_search);
        etSearch = (EditText) findViewById(R.id.et_search);
    }



}
