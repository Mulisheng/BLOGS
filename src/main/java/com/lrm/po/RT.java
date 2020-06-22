package com.lrm.po;

import javax.persistence.*;

@Entity
@Table(name = "t_rt")
public class RT {
    @Id
    @Column
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue
    private Long id;

//    @Column
//    private String des;


    public RT()
    {

    }


//    public String getDes() {
//        return des;
//    }
//
//    public void setDes(String des) {
//        this.des = des;
//    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                '}';
    }
}

