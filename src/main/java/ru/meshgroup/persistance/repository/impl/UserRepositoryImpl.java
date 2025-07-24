package ru.meshgroup.persistance.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.meshgroup.model.request.GetUserListRequest;
import ru.meshgroup.persistance.model.Email;
import ru.meshgroup.persistance.model.Email_;
import ru.meshgroup.persistance.model.Phone;
import ru.meshgroup.persistance.model.Phone_;
import ru.meshgroup.persistance.model.User;
import ru.meshgroup.persistance.model.User_;
import ru.meshgroup.persistance.repository.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Page<User> getUserList(GetUserListRequest request) {
        int pageSize = ObjectUtils.defaultIfNull(request.getSize(), 10);
        int pageNumber = ObjectUtils.defaultIfNull(request.getPage(), 0);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> user = userCriteriaQuery.from(User.class);

        Predicate[] predicates = buildPredicates(request, criteriaBuilder, user).toArray(new Predicate[0]);
        userCriteriaQuery.where(predicates);

        List<User> result = entityManager.createQuery(userCriteriaQuery)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

        return new PageImpl<>(result, PageRequest.of(pageNumber, pageSize), getTotalCount(request, criteriaBuilder));
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> user = userCriteriaQuery.from(User.class);

        Join<User, Phone> phoneJoin = user.join(User_.phoneList, JoinType.INNER);
        predicateList.add(criteriaBuilder.equal(phoneJoin.get(Phone_.phone), phone));

        userCriteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(userCriteriaQuery).getResultList().stream().findFirst();
    }

    private Long getTotalCount(GetUserListRequest request, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        List<Predicate> countPredicates = buildPredicates(request, criteriaBuilder, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot))
                .where(countPredicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(GetUserListRequest request, CriteriaBuilder criteriaBuilder, Root<User> user) {
        List<Predicate> predicateList = new ArrayList<>();

        if (StringUtils.isNotBlank(request.getName())) {
            predicateList.add(criteriaBuilder.like(
                    criteriaBuilder.upper(user.get(User_.name)), request.getName().toUpperCase() + "%"));
        }

        if (StringUtils.isNotBlank(request.getPhone())) {
            Join<User, Phone> phoneJoin = user.join(User_.phoneList, JoinType.INNER);
            predicateList.add(criteriaBuilder.equal(phoneJoin.get(Phone_.phone), request.getPhone()));
        }

        if (StringUtils.isNotBlank(request.getEmail())) {
            Join<User, Email> emailJoin = user.join(User_.emailList, JoinType.INNER);
            predicateList.add(criteriaBuilder.equal(emailJoin.get(Email_.email), request.getEmail()));
        }

        if (Objects.nonNull(request.getDateOfBirth())) {
            predicateList.add(criteriaBuilder.greaterThan(user.get(User_.dateOfBirth), request.getDateOfBirth()));
        }

        return predicateList;
    }

}
