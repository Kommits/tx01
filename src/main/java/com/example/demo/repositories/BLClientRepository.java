package com.example.demo.repositories;

import com.example.demo.entities.BLClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//
// такое ощущение как будто в спринг расширения аннотации не хватает @Entity(createDefaultJpaRepository = true)
public interface BLClientRepository extends JpaRepository<BLClientEntity, Long> {}
