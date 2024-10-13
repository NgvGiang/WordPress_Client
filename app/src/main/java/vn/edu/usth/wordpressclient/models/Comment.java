package vn.edu.usth.wordpressclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Comment implements Parcelable {
    private Long id;
    private Long post;
    private Long parent;
    private Long author;
    private String authorName;
    private String authorUrl;
    private String date;
    private String content;
    private String link;
    private String status;
    private String authorAvatar;

    public Comment() {
    }

    public Comment(Long id, Long post, Long parent, Long author, String authorName, String authorUrl, String date, String content, String link, String status, String authorAvatar) {
        this.id = id;
        this.post = post;
        this.parent = parent;
        this.author = author;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.date = date;
        this.content = content;
        this.link = link;
        this.status = status;
        this.authorAvatar = authorAvatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    protected Comment(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        post = (Long) in.readValue(Long.class.getClassLoader());
        parent = (Long) in.readValue(Long.class.getClassLoader());
        author = (Long) in.readValue(Long.class.getClassLoader());
        authorName = in.readString();
        date = in.readString();
        content = in.readString();
        link = in.readString();
        status = in.readString();
        authorAvatar = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(post);
        dest.writeValue(parent);
        dest.writeValue(author);
        dest.writeString(authorName);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeString(link);
        dest.writeString(status);
        dest.writeString(authorAvatar);
    }
}
