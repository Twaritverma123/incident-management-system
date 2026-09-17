package com.example.incident_management.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String userName;
    private String password;
    private String email;
    private List<String> roles= new ArrayList<>();

}
