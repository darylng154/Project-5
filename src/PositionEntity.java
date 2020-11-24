import processing.core.PImage;

import java.util.List;

abstract class PositionEntity extends ActEntity {


    public PositionEntity(String id, Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod) {

        super(id, position, actionPeriod, images);

        this.animationPeriod = animationPeriod;
    }


    abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
    abstract Point nextPosition(WorldModel world, Point destPos);
}
