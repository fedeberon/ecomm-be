package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.converts.exceptions.DetailNotFoundException;
import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Detail;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("checkout")
public class CheckoutController {

    private ICheckoutService checkoutService;

    @Autowired
    public CheckoutController(final ICheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<Checkout> checkout(@RequestBody List<Detail> details) throws DetailNotFoundException {
        if (details.isEmpty()) {
            throw new DetailNotFoundException("Details should not be null or empty");
        }
        final Cart cart = new Cart.CartBuilder().withDetails(details).build();
        /*
        ACLARACION: username es null por el momento - no se si se vaya a utilizar este endpoint en el futuro
        asi que no hare modificaciones mayores
        */
        final Checkout checkout = checkoutService.save(cart, CheckoutState.IN_PROCESS, null);

        return ResponseEntity.ok(checkout);
    }

    @PostMapping("/budget")
    public ResponseEntity<Checkout> budget(@RequestBody List<Detail> details) throws DetailNotFoundException {
        if (details.isEmpty()) {
            throw new DetailNotFoundException("Details should not be null or empty");
        }
        final Cart cart = new Cart.CartBuilder().withDetails(details).build();
        /*
        ACLARACION: username es null por el momento - no se si se vaya a utilizar este endpoint en el futuro
        asi que no hare modificaciones mayores
        */
        final Checkout checkout = checkoutService.save(cart, CheckoutState.BUDGET, null);
        return ResponseEntity.ok(checkout);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Checkout> get(@PathVariable final Long id) {
        final Checkout checkout = checkoutService.get(id);

        return ResponseEntity.ok(checkout);
    }

    @GetMapping(value = "/search/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Checkout>> search(@PathVariable final Long id) {
        final List<Checkout> checkout = checkoutService.search(id);

        return ResponseEntity.ok(checkout);
    }


    @GetMapping("search")
    public ResponseEntity<Page<Checkout>> findAll(Pageable pageable) {
        final Sort sort = Sort.by("id").descending();
        final Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        final Page<Checkout> checkouts = checkoutService.findAll(sortedPageable);

        return ResponseEntity.ok().body(checkouts);
    }
}
