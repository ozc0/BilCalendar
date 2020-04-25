package library;


/**
 * Personal Event class
 */
public class PersonalEvent extends Event {

    // Properties
    private int repetitiveValue;

    /**
     * Empty constructor of the class
     */
    public PersonalEvent() {

    }

    /**
     * Main constructor for the personal event
     * @param id id of the event
     * @param eventName name of the event
     * @param notes notes for the event
     * @param hostId id of the host user
     * @param eventDay date of the event
     * @param startHour starting hour of the event
     * @param startMinute starting minute of the event
     * @param endHour ending hour of the event
     * @param endMinute ending minute of the event
     * @param repetitiveValue repetitive value for the event ( 0 = one-time , 1 = weekly , 2 = monthly )
     */
    public PersonalEvent(  String id, String eventName, String notes, String hostId, Day eventDay
            , int startHour, int startMinute, int endHour, int endMinute, int repetitiveValue ) {
        super( id, eventName, notes, 0, hostId, eventDay, startHour, startMinute, endHour, endMinute );

        if ( repetitiveValue < 0 || repetitiveValue > 4 ) {
            throw new IllegalArgumentException( "Repetitive value cannot be smaller than 0 or bigger than 4" );
        }

        this.repetitiveValue = repetitiveValue;
    }

    /**
     * Getter for the repetitive value
     * @return repetitive value of the event
     */
    public int getRepetitiveValue() {
        return repetitiveValue;
    }
}