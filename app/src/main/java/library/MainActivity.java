package library;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bilcalendar.MainMenu;
import com.example.bilcalendar.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import database.DatabaseManager;

/**
 * Main Activity of the event
 */
public class MainActivity extends AppCompatActivity {


    // properties
    private static final int MY_REQUEST_CODE = 5101;
    DatabaseManager databaseManager;
    FirebaseAuth auth;
    List<AuthUI.IdpConfig> providers;
    User user;
    User curUser = null;
    boolean ready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //////////////////////
        ready = false;

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        auth = FirebaseAuth.getInstance();

        showSignIn();

        databaseManager = new DatabaseManager( this );
        //////////

    }

    /**
     * Sign in method for the application
     */
    private void showSignIn() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Base_MyTheme)
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == MY_REQUEST_CODE ) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if ( resultCode == RESULT_OK ) {
                // get user
                FirebaseUser user = auth.getCurrentUser();
                if ( databaseManager.getUser( user.getUid() ) == null ) {
                    curUser = databaseManager.addUser( user.getDisplayName() , "" , user.getUid() , user.getEmail() );
//                    Toast.makeText(this,"New user being added to database",Toast.LENGTH_LONG).show();
                }

                Toast.makeText(this,user.getEmail(),Toast.LENGTH_SHORT).show();
                ready = true;
                onStart();
            }
            else {
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        if ( !ready )
            return;

        Toast.makeText(this,"ON START",Toast.LENGTH_SHORT).show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if ( curUser == null )
            curUser = databaseManager.getUser( firebaseUser.getUid() );

        databaseManager.setCurUser( curUser );

        startActivity(new Intent(MainActivity.this, MainMenu.class));

    }

}
