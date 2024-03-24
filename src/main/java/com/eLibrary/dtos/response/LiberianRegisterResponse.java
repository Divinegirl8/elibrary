package com.eLibrary.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter

public class LiberianRegisterResponse {
    private Long id;

    @Override
    public String toString() {
        return "LiberianRegisterResponse{" +
                "id=" + id +
                '}';
    }
}
