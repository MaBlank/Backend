package com.example.backendfachpraktikumrefactored.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ListDTO {
    private UUID guid;
    private String text;
}
