package database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import library.Club;
import library.ClubEvent;
import library.Day;
import library.Event;
import library.PersonalEvent;
import library.SocialEvent;
import library.User;

/**
 * Database Manager that does all the operations regarding the database such as adding new user,
 * adding new event, attending an event, adding new club managements and so on...
 * @author Oguzhan Ozcelik
 */
public class DatabaseManager {

    // static variable that holds the one and only DatabaseManager object of the application
    private static DatabaseManager mInstance = null;

    // references of database tables
    private DatabaseReference userDB;
    private DatabaseReference eventDB;
    private DatabaseReference clubDB;
    private DatabaseReference partDB;
    private DatabaseReference hostedDB;
    private DatabaseReference attendedDB;
    private DatabaseReference adminsOfUserDB;
    private DatabaseReference adminsOfClubDB;

    // storage variables
    private HashMap<String,User> allUsers;
    private HashMap<String,Club> allClubs;
    private HashMap<String,Event> allEvents;
    private HashMap<String,String> findIdByEmail;
    private HashMap<String,ArrayList<String>> adminsOfClubs;
    private HashMap<String,ArrayList<String>> participantsOfEvent;
    private HashMap<String,ArrayList<String>> attendedEventsOfUser;
    private HashMap<String,ArrayList<String>> hostedEvents;
    private HashMap<String,ArrayList<String>> administrationsOfUser;
    private HashMap<String,Event> globalEvents;

    // current user's id
    private String curUserId = null;

    /**
     * Empty constructor of the DatabaseManager class
     */
    public DatabaseManager () {
        if ( mInstance == null )
            mInstance = this;
    }

    /**
     * Main constructor of the class
     * Sets up the environment after starting the application
     * @param tmp main page's context reference
     */
    public DatabaseManager ( Context tmp ) {

        if ( mInstance == null )
            mInstance = this;
        userDB = FirebaseDatabase.getInstance().getReference("users");
        eventDB = FirebaseDatabase.getInstance().getReference("events");
        clubDB = FirebaseDatabase.getInstance().getReference("clubs");
        partDB = FirebaseDatabase.getInstance().getReference("participants");
        hostedDB = FirebaseDatabase.getInstance().getReference("hostedEvents");
        attendedDB = FirebaseDatabase.getInstance().getReference("attendedEvents");
        adminsOfUserDB = FirebaseDatabase.getInstance().getReference("administrationsOfUsers");
        adminsOfClubDB = FirebaseDatabase.getInstance().getReference("adminsOfClubs");

        allUsers = new HashMap<>();
        allEvents = new HashMap<>();
        allClubs = new HashMap<>();
        findIdByEmail = new HashMap<>();
        adminsOfClubs = new HashMap<>();
        participantsOfEvent = new HashMap<>();
        attendedEventsOfUser = new HashMap<>();
        hostedEvents = new HashMap<>();
        administrationsOfUser = new HashMap<>();
        globalEvents = new HashMap<>();

        addUsersEventListener();
        addEventsEventListener();
        addClubsEventListener();
        addParticipantEventListener();
        addHostedEventsListener();
        addAttendedEventsListener();
        addAdministrationsOfUsersListener();
        addAdminsOfClubsListeners ();
    }

    /**
     * A method that returns the instance of DatabaseManager
     * @return instance of DatabaseManager object
     */
    public static synchronized DatabaseManager getInstance() {
        if ( null == mInstance )
            mInstance = new DatabaseManager();
        return mInstance;
    }

    /**
     * A method to get all global at given date in sorted order
     * @param day day of the date
     * @param month month of the date
     * @param year year of the date
     * @return all global events at given date as ArrayList of Event object
     */
    public ArrayList<Event> getGlobalEventsAtDay ( int day, int month, int year ) {
        ArrayList<Event> tmp = new ArrayList<>();
        ArrayList<Event> ret = new ArrayList<>();
        Day curDay = new Day ( day , month , year );

        if ( tmp == null )
            return ret;

        for ( String key : globalEvents.keySet() )
            tmp.add ( globalEvents.get(key) );

        for ( int i = 0 ; i < tmp.size() ; i++ )
            if ( tmp.get(i).getEventDay().compareTo( curDay ) == 0 )
                ret.add ( tmp.get (i) );

        Collections.sort(ret);
        return ret;
    }

