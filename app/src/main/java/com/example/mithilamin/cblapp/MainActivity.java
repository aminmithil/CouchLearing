package com.example.mithilamin.cblapp;

import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.Reducer;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.android.AndroidContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String DATABASE_NAME = "studentcbl";
    private TextView txtName, txtCollege, txtBranch;
    private Button btnAdd, btnView;
    private Manager manager;
    private Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            createCBL();
        } catch (CouchbaseLiteException | IOException e) {
            e.printStackTrace();
        }
        initializeView();
    }

    private void createCBL() throws CouchbaseLiteException, IOException {
        manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        database = manager.getDatabase(DATABASE_NAME);
    }

    private void initializeView() {
        txtName = (TextView) findViewById(R.id.text_name);
        txtCollege = (TextView) findViewById(R.id.text_college);
        txtBranch = (TextView) findViewById(R.id.text_branch);
        btnAdd = (Button) findViewById(R.id.btn_add_data);
        btnView = (Button) findViewById(R.id.btn_view_date);
        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_data:
                try {
                    String documentId = createDocument(database);
                    Log.e("doc Id", documentId);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_view_date:
                try {
                    Log.e("button view", "button dabau");
                    viewData(database);
                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //            String user = revision.getProperty("user").toString();
//            try {
//                JSONArray array = new JSONArray(user);
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject object = array.getJSONObject(i);
//                    String name = object.optString("name");
//                    String college = object.optString("college");
//                    String branch = object.optString("branch");
//                    txtName.setText(name);
//                    txtCollege.setText(college);
//                    txtBranch.setText(branch);
//                }
//            } catch (JSONException e) { Log.e("CBL Get", "naa malyo"); }
//            Log.e("CBL GET", revision.getProperty("services").toString());
    //Log.e("CBL", revision.getProperties().toString());
//            txtName.setText(name);
//            txtCollege.setText(college);
//            txtBranch.setText(branch);

    private void viewData(final Database database) throws CouchbaseLiteException {
        Query query = database.createAllDocumentsQuery();
        query.setMapOnly(true);
        QueryEnumerator queryEnumerator = query.run();
        for (Iterator<QueryRow> it = queryEnumerator; it.hasNext();) {
            QueryRow row = it.next();
            SavedRevision revision = row.getDocument().getCurrentRevision();
            Log.e("revision", revision.getProperties().toString());
            String user = revision.getProperty("user").toString();
            try {
                JSONObject userObj = new JSONObject(user);
                Log.e("JSON Objet", userObj.toString());
                Iterator<String> keys = userObj.keys();
                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    Log.e("Key", key);
                    if (userObj.get(key) instanceof JSONObject) {
                        Log.e("Key Object", userObj.get(key).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String createDocument(Database database) throws CouchbaseLiteException {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("user", createJSONData());
        document.putProperties(map);
        return documentId;
    }

    private String createJSONData() {
        try {
            JSONObject user = new JSONObject();
                JSONObject vehicles = new JSONObject();
                    JSONObject vehicle1 = new JSONObject();
                        JSONObject services = new JSONObject();
                            JSONObject service1 = new JSONObject();
                                JSONObject problems = new JSONObject();
                                    JSONObject wash = new JSONObject();
                                        JSONObject costWash = new JSONObject();
                                        wash.put("cost", 250);
                                problems.put("Wash", costWash);
                            service1.put("cost", 9099);
                            service1.put("date", "2016-04-01T00.00.00+05:30Z");
                            service1.put("status", "Paid");
                            service1.put("problems", problems);

                            JSONObject service2 = new JSONObject();
                            service2.put("cost", 485);
                            service2.put("status", "Due");
                        services.put("service-01", service1);
                        services.put("service-02", service2);
                    vehicle1.put("reg", "GJ01MF1234");
                    vehicle1.put("manuf", "Bajaj");
                    vehicle1.put("model", "Discover");
                    vehicle1.put("services", services);
                vehicles.put("vehicle-01", vehicle1);
            user.put("name", "Mithil");
            user.put("mobile", "9662112425");
            user.put("email", "aminmithil@gmail.com");
            user.put("vehicles", vehicles);
            return user.toString();
        } catch (JSONException e) { e.printStackTrace(); return null; }
    }
}
