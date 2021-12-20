package com.ideaas.ecomm.ecomm.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "WALLETS")
public class Wallet {
	
    @Id
    @Column(name = "WALL_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
    @OneToOne
    @JoinColumn(name = "WALL_PRO_ID")
	private Product product;

    @OneToOne
    @JoinColumn(name = "WALL_USU_USERNAME")
	private User user;

	@Column(name = "points")
	private Long points;
	
	@Column(name = "date")
	private LocalDateTime date;
	

}
