package br.sabadini;

import br.sabadini.configuration.DatabaseConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MultitenancyResource.API_TENANCY)
public class MultitenancyResource {
    public static final String API_TENANCY = "/api/v1.0/tenancies";


    @GetMapping
    public Boolean loadDatabase() {
        return Boolean.FALSE;
    }

    @GetMapping("/{name}")
    public Boolean loadDatabaseByName(@PathVariable String name) {
        DatabaseConfiguration.updateDatabaseByName(name);
        return Boolean.FALSE;
    }
}
