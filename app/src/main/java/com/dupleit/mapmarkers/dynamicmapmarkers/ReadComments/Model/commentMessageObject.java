package com.dupleit.mapmarkers.dynamicmapmarkers.ReadComments.Model;

/**
 * Created by android on 8/12/17.
 */

public class commentMessageObject {

    private int user_id;
    private String user_name;
    private String commment_text;
    private String comment_datetime;
    private String image_path;
    private Boolean image_attachment;
    private Boolean block;
    private Boolean delete;

    public commentMessageObject() {
    }

    public commentMessageObject(Boolean block,
                                String comment_datetime,
                                String commment_text,
                                Boolean delete,
                                Boolean image_attachment,
                                String image_path,
                                int user_id,
                                String user_name) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.commment_text = commment_text;
        this.comment_datetime = comment_datetime;
        this.image_path = image_path;
        this.image_attachment = image_attachment;
        this.block = block;
        this.delete = delete;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCommment_text() {
        return commment_text;
    }

    public void setCommment_text(String commment_text) {
        this.commment_text = commment_text;
    }

    public String getComment_datetime() {
        return comment_datetime;
    }

    public void setComment_datetime(String comment_datetime) {
        this.comment_datetime = comment_datetime;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Boolean isImage_attachment() {
        return image_attachment;
    }

    public void setImage_attachment(Boolean image_attachment) {
        this.image_attachment = image_attachment;
    }

    public Boolean isBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Boolean isDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

}
