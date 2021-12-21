package com.ideaas.ecomm.ecomm.services.interfaces;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Wallet;

public interface IWalletService {

	Wallet getOne(Long id);

	void saveAll(List<Wallet> wallets);
	
}
