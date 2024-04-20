package com.codefusion.wasbackend.transaction.controller;

import com.codefusion.wasbackend.transaction.dto.TransactionDTO;
import com.codefusion.wasbackend.transaction.service.TransactionService;
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
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;


    @GetMapping("/getTransactionById/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/allTransaction")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(transactionService.getTransactionsByStoreId(storeId));
    }

    @PostMapping(value = "/addTransaction", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TransactionDTO> addTransaction(@ModelAttribute TransactionDTO transactionDTO,
                                                         @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(transactionService.addTransaction(transactionDTO, file), HttpStatus.CREATED);
    }

    @PutMapping(value = "/updateTransaction/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id,
                                                            @ModelAttribute TransactionDTO transactionDTO,
                                                            @RequestParam(required = false) MultipartFile file) throws IOException {
        transactionDTO.setId(id);
        return ResponseEntity.ok(transactionService.update(id, transactionDTO, file));
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) throws IOException {
        transactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
