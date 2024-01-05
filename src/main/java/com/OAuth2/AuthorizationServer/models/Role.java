package com.OAuth2.AuthorizationServer.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
public class Role extends BaseClass{
    private String name;
}
