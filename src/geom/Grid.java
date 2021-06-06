package geom;


import core.Applet;
import processing.core.PFont;
import processing.core.PVector;
import storage.ColorType;
import storage.TruthVector;
import storage.Vector;
import util.Mapper;
import util.map.MapEase;
import util.map.MapType;

import java.text.DecimalFormat;

import static processing.core.PConstants.*;
import static util.Useful.ceilAny;
import static util.Useful.floorAny;

public class Grid {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int X = 3; // Make enum later (AXES)
    public static final int Y = 5;
    Applet p;

    public Applet getProcessingInstance() {
        return p;
    }

    public Vector getCamera() {
        return camera;
    }

    public Vector getIncrementor(){
        return incrementor;
    }

    public PVector getDisplacement() {
        return displacement;
    }

    public Vector getSpacing() {
        return spacing;
    }

    public Vector getBegin() {
        return begin;
    }

    public Vector getEnd() {
        return end;
    }

    public Vector getScale() {
        return scale;
    }

    public TruthVector getMoving() {
        return startMoving;
    }

    Vector camera = new Vector(0,0),spacing = new Vector(200,200);
    Vector startingCamera = new Vector(camera);
    Vector incrementor = new Vector(203.8f,200), startingBegin; // def incrementor: 200,200 // 203.8 fails
    Vector begin = new Vector(),end = new Vector();
    Vector scale = new Vector(400,800);
    PVector displacement = new Vector(0,0);
    TruthVector startMoving = new TruthVector();
    PFont font;
    DecimalFormat df = new DecimalFormat("####.##");
    public static float e = 1;

    public Grid(Applet p){
        this.p = p;
        String commonPath = "src\\data\\";
        font = p.createFont(commonPath + "cmunbmr.ttf", 150, true);
        // Empty for now because nothing much really happens
    }

    private void init(){
        p.colorMode(HSB);
        p.translate(WIDTH/2f,HEIGHT/2f);
        p.background(0);
        //p.shapeMode(CENTER);
        p.rectMode(CORNERS);
        p.textFont(font);
        p.textSize(60);
        p.strokeCap(ROUND);
        startingBegin = new Vector((float) ceilAny(displacement.x - WIDTH/2f,200),(int) floorAny(HEIGHT/2f + camera.y, 200));
    }

    private void update(){
        if (camera.x > 20)
            startMoving.x = true;

        startMoving.y = camera.y < 170-incrementor.y; // suppose camera goes up and down?

        displacement = PVector.sub(camera,startingCamera);
    }

    private void generate(){

        float largeStroke = 5, smallStroke = 2.5f;

        p.noFill();

        begin.x = (float) ceilAny(displacement.x - WIDTH/2f,200);
        end.x = (float) ceilAny(camera.x + WIDTH/2f,200);

        float ender = (end.x-begin.x)/incrementor.x;
        for (int i = 0; i < ender; i++){ // draws vert lines
            float x = begin.x + i*incrementor.x;
            if (x-displacement.x < 333-WIDTH/2f && startMoving.x)
                p.stroke(0,0,95,(float) Mapper.map2(x-displacement.x,90-WIDTH/2f, 333-WIDTH/2f,0,255, MapType.QUADRATIC, MapEase.EASE_IN_OUT));
            else
                p.stroke(0,0,95);

            if (Math.abs(Math.IEEEremainder(x-begin.x, 2*incrementor.x)) < EPSILON)
                p.strokeWeight(largeStroke);
            else
                p.strokeWeight(smallStroke);

            if (startMoving.y){
                p.line(x,camera.y+spacing.y,x,camera.y-spacing.y);
            } else {
                p.line(x,-spacing.y,x,Math.min(400,spacing.y));
            }
        }

        begin.y = (int) floorAny(HEIGHT/2f + camera.y, 200);
        end.y = (int) floorAny(-HEIGHT/2f + camera.y, incrementor.y); //This is the top of the p (as it is translated based on cameraPos)

        ender = (begin.y-end.y)/incrementor.y;
        for (int j = 0; j < ender; j++){  // draws horiz lines, processing draws y up to down cuz flipped (so invert the bounds)
            float y = begin.y - j*incrementor.y;
            if (Math.abs(Math.IEEEremainder(y-begin.y, 2*incrementor.y)) < EPSILON)
                p.strokeWeight(largeStroke);
            else
                p.strokeWeight(smallStroke);

            if (startMoving.x) {
                p.line(camera.x - spacing.x,y,camera.x + spacing.x,y);
            }
            else {
                p.line(Math.max(-spacing.x, -800),y,spacing.x,y);
            }

        }

    }

