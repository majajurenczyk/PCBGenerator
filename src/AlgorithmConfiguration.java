class AlgorithmConfiguration {
    static int populationSize = 1000;
    static int numberOfPopulations = 100;

    //PUNISHMENTS
    static int punishmentForNumberOfSegments = 10; //ok
    static int punishmentForPathsLength = 1; //ok
    static int punishmentForPathsLengthOutOfBoard = 5; //ok
    static int punishmentForNumberOfPathsOutOfBoard = 20; //ok
    static int punishmentForIntersects = 100; //ok

    //TOURNAMENT PARAMETERS
    static int tournamentN = 100;

    //CROSSING PARAMETERS
    static int crossingProbability = 80;

    //MUTATION PARAMETERS
    static int mutationProbability = 30;
}
