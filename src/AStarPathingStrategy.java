import java.io.PipedOutputStream;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/
        List<Point> path = new LinkedList<>();
        PriorityQueue<Point> openList = new PriorityQueue<>(Comparator.comparing( Point::getFValue).thenComparing( Point::getHValue ));
        List<Point> closedList = new ArrayList<>();
        openList.add( start );

        Point currentPoint;
        while (openList.size() > 0) {


             currentPoint = openList.poll();

            closedList.add( currentPoint );
            openList.remove( currentPoint );

            //Filtered list containing neighbors you can actually move to
            List<Point> neighbors = potentialNeighbors.apply( currentPoint )
                    .filter( canPassThrough )
                    .filter( p -> !p.equals( start ) /*&& !p.equals( end )*/
                            && !closedList.contains( p ) ).collect( Collectors.toList() );

            for (Point n : neighbors) {

                //set the g, h, f values, and set the parent values
                if (!openList.contains( n )) {
                    n.setParent(currentPoint, end);
                    openList.add( n );
                } else if (Point.distance( n, currentPoint ) + currentPoint.getGValue() < n.getGValue()){
                    n.setParent(currentPoint, end);
                }
                if (n == end) {
                    closedList.add( n );
                    return path;
                }
            }
            //check the parent value
            if(withinReach.test( currentPoint, end )){
                while (currentPoint.getParent() != null){
                    path.add( currentPoint );
                    currentPoint = currentPoint.getParent();
                }
                //System.out.println("check parent");
                return path;
            }

        }

        return path;
    }

}


