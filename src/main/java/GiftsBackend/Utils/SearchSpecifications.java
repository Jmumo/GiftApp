package GiftsBackend.Utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchSpecifications <T> {

    public Specification<T> searchSpecification(String name) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (name != null && !name.isEmpty()) {
                    predicates.add(criteriaBuilder.equal(root.get("name"),name));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
