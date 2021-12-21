package com.ideaas.ecomm.ecomm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
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
	
	
	
	
}
