import processing.core.PImage;

import java.util.List;

abstract class ActEntity extends Entity{


    public ActEntity(String id, Point position, int actionPeriod,List<PImage> images)
    {
        super(id, position, images);

        this.actionPeriod = actionPeriod;

    }

    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
