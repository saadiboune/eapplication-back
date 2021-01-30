package com.eapplication.eapplicationback.models.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoCompDTO {

    private List<String> listValues;
    private int countValues;

}
