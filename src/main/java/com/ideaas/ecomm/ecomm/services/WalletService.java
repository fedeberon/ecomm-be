package com.ideaas.ecomm.ecomm.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.repository.WalletDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;

@Service
public class WalletService implements IWalletService {

	private WalletDao dao;
	
	@Autowired
	public WalletService(WalletDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Wallet getOne(Long id) {
		return dao.getById(id);
	}
	
	
	public Wallet save(Wallet wallet) {
		return dao.save(wallet);
	}
	
	public void saveAll(List<Wallet> wallets) {
		dao.saveAll(wallets);
	}

	public List<Wallet> findAllByUser(final User user) {
		return dao.findByUser(user);
	}

	/**
	 * Gets points of wallet by user
	 */
	public Long getPointsWalletByUser(final User user) {
		final List<Wallet> walletOfUser = this.findAllByUser(user);
		return walletOfUser.stream().mapToLong(Wallet::getPoints).sum();
	}


	@Override
	public void productToCartInWallet(final User user,
									  final List<ProductToCart> productToCarts,
									  final WalletTransactionType type) {
		final List<Wallet> wallets = new ArrayList<>();
		productToCarts.forEach(productToCart -> {
			final Product product = productToCart.getProduct();
			Long points = null;

			switch (type) {
				case BUY:
					points = Math.round(product.getPrice() * productToCart.getQuantity());
					break;
				case SALE:
					points = getPoint(productToCart.getProduct());
				default:
					break;
			}

			final Wallet wallet =  Wallet.builder()
					.product(product)
					.user(user)
					.quantity(productToCart.getQuantity())
					.points(points * type.getValue())
					.build();

			wallets.add(wallet);

		});

		this.saveAll(wallets);
	}

	private Long getPoint(final Product product) {
		System.out.print("Tiene promocion" + product.getPromo());
		if(product.getPromo()){
			System.out.print("entro al if");
			return 0L;
		}
		else {
			System.out.print("entro al else");
			Long points = Objects.isNull(product.getPoints()) || product.getPoints() == 0
				? Math.round(product.getPrice() * 5 / 100)
				: product.getPoints();

			return points;
		}

	}
}