    /**
     * A method to get all attending events user is planning to attend at given date in sorted order
     * @param day day of the date
     * @param month month of the date
     * @param year year of the date
     * @return all attending events at given date as ArrayList of Event object
     */
    public ArrayList<Event> getAttendedEventsAtDay ( int day, int month, int year ) {
        ArrayList<Event> tmp = getAttendedEvents();
        if ( tmp == null ) {
            tmp = new ArrayList<>();
            return tmp;
        }

        ArrayList<Event> ret = new ArrayList<>();
        Day curDay = new Day ( day , month , year );

        for ( int i = 0 ; i < tmp.size() ; i++ )
            if ( tmp.get(i).getEventDay().compareTo( curDay ) == 0 )
                ret.add ( tmp.get (i) );

        Collections.sort(ret);
        return ret;
    }

    /**
     * A method to check if user is planning to attend to given event
     * @param userId id of the user
     * @param eventId id of the event
     * @return true if user is attending, false otherwise
     */
    public boolean isAttendedToEvent ( String userId, String eventId ) {
        boolean ret = false;

        ArrayList<String>  tmp = attendedEventsOfUser.get(userId);

        if ( tmp == null )
            return ret;

        for ( String event : tmp )
            if ( event.compareTo( eventId ) == 0 )
                ret = true;

        return ret;
    }

    /**
     * gets the Event class type of the id of the event
     * @param id id of the event
     * @return Event class type version
     */
    public Event getEvent ( String id ) {
        return allEvents.get(id);
    }

    /**
     * gets the User class type of the id of the user
     * @param id id of the user
     * @return User class type version
     */
    public User getUser ( String id ) {
        return  allUsers.get(id);
    }

    /**
     * gets the Club class type of the id of the club
     * @param id id of the club
     * @return Club class type version
     */
    public Club getClub ( String id ) { return allClubs.get(id); }

    /**
     * A method that adds a club to database
     * @param name given name of the club
     * @param user creator of the club
     * @return Club class version of the created club
     */
    public Club addClub ( String name , User user ) {

        String id = clubDB.push().getKey();
        Club club = new Club ( id , name  );

        clubDB.child(id).setValue(club);
        adminsOfClubDB.child(id).child(user.getId()).setValue(true);
        adminsOfUserDB.child(user.getId()).child(id).setValue(true);

        return club;
    }

    /**
     * Administrator adder to the specific club
     * @param clubId id of the club
     * @param userId id of the user that will be administrator
     */
    public void addNewAdmin ( String clubId , String userId ) {
        adminsOfClubDB.child(clubId).child(userId).setValue(true);
        adminsOfUserDB.child(userId).child(clubId).setValue(true);
    }

    /**
     * A method that adds the new registered user to the database
     * @param name name of the user
     * @param phone phoneNo of the user
     * @param id id of the user ( determined by database )
     * @param email e-mail address of the user
     * @return User class version of the newly registered user
     */
    public User addUser ( String name , String phone , String id , String email) {

        User user = new User ( name , id , phone , email );
        userDB.child(id).setValue(user);
        return user;
    }

    /**
     * Getter for all the ids of the administrators of the club
     * @param id id of the club
     * @return all administrators of the club
     */
    public ArrayList<User> getClubAdmins ( String id ) {
        ArrayList<User> ret = new ArrayList<>();
        ArrayList<String> tmp = adminsOfClubs.get(id);

        if ( tmp == null )
            return ret;

        for ( int i = 0 ; i < tmp.size() ; i++  )
            ret.add(getUser(tmp.get(i)));

        return ret;
    }

