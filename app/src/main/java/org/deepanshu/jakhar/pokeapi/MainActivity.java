package org.deepanshu.jakhar.pokeapi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button button, previous, next,search;
    EditText editText;
    TextView name, height, weight, experience, id, type;
    CircleImageView imageView;
    OkHttpClient okHttpClient;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    public static final String BASE_URL = "http://pokeapi.co/api/v2/pokemon/";
    Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient();

        button = (Button) findViewById(R.id.button);
        previous = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);
        search = (Button) findViewById(R.id.search);

        editText = (EditText) findViewById(R.id.editText);

        imageView = (CircleImageView) findViewById(R.id.imageView);

        name = (TextView) findViewById(R.id.name);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        experience = (TextView) findViewById(R.id.experience);
        id = (TextView) findViewById(R.id.id);
        type = (TextView) findViewById(R.id.type);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editText.setText(sharedPreferences.getString("EditText","0"));
        name.setText(sharedPreferences.getString("Name","0"));
        id.setText(sharedPreferences.getString("Id","0"));
        weight.setText(sharedPreferences.getString("Weight","0"));
        height.setText(sharedPreferences.getString("Height","0"));
        experience.setText(sharedPreferences.getString("Xp","0"));
        type.setText(sharedPreferences.getString("Types","0"));
        Log.d("TAG", "onCreate: " + sharedPreferences.getString("Image","0"));
        Picasso.with(getBaseContext()).load((sharedPreferences.getString("Image","0"))).networkPolicy(NetworkPolicy.OFFLINE).fit().into(imageView);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);

//        Snackbar.make(findViewById(android.R.id.content),"Please Wait!", BaseTransientBottomBar.LENGTH_SHORT).show();

        LinearLayout l = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_dialog,null);
        final EditText editText1 = (EditText) l.findViewById(R.id.editText1);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter the rank")
                .setView(l)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                onSearchButtonClick(editText.getText().toString());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String s = editText.getText().toString();
                int i = Integer.parseInt(s);
                if(i < 721){
                    i++;
                    s = String.valueOf(i);
                    editText.setText(s);
                    onSearchButtonClick(s);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String s = editText.getText().toString();
                int i = Integer.parseInt(s);
                if (i > 1) {
                    i--;
                    s = String.valueOf(i);
                    editText.setText(s);
                    onSearchButtonClick(s);
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String s=editText1.getText().toString();
                onSearchButtonClick(s);
            }
        });
    }
    public void onSearchButtonClick(String rank) {
        Request request = new Request.Builder()
                .url(BASE_URL + rank)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure: " + e.getLocalizedMessage());
                Log.e("TAG", "onFailure: " + call.request().url());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Gson gson = new Gson();

                pokemon = gson.fromJson(result, Pokemon.class);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(getBaseContext()).load((pokemon.getSprites().front_default)).fit().into(imageView);
                        name.setText(pokemon.getName());
                        height.setText(pokemon.getHeight());
                        weight.setText(pokemon.getWeight());
                        id.setText(pokemon.getId());
                        experience.setText(pokemon.getBase_experience());
                        ArrayList<Types> typesArrayList = pokemon.getTypes();
                        String types = "";
                        for (int i = 0; i < typesArrayList.size(); i++) {
                            Types t = typesArrayList.get(i);
                            types = types + t.getType().getName() + " ";
                        }
                        type.setText(types);
                        progressDialog.hide();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EditText",editText.getText().toString());
        editor.putString("Name",name.getText().toString());
        editor.putString("Id",id.getText().toString());
        editor.putString("Weight",weight.getText().toString());
        editor.putString("Height",height.getText().toString());
        editor.putString("Xp",experience.getText().toString());
        editor.putString("Types",type.getText().toString());
        editor.putString("Image",pokemon.getSprites().getFront_default());
        editor.apply();
    }
}
