package com.example.mycontacts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private ArrayList<Contact_item> contacts;
    private ListView lvContactList;
    private ContactListAdapter clAdapter;
    private Button btnExit, btnAdd;
    private ContactsDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        db = new ContactsDB(this);

        lvContactList = findViewById(R.id.listViewItems);
        btnAdd = findViewById(R.id.btnAdd);
        btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, ContactFormActivity.class);
                startActivity(intent);
                finish();
            }
        });

        contacts = new ArrayList<>();
        clAdapter = new ContactListAdapter(this, contacts);
        lvContactList.setAdapter(clAdapter);
        loadContacts();

        lvContactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popup = new PopupMenu(ContactListActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Contact_item contact = contacts.get(position);
                        if (item.getItemId() == R.id.update) {
                            Intent intent = new Intent(ContactListActivity.this, ContactFormActivity.class);
                            intent.putExtra("Name", contact.Name);
                            intent.putExtra("Email", contact.Email);
                            intent.putExtra("Phone1", contact.Phone1);
                            intent.putExtra("Phone2", contact.Phone2);
                            startActivity(intent);
                            finish();
                            return true;

                        } else if (item.getItemId() == R.id.delete) {
                            // Call your method to delete contact
                            int deleteResult = db.deleteContact(contact.Name);
                            if (deleteResult > 0) {
                                Toast.makeText(ContactListActivity.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                                recreate();
                            } else {
                                Toast.makeText(ContactListActivity.this, "Error deleting contact", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                popup.show(); // showing popup menu
                return true; // return true to indicate that we've consumed the long click event
            }
        });

    }

    private void loadContacts() {

        String q = "SELECT * FROM contacts;";
        ContactsDB db = new ContactsDB(this);
        Cursor cursor = db.selectAllContacts(q);
        contacts.clear();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String Name = cursor.getString(0);
                String Email = cursor.getString(1);
                String Phone1 = cursor.getString(2);
                String Phone2 = cursor.getString(3);
                String Photo = cursor.getString(4);
                Contact_item c = new Contact_item(Name, Email, Phone1, Phone2, Photo);
                contacts.add(c);
            }
            clAdapter.notifyDataSetInvalidated();
            clAdapter.notifyDataSetChanged();
        }
    }

}