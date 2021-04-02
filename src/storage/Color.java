package storage;
import java.util.Objects;
public class Color {
    Subcolor hue, saturation, brightness, alpha;

    public Color(float hue) {
        this(hue, 255, 255, 255);
    }

    public Color(float hue, float saturation) {
        this(hue, saturation, 255, 255);

    }

    public Color(float hue, float saturation, float brightness) {
        this(hue, saturation, brightness, 255);
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
                brightness = new Subcolor(255);
            }
            case RED -> {
                hue = new Subcolor(255);
                saturation = new Subcolor(255);
                brightness = new Subcolor(255);
            }
            case CYAN -> {
                hue = new Subcolor(120);
                saturation = new Subcolor(255);
                brightness = new Subcolor(255);
            }
            case BLACK -> {
                hue = new Subcolor(255);
                saturation = new Subcolor(0);
                brightness = new Subcolor(0);
            }
            case GREEN -> {
                hue = new Subcolor(107);
                saturation = new Subcolor(0);
                brightness = new Subcolor(255);
            }
            case MAGENTA -> {
                hue = new Subcolor(210);
                saturation = new Subcolor(255);
                brightness = new Subcolor(255);
            }
        }
        alpha = new Subcolor(255);
    }

    public Color() {
        this(0, 255, 255, 255);
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

    public java.awt.Color toJavaRGB() {
        return java.awt.Color.getHSBColor(hue.getValue() / 255, saturation.getValue() / 255, brightness.getValue() / 255);
    }

    public String toString() {
        return String.format("[ %s, %s, %s, %s ]", hue, saturation, brightness, alpha);
    }

    public Color invert() {
        return new Color(Math.abs((180 - hue.getValue()) % 255), saturation.getValue(), brightness.getValue(), alpha.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color color = (Color) o;
        return getHue().equals(color.getHue()) &&
                getSaturation().equals(color.getSaturation()) &&
                getBrightness().equals(color.getBrightness()) &&
                getAlpha().equals(color.getAlpha());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHue(), getSaturation(), getBrightness(), getAlpha());
    }
}

