package com.example.uberbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationStatusDto {
    @NotNull(message = "Invitation id does not exist!")
    private Long invitationId;
    private boolean accepted;
}
