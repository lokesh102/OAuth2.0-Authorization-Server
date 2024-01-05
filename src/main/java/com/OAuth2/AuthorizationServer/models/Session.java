package com.OAuth2.AuthorizationServer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "sessions")
@Getter
@Setter
public class Session extends BaseClass{
    private String token;
    private String ipAddress;
    @ManyToOne
    private User user;
    private Date expiry;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
