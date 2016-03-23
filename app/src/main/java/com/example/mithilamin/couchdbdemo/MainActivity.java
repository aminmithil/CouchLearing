package com.example.mithilamin.couchdbdemo;

import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";
    private Manager manager;
    private Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCBL();
    }

    private void createCBL() {
        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String documentId = createDocument(database);

        outputContents(database, documentId);

        updateDoc(database, documentId);

        outputContents(database, documentId);
        /*addAttachment(database, documentId);

        outputContentsWithAttachment(database, documentId);*/
    }

    private void updateDoc(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try{
            Map<String, Object> updatedProperties = new HashMap<>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put("eventDescription", "Everyone is invited!");
            updatedProperties.put("address", "B/24 Nandishwar");
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    private void outputContents(Database database, String documentId) {
        Document retrievedDocument = database.getDocument(documentId);
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));
    }

    private String createDocument(Database database) {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Mithil");
        map.put("location", "house");
        try{
            document.putProperties(map);
        } catch (CouchbaseLiteException e){
            e.printStackTrace();
        }
        return documentId;
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }
}
