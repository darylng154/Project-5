import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class ZombieNotFull extends Zombie {

    public ZombieNotFull (String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(this.getPosition(),
                Octo.class);

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
            int count = getResourceCount();
            count += 1;
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

    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore) {
        if (getResourceCount() >= getResourceLimit()) {
            ZombieFull zombie = new ZombieFull( getId(), getResourceLimit(), getResourceCount(),
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());


            world.removeEntity( this );
            scheduler.unscheduleAllEvents( this );
            world.addEntity( zombie );
            zombie.scheduleActions( scheduler, world, imageStore );

            return true;
        }

        return false;
    }


}

