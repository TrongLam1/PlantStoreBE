package com.java.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "plant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PlantEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@CreatedDate
	private Date createdDate;
	
    private String name;
    
    private String type;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "oldPrice")
    private Long oldPrice;
    
    @Column(name = "newPrice")
    private Long newPrice;
    
    private double sale;
    
    private double rating;
    
    private PlantStatus valid;
    
    private String imageUrl;
    
    private String imageId;
    
    @OneToMany(mappedBy = "plant")
    @JsonBackReference
    private List<CommentEntity> comments = new ArrayList<>();
}

