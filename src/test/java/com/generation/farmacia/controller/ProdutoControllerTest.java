package com.generation.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProdutoControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @BeforeAll
    void start() {
        categoriaRepository.deleteAll();
        produtoRepository.deleteAll();

        Categoria categoria = new Categoria();
        categoria.setNome("Medicamentos");
        categoria.setTipo("Categoria de Medicamentos");
        categoria.setPreco(BigDecimal.valueOf(0.0)); // Defina um preço padrão ou ajuste o construtor da Categoria
        categoria.setRequerPrescricao(false);
        categoriaRepository.save(categoria);

        Produto produto = new Produto(0L, "Aspirina", "Analgésico para alívio de dores", BigDecimal.valueOf(5.99), 100, "aspirina.jpg", categoria);
        produtoRepository.save(produto);
    }

    @Test
    @DisplayName("Cadastrar um produto")
    public void deveCriarUmProduto() {
        Categoria categoria = categoriaRepository.findById(1L).orElseThrow();

        HttpEntity<Produto> corpoRequisicao = new HttpEntity<>(
                new Produto(0L, "Paracetamol", "Analgésico para alívio de febre", BigDecimal.valueOf(7.99), 50, "paracetamol.jpg", categoria));

        ResponseEntity<Produto> corpoResposta = testRestTemplate
                .exchange("/produtos", HttpMethod.POST, corpoRequisicao, Produto.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Não deve repetir um produto")
    public void naoDeveRepetirProduto() {
        Categoria categoria = categoriaRepository.findById(1L).orElseThrow();

        Produto produtoExistente = new Produto(0L, "Ibuprofeno", "Anti-inflamatório", BigDecimal.valueOf(9.99), 80, "ibuprofeno.jpg", categoria);
        produtoRepository.save(produtoExistente);

        HttpEntity<Produto> corpoRequisicao = new HttpEntity<>(
                new Produto(0L, "Ibuprofeno", "Anti-inflamatório", BigDecimal.valueOf(9.99), 80, "ibuprofeno.jpg", categoria));

        ResponseEntity<Produto> corpoResposta = testRestTemplate
                .exchange("/produtos", HttpMethod.POST, corpoRequisicao, Produto.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar um produto")
    public void deveAtualizarUmProduto() {
        Categoria categoria = categoriaRepository.findById(1L).orElseThrow();

        Produto produtoCadastrado = produtoRepository.findAll().get(0);

        Produto produtoUpdate = new Produto(produtoCadastrado.getId(), "Dipirona Plus", "Analgésico para alívio de dores e febre", BigDecimal.valueOf(7.99), 100, "dipirona_plus.jpg", categoria);

        HttpEntity<Produto> corpoRequisicao = new HttpEntity<>(produtoUpdate);

        ResponseEntity<Produto> corpoResposta = testRestTemplate
                .exchange("/produtos", HttpMethod.PUT, corpoRequisicao, Produto.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Listar todos os produtos")
    public void deveMostrarTodosProdutos() {
        Categoria categoria = categoriaRepository.findById(1L).orElseThrow();

        Produto produto1 = new Produto(0L, "Dorflex", "Analgésico para dores musculares", BigDecimal.valueOf(10.99), 50, "dorflex.jpg", categoria);
        Produto produto2 = new Produto(0L, "Rinosoro", "Soro nasal para congestão", BigDecimal.valueOf(12.99), 30, "rinosoro.jpg", categoria);

        produtoRepository.saveAll(Arrays.asList(produto1, produto2));

        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/produtos", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}

