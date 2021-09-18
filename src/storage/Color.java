package storage;
import util.map.MapEase;
import util.map.MapType;

import java.util.Objects;

import static util.map.MapType.QUADRATIC;

public class Color {
    Subcolor hue, saturation, brightness, alpha;
    boolean interpStatus = true;

    public Color(float hue) {
        this(hue, 100, 100, 100);
    }

    public Color(float hue, float saturation) {
        this(hue, saturation, 100, 100);

    }

    public Color(float hue, float saturation, float brightness) {
        this(hue, saturation, brightness, 100);
    }

    public Color(float hue, float saturation, float brightness, float alpha) {
        this.hue = new Subcolor(hue);
        this.saturation = new Subcolor(saturation);
        this.brightness = new Subcolor(brightness);
        this.alpha = new Subcolor(alpha);
    }

    public Color(ColorType c) {
        switch (c) {
            case WHITE -> {
                hue = new Subcolor(0);
                saturation = new Subcolor(0);
                brightness = new Subcolor(100);
            }
            case RED -> {
                hue = new Subcolor(360);
                saturation = new Subcolor(100);
                brightness = new Subcolor(100);
            }
            case CYAN -> {
                hue = new Subcolor(180);
                saturation = new Subcolor(100);
                brightness = new Subcolor(100);
            }
            case BLACK -> {
                hue = new Subcolor(0);
                saturation = new Subcolor(0);
                brightness = new Subcolor(0);
            }
            case GREEN -> {
                hue = new Subcolor(150);
                saturation = new Subcolor(100);
                brightness = new Subcolor(100);
            }
            case MAGENTA -> {
                hue = new Subcolor(300);
                saturation = new Subcolor(100);
                brightness = new Subcolor(100);
            }
            case ORANGE -> {
                hue = new Subcolor(40);
                saturation = new Subcolor(100);
                brightness = new Subcolor(100);
            }
        }
        alpha = new Subcolor(100);
    }

    public Color() {
        this(0, 100, 100, 100);
    }

    public Color(Subcolor hue, Subcolor saturation, Subcolor brightness, Subcolor alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha = alpha; // Storing reference
    }

    public Color(Color color) {
        this.hue = new Subcolor(color.getHue());
        this.saturation = new Subcolor(color.getSaturation());
        this.brightness = new Subcolor(color.getBrightness());
        this.alpha = new Subcolor(color.getAlpha());
    }

    public Subcolor getHue() {
        return hue;
    }

    public Subcolor getSaturation() {
        return saturation;
    }

    public Subcolor getBrightness() {
        return brightness;
    }

    public Subcolor getAlpha() {
        return alpha;
    }

    public void setHue(float newHue) {
        hue.setValue(newHue);
    }

    public void setSaturation(float newSat) {
        saturation.setValue(newSat);
    }

    public void setBrightness(float newBri) {
        brightness.setValue(newBri);
    }

    public void setAlpha(float newAlpha) {
        alpha.setValue(newAlpha);
    }

    public boolean easeTo(Color color, MapType interpType, float time, MapEase easing) {
        interpStatus = this.getHue().easeTo(color.getHue().getValue(), interpType, time, easing) &
                this.getSaturation().easeTo(color.getSaturation().getValue(), interpType,time , easing) &
                this.getBrightness().easeTo(color.getBrightness().getValue(), interpType, time, easing) &
                this.getAlpha().easeTo(color.getAlpha().getValue(), interpType, time, easing);
        return interpStatus;
    }

    public boolean easeTo(Color color) {
        return this.easeTo(color, QUADRATIC, 1, MapEase.EASE_IN_OUT);
    }

    public boolean easeTo(Color color, float time) {
        return this.easeTo(color, QUADRATIC, time, MapEase.EASE_IN_OUT);
    }


    public java.awt.Color toJavaRGB() {
        return java.awt.Color.getHSBColor(hue.getValue() / 360, saturation.getValue() / 100, brightness.getValue() / 100);
    }

    public String toString() {
        return String.format("[ %s, %s, %s, %s ]", hue, saturation, brightness, alpha);
    }

    public Color invert() {
        return new Color(Math.abs((180 - hue.getValue()) % 360), saturation.getValue(), brightness.getValue(), alpha.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color color)) return false;
        return getHue().equals(color.getHue()) &&
                getSaturation().equals(color.getSaturation()) &&
                getBrightness().equals(color.getBrightness()) &&
                getAlpha().equals(color.getAlpha());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHue(), getSaturation(), getBrightness(), getAlpha());
    }

    public boolean getInterpolationStatus() {
        return this.interpStatus;
    }
}

