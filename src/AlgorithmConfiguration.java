class AlgorithmConfiguration {
    static int populationSize = 5;

    //PUNISHMENTS
    static int punishmentForNumberOfSegments = 2;
    static int punishmentForPathsLength = 1;
    static int punishmentForPathsLengthOutOfBoard = 5;
    static int punishmentForNumberOfPathsOutOfBoard = 3 ;
    static int punishmentForIntersects = 20;

    //TOURNAMENT PARAMETERS
    static int tournamentN = 10;

    //CROSSING PARAMETERS
    static int crossingProbability = 60;
    static int geneChangeProbability = 80;
}
