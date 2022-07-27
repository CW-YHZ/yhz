package com.cw.yhz.ExcelDemo.utils;

import com.cw.yhz.ExcelDemo.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Validator 校验框架工具
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {

    private static final Validator VALID = SpringUtils.getBean(Validator.class);

    /**
     * validate主动校验方式
     *
     * @param validateObject 进行校验的对象
     * @param <T>            传递的校验类型
     * @throws ServiceException 服务异常
     */
    public static <T> void validate(@Valid T validateObject, Class<?>... groups) throws ServiceException {
        Set<ConstraintViolation<@Valid T>> validateSet = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(validateObject, groups);
        if (CollectionUtils.isNotEmpty(validateSet)) {
            String messages = validateSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + ";" + m2)
                    .orElse("parameter error！" + validateObject);
            throw new RuntimeException(messages);
        }
    }

}
