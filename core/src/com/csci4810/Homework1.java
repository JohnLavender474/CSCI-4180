package com.csci4810;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.List;

public class Homework1 extends ApplicationAdapter {

    public record Line(int x0, int y0, int x1, int y1) {
    }

    public static class Grid {

        public final float ppm;
        public final int width;
        public final int height;
        public final float gridX;
        public final float gridY;
        public final Color gridColor;
        public final Color fillColor;
        public final List<Line> lines;

        public Grid(float ppm, float gridX, float gridY, int width, int height, Color gridColor, Color fillColor) {
            this.ppm = ppm;
            this.gridX = gridX;
            this.gridY = gridY;
            this.width = width;
            this.height = height;
            this.gridColor = gridColor;
            this.fillColor = fillColor;
            lines = new LinkedList<>();
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
            for (Line l : lines) {
                renderer.setColor(Color.PURPLE);
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
            for (Line l : lines) {
                renderer.setColor(fillColor);
                float deltaX = Math.abs(l.x1 - l.x0);
                float deltaY = Math.abs(l.y1 - l.y0);
                float m = deltaY / deltaX;
                for (int i = 0; i <= deltaX; i++) {
                    float x = l.x0 + i;
                    float y = Math.round(m * i + l.y0);
                    renderer.rect(gridX + x * ppm, gridY + y * ppm, ppm, ppm);
                }
            }
        }

        public void drawLinesBresenham(ShapeRenderer renderer) {
            for (Line l : lines) {
                renderer.setColor(fillColor);
                float deltaX = Math.abs(l.x1 - l.x0);
                float deltaY = Math.abs(l.y1 - l.y0);
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

    public static final int NUM_LINES = 3;

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
        grid = new Grid(PPM, x * PPM, y * PPM, GRID_WIDTH, GRID_HEIGHT, GRID_COLOR, FILL_COLOR);
        for (int i = 0; i < NUM_LINES; i++) {
            int x0 = Utils.getRand(0, grid.width);
            int y0 = Utils.getRand(0, grid.height);
            int x1 = Utils.getRand(0, grid.width);
            int y1 = Utils.getRand(0, grid.height);
            Line l = new Line(Math.min(x0, x1), Math.min(y0, y1), Math.max(x0, x1), Math.max(y0, y1));
            System.out.println(l);
            grid.add(l);
        }
    }

    @Override
    public void render() {
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
