package com.example.dotson.atgtipps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final Calendar date = GregorianCalendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String V75URL = "https://www.atg.se/services/v1/games/";
    String dateURL;
    String textDate, V75ID;
    DataHandler dh;
    Button v1,v2,v3,v4,v5,v6,v7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt = (Button) findViewById(R.id.downloadButton);

        Button v1 = (Button) findViewById(R.id.v1);
        Button v2 = (Button) findViewById(R.id.v2);
        Button v3 = (Button) findViewById(R.id.v3);
        Button v4 = (Button) findViewById(R.id.v4);
        Button v5 = (Button) findViewById(R.id.v5);
        Button v6 = (Button) findViewById(R.id.v6);
        Button v7 = (Button) findViewById(R.id.v7);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);
        v5.setOnClickListener(this);
        v6.setOnClickListener(this);
        v7.setOnClickListener(this);

        final Context c = this;

        final RequestQueue queue = Volley.newRequestQueue(this);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate(queue, c);
            }
        });

    }


    public void getDate(final RequestQueue q, final Context context){

        textDate = formatter.format(date.getTime());

        dateURL = "https://www.atg.se/services/v1/calendar/day/";
        dateURL += textDate;

        final JsonObjectRequest dateRequest = new JsonObjectRequest(Request.Method.GET, dateURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    V75ID = response.getJSONObject("games").getJSONArray("V75").getJSONObject(0).getString("id");
                    V75URL += V75ID;
                    final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, V75URL, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    for(int i = 1; i <= 7; i++){
                                        dh = new DataHandler(response, context, i);
                                        dh.horseNumber();
                                    }



                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub

                                }
                            });



                    q.add(jsObjRequest);
                } catch (JSONException e) {
                    date.add(Calendar.DAY_OF_MONTH, +1);
                    getDate(q, context);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

            }
        });
        q.add(dateRequest);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        intent = new Intent(this, V75List.class);
        switch (v.getId()){

            case R.id.v1:
                intent.putExtra("race", 1);
                startActivity(intent);
                break;
            case R.id.v2:
                intent.putExtra("race", 2);
                startActivity(intent);
                break;
            case R.id.v3:
                intent.putExtra("race", 3);
                startActivity(intent);
                break;
            case R.id.v4:
                intent.putExtra("race", 4);
                startActivity(intent);
                break;
            case R.id.v5:
                intent.putExtra("race", 5);
                startActivity(intent);
                break;
            case R.id.v6:
                intent.putExtra("race", 6);
                startActivity(intent);
                break;
            case R.id.v7:
                intent = new Intent(this, TabActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
