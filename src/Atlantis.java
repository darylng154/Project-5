import processing.core.PImage;

import java.util.List;

public class Atlantis extends ActEntity {

    private static final int ANIMATION_REPEAT_COUNT = 7;


    public Atlantis(String id, Point position,List<PImage> images)
    {

        super(id, position,-1, images );
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
                new AnimationAction(this, ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }

}
