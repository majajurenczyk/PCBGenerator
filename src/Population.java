public class Population {
    private Individual [] individualsInPopulation;
    private PCB problem;

    public Population(int populationSize, PCB problem){ //Population is an array of list of individuals which are solutions
        individualsInPopulation = new Individual[populationSize];
        this.problem = problem;
    }

    public void randomInitPopulation(){
        for(int i = 0; i < individualsInPopulation.length; i++){
            individualsInPopulation[i] = new Individual(this);
            individualsInPopulation[i].randomInitIndividual();
        }
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
}
