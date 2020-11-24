import processing.core.PImage;

import javax.naming.event.ObjectChangeListener;
import java.util.List;

abstract class Octo extends PositionEntity {

    private int resourceLimit;

    public Octo(String id, Point position,
                List<PImage> images, int resourceLimit,
                int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit(){return resourceLimit;}

    @Override
    public int getAnimationPeriod(){
        return animationPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                new ActivityAction( this, world, imageStore ),
                getActionPeriod());
        scheduler.scheduleEvent( this,
                new AnimationAction( this, 0 ), getAnimationPeriod() );

    }

    public Point nextPosition(WorldModel world,
                              Point destPos) {
        int horiz = Integer.signum( destPos.x - getPosition().x );
        Point newPos = new Point( getPosition().x + horiz,
                getPosition().y );

        if (horiz == 0 || world.isOccupied( newPos )) {
            int vert = Integer.signum( destPos.y - getPosition().y );
            newPos = new Point( getPosition().x,
                    getPosition().y + vert );

            if (vert == 0 || world.isOccupied( newPos )) {
                newPos = getPosition();
            }
        }

        //if(this.getPosition() == world.getBackgroundImage())
        //System.out.println();

        return newPos;
    }

}