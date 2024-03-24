package com.codefusion.wasbackend.productField.controller;

import com.codefusion.wasbackend.productField.dto.ProductFieldDTO;
import com.codefusion.wasbackend.productField.service.ProductFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productField")
@RequiredArgsConstructor
public class ProductFieldController {

    private final ProductFieldService productFieldService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductFieldDTO> getProductFieldById(@PathVariable Long id) {
        return new ResponseEntity<>(productFieldService.getProductFieldById(id), HttpStatus.OK);
    }

    @GetMapping("/getAllProductField")
    public ResponseEntity<List<ProductFieldDTO>> getAllProductFields() {
        return new ResponseEntity<>(productFieldService.getAllProductField(), HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductFieldDTO>> getProductFieldsByProductId(@PathVariable Long productId) {
        return new ResponseEntity<>(productFieldService.getProductFieldByProductId(productId), HttpStatus.OK);
    }

    @PostMapping("/addProductField")
    public ResponseEntity<ProductFieldDTO> addProductField(@RequestBody ProductFieldDTO productFieldDTO) {
        return new ResponseEntity<>(productFieldService.addProductField(productFieldDTO), HttpStatus.CREATED);
    }

    @PutMapping("/updateProductField/{id}")
    public ResponseEntity<ProductFieldDTO> updateProductField(@PathVariable Long id, @RequestBody ProductFieldDTO productFieldDTO){
        return new ResponseEntity<>(productFieldService.updateProductField(id, productFieldDTO), HttpStatus.OK);
    }

    @DeleteMapping("/deleteProductField/{id}")
    public ResponseEntity<?> deleteProductField(@PathVariable Long id){
        productFieldService.deleteProductField(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}