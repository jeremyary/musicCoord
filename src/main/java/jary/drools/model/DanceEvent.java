package jary.drools.model;

/**
 * @author jary
 * @since Nov/07/2014
 */
public class DanceEvent {

    private Long timestamp;

    private String type;

    private Integer[] position;

    private Integer intensity;

    public DanceEvent() {
    }

    public DanceEvent(final Long timestamp, final String type, final Integer[] position, final Integer intensity) {
        this.timestamp = timestamp;
        this.type = type;
        this.position = position;
        this.intensity = intensity;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public Integer[] getPosition() {
        return position;
    }

    public Integer getIntensity() {
        return intensity;
    }
}
