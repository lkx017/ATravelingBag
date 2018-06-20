package com.example.admin.tripapplication.cameraAction;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.camera.GraphicOverlay;

class FaceGraphic extends GraphicOverlay.Graphic {
    // This variable may be written to by one of many threads. By declaring it as volatile,
    // we guarantee that when we read its contents, we're reading the most recent "write"
    // by any thread.
    private volatile FaceData mFaceData;

    private Paint mHintTextPaint;
    private Paint mHintOutlinePaint;
    private Drawable character;

    FaceGraphic(GraphicOverlay overlay, Context context) {
        super(overlay);
        Resources resources = context.getResources();
        initializePaints(resources);
        initializeGraphics(resources);
    }


    private void initializeGraphics(Resources resources) {
        switch (FaceActivity.select) {
            case 0:
                switch (LocationService.city) {
                    case "김해시":
                        character = resources.getDrawable(R.drawable.gimhae);
                        break;
                    case "부산시":
                        character = resources.getDrawable(R.drawable.busan);
                        break;
                    case "대구시":
                        character = resources.getDrawable(R.drawable.daegu);
                        break;
                    case "대전시":
                        character = resources.getDrawable(R.drawable.daejeon);
                        break;
                    case "경주시":
                        character = resources.getDrawable(R.drawable.gyeongju);
                        break;
                    case "제주시":
                        character = resources.getDrawable(R.drawable.jeju);
                        break;
                    case "전주시":
                        character = resources.getDrawable(R.drawable.jeonju);
                        break;
                    default:
                        character = resources.getDrawable(R.drawable.suhorang);
                        break;
                }
                break;
            case 1:
                character = resources.getDrawable(R.drawable.pororo);
                break;
            case 2:
                character = resources.getDrawable(R.drawable.moomin);
                break;
            case 3:
                character = resources.getDrawable(R.drawable.pikachu);
                break;
        }
    }

    private void initializePaints(Resources resources) {
        mHintTextPaint = new Paint();
        mHintTextPaint.setColor(resources.getColor(R.color.overlayHint));
        mHintTextPaint.setTextSize(resources.getDimension(R.dimen.textSize));

        mHintOutlinePaint = new Paint();
        mHintOutlinePaint.setColor(resources.getColor(R.color.overlayHint));
        mHintOutlinePaint.setStyle(Paint.Style.STROKE);
        mHintOutlinePaint.setStrokeWidth(resources.getDimension(R.dimen.hintStroke));
    }

    /**
     * Update the face instance based on detection from the most recent frame.
     */
    void update(FaceData faceData) {
        mFaceData = faceData;
        postInvalidate(); // Trigger a redraw of the graphic (i.e. cause draw() to be called).
    }

    @Override
    public void draw(Canvas canvas) {
        // Confirm that the face data is still available
        // before using it.
        FaceData faceData = mFaceData;
        if (faceData == null) {
            return;
        }

        PointF detectPosition = faceData.getPosition();
        PointF detectLeftEyePosition = faceData.getLeftEyePosition();
        PointF detectRightEyePosition = faceData.getRightEyePosition();
        PointF detectNoseBasePosition = faceData.getNoseBasePosition();
        PointF detectMouthLeftPosition = faceData.getMouthLeftPosition();
        PointF detectMouthBottomPosition = faceData.getMouthBottomPosition();
        PointF detectMouthRightPosition = faceData.getMouthRightPosition();
        {
            if ((detectPosition == null) ||
                    (detectLeftEyePosition == null) ||
                    (detectRightEyePosition == null) ||
                    (detectNoseBasePosition == null) ||
                    (detectMouthLeftPosition == null) ||
                    (detectMouthBottomPosition == null) ||
                    (detectMouthRightPosition == null)) {
                return;
            }
        }

        //얼굴에 사각형을 그린다
        float centerX = translateX(faceData.getPosition().x + faceData.getWidth() / 2.0f);
        float centerY = translateY(faceData.getPosition().y + faceData.getHeight() / 2.0f);
        float offsetX = scaleX(faceData.getWidth() / 2.0f);
        float offsetY = scaleY(faceData.getHeight() / 2.0f);

        float left = centerX - offsetX;
        float right = centerX + offsetX;
        float top = centerY - offsetY;
        float bottom = centerY + offsetY;

        canvas.drawRect(left, top, right, bottom, mHintOutlinePaint);

        // If we've made it this far, it means that the face data *is* available.
        // It's time to translate camera coordinates to view coordinates.

        // Face position, dimensions, and angle
        PointF position = new PointF(translateX(detectPosition.x),
                translateY(detectPosition.y));
        float width = scaleX(faceData.getWidth());
        float height = scaleY(faceData.getHeight());

        // Nose coordinates
        PointF noseBasePosition = new PointF(translateX(detectNoseBasePosition.x),
                translateY(detectNoseBasePosition.y));

        drawPicture(canvas, position, width, height, noseBasePosition);
    }

    // Cartoon feature draw routines
    // =============================

    private void drawPicture(Canvas canvas, PointF facePosition, float faceWidth, float faceHeight, PointF noseBasePosition) {
        float picCenterX;
        float picCenterY;
        int left, right, top, bottom;

        if (FaceActivity.sp1.getSelectedItemPosition() == 0) {

            picCenterY = facePosition.y - faceHeight / 2 + 70;

            left = (int) (noseBasePosition.x - (faceWidth / 2));
            right = (int) (noseBasePosition.x + (faceWidth / 2));
            top = (int) (picCenterY - (faceHeight / 2));
            bottom = (int) (picCenterY + (faceHeight / 2));

        } else if (FaceActivity.sp1.getSelectedItemPosition() == 1) {

            picCenterY = facePosition.y + faceHeight * 2 - 70;

            left = (int) (noseBasePosition.x - (faceWidth / 2));
            right = (int) (noseBasePosition.x + (faceWidth / 2));
            top = (int) (picCenterY - (faceHeight / 2));
            bottom = (int) (picCenterY + (faceHeight / 2));

        } else if (FaceActivity.sp1.getSelectedItemPosition() == 2) {

            picCenterX = facePosition.x - faceWidth * 1.5f + 70;

            left = (int) (picCenterX - (faceWidth / 2));
            right = (int) (picCenterX + (faceWidth / 2));
            top = (int) (noseBasePosition.y - (faceHeight / 2));
            bottom = (int) (noseBasePosition.y + (faceHeight / 2));

        } else {

            picCenterX = facePosition.x + faceWidth / 2 - 70;

            left = (int) (picCenterX - (faceWidth / 2));
            right = (int) (picCenterX + (faceWidth / 2));
            top = (int) (noseBasePosition.y - (faceHeight / 2));
            bottom = (int) (noseBasePosition.y + (faceHeight / 2));

        }
        character.setBounds(left, top, right, bottom);
        character.draw(canvas);
    }
}