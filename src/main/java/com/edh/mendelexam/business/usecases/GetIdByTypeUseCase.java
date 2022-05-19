package com.edh.mendelexam.business.usecases;

import java.util.Set;

public interface GetIdByTypeUseCase {
    Set<Long> execute(String type);
}
