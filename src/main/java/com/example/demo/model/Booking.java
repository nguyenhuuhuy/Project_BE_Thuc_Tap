package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private IsConfirm isConfirm;
    private String reason;
    @Temporal(TemporalType.DATE)
    private Date date_book;
    @ManyToOne
    TimeSlot timeSlot;
    @ManyToOne
    User user;
}