    public void drawMainAxes(){
        p.stroke(0,0,255);
        p.strokeWeight(6);

        if (!startMoving.x && !startMoving.y){
            p.line(startingCamera.x + Math.max(-spacing.x, -800), begin.y,startingCamera.x + spacing.x, begin.y);
            p.line(begin.x,-spacing.y,begin.x,Math.min(400,spacing.y));
        } else if (startMoving.x && !startMoving.y) {
            p.line(displacement.x - spacing.x, begin.y,displacement.x + spacing.x, begin.y);
        } else if (!startMoving.x){
            p.line(begin.x,camera.y + spacing.y,begin.x,camera.y - spacing.y);
        }
    }

    public void label(){
        // y value rectangle
        p.fill(ColorType.BLACK);
        p.noStroke();
        p.rect(displacement.x-WIDTH/2f,displacement.y-HEIGHT/2f,displacement.x + 153-WIDTH/2f,displacement.y + HEIGHT/2f); // buffer
        p.fill(ColorType.WHITE);

        p.textAlign(RIGHT,CENTER);

        p.stroke(ColorType.RED);
        float ender = (begin.y-end.y)/incrementor.y; // remember y's flipped!
        for (int j = 0; j < ender; j++){
            float y = begin.y - j*incrementor.y;
            if (Math.abs(Math.IEEEremainder(y-begin.y, 2*incrementor.y)) < EPSILON) {
                // -600 is the original begin.y <--- dont trust anything idk
                float txt = textify(y,Y);
                float yCoord;
                if (y == begin.y) // hmm?
                    yCoord = y - 20;
                else
                    yCoord = y - 2;

                p.text(df.format(Math.abs(txt)), displacement.x + 130 - WIDTH / 2f, yCoord); // account for everything !

            }
        }

        // x value rectangle
        p.fill(ColorType.BLACK);
        p.noStroke();
        p.rect(displacement.x -WIDTH/2f,displacement.y + HEIGHT/2f+100,displacement.x + WIDTH/2f,displacement.y + HEIGHT/2f-130); // buffer
        p.fill(ColorType.WHITE);
        p.textAlign(CENTER,CENTER);
        // Fade out first line(s) if it gets too close
        p.stroke(ColorType.RED);
        // increment end because text needs to show up before line (super efficient line)
        ender = (end.x-begin.x)/incrementor.x;
        for (int i = 0; i < ender; i++){
            float x = begin.x + i*incrementor.x;
            float txt = textify(x,X);
            if (txt == 0)
                continue;

            if (x-displacement.x < 333-WIDTH/2f && startMoving.x) // cant hurt now can it?
                if (x-displacement.x < 90-WIDTH/2f)
                    p.fill(0,0,0,0);
                else
                    p.fill(0,0,255,(float) Mapper.map2(x-displacement.x,90-WIDTH/2f, 333-WIDTH/2f,0,255, MapType.QUADRATIC, MapEase.EASE_IN_OUT));
            else
                p.fill(ColorType.WHITE);

            if (Math.abs(Math.IEEEremainder(x-begin.x, 2*incrementor.x)) < EPSILON)
                // -600 is the original begin.x
                p.text(df.format(txt),x,displacement.y + HEIGHT/2f - 95); // account for everything !
        }
    }

    public float textify(float r, int XorY){
        if (XorY == X)
            return 1/scale.x * (r-startingBegin.x); // begin.x ORIGINAL
        return -1/scale.y * (r-startingBegin.y); // begin.y ORIGINAL
    }

    public boolean draw(){
        init();
        p.scale(e);
        update();
        p.translate(PVector.mult(camera,-1));
        generate();
        boolean b = spacing.easeTo(new Vector(2*WIDTH/3f,2*HEIGHT/3f),1); // better to err on the side of caution
       // camera.easeTo(new Vector(1200,-300),QUADRATIC,15);
       // PApplet.println(begin,end);
   //     incrementor.add(new Vector(0.1f));
       // processing.image(p,-WIDTH/2f,-HEIGHT/2f);
        return b;
    }
}
