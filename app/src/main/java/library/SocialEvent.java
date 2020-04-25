package library;

/*
	A class to create social events

	@author Barış Ogün Yörük
	@date 12/12/2019
 */
public class SocialEvent extends Event {

    // Properties
    private String location;
    private int quota;

    public SocialEvent () {

    }

    /**
     * Main constructor for the social event
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
     */
    public SocialEvent( String id, String eventName, String notes, String hostId, Day eventDay, int startHour, int startMinute, int endHour , int endMinute, String location, int quota ) {
        super( id, eventName, notes, 1, hostId, eventDay, startHour, startMinute, endHour, endMinute);

        if ( location == null ) {
            throw new NullPointerException( "location cannot be null" );
        }

        if ( quota < 0 ) {
            throw new IllegalArgumentException( "quota cannot be smaller than 0" );
        }

        this.location = location;
        this.quota = quota;
    }

    /**
     * Getter for the location of the event
     * @return location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for the quota
     * @return quota of the event
     */
    public int getQuota() {
        return quota;
    }

}