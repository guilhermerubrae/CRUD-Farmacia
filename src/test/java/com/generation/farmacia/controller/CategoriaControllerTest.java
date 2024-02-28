package com.generation.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;


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
public class CategoriaControllerTest {

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
        categoria.setPreco(BigDecimal.valueOf(2.50)); 
        categoria.setRequerPrescricao(false);
        categoriaRepository.save(categoria);

        Produto produto = new Produto(0L, "Aspirina", "Analgésico para alívio de dores", BigDecimal.valueOf(5.99), 100, "aspirina.jpg", categoria);
        produtoRepository.save(produto);
    }

    @Test
    @DisplayName("Cadastrar uma categoria")
    public void deveCriarUmaCategoria() {
        HttpEntity<Categoria> corpoRequisicao = new HttpEntity<>(new Categoria(1L, "Antibióticos", "Categoria de Antibióticos", BigDecimal.valueOf(55.0), null, false, null));

        ResponseEntity<Categoria> corpoResposta = testRestTemplate
                .exchange("/categorias", HttpMethod.POST, corpoRequisicao, Categoria.class);

        assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Não deve repetir uma categoria")
    public void naoDeveRepetirCategoria() {
        Categoria categoriaExistente = new Categoria(0L, "Antibióticos", "Categoria de Antibióticos", BigDecimal.valueOf(110.0), null, false, null);
        categoriaRepository.save(categoriaExistente);

        HttpEntity<Categoria> corpoRequisicao = new HttpEntity<>(new Categoria(0L, "Antibióticos", "Categoria de Antibióticos", BigDecimal.valueOf(110.0), null, false, null));

        ResponseEntity<Categoria> corpoResposta = testRestTemplate
                .exchange("/categorias", HttpMethod.POST, corpoRequisicao, Categoria.class);

        assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar uma categoria")
    public void deveAtualizarUmaCategoria() {
        Categoria categoriaCadastrada = categoriaRepository.findAll().get(0);

        Categoria categoriaUpdate = new Categoria(categoriaCadastrada.getId(), "Analgésicos", "Categoria de Analgésicos", BigDecimal.valueOf(0.0), null, false, null);

        HttpEntity<Categoria> corpoRequisicao = new HttpEntity<>(categoriaUpdate);

        ResponseEntity<Categoria> corpoResposta = testRestTemplate
                .exchange("/categorias", HttpMethod.PUT, corpoRequisicao, Categoria.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }

    @Test
    @DisplayName("Listar todas as categorias")
    public void deveMostrarTodasCategorias() {
        ResponseEntity<String> resposta = testRestTemplate
                .exchange("/categorias", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
    
}

