package com.ideaas.ecomm.ecomm.services.interfaces;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Size;

public interface ISizeService {
    List<Size> findAll();

    Size save(Size size);

    Size get(Long id);
}
