package com.cevicheria.platform.specification;

import org.springframework.data.jpa.domain.Specification;

import com.cevicheria.platform.model.Customer;

public class CustomerSpecification {

	public static Specification<Customer> nameContains(String name) {
		return (root, query, cb) -> {
			if (name == null || name.isBlank()) {
				return cb.conjunction();
			}
			String trimName = name.trim();

			return cb.like(root.get("name"), "%" + trimName + "%");
		};
	}

	public static Specification<Customer> documentTypeEquals(String documentType) {
		return (root, query, cb) -> cb.equal(root.get("documentType"), documentType);
	}

	public static Specification<Customer> documentNumberEquals(
            String documentNumber) {
        return (root, query, cb) ->
            cb.equal(root.get("documentNumber"), documentNumber);
    }

    public static Specification<Customer> phoneEquals(String phone) {
        return (root, query, cb) ->
            cb.equal(root.get("phone"), phone);
    }

    public static Specification<Customer> emailEquals(String email) {
        return (root, query, cb) ->
            cb.equal(root.get("email"), email);
    }

    public static Specification<Customer> activeEquals(Boolean active) {
        return (root, query, cb) ->
            cb.equal(root.get("active"), active);
    }
}
