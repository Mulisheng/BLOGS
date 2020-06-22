package com.lrm.service;

import com.lrm.po.Collect;

public interface CollectService {
    Collect checkCollect(Long blogid, Long userid);

}
