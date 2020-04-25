package com.example.bilcalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import database.DatabaseManager;
import library.Club;
import library.User;

/*
    A oontroller class to control activity_account_information_page.xml layout. In this class
    the user will be able to view her/his name, phone number, mail and clubs that he / she is admin
    to

    @author Team of Ministler
    @date 12/25/2019
 */
public class AccountInformationPageActivity extends AppCompatActivity {

    // Properties
    Context context = this;
    TextView nameViewForAccountInformationPage;
    TextView phoneNumberViewForAccountInformationPage;
    TextView mailViewForAccountInformatiınPage;
    ListView listViewOfClubs;
    private Toolbar toolbar;
    AccountInformationPageActivity thisIns = this;
    ArrayList<Club> clubsFromDatabase;

    /*
        A method view account information when activity is changed to this

        @param Bundle savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information_page);

        toolbar = findViewById(R.id.toolbar_for_account_information_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        listViewOfClubs = findViewById(R.id.list_view_for_account_information_page);

        nameViewForAccountInformationPage = findViewById(R.id.name_label_for_account_information_page);
        phoneNumberViewForAccountInformationPage = findViewById(R.id.phone_number_label_for_account_information_page);
        mailViewForAccountInformatiınPage = findViewById(R.id.mail_label_for_account_information_page);

        final DatabaseManager databaseManager = DatabaseManager.getInstance();

        clubsFromDatabase = databaseManager.getClubAdministrations();

        nameViewForAccountInformationPage.append( " " + databaseManager.getCurUser().getName() );
        phoneNumberViewForAccountInformationPage.append( " " + databaseManager.getCurUser().getPhoneNo() );
        mailViewForAccountInformatiınPage.append("" + databaseManager.getCurUser().getEmail() );
        TextView textForAddingNumber = findViewById(R.id.edit_phone_number);

        textForAddingNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("New Phone Number");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_NUMBER );
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        databaseManager.editPhoneNo(input.getText().toString());
                        startActivity(new Intent(AccountInformationPageActivity.this, AccountInformationPageActivity.class));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

        ArrayList<String> nameOfClubs = new ArrayList<>();

        for ( int i = 0; i < clubsFromDatabase.size(); i++ ) {
            nameOfClubs.add( clubsFromDatabase.get(i).getClubName() );
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nameOfClubs);

        listViewOfClubs.setAdapter( arrayAdapter );
        listViewOfClubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Email address of person that you want give admin to");

                final EditText input = new EditText(context);

                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = input.getText().toString();

                        ArrayList<User> users = databaseManager.getAllUsers();
                        ArrayList<String> usersMail = new ArrayList<>();

                        for ( int i = 0; i < users.size(); i++ ) {
                            usersMail.add( users.get(i).getEmail() );
                        }

                        Boolean isThereSuchEmail = false;

                        for ( int i = 0; i < usersMail.size(); i++ ) {
                            if ( usersMail.get(i).equals(str) ) {
                                isThereSuchEmail = true;
                                break;
                            }
                        }



                        if (isThereSuchEmail)
                            databaseManager.addNewAdmin(  clubsFromDatabase.get(position).getClubId(),  databaseManager.idOfEmail(str));
                        else
                            Toast.makeText( thisIns,"There is no such user", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }

}