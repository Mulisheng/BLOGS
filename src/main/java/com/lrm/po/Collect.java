package com.lrm.po;

import javax.persistence.*;

@Entity
@Table(name="t_collection")
public class Collect {
    @Id
    @GeneratedValue
    private Long id;
    private Long userid;
    private Long blogid;
    private String title;
    @ManyToOne
    private Blog blog;
    public Collect()
    {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getBlogid() {
        return blogid;
    }

    public void setBlogid(Long blogid) {
        this.blogid = blogid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", userid=" + userid +
                ", blogid=" + blogid +
                ", title='" + title + '\'' +
                '}';
    }
}
