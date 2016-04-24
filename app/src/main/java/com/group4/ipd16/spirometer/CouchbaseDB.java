package com.group4.ipd16.spirometer;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Samuel on 24/04/2016.
 */
public class CouchbaseDB extends BaseActivity {
    private Manager manager = null;
    private Database database = null;
    private static final String DB_NAME = "spiro_db";
    private static final String TAG = "dbEvents";
    private Context context;
    private View usersByNameView = null;

    public CouchbaseDB( Context context){
        this.context = context;
        setUpCouchbaseLiteDB();
    }

    private void setUpCouchbaseLiteDB() {
        try{
            manager = new Manager( new AndroidContext(context) , Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace(); return;
        } catch (IOException e) {
            e.printStackTrace(); return;
        }
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext();){
                QueryRow row = it.next();
                Log.i(TAG, String.valueOf(row.getDocument().getProperties()));
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    protected void CreateDocument(Map<String, Object> userMap){
        // create new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        //Map<String, Object> map = new HashMap<String, Object>();
        try{
            //save properties to the document
            document.putProperties(userMap);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "error putting", e);
        }
        //return documentId;
    }

    public List<String> getAllUsers(){
        List<String> users = new ArrayList<String>();
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext();){
                QueryRow row = it.next();
                Log.i(TAG, String.valueOf(row.getDocument().getProperties()));
                users.add( String.valueOf(row.getDocument().getProperty("first_name")) + " " + String.valueOf(row.getDocument().getProperty("last_name")));
            }

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    private void updateDoc(String documentId, Map<String, Object> updatedProperties ){
        Document document = database.getDocument(documentId);
        try{
            // update document with more data
            //Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            // save to couchbase local couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting ", e);
        }
    }
    private View getView(String name) {
        View view = null;
        try {
            view = this.getDatabaseInstance().getView(name);
        }
        catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return view;
    }

    public String login(String userName, String password) {

        createUsersByNameView();
        return getDocIdFromQuery(usersByNameView, userName, password);
    }

    private void createUsersByNameView() {
        usersByNameView = this.getView("usersByName");
        if (usersByNameView.getMap() == null) {
            usersByNameView.setMap(
                    new Mapper(){
                        @Override
                        public void map(Map<String, Object> document,
                                        Emitter emitter) {
                    /* Emit data to matieralized view */
                            emitter.emit(
                                    (String) document.get("first_name") + " " + document.get("last_name"), null);
                        }
                    }, "1.1" /* The version number of the mapper... */
            );
        }
    }
    private String getDocIdFromQuery(View view, String userName, String password) {
        // Get instance of Query from factory…
        Query orderedQuery = view.createQuery();
        orderedQuery.setDescending(true);
        orderedQuery.setStartKey(userName);
        try {
            QueryEnumerator results = orderedQuery.run();
       // Iterate through the rows to get the document ids
            for (Iterator<QueryRow> it = results; it.hasNext();) {
                QueryRow row = it.next();
                String docId = (String) row.getSourceDocumentId();
                Log.i(TAG, "DocID: " + docId);
                Log.i(TAG, "Given password: " + password);
                Log.i(TAG, "Password: " + row.getDocument().getProperty("password").toString());
                if(password.equals(row.getDocument().getProperty("password").toString())){
                    Log.i(TAG, "password found");
                    return docId;
                }
            }
            return "Access denied";
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error querying view.", e);
        }
        return null;
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException{
        if((this.database == null) & (this.manager != null)){
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
