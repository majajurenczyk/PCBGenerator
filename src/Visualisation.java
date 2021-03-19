import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Random;

public class Visualisation extends Application {
    private PCB pcb = new PCB();

    private Scene drawBoard(){
        pcb.readAndSetPCBParamsFromFile("C:\\Users\\User\\Desktop\\3rok\\6sem\\SI\\L\\lab1\\PCBGenerator\\src\\zad1.txt");

        Group gr = new Group();

        for(int i = 0; i <= pcb.getBoardWidth(); i++){
            for(int j = 0; j <= pcb.getBoardHeight(); j++){
                Line point = new Line(i*50, j*50, i*50, j*50);
                point.setStroke(Color.GRAY);
                point.setStrokeWidth(5);

                gr.getChildren().add(point);
            }
        }

        Individual ind = pcb.findSolution();
        System.out.println(ind.toString());

        Color[] colors = new Color[10];
        colors[0] = Color.RED;
        colors[1] = Color.BLUEVIOLET;
        colors[2] = Color.CYAN;
        colors[3] = Color.DARKMAGENTA;
        colors[4] = Color.DEEPPINK;
        colors[5] = Color.DARKOLIVEGREEN;
        colors[6] = Color.DARKBLUE;
        colors[7] = Color.DARKTURQUOISE;
        colors[8] = Color.FUCHSIA;
        colors[9] = Color.DIMGREY;

        int counter = 0;
        for(Path p : ind.getPathsOnBoard()){
            Random rand = new Random();
            for(Segment s: p.getSegmentsInPath()) {
                Line line = new Line();
                line.setStroke(colors[counter]);
                line.setStrokeWidth(3);

                line.setStartX(s.getSegmentStartPoint().getX()*50);
                line.setStartY(s.getSegmentStartPoint().getY()*50);
                line.setEndX(s.getSegmentEndPoint().getX()*50);
                line.setEndY(s.getSegmentEndPoint().getY()*50);

                gr.getChildren().add(line);
            }
            counter++;
        }

        Line lineLeft = new Line();
        Line lineRight = new Line();
        Line lineDown = new Line();
        Line lineUp = new Line();

        lineLeft.setStartX(0);
        lineLeft.setStartY(0);
        lineLeft.setEndX(0);
        lineLeft.setEndY(pcb.getBoardHeight()*50);

        lineRight.setStartX(pcb.getBoardWidth()*50);
        lineRight.setStartY(0);
        lineRight.setEndX(pcb.getBoardWidth()*50);
        lineRight.setEndY(pcb.getBoardHeight()*50);

        lineUp.setStartX(0);
        lineUp.setStartY(pcb.getBoardHeight()*50);
        lineUp.setEndX(pcb.getBoardWidth()*50);
        lineUp.setEndY(pcb.getBoardHeight()*50);

        lineDown.setStartX(0);
        lineDown.setStartY(0);
        lineDown.setEndX(pcb.getBoardWidth()*50);
        lineDown.setEndY(0);

        gr.getChildren().add(lineDown);
        gr.getChildren().add(lineLeft);
        gr.getChildren().add(lineUp);
        gr.getChildren().add(lineRight);

        return new Scene(gr, 500, 500);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Setting title to the scene
        primaryStage.setTitle("Visualisation");

        //Adding the scene to the stage
        primaryStage.setScene(drawBoard());

        //Displaying the contents of a scene
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
