package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

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

    //========= findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryInAndBrandIn =========
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(REPLACE(REPLACE(REPLACE(p.name, 'á', 'a'), 'é', 'e'), 'í', 'i')) LIKE LOWER(CONCAT('%', :normalizedSearch, '%')) " +
            "AND p.deleted = false " +
            "AND p.category IN :categories " +
            "AND p.brand IN :brands")
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryInAndBrandIn(
            @Param("normalizedSearch") String normalizedSearch,
            @Param("categories") Collection<Category> categories,
            @Param("brands") Collection<Brand> brands,
            Pageable pageable);

    //========= findAllByNameContainingIgnoreCaseAndDeletedFalseAndBrandIn =========
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(REPLACE(REPLACE(REPLACE(p.name, 'á', 'a'), 'é', 'e'), 'í', 'i')) LIKE LOWER(CONCAT('%', :normalizedSearch, '%')) " +
            "AND p.deleted = false " +
            "AND p.brand IN :brands")
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndBrandIn(
            @Param("normalizedSearch") String normalizedSearch,
            @Param("brands") Collection<Brand> brands,
            Pageable pageable);

    //========= findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryIn =========
    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(REPLACE(REPLACE(REPLACE(p.name, 'á', 'a'), 'é', 'e'), 'í', 'i')) LIKE LOWER(CONCAT('%', :normalizedSearch, '%')) " +
            "AND p.deleted = false " +
            "AND p.category IN :categories")
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAndCategoryIn(
            @Param("normalizedSearch") String normalizedSearch,
            @Param("categories") Collection<Category> categories,
            Pageable pageable);

    //========= findAllByNameContainingIgnoreCaseAndDeletedFalse =========
    @Query("SELECT p FROM Product p WHERE LOWER(REPLACE(REPLACE(REPLACE(p.name, 'á', 'a'), 'é', 'e'), 'í', 'i')) LIKE LOWER(CONCAT('%', :normalizedSearch, '%')) AND p.deleted = false")
    Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalse(@Param("normalizedSearch") String normalizedSearch, Pageable pageable);

    default Page<Product> findAllByNameContainingIgnoreCaseAndDeletedFalseAccentInsensitive(String search, Pageable pageable) {
        String normalizedSearch = normalizeAndRemoveAccents(search);
        return findAllByNameContainingIgnoreCaseAndDeletedFalse( normalizedSearch, pageable);
    }

    // Additional method to normalize strings and remove accents
    @Query("SELECT LOWER(REPLACE(REPLACE(REPLACE(p.name, 'á', 'a'), 'é', 'e'), 'í', 'i')) FROM Product p")
    String normalizeAndRemoveAccents(@Param("input") String input);

    //METODOS
    List<Product> findAllByCategoryAndDeletedFalse(Category category);
    List<Product> findAllByStoreAndDeletedFalse(Store store);
    List<Product> findAllByBrandAndDeletedFalse(Brand brand);
    List<Product> findAllByNameContainingIgnoreCaseAndDeletedFalse(String name);
    List<Product> findAll();
}
