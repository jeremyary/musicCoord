package jary.controllers;

import jary.drools.model.DanceEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 *
 * @author <a href='mailto:jeremy.ary@gmail.com'>jary</a>
 */
@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    DanceEvent home() {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void analysisInput() {
        return;
    }

    @RequestMapping(method = RequestMethod.GET)
    public void danceInput() {
        return;
    }


}