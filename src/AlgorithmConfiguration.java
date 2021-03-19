class AlgorithmConfiguration {
    static int populationSize = 100;
    static int numberOfPopulations =100;

    //PUNISHMENTS
    static int punishmentForNumberOfSegments = 5;
    static int punishmentForPathsLength = 1;
    static int punishmentForPathsLengthOutOfBoard = 5;
    static int punishmentForNumberOfPathsOutOfBoard = 50;
    static int punishmentForIntersects = 100;

    //TOURNAMENT PARAMETERS
    static int tournamentN = 20;

    //CROSSING PARAMETERS
    static int crossingProbability = 90;

    //MUTATION PARAMETERS
    static int mutationProbability = 30;
}
