import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Fish extends ActEntity {

    private static final Random rand = new Random();

    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;


    public Fish(String id, Point position,
                int actionPeriod,List<PImage> images)
    {
        super(id, position,actionPeriod, images );

    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents( this);

        Entity crab = new Crab(getId() + CRAB_ID_SUFFIX,
                pos, imageStore.getImageList(Crab.KEY),getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN));

        world.addEntity(crab);
        ((ActEntity)crab).scheduleActions(scheduler, world, imageStore);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction( this,  world, imageStore),
                getActionPeriod());
    }

}
