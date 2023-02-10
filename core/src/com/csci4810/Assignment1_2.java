package com.csci4810;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class Assignment1_2 extends ApplicationAdapter {

    public static class Line {

        public final int x0;
        public final int y0;
        public final int x1;
        public final int y1;

        public Line(int x0, int y0, int x1, int y1) {
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

    public static class Grid {

        public final float ppm;
        public final int width;
        public final int height;
        public final float gridX;
        public final float gridY;
        public final Color gridColor;
        public final Color fillColor;
        public final Color lineColor;
        public final List<Line> lines;
        public final ShapeRenderer renderer;

        public Grid(ShapeRenderer renderer, float ppm, float gridX, float gridY, int width, int height,
                    Color gridColor, Color fillColor, Color lineColor) {
            this.renderer = renderer;
            this.ppm = ppm;
            this.gridX = gridX;
            this.gridY = gridY;
            this.width = width;
            this.height = height;
            this.gridColor = gridColor;
            this.fillColor = fillColor;
            this.lineColor = lineColor;
            lines = new ArrayList<>();
        }

        public void add(Line line) {
            lines.add(line);
        }

        public void drawAnalogLines(boolean render) {
            if (render) {
                renderer.setColor(lineColor);
            }
            for (Line l : lines) {
                float offset = ppm / 2f;
                if (render) {
                    renderer.line(
                            gridX + l.x0 * ppm + offset,
                            gridY + l.y0 * ppm + offset,
                            gridX + l.x1 * ppm + offset,
                            gridY + l.y1 * ppm + offset
                    );
                }
            }
        }

        public void drawLinesBasic(boolean render) {
            if (render) {
                renderer.setColor(fillColor);
            }
            for (Line l : lines) {
                float dx = l.x1 - l.x0;
                float dy = l.y1 - l.y0;
                float x = l.x0;
                float y = l.y0;
                float m;
                if (Math.abs(dx) > Math.abs(dy)) {
                    m = dy / dx;
                    for (int i = 0; i <= Math.abs(dx); i++) {
                        if (render) {
                            renderer.rect(gridX + x * ppm, gridY + Math.round(y) * ppm, ppm, ppm);
                        }
                        x++;
                        y += m;
                    }
                } else {
                    m = dx / dy;
                    for (int i = 0; i <= Math.abs(dy); i++) {
                        if (render) {
                            renderer.rect(gridX + Math.round(x) * ppm, gridY + y * ppm, ppm, ppm);
                        }
                        if (dy < 0f) {
                            y--;
                            x -= m;
                        } else {
                            y++;
                            x += m;
                        }
                    }
                }
            }
        }

        public void drawLinesBresenham(boolean render) {
            if (render) {
                renderer.setColor(fillColor);
            }
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
                    if (render) {
                        renderer.rect(gridX + Math.round(x) * ppm, gridY + y * ppm, ppm, ppm);
                    }
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

    private static final Color GRID_COLOR = Color.PURPLE;
    private static final Color FILL_COLOR = Color.BLUE;
    private static final Color LINE_COLOR = Color.RED;

    private Grid grid;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private boolean renderLines;
    private boolean drawBresenham;
    private boolean drawAnalog;
    private boolean printTime;
    private int renderTimes;
    private int renderCount;
    private List<Long> renderRecords;

    @Override
    public void create() {
        renderRecords = new ArrayList<>();
        JsonValue json = new JsonReader().parse(Gdx.files.internal("Assignment1_2_Input.json"));
        int ppm = json.getInt("ppm");
        int gridWidth = json.getInt("gridWidth");
        int gridHeight = json.getInt("gridHeight");
        int numRandomLines = json.getInt("numRandomLines");
        renderLines = json.getBoolean("renderLines");
        drawBresenham = json.getBoolean("drawBresenham");
        drawAnalog = json.getBoolean("drawAnalog");
        printTime = json.getBoolean("printTime");
        renderTimes = json.getInt("renderTimes");
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        viewport = new FitViewport(gridWidth * ppm, gridHeight * ppm);
        viewport.getCamera().position.x = gridWidth * ppm / 2f;
        viewport.getCamera().position.y = gridHeight * ppm / 2f;
        grid = new Grid(renderer, ppm, 0f, 0f, gridWidth, gridHeight, GRID_COLOR, FILL_COLOR, LINE_COLOR);
        for (int i = 0; i < numRandomLines; i++) {
            int x0 = UtilMethods.getRand(0, grid.width);
            int y0 = UtilMethods.getRand(0, grid.height);
            int x1 = UtilMethods.getRand(0, grid.width);
            int y1 = UtilMethods.getRand(0, grid.height);
            grid.add(new Line(x0, y0, x1, y1));
        }

        // hard-coded lines:
        /*
        grid.add(new Line(0, 50, 10, 0));
        grid.add(new Line(50, 100, 500, 475));
        grid.add(new Line(600, 3, 100, 75));
        grid.add(new Line(0, 0, 50, 0));
        grid.add(new Line(0, 0, 0, 50));
        grid.add(new Line(450, 500, 200, 100));
        grid.add(new Line(250, 250, 100, 80));
        grid.add(new Line(500, 500, 200, 100));
         */
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        ScreenUtils.clear(Color.BLACK);
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        long start = System.nanoTime();
        if (drawBresenham) {
            grid.drawLinesBresenham(renderLines);
        } else {
            grid.drawLinesBasic(renderLines);
        }
        long end = System.nanoTime();
        if (printTime) {
            long time = end - start;
            renderRecords.add(time);
        }
        if (drawAnalog) {
            renderer.set(ShapeRenderer.ShapeType.Line);
            grid.drawAnalogLines(renderLines);
        }
        renderer.end();
        viewport.apply();
        if (renderTimes > 0) {
            renderCount++;
            if (renderCount >= renderTimes) {
                float avg = 0f;
                for (long l : renderRecords) {
                    avg += l;
                }
                avg /= (float) renderRecords.size();
                System.out.println("Render records: " + renderRecords);
                System.out.println("Average: " + avg);
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

}
