package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;
import com.ideaas.ecomm.ecomm.repository.WalletDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WalletService implements IWalletService {

	private WalletDao dao;

	private IUserService userService;
	private IProductService productService;
	
	@Autowired
	public WalletService(final WalletDao dao, final IUserService userService, final IProductService productService) {
		this.dao = dao;
		this.userService = userService;
		this.productService = productService;
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
		return dao.findAllByUser(user);
	}

	public List<Wallet> findNoExpiredByUser(final User user) {
		LocalDateTime actives = LocalDateTime.now().minusMonths(3);
		return dao.findByUserAndAndDateAfter(user, actives);
	}

	public List<Wallet> findRegistersActiveByUser(final User user) {
		List<Wallet> registers = findNoExpiredByUser(user);

		List<Wallet> actives= new ArrayList<>();

		registers.forEach(register -> {
			if(register.getIsConsumed() == false && register.getPoints() >= 0L){
				actives.add(register);
			}
		});

		return actives;
	}

	/**
	 * Gets points of wallet by user
	 */
	public Long getPointsWalletByUser(final User user) {
		final List<Wallet> walletOfUser = this.findAllByUser(user);
		return walletOfUser.stream().mapToLong(Wallet::getPoints).sum();
	}

	public Long getActivePointsWalletByUser(final User user) {
		final List<Wallet> walletOfUser = this.findRegistersActiveByUser(user);
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
					.date(LocalDateTime.now())
					.isConsumed(false)
					.build();

			wallets.add(wallet);

		});

		registerConsumerInBuyWithPoints(user, productToCarts);
		this.saveAll(wallets);
	}

	private Long getPoint(final Product product) {
		if(product.getPromo()){
			return 0L;
		}
		else {
			Long points = Objects.isNull(product.getPoints()) || product.getPoints() == 0
				? Math.round(product.getPrice() * 5 / 100)
				: product.getPoints();

			return points;
		}
	}

	@Override
	public Wallet addPoints(final Wallet wallet) {
		return dao.save(wallet);
	}

	
	@Override
	public Wallet removePoints(Wallet wallet) {
		User user = wallet.getUser();
		Long pointsToRemove = wallet.getPoints();
		registersConsumer(user, pointsToRemove);

		wallet = Wallet.builder()
				.product(null)
				.user(user)
				.quantity(1)
				.points(0 - pointsToRemove)
				.date(LocalDateTime.now())
				.isConsumed(false)
				.build();
				

		return dao.save(wallet);
	}


	private Long pointsOfCart(final List<ProductToCart> productToCarts){
		Long pointsOfProducts = 0L;
		
		List<Long> pointList = new ArrayList<>();

		productToCarts.forEach(productToCart -> {
			Product product = productToCart.getProduct();
			long points = getPoint(product);
			pointList.add(points  * productToCart.getQuantity());
		});
		
		for(long i: pointList){
			pointsOfProducts += i;
		}

		return pointsOfProducts;
	}

	@Override
	public Boolean walletValidate(final User user,final List<ProductToCart> productToCarts,final WalletTransactionType type) {
		Long pointsOfUser = getActivePointsWalletByUser(user);
		Long pointsOfProducts = pointsOfCart(productToCarts);
		
		return pointsOfUser >= pointsOfProducts;

	}

	long auxPointsOfUser = 0l;
	public void registersConsumer(final User user, final long pointsToConsume){
		List<Wallet> registersOfUser = findRegistersActiveByUser(user);
		auxPointsOfUser = 0l;	
			registersOfUser.forEach(register -> {
				if(auxPointsOfUser < pointsToConsume){
					auxPointsOfUser += register.getPoints();
					register.setIsConsumed(true);
				}
			});
		
			if(auxPointsOfUser > pointsToConsume){
				Wallet wallet =  Wallet.builder()
				.product(null)
				.user(user)
				.quantity(1)
				.points(auxPointsOfUser - pointsToConsume)
				.date(LocalDateTime.now())
				.isConsumed(false)
				.build();
				
				addPoints(wallet);
			}
	}

	public void registerConsumerInBuyWithPoints(final User user, final List<ProductToCart> productToCarts){
		Long pointsOfProducts = pointsOfCart(productToCarts);
		registersConsumer(user, pointsOfProducts);
	}

	@Override
	public void getPointsFromCheckout(Checkout checkout){
		User user = userService.get(checkout.getUsername()).get();
		List<Wallet> wallets = checkout.getProducts().stream()
				.map(item -> Wallet.builder()
						.product(item.getProduct())
						.quantity(item.getQuantity())
						.points(item.getProduct().getPoints())
						.user(user)
						.date(LocalDateTime.of(checkout.getDate(), checkout.getTime()))
						.isConsumed(false)
						.build())
				.collect(Collectors.toList());
		wallets.forEach(dao::save);
	}
}