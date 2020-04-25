package library;


import java.util.ArrayList;

/**
 * Club class that holds informations about a club
 * @author Oguzhan Ozcelik
 */
public class Club {

    // instances
    private String clubId;
    private String clubName;

    /**
     * Empty constructor
     */
    public Club() {
    }

    /**
     * Main constructor that creates new club object
     * @param clubId id of the club
     * @param clubName name of the club
     */
    public Club ( String clubId , String clubName ) {
        this.clubId = clubId;
        this.clubName = clubName;
    }

    /**
     * Getter for clubId
     * @return id of the club
     */
    public String getClubId() {
        return clubId;
    }

    /**
     * Getter for the clubName
     * @return name of the club
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * Setter for the clubName
     * @param name new name of the club
     */
    public void setName(String name) {
        this.clubName = name;
    }
}
