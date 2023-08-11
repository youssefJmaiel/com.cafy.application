package com.cafy.application.dao.specification;

import com.cafy.application.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Optional;
@AllArgsConstructor
@Repository
public class UserSpecification {

    public static Specification<User> perSearch(String name,String email,String contactNumber) {
        return  (root, cq, cb) -> {

            Predicate name1 = Optional.ofNullable(name).filter(org.apache.commons.lang3.StringUtils::isNotEmpty)
                    .map(x -> cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"))
                    .orElse(cb.conjunction());
            Predicate contactNumber1 = Optional.ofNullable(contactNumber).filter(org.apache.commons.lang3.StringUtils::isNotEmpty)
                    .map(x -> cb.like(cb.upper(root.get("contactNumber")), "%" + contactNumber.toUpperCase() + "%"))
                    .orElse(cb.conjunction());

            Predicate email1 = Optional.ofNullable(email).filter(org.apache.commons.lang3.StringUtils::isNotEmpty)
                    .map(x -> cb.like(cb.upper(root.get("email")), "%" + email.toUpperCase() + "%"))
                    .orElse(cb.conjunction());


            cq.orderBy(cb.desc(root.get("name")), cb.asc(root.get("id")));
            return cb.and(name1,email1,contactNumber1);
        };
    }
}
