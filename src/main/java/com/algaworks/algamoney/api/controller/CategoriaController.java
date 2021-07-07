package com.algaworks.algamoney.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;

@RestController

public class CategoriaController {
	
	// injeta repositório
	@Autowired
	private CategoriaRepository categoriaRepository;
	

	@RequestMapping(value="/categorias", method = RequestMethod.GET)
	// lista todas as categorias
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	// insere no bd
	@RequestMapping(value="/categorias", method = RequestMethod.POST)
	public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		// para informar o location (onde consegue recuperar o recurso)
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
					.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
		
		// retorna no body a resposta com o json da categoria criada
		// o created já retorna o código 201 created
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	

	@RequestMapping(value="/categorias/{codigo}", method = RequestMethod.GET)
	// o pathvariable é para indicar que o parâmetro refere-se ao código
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Categoria> categoria = this.categoriaRepository.findById(codigo);
		return categoria.isPresent() ? 
				ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();	
	}
	
	//update
	@RequestMapping(value="/categorias/{codigo}", method = RequestMethod.PUT)
	public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long codigo, @Valid @RequestBody Categoria newCategoria){
		Optional<Categoria> oldCategoria = categoriaRepository.findById(codigo);
		if(oldCategoria.isPresent()) {
			Categoria categoria = oldCategoria.get();
			categoria.setNome(newCategoria.getNome());
			categoriaRepository.save(categoria);
			return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	//delete
	@RequestMapping(value="/categorias/{codigo}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletarCategoria(@PathVariable Long codigo){
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		if(categoria.isPresent()) {
			categoriaRepository.delete(categoria.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
