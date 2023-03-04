package org.mehmetcc.candlesticksweb.candlestick;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CandlestickController {
    private final CandlestickService service;

    @GetMapping("/candlesticks")
    public ResponseEntity<?> all(@RequestParam String isin) {
        return service.retrieveCandlesticks(isin)
                .fold(
                        exceptionMessage -> new ResponseEntity<>(exceptionMessage.message(), exceptionMessage.status()),
                        retrievedMessage -> ResponseEntity.ok(retrievedMessage));
    }
}
