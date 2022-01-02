package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "WALLETS")
public class Wallet {
	
    @Id
    @Column(name = "WALL_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)	
	private Long id;
	
    @OneToOne
    @JoinColumn(name = "WALL_PRO_ID")
	private Product product;

    
    @Column(name = "WALL_PRO_QUANTITY")
	private Integer quantity;

	@Column(name = "points")
	private Long points;

    @OneToOne
    @JoinColumn(name = "WALL_USU_USERNAME")
	private User user;

	@Column(name = "date")
	private LocalDateTime date = LocalDateTime.now();

    public Wallet(final Product product, final User user, final Integer quantity, final Long points) {
        this.product = product;
        this.user = user;
        this.points = points;
        this.quantity = quantity;
    }

}
