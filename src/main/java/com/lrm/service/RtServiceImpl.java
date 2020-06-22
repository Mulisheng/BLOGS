package com.lrm.service;

import com.lrm.dao.RtRepository;
import com.lrm.po.RT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RtServiceImpl implements RtService {
    @Autowired
    private RtRepository rtRepository;
    @Transactional
    @Override
    public RT s(RT report){
        System.out.println("漂亮");
        RT rt=rtRepository.save(report);
        return rt;
    }
//    @Transactional
//    @Override
//    public Report saveReport(Report rept) {
//        return reportRepository.save(rept);
//    }

}
