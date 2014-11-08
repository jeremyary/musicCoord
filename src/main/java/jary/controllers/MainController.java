package jary.controllers;

import jary.drools.SessionMediator;
import jary.drools.model.DanceEvent;
import jary.drools.model.SongAnalysisEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Controller
@RequestMapping("/event/")
public class MainController {

    @Autowired
    protected SessionMediator sessionMediator;

    @RequestMapping(value = "song", method = RequestMethod.POST)
    public void analysisInput(@RequestBody final SongAnalysisEvent analysisEvent) {

        sessionMediator.insert(analysisEvent);
    }

    @RequestMapping(value = "dance", method = RequestMethod.POST)
    public void danceInput(@RequestBody final DanceEvent danceEvent) {

        sessionMediator.insert(danceEvent);
    }


}