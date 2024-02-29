package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.domain.dto.WalletDTO;
import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;

import java.util.List;

public interface IWalletService {

	Wallet getOne(Long id);

	void saveAll(List<Wallet> wallets);

	List<WalletDTO> findAllByUser(final User user);

    Long getPointsWalletByUser(User user);

	void productToCartInWallet(User user, List<ProductToCart> productToCarts, WalletTransactionType type);

    WalletDTO addPoints(WalletDTO dto);

    WalletDTO removePoints(WalletDTO dto);

    Boolean walletValidate(User user, List<ProductToCart> products, WalletTransactionType sale);

    Long getActivePointsWalletByUser(User user);

    List<Wallet> findRegistersActiveByUser(User user);

    void getPointsFromCheckout (Checkout checkout);
}
