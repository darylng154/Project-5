import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Crab extends PositionEntity {

    public static final String KEY = "crab";


    public Crab(String id, Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod) {
        super( id, position, images, actionPeriod, animationPeriod );

    }

    @Override
    public int getAnimationPeriod() {
        return animationPeriod;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> crabTarget = world.findNearest(
                getPosition(), Sgrass.class );
        long nextPeriod = getActionPeriod();

        if (crabTarget.isPresent()) {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveTo( world, crabTarget.get(), scheduler )) {
                ActEntity quake = new Quake( tgtPos,
                        imageStore.getImageList( Quake.KEY ) );

                world.addEntity( quake );
                nextPeriod += getActionPeriod();
                quake.scheduleActions( scheduler, world, imageStore );
            }
        }
        scheduler.scheduleEvent( this,
                new ActivityAction( this, world, imageStore ),
                nextPeriod );
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                new ActivityAction( this, world, imageStore ),
                getActionPeriod() );
        scheduler.scheduleEvent( this,
                new AnimationAction( this, 0 ), getAnimationPeriod() );
    }

    @Override
    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler) {
        if (Point.adjacent( getPosition(), target.getPosition() )) {
            world.removeEntity( target );
            scheduler.unscheduleAllEvents( target );
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

    @Override
    public Point nextPosition(WorldModel world,
                              Point destPos) {

        List<Point> points;
        AStarPathingStrategy strategy = new AStarPathingStrategy();
        //for single step
        //SingleStepPathingStrategy strategy = new SingleStepPathingStrategy();

        points = strategy.computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> Point.adjacent(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        //DIAGONAL_NEIGHBORS);
        //DIAGONAL_CARDINAL_NEIGHBORS);

        if (points.size() == 0) {
            System.out.println("No path found");
            return getPosition();
        }

        int horiz = Integer.signum(points.get(points.size()-1).x - getPosition().x);
        int vert = Integer.signum(points.get(points.size()-1).y - getPosition().y);

        //int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz,
                getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof Fish)))
        {
            //int vert = Integer.signum(destPos.y - getPosition().y);
            newPos = new Point(getPosition().x, getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Fish)))
            {
                newPos = getPosition();
            }
        }

        return newPos;

    }

    }

