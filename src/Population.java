import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Population {
    private Individual [] individualsInPopulation;
    private int []  fitnessInPopulation;

    Population(int populationSize){ //Population is an array of list of individuals which are solutions
        individualsInPopulation = new Individual[populationSize];
        fitnessInPopulation = new int[populationSize];
    }

    void randomInitPopulation(PCB problem){
        for(int i = 0; i < individualsInPopulation.length; i++){
            individualsInPopulation[i] = new Individual(problem);
            individualsInPopulation[i].randomInitIndividual();
        }
        setFitnessForAllSolutions(); //after generating population is counted fitness in population for each individual.
    }

    public int getIndexOfIndividual(Individual ind){
        for(int i = 0; i < individualsInPopulation.length; i++){
            if(ind == individualsInPopulation[i])
                return i;
        }
        return -1;
    }

    private void setFitnessForAllSolutions(){
        ArrayList<Integer> punishments = new ArrayList<>();
        for (Individual i: individualsInPopulation) {
            punishments.add(i.countIndividualFitness());
        }
        punishments.sort(Double::compare);

        for(int i = 0; i < individualsInPopulation.length; i++){
            fitnessInPopulation[i] = (int)(((double)punishments.get(0)/(double)(individualsInPopulation[i].getIndividualFitness()))*10000);
        }
    }

    public int [] getFitnessForAllSolutions(){
        return fitnessInPopulation;
    }


    //GETTERS AND SETTERS
    public Individual[] getIndividualsInPopulation() {
        return individualsInPopulation;
    }

    //OVERRIDE FROM OBJECT
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nPOPULATION: ");
        for (Individual p: individualsInPopulation) {
            result.append("\t").append(p.toString()).append("\n");
        }
        return result.toString();
    }
}
