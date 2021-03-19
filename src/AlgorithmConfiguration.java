class AlgorithmConfiguration {
    static int populationSize = 100;
    static int numberOfPopulations = 1000;

    //PUNISHMENTS
    static int punishmentForNumberOfSegments = 0;
    static int punishmentForPathsLength = 0;
    static int punishmentForPathsLengthOutOfBoard = 1;
    static int punishmentForNumberOfPathsOutOfBoard = 5;
    static int punishmentForIntersects = 15;

    //TOURNAMENT PARAMETERS
    static int tournamentN = 5;

    //CROSSING PARAMETERS
    static int crossingProbability = 95;

    //MUTATION PARAMETERS
    static int mutationProbability = 100;
}
