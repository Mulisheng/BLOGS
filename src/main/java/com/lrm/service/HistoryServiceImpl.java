package com.lrm.service;

import com.lrm.dao.BlogRepository;
import com.lrm.dao.HistoryRepository;
import com.lrm.po.History;
import com.lrm.po.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class HistoryServiceImpl implements  HistoryService{
    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public History check(Long userid, Long blogid) {
        System.out.println("check!!");
       History history=historyRepository.findByUseridAndBlogid(userid,blogid);
        return history;
    }

    @Override
    public void delete(Long id) {
        historyRepository.delete(id);
    }


}
