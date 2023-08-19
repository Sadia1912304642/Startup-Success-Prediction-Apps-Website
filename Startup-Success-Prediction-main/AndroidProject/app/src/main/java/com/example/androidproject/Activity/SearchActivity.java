package com.example.androidproject.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Adapter.searchRecyclerviewAdapter;
import com.example.androidproject.Api.Prediction.predictionApi;
import com.example.androidproject.Api.SearchList.searchListApi;
import com.example.androidproject.Api.clusterList.clusterListApi;
import com.example.androidproject.Api.verifyUser.VerifyUserApi;
import com.example.androidproject.R;
import com.example.androidproject.RetrofitClass;
import com.example.androidproject.model.Cluster;
import com.example.androidproject.model.PredictionModel;
import com.example.androidproject.model.SearchModel;
import com.example.androidproject.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class SearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

//    private Cluster selectedCluster;
    private String selectedCategory;

    Button logoutBtn,predict_btn;
    private TextView usermail,prediction_result;
    private EditText stp_name,stp_age,stp_relationship,stp_participants,stp_milestone,stp_mile_age01,stp_mile_age02,stp_total_fund,stp_fund_round,stp_fund_age01,stp_fund_age02;
    private Spinner stp_category;
    private CheckBox checkBox_angel,checkBox_vc,checkBox_roundA,checkBox_roundB,checkBox_roundC,checkBox_roundD;

    private RecyclerView recyclerView;

    private User loginuser = new User();

    //        Cluster initialization
//    private ArrayList<Cluster> items = new ArrayList<>();
//    private ArrayList<SearchModel> searchitems = new ArrayList<>();

    private ArrayList<String> categoryList = new ArrayList<>();

    private searchRecyclerviewAdapter searchrecyclerviewadapter;

//    private ArrayAdapter<Cluster> adapter;

//    private String mail = "tawsifmahmud05@gmail.com";
    private String token = "Token e697bea2c349db8c61a55accf6c0546db5de0dd8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        stp_name = findViewById(R.id.stp_name);
        stp_age = findViewById(R.id.stp_age);
        stp_relationship = findViewById(R.id.stp_relation);
        stp_participants = findViewById(R.id.stp_avg_participants);
        stp_milestone = findViewById(R.id.stp_milestone);
        stp_mile_age01 = findViewById(R.id.stp_mile_age01);
        stp_mile_age02 = findViewById(R.id.stp_mile_age02);
        stp_total_fund = findViewById(R.id.stp_total_fund);
        stp_fund_round = findViewById(R.id.stp_fund_round);
        stp_fund_age01 = findViewById(R.id.stp_fund_age01);
        stp_fund_age02 = findViewById(R.id.stp_fund_age02);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        predict_btn = findViewById(R.id.button_predict);

        usermail = findViewById(R.id.search_email);
        prediction_result = findViewById(R.id.textID_prediction);

        stp_category = findViewById(R.id.stp_category);

        checkBox_angel = findViewById(R.id.checkBox_angel);
        checkBox_vc = findViewById(R.id.checkBox_vc);
        checkBox_roundA = findViewById(R.id.checkBox_roundA);
        checkBox_roundB = findViewById(R.id.checkBox_roundB);
        checkBox_roundC = findViewById(R.id.checkBox_roundC);
        checkBox_roundD = findViewById(R.id.checkBox_roundD);


//        micButton = findViewById(R.id.mic);
//        searchKeyword = findViewById(R.id.search_keyword);
//        searchCluster = findViewById(R.id.search_cluster);
//        recyclerView = findViewById(R.id.recyclerListView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        searchrecyclerviewadapter = new searchRecyclerviewAdapter(getApplicationContext(),searchitems);
//        recyclerView.setAdapter(searchrecyclerviewadapter);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setcategory();


        stp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = (String) adapterView.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCategory = categoryList.get(0);
            }
        });


//        setRecyclerview();

        predict_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                predict();

