import processing.core.PImage;

import java.util.List;

abstract class Entity {

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    protected int actionPeriod;
    protected int animationPeriod;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    public String getId(){return id;}

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public int getAnimationPeriod(){
        throw new UnsupportedOperationException(
                String.format( "getAnimationPeriod not supported for %s",
                        this.getClass() ) );
    }

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public PImage getCurrentImage()
    {
        return images.get(imageIndex);
    }
}