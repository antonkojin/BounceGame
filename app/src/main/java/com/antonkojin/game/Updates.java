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
            if (u.rect.contains(x, y) && game.pointsCount >= u.price) {
                u.apply();
            }
        }
    }

    class Update {
        int ordinal;
        Rect rect;
        double price;
        private String description;

        Update(int ordinal, int length) {
            this.ordinal = ordinal;
            switch (ordinal) {
                case 0:
                    description = "moreMoney";
                    price = 1;
                    break;
                case 1:
                    description = "lessBaddies";
                    price = 10;
            }
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            double verticalSpacing = (screenHeight - (length * image.getHeight())) / ((length - 1) + 2);
            int top = (int) (verticalSpacing + ((image.getHeight() + verticalSpacing) * ordinal));
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int left = (screenWidth - image.getWidth()) / 2;
            this.rect = new Rect(left, top, left + image.getWidth(), top + image.getHeight());
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
            game.pointsCount -= price;
            price *= 2;
        }

        void draw(Canvas canvas) {
            canvas.drawBitmap(image, rect.left, rect.top, null);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(100);
            final int topOffset = 155;
            final int leftOffset = 100;
            canvas.drawText(this.description + "   " + (int) price, rect.left + leftOffset, rect.top + topOffset, paint);
        }
    }
}
