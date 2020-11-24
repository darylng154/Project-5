import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Sgrass extends ActEntity {

    private static final Random rand = new Random();

    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;

    //no animation period


    public Sgrass(String id, Point position,
                  int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);

    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = getPosition().findOpenAround(world);

        if (openPt.isPresent())
        {
            Entity fish = new Fish(FISH_ID_PREFIX + getId(),
                    openPt.get(), FISH_CORRUPT_MIN +
                            rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(world.FISH_KEY));
            world.addEntity(fish);
            ((ActEntity)fish).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction( this,  world, imageStore),
                getActionPeriod());
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new ActivityAction( this,  world, imageStore),
                getActionPeriod());
    }
}
