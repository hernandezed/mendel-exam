package com.edh.mendelexam.ports;

import com.edh.mendelexam.business.bos.TransactionNodeBo;

import java.util.Optional;

public interface TransactionNodePort {

    Optional<TransactionNodeBo> findById(Long id);
}