    /**
     * Getter for all the participants of the event
     * @param id id of the event
     * @return All the users that are attending to the event
     */
    public ArrayList<User> getParicipants ( String id ) {
        ArrayList<User> ret = new ArrayList<>();
        ArrayList<String> tmp = participantsOfEvent.get(id);

        if ( tmp == null )
            return ret;

        for ( int i = 0 ; i < tmp.size() ; i++ )
            ret.add ( getUser( tmp.get(i) ) );
        return ret;
    }

    /**
     * Getter for all the events that user is planning to attend
     * @return all the events as ArrayList
     */
    public ArrayList<Event> getAttendedEvents () {
        ArrayList<Event> ret = new ArrayList<>();
        ArrayList<String> tmp = attendedEventsOfUser.get ( curUserId );

        if ( tmp == null )
            return ret;

        for ( int i = 0 ; i < tmp.size() ; i++ )
            ret.add ( allEvents.get( tmp.get(i) ) );

        Collections.sort(ret);
        return ret;
    }

    /**
     * Getter for all the hosted events of the user
     * @return all the hosted events of the user as ArrayList
     */
    public ArrayList<Event> getHostedEvents () {
        ArrayList<Event> ret = new ArrayList<>();
        ArrayList<String> tmp = hostedEvents.get ( curUserId );

        if ( tmp == null )
            return ret;

        for ( int i = 0 ; i < tmp.size() ; i++ )
            ret.add ( allEvents.get( tmp.get(i) ) );

        Collections.sort(ret);
        return ret;
    }

    /**
     * Getter for all Available events for the user both personal and global
     * @return all available events for the user as ArrayList
     */
    public ArrayList<Event> getAllAvailableEvents () {
        ArrayList<Event> ret = new ArrayList<>();
        ArrayList<Event> tmp = new ArrayList<>();

        for ( String key : allEvents.keySet() )
            tmp.add ( allEvents.get(key) );

        for ( int i = 0 ; i < tmp.size() ; i++ ) {
            if (tmp.get(i).getEventType() > 0 || tmp.get(i).getHostId().compareTo( curUserId ) == 0)
                ret.add(tmp.get(i));
        }

        Collections.sort ( ret );

        return ret;
    }

    /**
     * Getter for the number of the participant of the event
     * @param eventId id of the event
     * @return number of participants of the event
     */
    public int noOfParticipantsOfEvent ( String eventId  ) {

        ArrayList<String> tmp = participantsOfEvent.get(eventId);
        int ret = 0;

        if ( tmp == null )
            return ret;
        return tmp.size();
    }

    /**
     * Getter for hosted events of the user at given date
     * @param day day of the date
     * @param month month of the date
     * @param year year of the date
     * @return hosted events at given date as ArrayList
     */
    public ArrayList<Event> getHostedEventsAtDay ( int day, int month, int year ) {

        ArrayList<Event> tmp = getHostedEvents();
        if ( tmp == null ) {
            tmp = new ArrayList<>();
            return tmp;
        }

        ArrayList<Event> ret = new ArrayList<>();
        Day curDay = new Day ( day , month , year );

        for ( int i = 0 ; i < tmp.size() ; i++ )
            if ( tmp.get(i).getEventDay().compareTo( curDay ) == 0 )
                ret.add ( tmp.get (i) );

        Collections.sort(ret);
        return ret;
    }

    /**
     * Getter for all the administrations of the given user
     * @return all the clubs that user is administrator as ArrayList
     */
    public ArrayList<Club> getClubAdministrations () {
        ArrayList<Club> ret = new ArrayList<>();
        ArrayList<String> tmp = administrationsOfUser.get ( curUserId );

        if (tmp == null)
            return ret;

        for ( int i = 0 ; i < tmp.size() ; i++ )
            ret.add ( allClubs.get( tmp.get(i) ) );
        return ret;
    }

    /**
     * A method that cancels and remove the given event
     * @param idOfEvent id of the event
     */
    public void cancelHostedEvent ( String idOfEvent ) {
        ArrayList<String> tmp = participantsOfEvent.get(idOfEvent);
        for ( int i = 0 ; i < tmp.size() ; i++ )
            removeParticipantFromEvent( idOfEvent , tmp.get(i) );
        userDB.child( allEvents.get(idOfEvent).getHostId() ).child("hostedEvents").child(idOfEvent).removeValue();
        eventDB.child( idOfEvent ).removeValue();
    }

