package vn.edu.usth.wordpressclient.model;

public class CommentCardModel {
    private int id;
    private int post;
    private int author;
    private String authorName;
    private String date;
    private String content;
    private String link;
    private String status;
    private String authorAvatar;
    private String postTitle;

    public CommentCardModel() {
    }

    public CommentCardModel(int id, int post, int author, String authorName, String date, String content, String link, String status, String authorAvatar) {
        this.id = id;
        this.post = post;
        this.author = author;
        this.authorName = authorName;
        this.date = date;
        this.content = content;
        this.link = link;
        this.status = status;
        this.authorAvatar = authorAvatar;
    }

    public int getId() {
        return id;
    }

    public int getPost() {
        return post;
    }

    public int getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }
}
