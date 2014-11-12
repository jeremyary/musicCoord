package jary.drools.model;

/**
 * @author jary
 * @since Nov/07/2014
 */
public class DanceEvent {

    private Long timestamp;

    private Integer intensity;

    private String type;

    public DanceEvent() {
    }

    public DanceEvent(Long timestamp, Integer intensity, String type) {
        this.timestamp = timestamp;
        this.intensity = intensity;
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getIntensity() {
        return intensity;
    }

    public void setIntensity(Integer intensity) {
        this.intensity = intensity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