    /**
     * A method that adds user to an event participants
     * @param eventId id of the event
     * @param userId id of the user
     */
    public void addParticipantToEvent ( String eventId, String userId ) {

        partDB.child(eventId).child(userId).setValue(true);
        attendedDB.child(userId).child(eventId).setValue(true);

    }

    /**
     * A method that removes participant from an event he/she is attending
     * @param eventId id of the event
     * @param userId id of the user
     */
    public void removeParticipantFromEvent ( String eventId, String userId  ) {

        partDB.child(eventId).child(userId).removeValue();
        attendedDB.child(userId).child(eventId).removeValue();

    }

    /**
     * A method to add a new club event to the database
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay day of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param location location of the event
     * @param quota maximum amount of people that can attend to the event
     * @param club club of the event
     * @return ClubEvent class type object of the newly created event
     */
    public ClubEvent addClubEvent (String eventName, String notes, String hostId
            , Day eventDay, int startHour, int startMinute, int endHour
            , int endMinute, String location, int quota, Club club ) {

        String id = eventDB.push().getKey();
        ClubEvent clubEvent = new ClubEvent( id , eventName , notes, hostId ,eventDay
                ,startHour, startMinute , endHour , endMinute , location , quota , club.getClubId());

        eventDB.child(id).setValue(clubEvent);
        addParticipantToEvent(id , hostId);
        hostedDB.child(hostId).child(id).setValue(true);

        return clubEvent;
    }

    /**
     * A method to add a new personal event to the database
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay day of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param repetitiveValue 0 if it is one-time, 1 if it is weekly, 2 if it is monthly
     * @return PersonalEvent class type object of the newly created event
     */
    public PersonalEvent addPersonalEvent (String eventName, String notes, String hostId
            , Day eventDay, int startHour, int startMinute, int endHour
            , int endMinute, int repetitiveValue ) {

        String id = eventDB.push().getKey();
        PersonalEvent personalEvent = new PersonalEvent( id , eventName , notes, hostId ,eventDay
                ,startHour, startMinute , endHour , endMinute, repetitiveValue );

        eventDB.child(id).setValue(personalEvent);
        addParticipantToEvent(id , hostId);
        hostedDB.child(hostId).child(id).setValue(true);

        return personalEvent;
    }

    /**
     * A method to add a new social event to the database
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay day of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param location location of the event
     * @param quota maximum amount of people that can attend to the event
     * @return SocialEvent class type object of the newly created event
     */
    public SocialEvent addSocialEvent (String eventName, String notes, String hostId
            , Day eventDay, int startHour, int startMinute, int endHour
            , int endMinute, String location, int quota  ) {

        String id = eventDB.push().getKey();
        SocialEvent socialEvent = new SocialEvent ( id , eventName , notes, hostId ,eventDay
                ,startHour, startMinute , endHour , endMinute, location, quota );

        eventDB.child(id).setValue(socialEvent);
        addParticipantToEvent(id , hostId);
        hostedDB.child(hostId).child(id).setValue(true);

        return socialEvent;
    }

    /**
     * Takes the email address of the user and returns id of the user
     * @param email e-mail address of the user
     * @return id of the user
     */
    public String idOfEmail( String email ) {
        if ( findIdByEmail.containsKey(email) )
            return findIdByEmail.get ( email );
        return null;
    }

    /**
     * Sets the current user when application starts and user logs in
     * @param user current user that is logged in
     */
    public void setCurUser ( User user ) {
        curUserId = user.getId();
    }

    /**
     * A method that edits the phone number of the current user
     * @param phoneNo new phone number of the current user
     */
    public void editPhoneNo ( String phoneNo ) {

        String id = getCurUser().getId();
        userDB.child(id).child("phoneNo").setValue( phoneNo );
    }

