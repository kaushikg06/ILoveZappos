package com.example.kaush.ilovezappos;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    String[] items;
    ArrayAdapter arrayAdapter;
    ListView listView;
    EditText editText;
    private SearchManager searchManager;
    private SearchView searchView;
    private ListAdapter listAdapter;
    private MenuItem searchItem;
    Context mContext=this;
    public final static String EXTRA_MESSAGE = "com.example.kaush.zapposandroidproject.MESSAGE";
    public static final String ROOT_URL = "https://api.zappos.com/";
    private List<Products> products;
    private List<Result> results;

    public static final String product_id ="product_id";
    public static final String brand_name ="name";
    public static final String price ="price";
    public static final String product_name ="product_name";
    public static final String result_string = "result";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //actual
        listView = (ListView) findViewById(R.id.listView);
        getProducts();
        listView.setOnItemClickListener(this);

    }

    private void getProducts() {
        final ProgressDialog loading = ProgressDialog.show(this, "Fetching Data","Please wait..",false,
                false);

        ProductAPI api = ProductAPI.Factory.getIstance(mContext);
        api.getProductResults("nike","b743e26728e16b81da139182bb2094357c31d331").enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if(response.isSuccessful()) {
                    loading.dismiss();
                    Log.d("OnResponse", "success");
                    results = response.body().getResults();
                    showList();
                }
                else {
                    Log.d("TAG 2","Failed inside onResponse");
                }
            }
            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Log.d("TAG 3","OnFailure");

            }
        });
    }


    private void showList() {

        ArrayList<String> product_name = new ArrayList<>();
        for(Result item: results){
            product_name.add(item.getProductName());
        }
        arrayAdapter = new ArrayAdapter <>(MainActivity.this, android.R.layout.simple_list_item_1,
                product_name);
        listView.setAdapter(arrayAdapter);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, ShowProductDetails.class)));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int count=0;
                Intent intent = new Intent(getApplicationContext(), ShowProductDetails.class);
                for (Result item : results) {
                    if (item.getProductName().equalsIgnoreCase(query)) {
                        intent.putExtra(result_string, (Serializable) item);
                        startActivity(intent);
                        return true;
                    }
                    else{
                        continue;
                    }
                }

                Toast.makeText(getApplicationContext(),"Item not available",Toast.LENGTH_LONG).show();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });

        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ShowProductDetails.class);
        String productName = (String)parent.getItemAtPosition(position);

        for(Result item: results){
            if(item.getProductName().equalsIgnoreCase(productName)) {
                intent.putExtra(result_string, (Serializable) item);
            }
        }

        startActivity(intent);
    }





    /*@Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.toString();
        return false;
    }*/


}
