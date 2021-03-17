import java.util.ArrayList;
import java.util.Random;

public class GeneticOperators {

    //============SELECTION============//

    //TOURNAMENT
    public static Individual selectionOperatorTournament(Population population){
        Random rand = new Random();
        int counter = 0;
        int drawnIndex = -1;
        ArrayList<Individual> drawnIndividuals = new ArrayList<>();
        while(counter != AlgorithmConfiguration.tournamentN || counter < population.getIndividualsInPopulation().length){
            drawnIndex = rand.nextInt(population.getIndividualsInPopulation().length);
            drawnIndividuals.add(population.getIndividualsInPopulation()[drawnIndex]);
            counter++;
        }
        drawnIndividuals.sort((Individual i1, Individual i2) -> Double.compare(i1.getIndividualFit(), i2.getIndividualFit()));
        return drawnIndividuals.get(drawnIndividuals.size()-1).deepCopyIndividual(null);
    }

    //ROULETTE
    public static Individual selectionOperatorRoulette(Population population) {
        Random rand = new Random();
        double [] weights = countPropsOfBeingDrawn(population.getFitnessForAllSolutions());
        double randomValue = ((double)rand.nextInt(100))/100;
        int resultIndex = -1;
        double previousSum = 0;
        for(int i = 0; i < weights.length; i++){
            previousSum = 0;
            for(int j = 0; j < i; j++){
                System.out.println(weights[j]);
                previousSum = previousSum + weights[j];
            }
            if(randomValue >= previousSum && randomValue < previousSum + weights[i])
                resultIndex = i;
        }
        if(resultIndex != -1) {
            return population.getIndividualsInPopulation()[resultIndex].deepCopyIndividual(null);
        }
        else
            return null;
    }


    private static double [] countPropsOfBeingDrawn(ArrayList<Double> fitnessValues){
        double [] weights = new double [fitnessValues.size()];
        double sum = fitnessValues.stream()
                .mapToDouble(d -> d)
                .sum();
        for(int i = 0; i < fitnessValues.size(); i++){
            weights[i] = fitnessValues.get(i)/sum;
        }
        return weights;
    }

    //=================CROSSING==================//

    public static Individual crossing(Individual firstParent, Individual secondParent){
        Random rand = new Random();

        Individual child = firstParent.deepCopyIndividual(null);
        int crossingChance = rand.nextInt(101);
        if(crossingChance > AlgorithmConfiguration.crossingProbability)
            return child;

        int numberOfGenotypeChanges = rand.nextInt(firstParent.getPathsOnBoard().length-1)+1; // [1, size-1] <- at least one gene crossing
        int changesCounter = 0;
        int indexToChange = rand.nextInt(firstParent.getPathsOnBoard().length);
        ArrayList<Integer> changedIndexes = new ArrayList<>();
        while(changesCounter != numberOfGenotypeChanges){
           while(changedIndexes.contains(indexToChange)){
               indexToChange = rand.nextInt(firstParent.getPathsOnBoard().length);
           }
           changedIndexes.add(indexToChange);
           child.getPathsOnBoard()[indexToChange] = secondParent.getPathsOnBoard()[indexToChange].deepCopyPath(child);
           changesCounter++;
        }
        return child;
    }
}
