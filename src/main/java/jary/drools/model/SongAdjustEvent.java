package jary.drools.model;

/**
 * @author jary
 * @since Nov/07/2014
 */
public class SongAdjustEvent {

    protected String title;

    protected Long timestamp;

    protected Long offsetInMillis;

    protected Double rateAdjustFactor;

    public SongAdjustEvent() {
    }

    public SongAdjustEvent(String title, Long timestamp, Long offsetInMillis, Double rateAdjustFactor) {
        this.title = title;
        this.timestamp = timestamp;
        this.offsetInMillis = offsetInMillis;
        this.rateAdjustFactor = rateAdjustFactor;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Long getOffsetInMillis() {
        return offsetInMillis;
    }

    public Double getRateAdjustFactor() {
        return rateAdjustFactor;
    }
}
