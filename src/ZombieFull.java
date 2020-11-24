import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class ZombieFull extends Zombie {
    public ZombieFull(String id, int resourceLimit, int resourceCount,
                    Point position, int actionPeriod, int animationPeriod,
                    List<PImage> images) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    // hit the obstacle and turn NotFull
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Obstacle.class);

        if (fullTarget.isPresent() &&
                moveTo(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((ActEntity)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction( this,  world, imageStore),
                    this.getActionPeriod());
        }
    }

    @Override
    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler) {
        if (Point.adjacent( getPosition(), target.getPosition() )) {
            return true;
        } else {
            Point nextPos = nextPosition( world, target.getPosition() );

            if (!getPosition().equals( nextPos )) {
                Optional<Entity> occupant = world.getOccupant( nextPos );
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents( occupant.get() );
                }

                world.moveEntity( this, nextPos );

            }
            return false;
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        ZombieNotFull zombie = new ZombieNotFull(getId(),
                getPosition(), getImages(), getResourceLimit(), getResourceCount(), getActionPeriod(), getAnimationPeriod());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(zombie);
        zombie.scheduleActions(scheduler, world, imageStore);
    }

}
