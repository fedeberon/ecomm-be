package com.ideaas.ecomm.ecomm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideaas.ecomm.ecomm.domain.Size;
import com.ideaas.ecomm.ecomm.services.interfaces.ISizeService;

@RestController
@RequestMapping("size")
@CrossOrigin
public class SizeController {
    
    private ISizeService sizeService;

    @Autowired
    public SizeController(final ISizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping 
    private ResponseEntity<List<Size>> findAll() {
        List<Size> talles = sizeService.findAll();
        
        return ResponseEntity.ok(talles);
    }

    @GetMapping("{id}")
    public ResponseEntity<Size> findById(@PathVariable final Long id){
        final Size size = sizeService.get(id);

        return ResponseEntity.ok(size);
    }

    @PostMapping
    public ResponseEntity<Size> save(@RequestBody Size size) {
        final Size sizeSaved = sizeService.save(size);

        return ResponseEntity.accepted().body(sizeSaved);
    }

    @PutMapping("{id}")
    public ResponseEntity<Size> update(@PathVariable final Long id, @RequestBody final Size size){
        final Size sizeToUpdate = sizeService.get(id);

        sizeToUpdate.setName(size.getName());

        sizeService.save(sizeToUpdate);
        return ResponseEntity.ok(sizeToUpdate);
    }
}
