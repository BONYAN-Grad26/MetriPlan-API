package com.abdelaziz26.metriplate.utils.specifications;

import com.abdelaziz26.metriplate.entities.DietaryTag;
import com.abdelaziz26.metriplate.entities.Ingredient;
import com.abdelaziz26.metriplate.enums.DietaryTagType;
import jakarta.persistence.criteria.Join;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class IngredientSpecifications {
    public static Specification<@NotNull Ingredient> hasDietaryTag(Long tagId) {
        return (root, query, cb) -> {
            Join<Ingredient, DietaryTag> tags = root.join("dietaryTags");
            return cb.equal(tags.get("id"), tagId);
        };
    }

    public static Specification<@NotNull Ingredient> hasDietaryTagType(List<DietaryTagType> types) {
        return (root, query, cb) -> {
            Join<Ingredient, DietaryTag> tags = root.join("dietaryTags");
            return tags.get("type").in(types);
        };
    }
}
