import java.lang.reflect.Array;
import java.util.ArrayList;

public class Population {
    private Individual [] individualsInPopulation;
    private PCB problem;

    Population(int populationSize, PCB problem){ //Population is an array of list of individuals which are solutions
        individualsInPopulation = new Individual[populationSize];
        this.problem = problem;
    }

    void randomInitPopulation(){
        for(int i = 0; i < individualsInPopulation.length; i++){
            individualsInPopulation[i] = new Individual(this);
            individualsInPopulation[i].randomInitIndividual();
        }
    }

    ArrayList<Integer> getFitnessForAllSolutions(){
        ArrayList<Integer> results = new ArrayList<>();
        for (Individual i: individualsInPopulation) {
            results.add(i.individualFitness());
        }
        return results;
    }

    //GETTERS AND SETTERS
    public Individual[] getIndividualsInPopulation() {
        return individualsInPopulation;
    }

    PCB getProblem() {
        return problem;
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
