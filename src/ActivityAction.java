public class ActivityAction implements Action {
    private ActEntity actEntity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(ActEntity actEntity, WorldModel world,
                  ImageStore imageStore)
    {
        this.actEntity = actEntity;
        this.world = world;
        this.imageStore = imageStore;
    }


    @Override
    public void executeAction(EventScheduler scheduler) {
        actEntity.executeActivity( world, imageStore, scheduler );
    }

    }

