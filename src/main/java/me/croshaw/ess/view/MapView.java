package me.croshaw.ess.view;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import me.croshaw.ess.model.IPollutionMap;

public class MapView {
    private static final Color[] colors = new Color[]
            {
                    Color.rgb(100, 255,0)
                    , Color.rgb(255, 200, 0)
                    , Color.rgb(255, 100, 0)
                    , Color.rgb(255,0,0)
            };
    private boolean drawBorder = true;
    private boolean drawText = true;
    private final IPollutionMap mapInfo;
    private final Bounds bounds;
    private final double step;
    public MapView(IPollutionMap mapInfo, Bounds bounds, double temp) {
        this.mapInfo = mapInfo;
        this.bounds = bounds;
        step = temp / colors.length;
    }
    public void draw(GraphicsContext g) {
        Canvas canvas = g.getCanvas();
        double width = canvas.getWidth() - bounds.getWidth();
        double height = canvas.getHeight() - bounds.getHeight();
        double[][] map = mapInfo.getPollutionMap();
        double cellHeight = height / map.length;
        double cellWidth = width / map[0].length;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                int colorIndex = (int)Math.min(Math.ceil(map[i][j] / step), colors.length-1);
                double x = j * cellWidth;
                double y = i * cellHeight;
                g.setFill(colors[colorIndex]);
                g.fillRect(bounds.getMinX()+x, bounds.getMinY()+y, cellWidth, cellHeight);
                if(drawText) {
                    g.setFill(Color.BLACK);
                    Font font = g.getFont();
                    Text text = new Text(Double.toString(map[i][j]));
                    text.setFont(g.getFont());
                    g.fillText(Double.toString(map[i][j]), bounds.getMinX() + x + (cellWidth / 2 - text.getLayoutBounds().getWidth() / 2), bounds.getMinY() + y + (cellHeight / 2));
                }
            }
        }
        if(drawBorder) {
            g.setFill(Color.BLACK);
            for (int i = 0; i <= map.length; i++) {
                g.fillRect(bounds.getMinX(), bounds.getMinY() + i * cellHeight, width, 1);
            }
            for (int i = 0; i <= map[0].length; i++) {
                g.fillRect(i * cellWidth + bounds.getMinX(), bounds.getMinY(), 1, height);
            }
        }
    }
}
