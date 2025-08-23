package com.bna.web.service;

import com.bna.service.dto.MondatDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MondatEnAtt {

    private final Map<String, MondatDTO> pendingTransactions = new ConcurrentHashMap<>();

    public String addPendingTransaction(MondatDTO mondat) {
        String token = UUID.randomUUID().toString();
        pendingTransactions.put(token, mondat);
        return token;
    }

    public MondatDTO confirmTransaction(String token) {
        return pendingTransactions.remove(token); // removes and returns
    }

    public boolean exists(String token) {
        return pendingTransactions.containsKey(token);
    }
}