//                searchfiles();
//                    setRecyclerview();



//                searchrecyclerviewadapter = new searchRecyclerviewAdapter(getApplicationContext(),searchitems);
//                recyclerView.setAdapter(searchrecyclerviewadapter);
//                searchrecyclerviewadapter.notifyDataSetChanged();
//                recyclerView.setAdapter(searchrecyclerviewadapter);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    gotoMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                }

                            }


                        });
            }
        });

//        Speech to Text
//        micButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");
//                micButton.setBackground(getDrawable(R.drawable.ic_voice_red));
//                try {
//                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
//                } catch (Exception e) {
//                    Toast.makeText(SearchActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    micButton.setBackground(getDrawable(R.drawable.ic_voice));
//
//                }
//            }
//        });
    }

    private void predict() {




        int is_software=0 , is_web =0, is_advertise=0, is_biotech=0, is_consulting=0, is_ecommerce=0, is_enterprise =0, is_gaming =0, is_mobile =0, is_others = 0;

        if (selectedCategory == "Software"){
            is_software = 1;
        }
        else if(selectedCategory == "Web"){
            is_web = 1;
        }
        else if(selectedCategory == "Mobile"){
            is_mobile = 1;
        }
        else if (selectedCategory == "Enterprise"){
            is_enterprise = 1;
        }
        else if(selectedCategory == "Advertising"){
            is_advertise = 1;
        }
        else if(selectedCategory == "Gaming Co."){
            is_gaming = 1;
        }
        else if (selectedCategory == "E-commerce"){
            is_ecommerce = 1;
        }
        else if(selectedCategory == "Bio-tech"){
            is_biotech = 1;
        }
        else if(selectedCategory == "Consulting"){
            is_consulting = 1;
        }
        else if (selectedCategory == "Others"){
            is_others = 1;
        }

        int has_ABCD = 0;

        if( checkBox_roundA.isChecked() || checkBox_roundB.isChecked() || checkBox_roundC.isChecked() || checkBox_roundD.isChecked()){
            has_ABCD = 1;
        }

        int has_investor = 0;

        if( checkBox_vc.isChecked() || checkBox_angel.isChecked()){
            has_investor = 1;
        }

        int has_seed = 0;

        if( has_ABCD == 1 && has_investor == 1){
            has_seed = 1;
        }

        int invalid_startup = 1;
        if( has_ABCD == 1 && checkBox_vc.isChecked() && checkBox_angel.isChecked() ){
            invalid_startup = 0;
        }






        float age_first_funding_year = Float.parseFloat(stp_fund_age01.getText().toString());
        float age_last_funding_year = Float.parseFloat(stp_fund_age02.getText().toString());
        float age_first_milestone_year = Float.parseFloat(stp_mile_age01.getText().toString());
        float age_last_milestone_year = Float.parseFloat(stp_mile_age02.getText().toString());
        float relationships = Float.parseFloat(stp_relationship.getText().toString());
        float funding_rounds = Float.parseFloat(stp_fund_round.getText().toString());
        float funding_total_usd = Float.parseFloat(stp_total_fund.getText().toString());
        float milestones = Float.parseFloat(stp_milestone.getText().toString());
        float avg_participants = Float.parseFloat(stp_participants.getText().toString());
        float startUp_age_year = Float.parseFloat(stp_age.getText().toString());

        Toast.makeText(getApplicationContext(),"waiting",Toast.LENGTH_LONG).show();


        RetrofitClass retrofitClass = new RetrofitClass();
        predictionApi predictionApi = retrofitClass.getRetrofit().create(predictionApi.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("age_first_funding_year", age_first_funding_year);
        jsonObject.addProperty("age_last_funding_year", age_last_funding_year);
        jsonObject.addProperty("age_first_milestone_year", age_first_milestone_year);
        jsonObject.addProperty("age_last_milestone_year", age_last_milestone_year);
        jsonObject.addProperty("relationships", relationships);
        jsonObject.addProperty("funding_rounds", funding_rounds);
        jsonObject.addProperty("funding_total_usd", funding_total_usd);
        jsonObject.addProperty("milestones", milestones);
        jsonObject.addProperty("is_software", is_software);
        jsonObject.addProperty("is_web", is_web);
        jsonObject.addProperty("is_mobile", is_mobile);
        jsonObject.addProperty("is_enterprise", is_enterprise);
        jsonObject.addProperty("is_advertising", is_advertise);
        jsonObject.addProperty("is_gamesvideo", is_gaming);
        jsonObject.addProperty("is_ecommerce", is_ecommerce);
        jsonObject.addProperty("is_biotech", is_biotech);
        jsonObject.addProperty("is_consulting", is_consulting);
        jsonObject.addProperty("is_othercategory", is_others);
        jsonObject.addProperty("avg_participants", avg_participants);
        jsonObject.addProperty("has_RoundABCD", has_ABCD);
        jsonObject.addProperty("has_Investor", has_investor);
        jsonObject.addProperty("has_Seed", has_seed);
        jsonObject.addProperty("invalid_startup", invalid_startup);
        jsonObject.addProperty("startUp_age_year", startUp_age_year);




        //CALL THE REQUEST
        Call<PredictionModel> call = predictionApi.getPrediction(
                jsonObject
                );



        call.enqueue(new Callback<PredictionModel>() {
            @Override
            public void onResponse(Call<PredictionModel> call, Response<PredictionModel> response) {

                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"successful",Toast.LENGTH_LONG).show();
                    PredictionModel model =response.body();
                    Toast.makeText(getApplicationContext(),""+response.body().getInfo(),Toast.LENGTH_LONG).show();



                    if (model.getStartup_status() == 1){
                        prediction_result.setText(stp_name.getText().toString() + "!! will be Successfull!!");
                    }
                    else{
                        prediction_result.setText(stp_name.getText().toString() + " have to work hard !");

                    }
                }

            }

            @Override
            public void onFailure(Call<PredictionModel> call, Throwable t) {
                Log.d("verify",t.getMessage());
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


    }


//    private void setRecyclerview() {
//            searchrecyclerviewadapter = new searchRecyclerviewAdapter(getApplicationContext(),searchitems);
//            recyclerView.setAdapter(searchrecyclerviewadapter);
//
//
//
//    }

//    private void searchfiles() {
//
//        searchitems.clear();
//
//        // INITIALIZE RETROFIT
//        RetrofitClass retrofitClass = new RetrofitClass();
//        searchListApi searchListApi = retrofitClass.getRetrofit().create(searchListApi.class);
//
//        String keyword = stp_name.getText().toString();
////        String clusderid = String.valueOf(selectedCluster.getId());
//
//
//        //CALL THE REQUEST
//        Call<List<SearchModel>> call = searchListApi.getSearchList(keyword,clusderid);
//
//
//        call.enqueue(new Callback<List<SearchModel>>() {
//            @Override
//            public void onResponse(Call<List<SearchModel>> call, Response<List<SearchModel>> response) {
//
//                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"successful",Toast.LENGTH_LONG).show();
//                    List<SearchModel> searchresult =response.body();
//
//
//                    for(SearchModel model : searchresult){
//                        try{
//                            searchitems.add(model);
//                            Toast.makeText(getApplicationContext(),model.getId(),Toast.LENGTH_LONG).show();
//                        }catch(Exception e){
//
//                        }
//
//                    }
//                    for(SearchModel model : searchitems){
//                        model.setKeyword(keyword);
//                    }
//                    searchrecyclerviewadapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<SearchModel>> call, Throwable t) {
//                Log.d("verify",t.getMessage());
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }

    @Override
    protected void onStart() {

        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
//            userName.setText(account.getDisplayName());
            usermail.setText(account.getEmail());
//            userId.setText(account.getId());
//            usermail.setText(account.getDisplayName());
//            checkuser(account.getEmail());

//            checkuser(account.getEmail());

//            try{
//                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
//            }catch (NullPointerException e){
//                Toast.makeText(getApplicationContext(),"image not found",Toast.LENGTH_LONG).show();
//            }

        }else{
            gotoMainActivity();
        }
    }

