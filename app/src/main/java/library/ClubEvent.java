package library;


/**
 * Class for the club events
 */
public class ClubEvent extends Event {

    // Properties
    private String clubOfEvent;
    private String location;
    private int quota;

    /**
     * empty constructor
     */
    public ClubEvent() {
        super();
    }

    /**
     * Main constructor for the club event
     * @param id id of the event
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay date of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param location location of the event
     * @param quota quota of the event
     * @param clubb id of the club
     */
    public ClubEvent(String id, String eventName, String notes, String hostId, Day eventDay, int startHour, int startMinute, int endHour , int endMinute, String location, int quota, String clubb) {
        super( id, eventName, notes, 2, hostId, eventDay, startHour, startMinute, endHour, endMinute);

        this.clubOfEvent = clubb;
        this.location = location;
        this.quota = quota;
    }

    /**
     * Getter for the club of the event
     * @return id of the club of the event
     */
    public String getClubOfEvent() {
        return clubOfEvent;
    }

    /**
     * Getter for the location
     * @return location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for the quota of the event
     * @return quota of the event
     */
    public int getQuota() {
        return quota;
    }

    /**
     * Setter for the location of the event
     * @param location new location of the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Setter for the quota
     * @param quota new quota
     */
    public void setQuota(int quota) {
        this.quota = quota;
    }

    /**
     * Setter for the club of the event
     * @param clubb clubb of the event
     */
    public void setClubOfEvent(String clubb) {
        this.clubOfEvent = clubb;
    }
}