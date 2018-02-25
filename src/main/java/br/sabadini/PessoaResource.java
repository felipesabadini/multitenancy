package br.sabadini;

import br.sabadini.configuration.TenantThreadLocalStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = PessoaResource.API_PESSSOAS)
public class PessoaResource {

    public static final String API_PESSSOAS = "/api/v1.0/pessoas";
    private final PessoaRepository repository;

    @Autowired
    public PessoaResource(PessoaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<PessoaEntity> salvar(PessoaEntity pessoa) {
        this.repository.saveAndFlush(pessoa);
        return ResponseEntity
                .created(URI.create(API_PESSSOAS.concat(pessoa.getId())))
                .body(pessoa);
    }

    @GetMapping
    public ResponseEntity<List> recuperarTodos() {
        TenantThreadLocalStorage.SCHEMA_ID.set("SABADINI");
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaEntity> recuperarPorId(@PathVariable String id) {

        PessoaEntity pessoa = this.repository.findOne(id);
        if(pessoa != null) {
            return ResponseEntity.ok(pessoa);
        }
        return ResponseEntity.notFound().build();
    }
}
