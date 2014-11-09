package jary.controllers;

import jary.drools.SessionMediator;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/event/")
public class MainController {

    @Autowired
    protected SessionMediator sessionMediator;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String main() {

        return "nothing to see here, move along...";
    }
}