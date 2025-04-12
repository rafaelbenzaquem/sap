package br.jus.trf1.sipe.comum.validadores;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class UnicoValidador implements ConstraintValidator<Unico, Object> {

    private Class<?> domainClass;
    private String fieldName;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void initialize(Unico constraint) {
        this.domainClass = constraint.domainClass();
        this.fieldName = constraint.fieldName();
    }

    public boolean isValid(Object valor, ConstraintValidatorContext context) {
        Query q = em.createQuery("select d from " + domainClass.getSimpleName() +
                " d where " + fieldName + "=:valor");
        q.setParameter("valor", valor);
        List<?> list = q.getResultList();
        return list.isEmpty();
    }
}
