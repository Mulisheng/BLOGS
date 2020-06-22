package com.lrm.service;

import com.lrm.dao.BiaoRepository;
import com.lrm.po.Biao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BiaoServiceImpl implements BiaoService{
    @Autowired
    BiaoRepository biaoRepository;

    @Transactional
    @Override
    public String bsave(Biao biao){
        biaoRepository.save(biao);
        return "";
    }

}
