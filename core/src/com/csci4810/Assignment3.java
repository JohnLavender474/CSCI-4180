package com.csci4810;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Assignment3 extends ApplicationAdapter {

    private static class Line {

        public final int x0;
        public final int y0;
        public final int x1;
        public final int y1;

        private Line(int x0, int y0, int x1, int y1) {
            if (x0 < x1) {
                this.x0 = x0;
                this.y0 = y0;
                this.x1 = x1;
                this.y1 = y1;
            } else {
                this.x0 = x1;
                this.y0 = y1;
                this.x1 = x0;
                this.y1 = y0;
            }
        }

    }

    private static class Grid {

        private final float ppm;
        private final int width;
        private final int height;
        private final float gridX;
        private final float gridY;
        private final Color fillColor;
        private final Color lineColor;
        private final List<Line> lines;
        private final ShapeRenderer renderer;

        private Grid(ShapeRenderer renderer, float ppm, float gridX, float gridY, int width, int height,
                     Color fillColor, Color lineColor) {
            this.renderer = renderer;
            this.ppm = ppm;
            this.gridX = gridX;
            this.gridY = gridY;
            this.width = width;
            this.height = height;
            this.fillColor = fillColor;
            this.lineColor = lineColor;
            lines = new ArrayList<>();
        }

        public void add(Line line) {
            lines.add(line);
        }

        public void drawLines() {
            renderer.setColor(fillColor);
            for (Line l : lines) {
                int x0 = l.x0;
                int y0 = l.y0;
                int x1 = l.x1;
                int y1 = l.y1;
                int xDiff = x1 - x0;
                int yDiff = y1 - y0;
                int dx0 = 0;
                int dx1 = 0;
                int dy0 = 0;
                int dy1 = 0;
                if (xDiff < 0) {
                    dx0 = -1;
                    dx1 = -1;
                } else if (xDiff > 0) {
                    dx0 = 1;
                    dx1 = 1;
                }
                if (yDiff < 0) {
                    dy0 = -1;
                } else if (yDiff > 0) {
                    dy0 = 1;
                }
                int longest = Math.abs(xDiff);
                int shortest = Math.abs(yDiff);
                if (longest <= shortest) {
                    longest = Math.abs(yDiff);
                    shortest = Math.abs(xDiff);
                    if (yDiff < 0) {
                        dy1 = -1;
                    } else if (yDiff > 0) {
                        dy1 = 1;
                    }
                    dx1 = 0;
                }
                int num = longest / 2;
                int x = x0;
                int y = y0;
                int i = 0;
                while (i <= longest) {
                    i++;
                    renderer.rect(gridX + Math.round(x) * ppm, gridY + y * ppm, ppm, ppm);
                    num += shortest;
                    if (num >= longest) {
                        num -= longest;
                        x += dx0;
                        y += dy0;
                    } else {
                        x += dx1;
                        y += dy1;
                    }
                }
            }
        }

    }

}
