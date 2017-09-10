package com.example.feng.fiveinarow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feng on 2017/9/9.
 */

public class WuziqiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE =10;
    private boolean isGameOver;
    private boolean isWhiteWinner;
    private int MAX_COUNT =5;


    private Paint mPaint =new Paint();

    private Bitmap mwhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPiece = 3 * 1.0f /4;

    private  boolean isWhite =true;
    private ArrayList<Point> mWhiteArray =new ArrayList<>();
    private ArrayList<Point> mBlackArray =new ArrayList<>();
    public WuziqiPanel(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mwhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.chess2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.chess1);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize =MeasureSpec.getSize(widthMeasureSpec);
        int widthMode =MeasureSpec.getMode(widthMeasureSpec);

        int heightthSize =MeasureSpec.getSize(heightMeasureSpec);
        int heightMode =MeasureSpec.getMode(heightMeasureSpec);


        int width =Math.min(widthSize,heightthSize);

        if(widthMode ==MeasureSpec.UNSPECIFIED){
            width =heightthSize;
        }
        else if(heightMode ==MeasureSpec.UNSPECIFIED){
            width =widthSize;
        }

        setMeasuredDimension(width,width);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isGameOver) return false;
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN)
        {
            int x =(int)event.getX();
            int y =(int)event.getY();

            Point p = getvalid(x,y);
            if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }
            if(isWhite){
                mWhiteArray.add(p);

            }
            else{
                mBlackArray.add(p);
            }
            invalidate();
            isWhite =!isWhite;

        }

        return true;
    }

    private Point getvalid(int x, int y) {
      return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth *1.0f/MAX_LINE;

        int pieceWidth = (int) (mLineHeight *ratioPiece);
        mwhitePiece =Bitmap.createScaledBitmap(mwhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece =Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkisgameover();


    }

    private void checkisgameover() {
        boolean whitewin = checkfiveinline(mWhiteArray);

        boolean blackwin =checkfiveinline(mBlackArray);

        if(whitewin||blackwin){
            isGameOver =true;
            isWhiteWinner = whitewin;
            String text = isWhiteWinner ? "白棋胜利":"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkfiveinline(List<Point>mlist) {
       for(Point p:mlist){
           int x =p.x;
           int y =p.y;

           boolean win =checkhorizontal(x,y,mlist);
           boolean win1 =checkhvertical(x,y,mlist);
           boolean win2 =checkLeftDiagonal(x,y,mlist);
           boolean win3 =checkRightDiagonal(x,y,mlist);
           if(win||win1||win2||win3){
               return true;
           }
       }
       return false;
    }

    private boolean checkhorizontal(int x, int y, List<Point> mlist) {

        int count =0;
        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x-i,y))){
             count++;
            }else{
                break;

            }
        }
        count =0;
        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x+i,y))){
                count++;
            }else{

                break;
            }
        }
        if(count ==MAX_COUNT){
            return true;
        }
        return false;
    }

    private boolean checkhvertical(int x, int y, List<Point> mlist) {
        int count =0;

        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x,y+i))){
                count++;
            }else{

                break;
            }
        }

        count =0;
        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x,y-i))){
                count++;
            }else{
                break;
            }
        }
        if(count ==MAX_COUNT){
            return true;
        }
        return false;
    }
    private boolean checkLeftDiagonal(int x, int y, List<Point> mlist) {
        int count =0;

        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x-i,y+i))){
                count++;
            }else{

                break;
            }
        }

        count =0;
        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x+i,y+i))){
                count++;
            }else{

                break;
            }
        }
        if(count ==MAX_COUNT){
            return true;
        }
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, List<Point> mlist) {
        int count =0;

        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x,y+i))){
                count++;
            }else{

                break;
            }
        }
        count =0;
        for(int i =0;i<MAX_COUNT;i++){
            if(mlist.contains(new Point(x,y-i))){
                count++;
            }else{

                break;
            }
        }
        if(count == MAX_COUNT){
            return true;
        }
        return false;
    }


    private void drawPieces(Canvas canvas) {
        for(int i =0;i<mWhiteArray.size();i++){
            Point whitePoint =mWhiteArray.get(i);
            canvas.drawBitmap(mwhitePiece,(whitePoint.x+((1-ratioPiece)/2))*mLineHeight,(whitePoint.y+((1-ratioPiece)/2))*mLineHeight,null);
        }
        for(int i =0;i<mBlackArray.size();i++){
            Point blackPoint =mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,(blackPoint.x+((1-ratioPiece)/2))*mLineHeight,(blackPoint.y+((1-ratioPiece)/2))*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas cancas) {
        int  w =mPanelWidth;
        float lineHeight =mLineHeight;
        for(int i =0;i<MAX_LINE;i++){
            int startX = (int) (lineHeight/2);
            int endX = (int) (w -lineHeight/2);
            int  y1 = (int) ((0.5 +i)*lineHeight);
            cancas.drawLine(startX,y1,endX,y1,mPaint);
            cancas.drawLine(y1,startX,y1,endX,mPaint);
        }
    }
    private static final String INSTANCE ="instance";
    private static final String INSTANCE_GAME_OVER ="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY ="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY ="instance_black_array";


    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle =new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_WHITE_ARRAY,isGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle){
            Bundle bundle =(Bundle) state;
            isGameOver =bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray =bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray =bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
        }

        super.onRestoreInstanceState(state);
    }

    public void start(){
        mBlackArray.clear();
        mWhiteArray.clear();
        isGameOver =false;
        isWhiteWinner =false;
        invalidate();
    }
}
