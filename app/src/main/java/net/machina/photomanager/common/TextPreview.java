package net.machina.photomanager.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

public class TextPreview extends View {

    protected int mFillColor, mStrokeColor;
    protected String mText;
    protected Typeface mFont;
    protected Paint fillPaint, strokePaint;
    protected Rect textBounds = new Rect();

    public TextPreview(Context context, String text, int fillColor, int strokeColor, Typeface font) {
        super(context);

        if (text.length() < 1) {
            this.mText = "Tekst";
        } else {
            this.mText = text;
        }

        this.mFillColor = fillColor;
        this.mStrokeColor = strokeColor;
        this.mFont = font;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        fillPaint.reset();
        strokePaint.reset();

        fillPaint.setAntiAlias(true);
        strokePaint.setAntiAlias(true);

        fillPaint.setTextSize(130);
        strokePaint.setTextSize(130);

        fillPaint.setTypeface(this.mFont);
        strokePaint.setTypeface(this.mFont);

        fillPaint.setStyle(Paint.Style.FILL);
        strokePaint.setStyle(Paint.Style.STROKE);

        strokePaint.setStrokeWidth(5);

        fillPaint.setColor(this.mFillColor);
        strokePaint.setColor(this.mStrokeColor);

        fillPaint.getTextBounds(this.mText, 0, this.mText.length() - 1, textBounds);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(this.mText, 1, textBounds.height() + 1, fillPaint);
        canvas.drawText(this.mText, 1, textBounds.height() + 1, strokePaint);
    }
}