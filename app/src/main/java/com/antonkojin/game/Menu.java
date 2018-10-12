package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.List;

class Menu {
    private static Bitmap image;
    Game game;
    List<MenuItem> menuItems;

    Menu(Game game) {
        image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.update);
        this.game = game;
        menuItems = new LinkedList<>();
        menuItems.add(new MenuItem(0, 3));
        menuItems.add(new MenuItem(1, 3));
        menuItems.add(new MenuItem(2, 3));
    }

    void draw(Canvas canvas) {
        for (MenuItem u : menuItems) u.draw(canvas);
    }

    public void select(int x, int y) {
        for (MenuItem u : menuItems) {
            if (u.rect.contains(x, y)) {
                u.apply();
            }
        }
    }

    class MenuItem {
        int ordinal;
        Rect rect;
        private String description;

        MenuItem(int ordinal, int length) {
            this.ordinal = ordinal;
            switch (ordinal) {
                case 0:
                    description = "play";
                    break;
                case 1:
                    description = "reset";
                    break;
                case 2:
                    description = "test";
            }
            int left = (game.worldBounds.width() - image.getWidth()) / 2;
            int top = left + (image.getHeight() + left) * ordinal;
            this.rect = new Rect(left, top, left + image.getWidth(), top + image.getHeight());
        }

        void apply() {
            switch (this.description) {
                case "play":
                    game.gameStart();
                    break;
                case "reset":
                    game.gameOver();
                    game.gameStart();
                    break;
            }
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(image, rect.left, rect.top, null);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            final int topOffset = 155;
            final int leftOffset = 100;
            canvas.drawText(this.description, rect.left + leftOffset, rect.top + topOffset, paint);
        }
    }
}
