package com.lrm.service;

import com.lrm.dao.CollectRepository;
import com.lrm.po.Collect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl implements CollectService{
    @Autowired
    CollectRepository collectRepository;
    @Override
    public Collect checkCollect(Long blogid, Long userid) {
        return collectRepository.findCollectByBlogidAndUserid(blogid, userid);
    }
}
