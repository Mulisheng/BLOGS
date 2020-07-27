package com.lrm.po;

import javax.persistence.*;

@Entity
@Table(name = "following")
public class Following {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    private User user;

    private  Long followingId;

    private String self;

    private String followName;

    public Following()
    {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getFollowName() {
        return followName;
    }

    public void setFollowName(String followName) {
        this.followName = followName;
    }

    @Override
    public String toString() {
        return "Following{" +
                "id=" + id +
                ", user=" + user +
                ", followingId=" + followingId +
                ", self='" + self + '\'' +
                ", followName='" + followName + '\'' +
                '}';
    }
}
