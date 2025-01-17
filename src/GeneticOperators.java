import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GeneticOperators {

    //============SELECTION============//

    //TOURNAMENT
    public static Individual selectionOperatorTournament(Population population) {
        Random rand = new Random();
        int counter = 0;
        int drawnIndex = -1;
        ArrayList<Individual> drawnIndividuals = new ArrayList<>();
        ArrayList<Integer> drawnIndexes = new ArrayList<>();
        while (counter != AlgorithmConfiguration.tournamentN) {
            drawnIndex = rand.nextInt(population.getIndividualsInPopulation().length);
            if (!drawnIndexes.contains(drawnIndex)) {
                drawnIndividuals.add(population.getIndividualsInPopulation()[drawnIndex]);
                drawnIndexes.add(drawnIndex);
                counter++;
            }
        }
        drawnIndividuals.sort(Comparator.comparingDouble((Individual i) -> population.getFitnessForAllSolutions()[population.getIndexOfIndividual(i)]));
        return drawnIndividuals.get(drawnIndividuals.size() - 1);
    }

    //ROULETTE
    public static Individual selectionOperatorRoulette(Population population) {
        Random rand = new Random();
        double[] weights = countPropsOfBeingDrawn(population.getFitnessForAllSolutions());
        double randomValue = ((double) rand.nextInt(100)) / 100;
        int resultIndex = -1;
        double previousSum = 0;
        for (int i = 0; i < weights.length; i++) {
            previousSum = 0;
            for (int j = 0; j < i; j++) {
                previousSum = previousSum + weights[j];
            }
            if (randomValue >= previousSum && randomValue < previousSum + weights[i])
                resultIndex = i;
        }
        if (resultIndex != -1) {
            return population.getIndividualsInPopulation()[resultIndex];
        } else
            return null;
    }


    private static double[] countPropsOfBeingDrawn(int [] fitValues) {
        ArrayList<Integer> fitnessValues = new ArrayList<>();
        for (int i : fitValues) fitnessValues.add(i);

        double[] weights = new double[fitnessValues.size()];
        double sum = fitnessValues.stream()
                .mapToDouble(d -> d)
                .sum();
        for (int i = 0; i < fitnessValues.size(); i++) {
            weights[i] = fitnessValues.get(i) / sum;
        }
        return weights;
    }

    //=================CROSSING==================//

    public static Individual crossing(Individual firstParent, Individual secondParent) {
        Random rand = new Random();

        Individual child = null;
        int crossingChance = rand.nextInt(101);
        if (crossingChance > AlgorithmConfiguration.crossingProbability)
            return firstParent;
        else {
            child = firstParent.deepCopyIndividual();
        }

        int numberOfGenotypeChanges = rand.nextInt(firstParent.getPathsOnBoard().length - 1) + 1; // [1, size-1] <- at least one gene crossing
        int changesCounter = 0;
        int indexToChange = rand.nextInt(firstParent.getPathsOnBoard().length);
        ArrayList<Integer> changedIndexes = new ArrayList<>();
        while (changesCounter != numberOfGenotypeChanges) {
            while (changedIndexes.contains(indexToChange)) {
                indexToChange = rand.nextInt(firstParent.getPathsOnBoard().length);
            }
            changedIndexes.add(indexToChange);
            child.getPathsOnBoard()[indexToChange] = secondParent.getPathsOnBoard()[indexToChange].deepCopyPath();
            changesCounter++;
        }
        return child;
    }

    public static Individual mutationRand(Individual child, Individual firstParent){
        Random rand = new Random();
        Individual childToMutate;
        if (child == firstParent)
            childToMutate = child.deepCopyIndividual();
        else
            childToMutate = child;

        int mutProb = rand.nextInt(101);
        if(mutProb <= AlgorithmConfiguration.mutationProbability) {
            int pathToMutateIndex = rand.nextInt(childToMutate.getPathsOnBoard().length);
            Path pathToMutate = childToMutate.getPathsOnBoard()[pathToMutateIndex];
            ArrayList<Segment> newSegments = new ArrayList<>();
            Path newPath = new Path(pathToMutate.getPathStartPoint(), pathToMutate.getPathEndPoint(), newSegments);
            newPath.randomInitPath();
            child.getPathsOnBoard()[pathToMutateIndex] = newPath;
        }
        return childToMutate;
    }


    public static Individual mutation(Individual child, Individual firstParent){
        Random rand = new Random();
        Individual childToMutate; //new child or  old parent?
        if (child == firstParent)
            childToMutate = child.deepCopyIndividual();
        else
            childToMutate = child;

        int pathToMutateIndex = rand.nextInt(childToMutate.getPathsOnBoard().length);
        Path pathToMutate = childToMutate.getPathsOnBoard()[pathToMutateIndex];
        int segmentToMutateIndex = rand.nextInt(pathToMutate.getSegmentsInPath().size());
        Segment segmentToMutate = pathToMutate.getSegmentsInPath().get(segmentToMutateIndex); //SEGMENT TO MOVE

        int moveSegmentDirection = rand.nextInt(2); //1 - UP, RIGHT  0 - DOWN, LEFT

        //SPECIFY NEW POINTS FOR SEGMENTS

        int x_change = 0;
        int y_change = 0;

        if(segmentToMutate.getSegmentOrientationData() == Direction.VERTICAL_UP || segmentToMutate.getSegmentOrientationData() == Direction.VERTICAL_DOWN){
            if(moveSegmentDirection == 1)
                x_change = 1;
            else{
                x_change = -1;
            }
        }
        else if(segmentToMutate.getSegmentOrientationData() == Direction.HORIZONTAL_RIGHT || segmentToMutate.getSegmentOrientationData() == Direction.HORIZONTAL_LEFT){
            if(moveSegmentDirection == 1)
                y_change = 1;
            else{
                y_change = -1;
            }
        }

        Point startPoint = new Point(segmentToMutate.getSegmentStartPoint().getX()+x_change, segmentToMutate.getSegmentStartPoint().getY()+y_change);
        Point endPoint = new Point(segmentToMutate.getSegmentEndPoint().getX()+x_change, segmentToMutate.getSegmentEndPoint().getY()+y_change);

        //USTAWIANIE FRAGMENTU PRZED
        if(segmentToMutateIndex == 0){
            pathToMutate.getSegmentsInPath().add(0, new Segment(segmentToMutate.getSegmentStartPoint(), startPoint, Direction.NO_DIRECTION)); //NO CARE WHAT ORIENT
            segmentToMutateIndex++;
        }
        else{
            pathToMutate.getSegmentsInPath().get(segmentToMutateIndex - 1).setSegmentEndPoint(startPoint);
            if(pathToMutate.getSegmentsInPath().get(segmentToMutateIndex - 1).getSegmentStartPoint().equals(pathToMutate.getSegmentsInPath().get(segmentToMutateIndex - 1).getSegmentEndPoint())){
                pathToMutate.getSegmentsInPath().remove(segmentToMutateIndex - 1);
                segmentToMutateIndex--;
            }
        }
        if(segmentToMutateIndex == pathToMutate.getSegmentsInPath().size()-1){
            pathToMutate.getSegmentsInPath().add(new Segment(endPoint, segmentToMutate.getSegmentEndPoint(), Direction.NO_DIRECTION));
        }
        else{
            pathToMutate.getSegmentsInPath().get(segmentToMutateIndex + 1).setSegmentStartPoint(endPoint);
            if(pathToMutate.getSegmentsInPath().get(segmentToMutateIndex + 1).getSegmentStartPoint().equals(pathToMutate.getSegmentsInPath().get(segmentToMutateIndex + 1).getSegmentEndPoint())){
                pathToMutate.getSegmentsInPath().remove(segmentToMutateIndex + 1);
            }
        }
        segmentToMutate.setSegmentStartPoint(startPoint);
        segmentToMutate.setSegmentEndPoint(endPoint);
        return childToMutate;
    }
}
