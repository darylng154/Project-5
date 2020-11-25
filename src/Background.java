import java.util.List;
import processing.core.PImage;

final class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex;


   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public List<PImage> getImages(){

      return images;
   }

   public int getImageIndex(){

      return imageIndex;
   }

   public void setBackground(WorldModel world, Point pos)
   {
      if (world.withinBounds(pos))
      {
         setBackgroundCell(world, pos);
      }
   }

   public void setBackgroundCell(WorldModel world, Point pos)
   {

      world.getBackground()[pos.y][pos.x] = this;
   }

   public PImage getCurrentImage()
   {
      return images.get(imageIndex);
   }

   public String getId()
   {
      return id;
   }
}
