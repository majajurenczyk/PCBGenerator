public class Population {
    private Individual [] population;

    public Population(int populationSize){ //Population is an array of list of individuals which are solutions
        population = new Individual[populationSize];
    }

    public boolean initPopulation(){
        if(population != null){
            return true;
        }
        return false;
    }

    //GETTERS AND SETTERS
    public Individual[] getSolution() {
        return population;
    }

    public void setSolution(Individual[] solution) {
        this.population = solution;
    }
}
