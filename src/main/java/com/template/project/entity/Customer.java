package com.template.project.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
public class Customer {

    @Id
    private Long chatId;

    private String username;

    @OneToMany(mappedBy="customer")
    private Set<Order> orders;

}