    /**
     * Getter for the current user
     * @return User class type of the current user
     */
    public User getCurUser () {
        return getUser( curUserId );
    }

    /**
     * Getter for all the users that is in the database
     * @return all the users as ArrayList
     */
    public ArrayList<User> getAllUsers () {

        ArrayList<User> ar = new ArrayList<>();
        for ( String key : allUsers.keySet() )
            ar.add ( allUsers.get(key) );

        return ar;
    }

    /**
     * Event listener for the users table of the database
     */
    private void addUsersEventListener () {
        userDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue ( User.class );
                findIdByEmail.put(user.getEmail(),user.getId());
                allUsers.put ( user.getId() , user  );

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue ( User.class );
                findIdByEmail.put(user.getEmail(),user.getId());
                allUsers.put ( user.getId() , user  );
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue ( User.class );
                findIdByEmail.put(user.getEmail(),null);
                allUsers.put ( user.getId() , null  );

                attendedEventsOfUser.remove(user.getId());
                hostedEvents.remove(user.getId());
                administrationsOfUser.remove(user.getId());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the adminsOfClubs table of the database
     */
    private void addAdminsOfClubsListeners () {
        adminsOfClubDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String clubId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    tmp2.add(user.getKey());
                }

                adminsOfClubs.put(clubId,tmp2);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String clubId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    tmp2.add(user.getKey());
                }

                adminsOfClubs.put(clubId,tmp2);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String clubId = dataSnapshot.getKey();

                adminsOfClubs.put(clubId,null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the administrations table of the database
     */
    private void addAdministrationsOfUsersListener () {
        adminsOfUserDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot club : dataSnapshot.getChildren()) {
                    tmp2.add(club.getKey());
                }

                administrationsOfUser.put(userId,tmp2);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot club : dataSnapshot.getChildren()) {
                    tmp2.add(club.getKey());
                }

                administrationsOfUser.put(userId,tmp2);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();

                administrationsOfUser.put(userId,null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the attendedEvents table of the database
     */
    private void addAttendedEventsListener () {
        attendedDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    tmp2.add(event.getKey());
                }

                attendedEventsOfUser.put(userId,tmp2);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    tmp2.add(event.getKey());
                }

                attendedEventsOfUser.put(userId,tmp2);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();

                attendedEventsOfUser.put(userId,null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the hostedEvents table of the database
     */
    private void addHostedEventsListener () {

        hostedDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    tmp2.add(event.getKey());
                }

                hostedEvents.put(userId,tmp2);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String userId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot event : dataSnapshot.getChildren()) {
                    tmp2.add(event.getKey());
                }

                hostedEvents.put(userId,tmp2);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();

