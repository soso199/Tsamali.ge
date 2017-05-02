package ge.idevelopers.tsamali.models;

/**
 * Created by soso on 4/5/17.
 */

public class OffersModel {
    private String title;
    private String img;
    private String text;
    private String link;
    private int views;
    private int id;

    public OffersModel(String title, String img, String text, int views,String link,int id) {
        this.title = title;
        this.img = img;
        this.text = text;
        this.views = views;
        this.link=link;
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
