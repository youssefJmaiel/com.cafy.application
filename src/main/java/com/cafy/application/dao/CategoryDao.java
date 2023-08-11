package com.cafy.application.dao;

import com.cafy.application.entity.Category;
import com.cafy.application.view.iCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {

    List<Category> getAllCategory();



}
