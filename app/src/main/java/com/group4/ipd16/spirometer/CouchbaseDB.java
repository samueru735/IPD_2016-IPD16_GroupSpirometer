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
import com.couchbase.lite.UnsavedRevision;
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
public class CouchbaseDB  {
    private static final String USER_ID = "user_id";
    private static Manager manager = null;
    private static Database database = null;
    private static final String DB_NAME = "spiro_db";
    private static final String TAG = "dbEvents";
    private static Context context;
    private View usersByNameView = null;
    private String userId;
    private User currentUser;

private  static CouchbaseDB spriroDB = new CouchbaseDB();
  //  @Override
 /*   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpCouchbaseLiteDB();
       // context = getApplicationContext();
    }*/

    public static CouchbaseDB getSpiroDB(){
        if(spriroDB != null)
            return spriroDB;
        else {
            return new CouchbaseDB();
        }
    }

    public CouchbaseDB()// Context context)
     {
        // this.context = getApplicationContext();
    //    this.context = context;
      //  setUpCouchbaseLiteDB();
    }

    public void setUpCouchbaseLiteDB() {
        try{
            manager = new Manager( new AndroidContext(context), Manager.DEFAULT_OPTIONS);
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
    public void setCurrentUser(Document user) {
        Log.i(TAG, "Set user: " + user.getProperties());
        //int id, String first_name, String last_name, String doctor_email, int age, int height, int weight, String gender, String ethnicity
        currentUser = new User(userId, user.getProperty("first_name").toString(), user.getProperty("last_name").toString(), user.getProperty("mail_doctor").toString(),
                user.getProperty("age").toString(), user.getProperty("height").toString(), user.getProperty("weight").toString(), user.getProperty("gender").toString(), user.getProperty("ethnicity").toString());
    }
    public User getCurrentUser(){
        return currentUser;
    }
    public String getUserID(){
        return userId;
    }
    public User getUserById(String userId){
        Log.i(TAG, "user id: " + userId);
        Document userDocument = database.getDocument(userId);
        //return userDocument;
        setCurrentUser(userDocument);
        return getCurrentUser();
    }
    public void deleteDoc(String userid) {
        Document doc = database.getDocument(userid);
        try {
            doc.delete();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
    public void cleanDB(){
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext();){
                QueryRow row = it.next();
                Document doc = row.getDocument();
                if(String.valueOf(doc.getProperty("first_name")).equals(""))
                    doc.delete();
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public String getFvcFromId(String userId){
        Document doc = database.getDocument(userId);
        Map<String, Object> properties =  doc.getProperties();
        Log.i("TAG", "Properties: " + properties);
        String fvc = (String) properties.get("fvc");
        Log.i("TAG", "FVC: " + fvc);
        return fvc;
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
                Document doc = row.getDocument();
                Log.i(TAG, String.valueOf(row.getDocument().getProperties()));
                // check for 'empty' users
                if(String.valueOf(row.getDocument().getProperty("first_name")) == "" )
                    row.getDocument().delete();
                else{
                    users.add(String.valueOf(row.getDocument().getProperty("first_name")) + " " + String.valueOf(row.getDocument().getProperty("last_name")));
                    getUserById(doc.getId());
                }
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public void updateDoc(String documentId, final Map<String, Object> updatedProperties ){
        Document doc = database.getDocument(documentId);
        try {
            doc.update(new Document.DocumentUpdater() {
                @Override
                public boolean update(UnsavedRevision newRevision) {
                    Map<String, Object> properties =  newRevision.getUserProperties();
                    Log.i(TAG, "User properties: " + properties);
                    properties.putAll(updatedProperties);
                    newRevision.setUserProperties(properties);
                    return true;
                }
            });
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
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
                String docId = row.getSourceDocumentId();
                Log.i(TAG, "DocID: " + docId);
                Log.i(TAG, "Given password: " + password);
                try{
                    Log.i(TAG, "Password: " + row.getDocument().getProperty("password").toString());
                    if(password.equals(row.getDocument().getProperty("password").toString())){
                        userId = docId;
                        setCurrentUser(row.getDocument());
                        Log.i(TAG, "password found");
                        return docId;
                    }
                }
                catch (Exception e){
                    row.getDocument().delete();
                    return null;
                }

            }
            return "Access denied";
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error querying view.", e);
        }
        return null;
    }

  /*  private void saveUserId(String userId){
    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(USER_ID, MODE_PRIVATE);
    //SharedPreferences sharedPref = getSharedPreferences(USER_ID,MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString("user_id", userId);
    editor.commit();
    }*/

    public Database getDatabaseInstance() throws CouchbaseLiteException{
        if((this.database == null) & (this.manager != null)){
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        }
        return manager;
    }


    public void setContext(Context context) {
        this.context = context;
    }


}
