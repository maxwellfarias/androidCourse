package co.tiagoaguiar.codelab.myapplication.archives;

public class MainItem {
    private int id;
    private int textStringId;
    private int drawable;
    private int color;


    public MainItem(int id, int textStringId, int drawable, int color) {
        this.id = id;
        this.textStringId = textStringId;
        this.drawable = drawable;
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTextStringId(int textStringId) {
        this.textStringId = textStringId;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getTextStringId() {
        return textStringId;
    }

    public int getId() {
        return id;
    }
}
