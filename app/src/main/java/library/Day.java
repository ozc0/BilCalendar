package library;

/**
 * Day class that holds days
 */
public class Day implements Comparable<Day> {

    // instances
    private int dayNo;
    private int monthNo;
    private int yearNo;

    /**
     * Empty constructor
     */
    public Day() {

    }

    /**
     * Main constructor
     * @param dayNo day of the date
     * @param monthNo month of the date
     * @param yearNo year of the date
     */
    public Day(int dayNo, int monthNo, int yearNo) {
        this.dayNo = dayNo;
        this.monthNo = monthNo;
        this.yearNo = yearNo;
    }

    /**
     * Getter for the day
     * @return day of the date
     */
    public int getDayNo() {
        return dayNo;
    }

    /**
     * Getter for the month
     * @return month of the date
     */
    public int getMonthNo() {
        return monthNo;
    }

    /**
     * Getter for the year
     * @return year of the date
     */
    public int getYearNo() {
        return yearNo;
    }

    @Override
    public int compareTo(Day o) {
        if ( this.yearNo != o.getYearNo() )
            return this.getYearNo() - o.getYearNo();
        if ( this.getMonthNo() != o.getMonthNo() )
            return this.getMonthNo() - o.getMonthNo();
        return this.getDayNo()-o.getDayNo();
    }
}
