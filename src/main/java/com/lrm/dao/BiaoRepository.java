package com.lrm.dao;

import com.lrm.po.Biao;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BiaoRepository extends JpaRepository<Biao,Long>, JpaSpecificationExecutor<Biao> {


}
