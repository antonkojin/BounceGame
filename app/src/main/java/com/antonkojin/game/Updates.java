package com.antonkojin.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

class Updates {
    private static Bitmap image;
    Game game;
    List<Update> updates;

    Updates(Game game) {
        image = BitmapFactory.decodeResource(game.context.getResources(), R.drawable.update);
        this.game = game;
        updates = new LinkedList<>();
        updates.add(new Update(0, 2));
        updates.add(new Update(1, 2));
    }

    void draw(Canvas canvas) {
        for (Update u : updates) u.draw(canvas);
    }

    public void select(int x, int y) {
        for (Update u : updates) {
            if (u.rect.contains(x, y)) {
                u.apply();
            }
        }
    }

    class Update {
        int ordinal;
        Rect rect;
        double price = 0;
        private String description;

        Update(int ordinal, int length) {
            this.ordinal = ordinal;
            switch (ordinal) {
                case 0:
                    description = "moreMoney";
                    break;
                case 1:
                    description = "lessBaddies";
            }
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            double spacing = screenHeight - (length * image.getHeight()) / ((length - 1) + 2);
            int top = (int) (spacing + ((image.getHeight() + spacing) * ordinal));
            int left = (int) spacing;
            this.rect = new Rect(left, top, left + image.getWidth(), top + image.getHeight());
            Log.i("", "Update x: " + rect.centerX() + " y: " + rect.centerY());
        }

        void apply() {
            switch (this.description) {
                case "moreMoney":
                    Points.spawnDelta *= 0.9;
                    break;
                case "lessBaddies":
                    Ants.spawnDelta *= 1.1;
                    break;
            }
            game.pause = false;
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(image, rect.left, rect.top, null);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            canvas.drawText(this.description, rect.left + 20, rect.top + 20, paint);
            canvas.drawText(String.valueOf(price), rect.right - 20, rect.top + 20, paint);
            canvas.drawRect(rect, paint);
        }
    }
}
