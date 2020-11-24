import java.util.ArrayList;
import java.util.Optional;

final class Point
{
   private static final int FISH_REACH = 1;

   public final int x;
   public final int y;
   protected int gValue;
   protected int hValue;
   protected int fValue;
   protected Point parent;


   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
              (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
   }
   public Optional<Point> findOpenAround(WorldModel world)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(x + dx, y + dy);
            if (world.withinBounds(newPt) &&
                    !world.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }
   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public int getGValue(){
      return gValue;
   }


   public int getHValue(){
      return hValue;
   }


   public int getFValue(){
      return fValue;
   }

   public Point getParent(){return parent;}

   public void setParent(Point p, Point end){
      parent = p;
      gValue = distance(this, p) + p.getGValue();
      hValue = distance(this, end);
      updateFValue();
   }

   private void updateFValue(){
      fValue = gValue + hValue;
   }

   public static int distance (Point a, Point b){
      return (int)Math.sqrt(Math.pow((a.x - b.x),2)+ (Math.pow((a.y - b.y),2)));
   }

   public static ArrayList<Point> get3x3(WorldModel w, Point p)
   {
      ArrayList<Point> list = new ArrayList<>();

      for(int i = -1; i <= 1; i++)
      {
         for(int i2 = -1; i2 <= 1; i2++)
         {
            if(w.withinBounds(new Point(p.x + i, p.y + i2)))
               list.add(new Point(p.x + i, p.y + i2));
         }
      }

      return list;
   }
}
