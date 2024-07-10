package sunsetsatellite.signalindustries.util;

public class IdMetaPair {
    public final int id;
    public final int meta;

    public IdMetaPair(int id, int meta) {
        this.id = id;
        this.meta = meta;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdMetaPair)) return false;

        IdMetaPair that = (IdMetaPair) o;
        return id == that.id && meta == that.meta;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + meta;
        return result;
    }

    @Override
    public String toString() {
        return "#" + id + ":" + meta;
    }
}
