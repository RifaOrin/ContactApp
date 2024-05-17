package com.example.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDB extends SQLiteOpenHelper {

    public ContactsDB(Context context) {
        super(context, "Contacts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE contacts  ("
                + "Name TEXT,"
                + "Email TEXT,"
                + "Phone1 TEXT,"
                + "Phone2 TEXT,"
                + "Photo TEXT"
                + ")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public long insertContact(String name, String email, String phone1, String phone2, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();
        cols.put("Name", name);
        cols.put("Email", email);
        cols.put("Phone1", phone1);
        cols.put("Phone2", phone2);
        cols.put("Photo", photo);
        long result = db.insert("contacts", null ,  cols);
        db.close();
        return result;
    }

    public int updateContact(String oldName, String newName, String email, String phone1, String phone2, String photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();
        cols.put("Name", newName);
        cols.put("Email", email);
        cols.put("Phone1", phone1);
        cols.put("Phone2", phone2);
        cols.put("Photo", photo);
        int result = db.update("contacts", cols, "Name=?", new String[ ] {oldName} );
        db.close();
        return result;
    }

    public int deleteContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("contacts", "Name=?", new String[ ] {name} );
        db.close();
        return result;
    }

    public Cursor selectContact(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = null;
        try {
            cur = db.rawQuery("SELECT * FROM contacts WHERE Name=?", new String[ ] {name});
        } catch (Exception e){
            e.printStackTrace();
        }
        return cur;
    }

    public Cursor selectAllContacts(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
        } catch (Exception e){
            e.printStackTrace();
        }
        return cursor;
    }
}
