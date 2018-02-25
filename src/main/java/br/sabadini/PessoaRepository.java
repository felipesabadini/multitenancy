package br.sabadini;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by felipesabadinifacina on 09/02/18.
 */
public interface PessoaRepository extends JpaRepository<PessoaEntity, String> {
}
