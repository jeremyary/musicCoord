package jary.drools.model;

/**
 * @author jary
 * @since Nov/09/2014
 */
public class Interval {

    private Long timestamp;

    private Boolean expired;

    public Interval(Boolean expired) {
        this.timestamp = System.currentTimeMillis();
        this.expired = expired;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}

