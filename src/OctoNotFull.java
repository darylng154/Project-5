import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo  {

    private int resourceCount;

    public OctoNotFull(String id, Point position, List<PImage> images,int resourceLimit,
                        int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Fish.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction( this,  world, imageStore),
                    this.getActionPeriod());
        }
    }


    //use this refer to Entity octo
    //Keep Entity target to compare with object itself ( which is Entity Octo)
    @Override
    public boolean moveTo( WorldModel world,
                                  Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(getPosition(), target.getPosition()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }



    /**
     * Transform not full octopus to full octopus
     * @param world
     * @param scheduler
     * @param imageStore
     * @return
     */
    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= getResourceLimit()) {
            OctoFull octo = new OctoFull( getId(), getResourceLimit(),
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());


            world.removeEntity( this );
            scheduler.unscheduleAllEvents( this );
            world.addEntity( octo );
            octo.scheduleActions( scheduler, world, imageStore );

            return true;
        }

        return false;
    }


}
