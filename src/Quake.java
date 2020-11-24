import processing.core.PImage;

import javax.swing.*;
import java.util.List;

public class Quake extends ActEntity {

    public static final String KEY = "quake";
    private static final String ID = "quake";
    private static final int ACTION_PERIOD = 1100;
    private static final int ANIMATION_PERIOD = 100;
    private static final int ANIMATION_REPEAT_COUNT = 10;


    public Quake(Point position, List<PImage> images)

    {
        super(ID, position,ACTION_PERIOD, images );
        this.animationPeriod = ANIMATION_PERIOD;

    }

    @Override
    public int getAnimationPeriod(){
        return animationPeriod;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction( this,  world, imageStore),
                getActionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this, ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }

}

