package com.csci4810;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class Homework1 extends ApplicationAdapter {

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

        public Grid(float ppm, float gridX, float gridY, int width, int height,
                    Color gridColor, Color fillColor, Color lineColor) {
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

        public void drawGridLabels(SpriteBatch spriteBatch, BitmapFont font) {
            for (int x = 0; x < width; x++) {
                font.draw(spriteBatch, Integer.toString(x), gridX + (x + .5f) * ppm, gridY + (height + .5f) * ppm);
            }
            for (int y = 0; y < height; y++) {
                font.draw(spriteBatch, Integer.toString(y), gridX - ppm, gridY + (y + .5f) * ppm);
            }
        }

        public void drawGrid(ShapeRenderer renderer) {
            renderer.setColor(gridColor);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    renderer.rect(gridX + x * ppm, gridY + y * ppm, ppm, ppm);
                }
            }
        }

        public void drawDirectLines(ShapeRenderer renderer) {
            renderer.setColor(lineColor);
            for (Line l : lines) {
                float offset = ppm / 2f;
                renderer.line(
                        gridX + l.x0 * ppm + offset,
                        gridY + l.y0 * ppm + offset,
                        gridX + l.x1 * ppm + offset,
                        gridY + l.y1 * ppm + offset
                );
            }
        }

        public void drawLinesBasic(ShapeRenderer renderer) {
            renderer.setColor(fillColor);
            for (Line l : lines) {
                float dx = l.x1 - l.x0;
                float dy = l.y1 - l.y0;
                float x = l.x0;
                float y = l.y0;
                float m;
                if (dx > dy) {
                    m = dy / dx;
                    for (int i = 0; i <= dx; i++) {
                        renderer.rect(gridX + x * ppm, gridY + Math.round(y) * ppm, ppm, ppm);
                        x++;
                        y += m;
                    }
                } else {
                    m = dx / dy;
                    for (int i = 0; i <= dy; i++) {
                        renderer.rect(gridX + Math.round(x) * ppm, gridY + y * ppm, ppm, ppm);
                        y++;
                        x += m;
                    }
                }
            }
        }

        public void drawLinesBresenham(ShapeRenderer renderer) {
            renderer.setColor(fillColor);
            for (Line l : lines) {
                int dx = l.x1 - l.x0;
                int dy = l.y1 - l.y0;
                int inc1 = 2 * dy;
                int inc2 = 2 * (dy - dx);
                int e = 2 * dy - dx;
                int x = l.x0;
                int y = l.y0;
                while (x <= l.x1) {
                    renderer.rect(gridX + x * ppm, gridY + y * ppm, ppm, ppm);
                    if (e >= 0) {
                        y++;
                        e += inc2;
                    } else {
                        e += inc1;
                    }
                    x++;
                }
            }
        }

    }

    public static final float PPM = 32f;
    public static final int SCREEN_WIDTH = 20;
    public static final int SCREEN_HEIGHT = 20;

    public static final int GRID_WIDTH = 16;
    public static final int GRID_HEIGHT = 14;

    public static final Color GRID_COLOR = Color.RED;
    public static final Color FILL_COLOR = Color.BLUE;
    public static final Color LINE_COLOR = Color.PURPLE;

    public static final int NUM_LINES = 1;

    public Grid grid;
    public BitmapFont font;
    public Viewport viewport;
    public ShapeRenderer renderer;
    public SpriteBatch spriteBatch;

    @Override
    public void create() {
        font = new BitmapFont();
        spriteBatch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        viewport = new FitViewport(SCREEN_WIDTH * PPM, SCREEN_HEIGHT * PPM);
        viewport.getCamera().position.x = SCREEN_WIDTH * PPM / 2f;
        viewport.getCamera().position.y = SCREEN_HEIGHT * PPM / 2f;
        float x = (float) SCREEN_WIDTH / (float) GRID_WIDTH;
        float y = (float) SCREEN_HEIGHT / (float) GRID_HEIGHT;
        grid = new Grid(PPM, x * PPM, y * PPM, GRID_WIDTH, GRID_HEIGHT, GRID_COLOR, FILL_COLOR, LINE_COLOR);

        grid.add(new Line(3, 3, 7, 5));
        grid.add(new Line(3, 12, 8, 7));
        grid.add(new Line(9, 5, 9, 1));
        grid.add(new Line(11, 5, 13, 13));
        grid.add(new Line(1, 1, 6, 1));

        // TODO: randomly generate lines
        /*
        for (int i = 0; i < NUM_LINES; i++) {
            int x0 = Utils.getRand(0, grid.width);
            int y0 = Utils.getRand(0, grid.height);
            int x1 = Utils.getRand(0, grid.width);
            int y1 = Utils.getRand(0, grid.height);
            grid.add(new Line(x0, y0, x1, y1));
        }
         */
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        ScreenUtils.clear(Color.BLACK);

        // render text
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        grid.drawGridLabels(spriteBatch, font);
        spriteBatch.end();

        // render lines
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        grid.drawGrid(renderer);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        grid.drawLinesBasic(renderer);
        // grid.drawLinesBresenham(renderer);
        renderer.set(ShapeRenderer.ShapeType.Line);
        grid.drawDirectLines(renderer);
        renderer.end();

        viewport.apply();
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
