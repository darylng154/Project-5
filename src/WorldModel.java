import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel {

   private static final String OCTO_KEY = "octo";
   private static final int OCTO_NUM_PROPERTIES = 7;
   private static final int OCTO_ID = 1;
   private static final int OCTO_COL = 2;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   //keep FISH_KEY public because both WorldModel Class and Entity are using it
   public static final String FISH_KEY = "fish";
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_ID = 1;
   private static final int FISH_COL = 2;
   private static final int FISH_ROW = 3;
   private static final int FISH_ACTION_PERIOD = 4;


   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;
   private static final int ATLANTIS_ANIMATION_PERIOD = 70;

   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final int PROPERTY_KEY = 0;


   private int numRows;
   private int numCols;
   private Background[][] background;
   private Entity occupancy[][];
   private Set<Entity> entities;


   public WorldModel(int numRows, int numCols, Background defaultBackground) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++) {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }


   public int getNumRows(){
      return numRows;
   }

   public int getNumCols(){
      return numCols;
   }

   public Background[][] getBackground(){
      return background;
   }

   public Set<Entity> getEntities(){
      return entities;
   }

   public void load(Scanner in, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!processLine(in.nextLine(), imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         } catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         } catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public boolean processLine(String line, ImageStore imageStore) {
      String[] properties = line.split("\\s");

      if (properties.length > 0) {
         switch (properties[PROPERTY_KEY]) {
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case OCTO_KEY:
               return parseOcto(properties, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case FISH_KEY:
               return parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties, imageStore);
         }
      }

      return false;
   }

   public boolean parseBackground(String[] properties, ImageStore imageStore) {
      if (properties.length == BGND_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         Background bGround = new Background(id, imageStore.getImageList(id));
         bGround.setBackground(this, pt);
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   public boolean parseOcto(String[] properties, ImageStore imageStore) {
      if (properties.length == OCTO_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = new OctoNotFull(properties[OCTO_ID],
                 pt,
                 imageStore.getImageList(OCTO_KEY),
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]));
         tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }


   public boolean parseObstacle(String[] properties, ImageStore imageStore) {
      if (properties.length == OBSTACLE_NUM_PROPERTIES) {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public boolean parseFish(String[] properties, ImageStore imageStore) {
      if (properties.length == FISH_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = new Fish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   public boolean parseAtlantis(String[] properties, ImageStore imageStore) {
      if (properties.length == ATLANTIS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = new Atlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   public boolean parseSgrass(String[] properties, ImageStore imageStore) {
      if (properties.length == SGRASS_NUM_PROPERTIES) {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = new Sgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   public boolean withinBounds(Point pos) {
      return pos.y >= 0 && pos.y < numRows &&
              pos.x >= 0 && pos.x < numCols;
   }

   public boolean isOccupied(Point pos) {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public Optional<Entity> findNearest(Point pos, Class kind) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities) {
         if (kind.isInstance(entity)) {
            ofType.add(entity);
         }
      }

      return this.nearestEntity(ofType, pos);
   }


   public Optional<PImage> getBackgroundImage(Point pos) {
      if (withinBounds(pos)) {

         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      } else {
         return Optional.empty();
      }
   }

   public Optional<Entity> getOccupant(Point pos) {
      if (isOccupied(pos)) {
         return Optional.of(getOccupancyCell(pos));
      } else {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell(Point pos) {

      return this.occupancy[pos.y][pos.x];
   }

   public void setOccupancyCell(Point pos,
                                       Entity entity) {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public Background getBackgroundCell(Point pos)
   {

      return this.getBackground()[pos.y][pos.x];
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void removeEntity(Entity entity)
   {

      this.removeEntityAt(entity.getPosition());
   }


   public void removeEntityAt(Point pos)
   {
      if (this.withinBounds(pos)
              && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.getEntities().remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

         for (Entity other : entities)
         {
            int otherDistance = Point.distanceSquared(other.getPosition(), pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }





}
