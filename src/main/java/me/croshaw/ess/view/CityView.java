package me.croshaw.ess.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import me.croshaw.ess.model.City;
import me.croshaw.ess.settings.CitySettings;
import me.croshaw.ess.util.Pair;

public class CityView {
    City city;
    CitySettings citySettings;
    private Pair<Integer, Integer> curCell;
    private static final Color[] colors = new Color[]
            {
              Color.rgb(100, 255,0)
            , Color.rgb(255, 200, 0)
            , Color.rgb(255, 100, 0)
            , Color.rgb(255,0,0)
            };
    double step;
    public CityView(City city, CitySettings citySettings) {
        this.city = city;
        this.citySettings = citySettings;
        step = citySettings.getPermissibleConcentration() / colors.length;
    }
    public void mouseMoved(MouseEvent event) {
//        double x = event.getSceneX();
//        double y = event.getSceneY();
//        System.out.println("X: %s Y: %s".formatted(x, y));
//        if(event.getSource() instanceof Canvas canvas) {
//            double margin = 5;
//            if(x > margin && x < canvas.getWidth() - 2*margin && y > margin && y < canvas.getHeight()*0.9 - 2*margin) {
//                double canvasHeight = canvas.getHeight() * 0.9 - margin * 2;
//                double canvasWidth = canvas.getWidth() - margin * 2;
//                double[][] map = city.getCurrentPollutionMap();
//                double height = canvasHeight / map.length;
//                double width = canvasWidth / map[0].length;
//                int row = (int) (y / height);
//                int col = (int) (x / width);
//                curCell = new Pair<>(row, col);
//            } else
//                curCell = null;
//        } else {
//            curCell = null;
//        }
    }
    public void drawTo(GraphicsContext g, boolean drawBorder) {
        double margin = 5;
        double canvasHeight = g.getCanvas().getHeight() * 0.9 - margin*2;
        double canvasWidth = g.getCanvas().getWidth() - margin*2;

        double[][] map = city.getPollutionMap();
        double height = canvasHeight / map.length;
        double width = canvasWidth / map[0].length;
        for(int i = 0 ; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                int colorIndex = (int)Math.min(Math.ceil(map[i][j] / step), colors.length-1);
                double x = j * width;
                double y = i * height;
                g.setFill(colors[colorIndex]);
                g.fillRect(margin+x, margin+y, width, height);
            }
        }
        if(curCell != null){
            g.setFill(Color.BLACK);
            g.fillText(Double.toString(map[curCell.getFirst()][curCell.getSecond()]), margin, canvasHeight + 4 * margin);
        }

        if(drawBorder) {
            g.setFill(Color.BLACK);
            for (int i = 0; i <= map.length; i++) {
                g.fillRect(margin, i * height + margin, canvasWidth, 1);
            }
            for (int i = 0; i <= map[0].length; i++) {
                g.fillRect(i * width + margin, margin, 1, canvasHeight);
            }
        }
    }
}
