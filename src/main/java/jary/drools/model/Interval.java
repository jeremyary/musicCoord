package jary.drools.model;

/**
 * @author jary
 * @since Nov/09/2014
 */
public class Interval {

    private Integer interval;

    private Integer variation;

    public Interval(Integer interval) {
        this.interval = interval;
        this.variation = 500;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getVariation() {
        return variation;
    }

    public void setVariation(Integer variation) {
        this.variation = variation;
    }
}

