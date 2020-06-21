package com.lrm.service;

import com.lrm.po.History;
import com.lrm.po.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryService {
    History check(Long userid, Long blogid);
    void delete(Long id);
}
