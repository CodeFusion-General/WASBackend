package com.codefusion.wasbackend.store.controller;

import com.codefusion.wasbackend.store.service.StoreService;
import com.codefusion.wasbackend.store.dto.StoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;


    @GetMapping("/getStoreById/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping("/allStore")
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<StoreDTO>> getStoresByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoresByUserId(storeId));
    }

    @PostMapping(value = "/addStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreDTO> addStore(@ModelAttribute StoreDTO storeDTO, @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(storeService.addStore(storeDTO, file), HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateStore/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoreDTO> updateStore(@PathVariable Long id,
                                                @ModelAttribute StoreDTO storeDTO,
                                                @RequestParam(required = false) MultipartFile file) throws IOException {
        storeDTO.setId(id);
        return ResponseEntity.ok(storeService.update(id, storeDTO, file));
    }

    @DeleteMapping("/deleteStore/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) throws IOException {
        storeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
