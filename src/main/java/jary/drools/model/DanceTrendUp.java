package jary.drools.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jary
 * @since Nov/09/2014
 */
public class DanceTrendUp {

    List<DanceEvent> events;

    public DanceTrendUp() {
        this.events = new ArrayList<>();
    }

    public List<DanceEvent> getEvents() {
        return events;
    }
}
