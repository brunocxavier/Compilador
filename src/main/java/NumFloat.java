public class NumFloat extends Token{
    public final float value;

    public NumFloat(String value) {
        super(Tag.NUM);
        this.value = Float.parseFloat(value);
    }

    public String toString(){
        return "" + value;
    }

    public float getValue() {
        return value;
    }
}
