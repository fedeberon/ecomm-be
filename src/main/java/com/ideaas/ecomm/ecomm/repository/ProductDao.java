package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;

import org.hibernate.annotations.OrderBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderColumn;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends PagingAndSortingRepository<Product, Long> {

    List<Product> findAllByCategory(Category category);

    List<Product> findByCategoryAndDeleted(Category category, Boolean deleted);

    List<Product> findAllByNameContainingIgnoreCase(String value);

    List<Product> searchAllByBrandInAndDeleted(Collection<Brand> brandId, Boolean deleted);

    List<Product> searchAllByCategoryInAndDeleted(Collection<Category> categories, Boolean deleted);

    List<Product> findAllByNameContainingIgnoreCase(String value, Pageable pageable);

    List<Product> findByDeleted(Boolean deleted);

    Page<Product> findByDeleted(Boolean deleted, Pageable pageable);

    List<Product> findAllByNameContainingIgnoreCaseAndDeleted(String value, boolean b);

    @Query("SELECT p FROM Product p WHERE p.name like %:name% and p.category = :category")
    List<Product> getRelationship(String name, Category category);

    List<Product> findByCategoryInAndBrandInAndDeletedFalse(List<String> categorias, List<String> marcas);

    //NUEVOS METODOS CON FILTROS
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryInAndBrandIn(String name,Collection<Category> categories,Collection<Brand> brands, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndBrandIn(String name, Collection<Brand> brands, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryIn(String name,Collection<Category> categories, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);

}
