package com.lrm.service;

import com.lrm.dao.BiaoRepository;
import com.lrm.po.Biao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface BiaoService {
    String bsave(Biao biao);

}
