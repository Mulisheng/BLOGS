package com.lrm.po;

import javax.persistence.*;

@Entity
@Table(name = "t_biao")
public class Biao {
    @Id
    @GeneratedValue
    private Long id;
    private String dct;
    private Long blogid;
    private String url;
    private String type;
    private boolean status=false;
    @ManyToOne()
    private User user;

    @ManyToOne()
    private Blog blog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDct() {
        return dct;
    }

    public void setDct(String dct) {
        this.dct = dct;
    }

    public Long getBlogid() {
        return blogid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBlogid(Long blogid) {
        this.blogid = blogid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "Biao{" +
                "id=" + id +
                ", dct='" + dct + '\'' +
                ", blogid=" + blogid +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}


