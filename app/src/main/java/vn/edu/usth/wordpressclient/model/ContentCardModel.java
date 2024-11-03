package vn.edu.usth.wordpressclient.model;

public class ContentCardModel {
    int id;
    String title;
    String content;
    String date;

    public ContentCardModel(int id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
}