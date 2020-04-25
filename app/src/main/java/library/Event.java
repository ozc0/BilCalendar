package library;


/**
 * Event template for all the events
 * @author Ministler tea,
 */
public class Event  implements Comparable<Event>{

    // Properties
    private String eventId;
    private String eventName;
    private String notes;
    private Day eventDay;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;
    private int eventType;
    private String hostId;

    /**
     * Empty constructor
     */
    public Event() {
    }

    /**
     * Main constructor for the event
     * @param eventId id of the event
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay date of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param eventType type of the event ( 0 = personal , 1 = social , 2 = club )
     */
    public Event( String eventId, String eventName, String notes, int eventType, String hostId, Day eventDay, int startHour, int startMinute, int endHour , int endMinute) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.notes = notes;
        this.eventType = eventType;
        this.hostId = hostId;
        this.eventDay = eventDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    /**
     * Getter of the eventName
     * @return name of the event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Getter for the notes
     * @return notes of the event
     */
    public String getNotes() {
        return notes;
    }


    /**
     * Getter for the starting hour
     * @return starting hour of the event
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * Getter for the starting minute
     * @return starting minute of the event
     */
    public int getStartMinute() {
        return startMinute;
    }

    /**
     * Getter for the ending hour
     * @return ending hour of the event
     */
    public int getEndHour () {
        return endHour;
    }

    /**
     * Getter for the ending minute
     * @return ending minute of the event
     */
    public int getEndMinute () {
        return endMinute;
    }

    /**
     * Getter for the eventType
     * @return type of the event as integer ( 0 = personal , 1 = social , 2 = club )
     */
    public int getEventType() {
        return eventType;
    }


    /**
     * Setter for the eventName
     * @param newName new name of the event
     * @return true if it is valid, otherwise false
     */
    public boolean setEventName( String newName ) {

        if ( newName == null && newName.length() < 5 ) {
            return false;
        }
        else {
            this.eventName = newName;
            return true;
        }

    }

    /**
     * Setter for the notes of the event
     * @param newNotes new notes of the event
     * @return true if it is valid, otherwise false
     */
    public boolean setNotes( String newNotes ) {

        if ( newNotes == null ) {
            return false;
        }
        else {
            this.notes = newNotes;
            return true;
        }

    }

    /**
     * Getter for the id of the event
     * @return id of the event
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Getter for the day of the event
     * @return day of the event
     */
    public Day getEventDay() {
        return eventDay;
    }

    /**
     * Getter for the id of the host of the event
     * @return id of the host of the event
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * Setter for the day of the event
     * @param eventDay day of the event
     */
    public void setEventDay(Day eventDay) {
        this.eventDay = eventDay;
    }

    /**
     * Setter for the starting hour
     * @param startHour new starting hour of the event
     */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    /**
     * Setter for the ending hour
     * @param endHour new ending hour of the event
     */
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    /**
     * Setter for the starting minute
     * @param startMinute new starting minute of the event
     */
    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    /**
     * Setter for the ending minute
     * @param endMinute new ending minute of the event
     */
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    /**
     * Setter for the event type
     * @param eventType new type of the event
     */
    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    /**
     * Setter for the id of the host
     * @param hostId id of the host
     */
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    @Override
    public int compareTo(Event o) {
        if ( this.getEventDay().getYearNo() != o.getEventDay().getYearNo() )
            return this.getEventDay().getYearNo() - o.getEventDay().getYearNo();
        if ( this.getEventDay().getMonthNo() != o.getEventDay().getMonthNo() )
            return this.getEventDay().getMonthNo() - o.getEventDay().getMonthNo();
        if ( this.getEventDay().getDayNo() != o.getEventDay().getDayNo() )
            return this.getEventDay().getDayNo() - o.getEventDay().getDayNo();
        if (  this.getStartHour() != o.getStartHour() )
            return this.getStartHour() - o.getStartHour();
        return this.getStartMinute() - o.getStartMinute();
    }
}