//    private void getcluster(int id) {
//        items.clear();
//
//        // INITIALIZE RETROFIT
//        RetrofitClass retrofitClass = new RetrofitClass();
//        clusterListApi clusterListApi = retrofitClass.getRetrofit().create(clusterListApi.class);
//
//
//        //CALL THE REQUEST
//        Call<List<Cluster>> call = clusterListApi.getClusterList(id);
//
//
//        call.enqueue(new Callback<List<Cluster>>() {
//            @Override
//            public void onResponse(Call<List<Cluster>> call, Response<List<Cluster>> response) {
//
//                if(response.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"successful",Toast.LENGTH_LONG).show();
//                    List<Cluster> clusters =response.body();
//
//                    for(Cluster cluster : clusters){
//                        try{
//                            items.add(cluster);
//                            adapter = new ArrayAdapter<Cluster>(getApplicationContext(),
//                                     android.R.layout.simple_spinner_dropdown_item,
//                                     items);
//                            stp_category.setAdapter(adapter);
//                        }catch(Exception e){
//
//                        }
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Cluster>> call, Throwable t) {
//                Log.d("verify",t.getMessage());
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
//
//            }
//        });
//
//    }
//
//    private void checkuser(String email) {
//
//        // INITIALIZE RETROFIT
//        RetrofitClass retrofitClass = new RetrofitClass();
//        VerifyUserApi verifyUserApi = retrofitClass.getRetrofit().create(VerifyUserApi.class);
//
//
//        //CALL THE REQUEST
//        Call<List<User>> call = verifyUserApi.getUser(email);//"Token "+ token
//
//
//        call.enqueue(new Callback<List<User>>() {
//            @Override
//            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//
//                if(response.isSuccessful()){
//
//                    List<User> users =response.body();
//                    if(users.isEmpty()){
//                        predict_btn.setVisibility(View.GONE);
//                        usermail.setText("SignIn from web\nAnd Create Cluster to search");
//                        Toast.makeText(getApplicationContext(),"No user with this email",Toast.LENGTH_LONG).show();
//
//                    }else{
//                        Toast.makeText(getApplicationContext(),"successful",Toast.LENGTH_LONG).show();
//                        for(User user : users){
//                            loginuser = user;
//                            String responsetest = "";
//                            usermail.setText(user.getEmail());
//                            getcluster(user.getId());
//                            responsetest += user.getEmail();
//                            Log.v("Tag",""+responsetest);
//                            break;
//                        }
//                    }
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<User>> call, Throwable t) {
//                Log.d("verify",t.getMessage());
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
//
//            }
//        });
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        micButton.setBackground(getDrawable(R.drawable.ic_voice));
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
//            if (resultCode == RESULT_OK && data != null) {
//                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                searchKeyword.setText(Objects.requireNonNull(result).get(0));
//
//            }
//        }
//    }

    private void gotoMainActivity(){
        Intent intent=new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
    }

    private void setcategory() {
        categoryList.add("Software");
        categoryList.add("Web");
        categoryList.add("Mobile");
        categoryList.add("Enterprise");
        categoryList.add("Advertising");
        categoryList.add("Gaming Co.");
        categoryList.add("E-commerce");
        categoryList.add("Bio-tech");
        categoryList.add("Consulting");
        categoryList.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,categoryList);
        stp_category.setAdapter(adapter);

//        ArrayAdapter<String> branchListAdapter = new ArrayAdapter<>(getApplicationContext(),
//                android.R.layout.simple_spinner_item, categoryList);
//        stp_category.setAdapter(branchListAdapter);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}