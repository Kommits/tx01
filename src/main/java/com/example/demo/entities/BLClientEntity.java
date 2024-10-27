package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//
// я не буду плодить ДТО так как у нас единственная энтити BL и она нуль валидна
// хотя по хорошему надо делать ДТО и на вход и на выход (ну то есть энтити как параметр - это прямо ну такое)
// но при одной-энтити-на-всё очень показательно как Se/De делает сам спринг (поэтому я так и оставлю,
// это специальная задумка именно для кейса с кафкой)

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "bl_client")
public class BLClientEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "first_name") private String firstName;
    @Column(name = "last_name") private String lastName;
    @Column(name = "middle_name") private String middleName;
}
