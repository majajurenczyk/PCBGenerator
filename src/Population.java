import java.lang.reflect.Array;
import java.util.ArrayList;

public class Population {
    private Individual [] individualsInPopulation;
    private PCB problem;

    public Population(int populationSize, PCB problem){ //Population is an array of list of individuals which are solutions
        individualsInPopulation = new Individual[populationSize];
        this.problem = problem;
    }

    public void randomInitPopulation(){
        for(int i = 0; i < individualsInPopulation.length; i++){
            System.out.println("TUTAJ SIE ROBI INYWIDUAL: " + i);
            individualsInPopulation[i] = new Individual(this);
            individualsInPopulation[i].randomInitIndividual();
        }
    }

    public ArrayList<Integer> getFitnessForAllSolutions(){
        ArrayList<Integer> results = new ArrayList<>();
        for (Individual i: individualsInPopulation) {
            results.add(i.individualFitness());
        }
        return results;
    }

    //GETTERS AND SETTERS
    public Individual[] getSolution() {
        return individualsInPopulation;
    }

    public void individualsInPopulation(Individual[] population) {
        this.individualsInPopulation = population;
    }

    public PCB getProblem() {
        return problem;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("POPULATION: ");
        for (Individual p: individualsInPopulation) {
            result.append("\t").append(p.toString()).append("\n");
        }
        return result.toString();
    }
}
