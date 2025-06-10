package de.beimax.spacealert.render;

import de.beimax.spacealert.mission.Event;
import de.beimax.spacealert.mission.EventList;
import de.beimax.spacealert.mission.Mission;

import java.util.Map;

public class TextplayerRenderer implements Renderer {
    @Override
    public boolean print(Mission mission) {
        // start time
        long start = System.currentTimeMillis();
        int nextEventAt = 0;

        EventList events = mission.getMissionEvents();

        // get next event
        Map.Entry<Integer, Event> nextEvent = events.getNextEvent(nextEventAt);
        do {
            // wait for event time
            while(System.currentTimeMillis() - start < ((long) nextEventAt) * 1000) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
            }
            Event event = nextEvent.getValue();
            int eventTime = nextEvent.getKey();
            System.out.println(EventList.formatTime(eventTime) + " - " + event.getDescription(eventTime));

            // get next event
            nextEvent = events.getNextEvent(eventTime+1);
            if (nextEvent != null)
                nextEventAt = nextEvent.getKey();
        } while (nextEvent != null);

        return true;
    }

    @Override
    public boolean output(Mission mission) {
        return false;
    }
}
