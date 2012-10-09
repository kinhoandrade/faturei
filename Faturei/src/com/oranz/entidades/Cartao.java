package com.oranz.entidades;

public class Cartao {
	private int id;
	private String cartao;
	private String fechamento;
	
	public Cartao() {
		super();
	}
	public String getCartao() {
		return cartao;
	}
	public void setCartao(String cartao) {
		this.cartao = cartao;
	}
	public String getFechamento() {
		return fechamento;
	}
	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
