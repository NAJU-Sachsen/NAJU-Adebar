package de.naju.adebar.controller.api;

import de.naju.adebar.api.data.SimpleEventJSON;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.model.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * REST controller to access event data
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_eventController")
@RequestMapping("/api/events")
public class EventController {
    private LocalGroupManager groupManager;

    @Autowired
    public EventController(LocalGroupManager groupManager) {
        Object[] params = {groupManager};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.groupManager = groupManager;
    }

    /**
     * Provides all events of a local group
     * @param groupId the id of the local group
     * @return all events that are hosted by that group
     */
    @RequestMapping("/localGroup")
    Iterable<SimpleEventJSON> sendEventsForLocalGroup(@RequestParam("groupId") long groupId) {
        LocalGroup localGroup = groupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
        List<SimpleEventJSON> events = new LinkedList<>();
        for (Event event : localGroup.getEvents()) {
            events.add(new SimpleEventJSON(event));
        }
        return events;
    }

}
