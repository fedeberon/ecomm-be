package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecommendService {
    private final ProductDao dao;

    @Autowired
    public RecommendService(final ProductDao dao) { this.dao = dao; }

    //Genera la cadena de handlers y obtiene los productos solicitados en una lista
    public List<Product> generateRecommendedProducts(Long productId, Integer amount) {
        return dao.findById(productId)
                .map(targetProduct -> sortProducts(getProductList(targetProduct, new ArrayList<>(), amount), amount, targetProduct))
                .orElse(Collections.emptyList());
    }

    //Obtiene la lista de productos relacionados estableciendo la cadena de handlers
    private List<Product> getProductList(Product targetProduct, List<Product> products, Integer amount){

        AbstractRecommendationHandler categoryHandler = new CategoryRecommendationHandler(dao);
        AbstractRecommendationHandler storeHandler = new StoreRecommendationHandler(dao);
        AbstractRecommendationHandler brandHandler = new BrandRecommendationHandler(dao);
        AbstractRecommendationHandler partialNameHandler = new PartialNameRecommendationHandler(dao);
        AbstractRecommendationHandler defaultHandler = new DefaultHandler(dao);

        categoryHandler.setNextHandler(storeHandler);
        storeHandler.setNextHandler(brandHandler);
        brandHandler.setNextHandler(partialNameHandler);
        partialNameHandler.setNextHandler(defaultHandler);
        return categoryHandler.handleRequest(targetProduct, products, amount);
    }

    private List<Product> sortProducts(List<Product> productList, Integer amount, Product targetProduct) {
        String targetStoreName = targetProduct.getStore().getName();

        Comparator<Product> bySameStoreFirst = Comparator
                .<Product, Boolean>comparing(p -> targetStoreName.equals(p.getStore().getName()))
                .reversed()
                .thenComparing(Comparator.comparing(Product::getSales, Comparator.reverseOrder()));

        return productList.stream()
                .sorted(bySameStoreFirst)
                .limit(amount)
                .collect(Collectors.toList());
    }


    //=================================================================================================================
    //HANDLERS
    //=================================================================================================================

    //Interfaz para todos los handlers: define 1) los parametros base que deben recibir y 2) el metodo setNextHandler
    private abstract class AbstractRecommendationHandler {
        protected final ProductDao dao;
        protected AbstractRecommendationHandler nextHandler;

        public AbstractRecommendationHandler(ProductDao dao) {
            this.dao = dao;
        }

        public void setNextHandler(AbstractRecommendationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }

        abstract List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount);

        protected List<Product> handleRecommendationRequest(AbstractRecommendationHandler nextHandler, List<Product> productsToReturn, List<Product> newProducts, Product target, Integer amount) {
            productsToReturn = addUniqueProducts(productsToReturn , filterOutTargetProduct(target, newProducts), amount);
            return nextHandler != null ? nextHandler.handleRequest(target, productsToReturn, amount) : productsToReturn;
        }

        //Asegura que cuando se vayan a agregar mas productos a la lista, ninguno este repetido
        protected List<Product> addUniqueProducts(List<Product> productsToReturn, List<Product> recommendedProducts, Integer amount) {
            Set<Long> previousProductIds = productsToReturn.stream().map(Product::getId).collect(Collectors.toSet());

            recommendedProducts.stream()
                    .filter(product -> !previousProductIds.contains(product.getId()) && productsToReturn.size() < amount)
                    .forEach(productsToReturn::add);

            return productsToReturn;
        }

        //Remueve el producto base en caso de que se encuentre repetido dentro de la lista obtenida.
        protected List<Product> filterOutTargetProduct(Product targetProduct, List<Product> products) {
            return products.stream()
                    .filter(p -> !p.getId().equals(targetProduct.getId()))
                    .collect(Collectors.toList());
        }

        protected List<Product> findAllByKeywords(String keywords) {
            String[] keywordArray = keywords.split("[\\s,.-]+");

            return Arrays.stream(keywordArray)
                    .flatMap(keyword -> StreamSupport.stream(dao.findAllByNameContainingIgnoreCaseAndDeletedFalse(keyword).spliterator(), false))
                    .collect(Collectors.toList());
        }
    }

    private class CategoryRecommendationHandler extends AbstractRecommendationHandler {
        public CategoryRecommendationHandler(ProductDao dao) {
            super(dao);
        }

        @Override
        public List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount) {
            return handleRecommendationRequest(nextHandler, previousProducts, dao.findAllByCategoryAndDeletedFalse(targetProduct.getCategory()), targetProduct, amount);
        }

        @Override
        public void setNextHandler(AbstractRecommendationHandler nextHandler) { this.nextHandler = nextHandler; }
    }

    private class StoreRecommendationHandler extends AbstractRecommendationHandler {
        public StoreRecommendationHandler(ProductDao dao) {
            super(dao);
        }

        @Override
        public List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount) {
            return handleRecommendationRequest(nextHandler, previousProducts, dao.findAllByStoreAndDeletedFalse(targetProduct.getStore()), targetProduct, amount);
        }

        @Override
        public void setNextHandler(AbstractRecommendationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
    }

    private class BrandRecommendationHandler extends AbstractRecommendationHandler {
        public BrandRecommendationHandler(ProductDao dao) {
            super(dao);
        }

        @Override
        public List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount) {
            return handleRecommendationRequest(nextHandler, previousProducts, dao.findAllByBrandAndDeletedFalse(targetProduct.getBrand()), targetProduct, amount);
        }

        @Override
        public void setNextHandler(AbstractRecommendationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
    }


    private class PartialNameRecommendationHandler extends AbstractRecommendationHandler {
        public PartialNameRecommendationHandler(ProductDao dao) {
            super(dao);
        }

        @Override
        public List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount) {
            return handleRecommendationRequest(nextHandler, previousProducts, findAllByKeywords(targetProduct.getName()), targetProduct, amount);
        }

        @Override
        public void setNextHandler(AbstractRecommendationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }

    }

    private class DefaultHandler extends AbstractRecommendationHandler {
        public DefaultHandler(ProductDao dao) {
            super(dao);
        }

        @Override
        public List<Product> handleRequest(Product targetProduct, List<Product> previousProducts, Integer amount) {
            return handleRecommendationRequest(nextHandler, previousProducts, dao.findAll(), targetProduct, amount);
        }

        @Override
        public void setNextHandler(AbstractRecommendationHandler nextHandler) {
            this.nextHandler = nextHandler;
        }
    }
}