                hostedEvents.put(userId,null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Event listener for the participants table of the database
     */
    private void addParticipantEventListener () {
        partDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String eventId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot participants : dataSnapshot.getChildren()) {
                    tmp2.add(participants.getKey());
                }

                participantsOfEvent.put(eventId,tmp2);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String eventId = dataSnapshot.getKey();
                ArrayList<String> tmp2 = new ArrayList<>();
                for (DataSnapshot participants : dataSnapshot.getChildren()) {
                    tmp2.add(participants.getKey());
                }

                participantsOfEvent.put(eventId,tmp2);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String eventId = dataSnapshot.getKey();

                participantsOfEvent.put(eventId,null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the events table of the database
     */
    private void addEventsEventListener() {
        eventDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HashMap<String,Object> tmp = (HashMap<String,Object>) dataSnapshot.getValue();
                int eventType = ((Long) tmp.get("eventType")).intValue();

                ObjectMapper mapper = new ObjectMapper();
                GenericTypeIndicator<Map<String,Object>> indicator = new GenericTypeIndicator<Map<String, Object>>() {};

                if ( eventType == 0 ) {
                    PersonalEvent personalEvent = mapper.convertValue(dataSnapshot.getValue(indicator), PersonalEvent.class);
                    allEvents.put(personalEvent.getEventId() , personalEvent);


                }
                if ( eventType == 1 ) {
                    SocialEvent socialEvent = mapper.convertValue(dataSnapshot.getValue(indicator), SocialEvent.class);
                    allEvents.put(socialEvent.getEventId() , socialEvent);

                    globalEvents.put(socialEvent.getEventId(), socialEvent);
                }
                if ( eventType == 2 ) {
                    ClubEvent clubEvent = mapper.convertValue(dataSnapshot.getValue(indicator), ClubEvent.class);
                    allEvents.put(clubEvent.getEventId() , clubEvent);

                    globalEvents.put(clubEvent.getEventId(), clubEvent);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                HashMap<String,Object> tmp = (HashMap<String,Object>) dataSnapshot.getValue();
                int eventType = ((Long) tmp.get("eventType")).intValue();

                ObjectMapper mapper = new ObjectMapper();
                GenericTypeIndicator<Map<String,Object>> indicator = new GenericTypeIndicator<Map<String, Object>>() {};

                if ( eventType == 0 ) {
                    PersonalEvent personalEvent = mapper.convertValue(dataSnapshot.getValue(indicator), PersonalEvent.class);
                    allEvents.put(personalEvent.getEventId() , personalEvent);

                }
                if ( eventType == 1 ) {
                    SocialEvent socialEvent = mapper.convertValue(dataSnapshot.getValue(indicator), SocialEvent.class);
                    allEvents.put(socialEvent.getEventId() , socialEvent);

                    globalEvents.put(socialEvent.getEventId(), socialEvent);
                }
                if ( eventType == 2 ) {
                    ClubEvent clubEvent = mapper.convertValue(dataSnapshot.getValue(indicator), ClubEvent.class);
                    allEvents.put(clubEvent.getEventId() , clubEvent);

                    globalEvents.put(clubEvent.getEventId(), clubEvent);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> tmp = (HashMap<String,Object>) dataSnapshot.getValue();
                int eventType = ((Long) tmp.get("eventType")).intValue();

                ObjectMapper mapper = new ObjectMapper();
                GenericTypeIndicator<Map<String,Object>> indicator = new GenericTypeIndicator<Map<String, Object>>() {};

                if ( eventType == 0 ) {
                    PersonalEvent personalEvent = mapper.convertValue(dataSnapshot.getValue(indicator), PersonalEvent.class);
                    allEvents.put(personalEvent.getEventId() , null);

                    if ( participantsOfEvent.containsKey(personalEvent.getEventId()) )
                        participantsOfEvent.remove ( personalEvent.getEventId() );
                }
                if ( eventType == 1 ) {
                    SocialEvent socialEvent = mapper.convertValue(dataSnapshot.getValue(indicator), SocialEvent.class);
                    allEvents.put(socialEvent.getEventId() , null);

                    if ( participantsOfEvent.containsKey(socialEvent.getEventId()) )
                        participantsOfEvent.remove ( socialEvent.getEventId() );
                    if ( globalEvents.containsKey(socialEvent.getEventId()) )
                        globalEvents.remove(socialEvent.getEventId());
                }
                if ( eventType == 2 ) {
                    ClubEvent clubEvent = mapper.convertValue(dataSnapshot.getValue(indicator), ClubEvent.class);
                    allEvents.put(clubEvent.getEventId() , null);

                    if ( participantsOfEvent.containsKey(clubEvent.getEventId()) )
                        participantsOfEvent.remove ( clubEvent.getEventId() );
                    if ( globalEvents.containsKey(clubEvent.getEventId()) )
                        globalEvents.remove(clubEvent.getEventId());
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Event listener for the clubs table of the database
     */
    private void addClubsEventListener() {
        clubDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club club = dataSnapshot.getValue ( Club.class );
                allClubs.put( club.getClubId() , club );
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club club = dataSnapshot.getValue ( Club.class );
                allClubs.put( club.getClubId() , club );

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Club club = dataSnapshot.getValue ( Club.class );
                allClubs.put( club.getClubId() , null );

                adminsOfClubs.put(club.getClubId(),null);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}