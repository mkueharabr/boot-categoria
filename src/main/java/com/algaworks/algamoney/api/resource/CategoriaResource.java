package com.algaworks.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	// injeta repositório
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	// lista todas as categorias
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}
	
	@PostMapping
	// insere no bd
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
	
	@GetMapping("/{codigo}")
	// o pathvariable é para indicar que o parâmetro refere-se ao código
	public Categoria buscaPeloCodigo(@PathVariable Long codigo) {
		return this.categoriaRepository.findById(codigo).orElse(null);
	}
}
