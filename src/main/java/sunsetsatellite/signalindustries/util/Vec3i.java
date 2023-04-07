package sunsetsatellite.signalindustries.util;

public class Vec3i {
    public int x;
    public int y;
    public int z;

    public Vec3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    };


    @Override
    public String toString() {
        return "Vec3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vec3i vec3I = (Vec3i) o;

        if (x != vec3I.x) return false;
        if (y != vec3I.y) return false;
        return z == vec3I.z;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
