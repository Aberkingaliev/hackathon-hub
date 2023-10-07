package com.hackathonhub.serviceidentity.services.token;

import com.hackathonhub.serviceidentity.services.token.validators.Validator;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Token validator class
 * This class is responsible for running the validation logic
 * It will run all the validators in the order of priority
 */
@NoArgsConstructor
@Component
public class TokenValidationManager {
    private TokenValidationContext context;
    private List<Validator> validators = new ArrayList<>();

    /**
     * Initialize context
     * @param context - @see {@link TokenValidationContext}
     * @return - this
     */
    public TokenValidationManager init(final TokenValidationContext context) {
        if(context == null || context.isEmpty()) {
            throw new IllegalArgumentException("Context is empty");
        }

        this.context = context;
        return this;
    }

    /**
     * Add validator to the list of validators
     * @param validator - each validator should be implementing @see {@link Validator}
     * @return - this
     */
    public TokenValidationManager addValidator(final Validator validator) {
        this.validators.add(validator);
        return this;
    }

    /**
     * Runner method
     * If any of the validators fail, runner will stop and return the context
     * @return - returned changed context @see {@link TokenValidationContext}
     */
    public TokenValidationContext execute() {
        this.sortValidators();
        for(final Validator validator : validators) {
            validator.doValidation(this.context);
            if(this.context.getIsValid() != null && !this.context.getIsValid()) {
                break;
            }
        }

        return this.context;
    }


    /**
     * [Private method] Sort validators before: @see {@link TokenValidationManager#execute()}
     */
    private void sortValidators() {
        this.validators = this.validators
                .stream()
                .sorted(Comparator.comparingInt(Validator::getPriority))
                .collect(Collectors.toList());
    }
}
