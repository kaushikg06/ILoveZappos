package com.example.kaush.ilovezappos;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.kaush.ilovezappos.databinding.DisplayProductBinding;

/**
 * Created by kaush on 2/3/2017.
 */

public class ShowProductDetails extends AppCompatActivity {

    private TextView productId;
    private TextView brandName;
    private TextView price;
    private TextView product_name;
    FloatingActionButton fabAdd, fabAdded;
    Animation FabOpen, FabClose;
    boolean isOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_product);

        final com.example.kaush.ilovezappos.Result result = (com.example.kaush.ilovezappos.Result) getIntent().getSerializableExtra(com.example.kaush.ilovezappos.MainActivity.result_string);
        DisplayProductBinding binding = DataBindingUtil.setContentView(this, R.layout.display_product);
        binding.setProductResult(result);
        //  brandName = (TextView) findViewById(R.id.brandName);
        //price = (TextView)findViewById(R.id.price);
        //  product_name = (TextView) findViewById(R.id.product_name);

        // Intent intent = getIntent();

        //  brandName.setText(intent.getStringExtra(MainActivity.brand_name));
        //price.setText(intent.getStringExtra(MainActivity.price));
        //  product_name.setText(intent.getStringExtra(MainActivity.product_name));

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add_to_cart);
        fabAdded = (FloatingActionButton) findViewById(R.id.fab_added_to_cart);

        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animations);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fabAdded.startAnimation(FabOpen);
                fabAdd.startAnimation(FabClose);
                fabAdded.setClickable(true);
                fabAdd.setClickable(false);

                Snackbar.make(view, "Product has been added to cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        fabAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabAdd.startAnimation(FabOpen);
                fabAdded.startAnimation(FabClose);
                fabAdded.setClickable(false);
                fabAdd.setClickable(true);

                Snackbar.make(v, "Product has been removed from cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }


}
