package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.WantVisitResponse;
import com.grocery.travel_app.service.WantVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visit-list")
@RequiredArgsConstructor
public class WantVisitController {
    private final WantVisitService wantVisitService;

    @PostMapping
    public ResponseEntity<WantVisitResponse> addToVisitList(@RequestParam Long destinationId ,@RequestParam Long userId){
        WantVisitResponse response = wantVisitService.addToVisitList(destinationId,userId);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WantVisitResponse>> getUserVisitList(@PathVariable Long userId){
        return ResponseEntity.ok(wantVisitService.getUserVisitList(userId));
    }
    @DeleteMapping("/{visitId}/user/{userId}")
    public ResponseEntity<Void> removeFromVisitList(@PathVariable Long visitId ,@PathVariable Long userId){
        wantVisitService.removeFromVisitList(visitId,userId);
        return ResponseEntity.noContent().build();
    }
}
