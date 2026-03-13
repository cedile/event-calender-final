package com.devtiro.tasks.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public class ParticipantDto {

    private UUID id;

    @NotBlank(message = "Vorname ist erforderlich")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Vorname darf nur Buchstaben enthalten")
    private String firstName;

    @NotBlank(message = "Nachname ist erforderlich")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Nachname darf nur Buchstaben enthalten")
    private String lastName;

    public ParticipantDto() {
    }

    public ParticipantDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

