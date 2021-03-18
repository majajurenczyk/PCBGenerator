class AlgorithmConfiguration {
    static int populationSize = 100;
    static int numberOfPopulations = 10000;

    //PUNISHMENTS
    static int punishmentForNumberOfSegments = 5;
    static int punishmentForPathsLength = 1;
    static int punishmentForPathsLengthOutOfBoard = 10;
    static int punishmentForNumberOfPathsOutOfBoard = 15 ;
    static int punishmentForIntersects = 10000                                    ;

    //TOURNAMENT PARAMETERS
    static int tournamentN = 10;

    //CROSSING PARAMETERS
    static int crossingProbability = 80;
}
