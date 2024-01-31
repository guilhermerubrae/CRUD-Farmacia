package com.generation.farmacia.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_categorias")
public class Categoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome não pode ficar vazio")
	@Size(min = 4, message = "Nome deve conter minímo de 4 letras")
	private String nome;

	@NotNull
	private String tipo;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@NotNull(message = "Preço é obrigatório!")
	@Positive(message = "O preço deve ser maior do que zero!")
	private BigDecimal preco;
	
	@Column(name = "validade")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validade;
	
	@AssertTrue
	@NotNull(message = "Prescrição médica é obrigatória!")
	private boolean requerPrescricao;
	
	@OneToMany(mappedBy ="categoria", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("categoria")
	private List<Produto>produto;
	
	public Categoria() {
	}

	public Categoria(Long id, String nome, String tipo, BigDecimal preco, LocalDate validade, boolean requerPrescricao,
			List<Produto> produto) {
		this.id = id;
		this.nome = nome;
		this.tipo = tipo;
		this.preco = preco;
		this.validade = validade;
		this.requerPrescricao = requerPrescricao;
		this.produto = produto;
	}

	// Construtor para criação rápida dos atributos importantes
	public Categoria(String nome, BigDecimal preco, boolean requerPrescricao) {
		this.nome = nome;
		this.preco = preco;
		this.requerPrescricao = requerPrescricao;
	}


	@Override
	public int hashCode() {
		return Objects.hash(descricao, id, nome, preco, produto, requerPrescricao, tipo, validade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return Objects.equals(descricao, other.descricao) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(preco, other.preco)
				&& Objects.equals(produto, other.produto) && requerPrescricao == other.requerPrescricao
				&& Objects.equals(tipo, other.tipo) && Objects.equals(validade, other.validade);
	}

	public List<Produto> getProduto() {
		return produto;
	}

	public void setProduto(List<Produto> produto) {
		this.produto = produto;
	}

	public boolean isRequerPrescricao() {
		return requerPrescricao;
	}

	public void setRequerPrescricao(boolean requerPrescricao) {
		this.requerPrescricao = requerPrescricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}