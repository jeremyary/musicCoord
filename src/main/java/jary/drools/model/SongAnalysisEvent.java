package jary.drools.model;

/**
 * @author jary
 * @since Nov/07/2014
 */
public class SongAnalysisEvent {

    /*{
        "title": "blah",                        // song title
            "startTimestamp": 1415489686,       // start time of playing song, long - time in millis
            "initialLength": 224,               // frames length of song
            "tempo": 22000                      // tempo of song
        "beatFrames": [
        ...
        ]

        { "title": "foo", "startTimestamp": 1415489686, "initialLength": 500, "beatFrames": [50, 100, 150] }
    }*/

    private String title;

    private Long startTimestamp;

    private Integer initialLength;

    private Double tempo;

    private Integer[] beatFrames;

    public SongAnalysisEvent() {
    }

    public SongAnalysisEvent(String title, Long startTimestamp, Integer initialLength, Double tempo, Integer[] beatFrames) {
        this.title = title;
        this.startTimestamp = startTimestamp;
        this.initialLength = initialLength;
        this.tempo = tempo;
        this.beatFrames = beatFrames;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Integer getInitialLength() {
        return initialLength;
    }

    public void setInitialLength(Integer initialLength) {
        this.initialLength = initialLength;
    }

    public Integer[] getBeatFrames() {
        return beatFrames;
    }

    public void setBeatFrames(Integer[] beatFrames) {
        this.beatFrames = beatFrames;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }
}
