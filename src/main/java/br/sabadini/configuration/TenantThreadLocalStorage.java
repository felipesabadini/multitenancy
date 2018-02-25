package br.sabadini.configuration;

public class TenantThreadLocalStorage {

    public static final ThreadLocal<String> tenanctID = new ThreadLocal<>();
    public static final ThreadLocal<String> SCHEMA_ID = new ThreadLocal<>();

    protected TenantThreadLocalStorage() {}
}
