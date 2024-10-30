package vn.edu.usth.wordpressclient.model;

public class MediaCardModel {
    String picture_url;

    public MediaCardModel(String picture_url){
        this.picture_url = picture_url;
    }

    public String getPicture_url(){
        return  picture_url;
    }
}
