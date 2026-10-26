/*
 *
 */

package util.vector;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

public class Vector2f {

    public final static float TWO_PI = (float) (2.0 * Math.PI);

    public float x = 0.0f;
    public float y = 0.0f;

    public Vector2f() {}

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f build(float length, float angle) {
        float x = (float) Math.cos(angle) * length;
        float y = (float) Math.sin(angle) * length;
        return new Vector2f(x, y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return getAngle(x, y);
    }

    public float getLength() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public Vector2f normalize() {
        float length = getLength();
        if (length == 0.0f) {
            return this;
        }
        this.x /= length;
        this.y /= length;
        return this;
    }

    public Vector2f add(Vector2f vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public Vector2f subtract(Vector2f vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }

    public Vector2f multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    // --- static utility methods ---

    public static float vectorProduct(Vector2f v1, Vector2f v2) {
        return v1.x*v2.y - v1.y*v2.x;
    }

    public static float dotProduct(Vector2f v1, Vector2f v2) {
        return v1.x*v2.x + v1.y*v2.y;
    }

    public static Vector2f normalize(Vector2f v) {
        float length = v.getLength();
        return new Vector2f(v.x/length, v.y/length);
    }

    public static float normalizeAngle(float angle) {
        angle %= TWO_PI;
        return angle < 0 ? angle + TWO_PI : angle;
    }

    public static Vector2f add(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2f subtract(Vector2f v1, Vector2f v2) {
        return new Vector2f(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector2f multiply(Vector2f v, float scalar) {
        return new Vector2f(v.x * scalar, v.y * scalar);
    }

    public static float getAngle(Vector2f v1, Vector2f v2) {
        float angle1 = v1.getAngle();
        float angle2 = v2.getAngle();
        return normalizeAngle(angle1 - angle2);
    }

    public static float getAngle(float x, float y) {
        float angle = (float) Math.atan2(y, x);
        return normalizeAngle(angle);
    }

}
