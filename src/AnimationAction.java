public class AnimationAction implements Action {

    private Entity entity;
    private int repeatCount;

    public AnimationAction(Entity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    new AnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
}
