package com.ztesoft.zwfw.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/9/14.
 */

public class Chat implements Serializable{

    private String title;
    private String content;
    private String  attachments;
    private String isPublic;
    private Long  toUserId;
    private Long byUserId;
    private String state ;
    private Long readState;
    private String chatType;
    private List<Comment> commentDtoList;
    private String toUserName;
    private String byUserName;
    private String toUserCode;
    private String byUserCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Long getByUserId() {
        return byUserId;
    }

    public void setByUserId(Long byUserId) {
        this.byUserId = byUserId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getReadState() {
        return readState;
    }

    public void setReadState(Long readState) {
        this.readState = readState;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public List<Comment> getCommentDtoList() {
        return commentDtoList;
    }

    public void setCommentDtoList(List<Comment> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getByUserName() {
        return byUserName;
    }

    public void setByUserName(String byUserName) {
        this.byUserName = byUserName;
    }

    public String getToUserCode() {
        return toUserCode;
    }

    public void setToUserCode(String toUserCode) {
        this.toUserCode = toUserCode;
    }

    public String getByUserCode() {
        return byUserCode;
    }

    public void setByUserCode(String byUserCode) {
        this.byUserCode = byUserCode;
    }
}
