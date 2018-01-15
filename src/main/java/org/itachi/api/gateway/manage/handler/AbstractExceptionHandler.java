package org.itachi.api.gateway.manage.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.el.ExpressionFactoryImpl;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenCollector;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.TokenIterator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.itachi.api.gateway.manage.util.LocaleUtil;
import org.itachi.api.gateway.manage.util.ThrowableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.*;

/**
 * Created by itachi on 2017/8/13.
 * User: itachi
 * Date: 2017/8/13
 * Time: 10:10
 *
 * @author itachi
 */
public abstract class AbstractExceptionHandler {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionHandler.class);

    protected static String CODE = "errorCode";
    protected static String MESSAGE = "errorMessage";
    private static final String ERRORS = "errors";
    private static final String FIELD = "field";
    private static final String PARAMETER_ERROR = "参数不合法！";

    @Autowired
    private ResourceBundleLocator resourceBundleLocator;

    @Autowired
    private Validator validator;

    private HttpServletRequest request;

    protected ObjectMapper mapper = new ObjectMapper();

    public AbstractExceptionHandler() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected HttpStatus handleThrowable(HttpServletRequest request, Throwable throwable, StringBuilder builder) {
        Map<String, Object> result = new HashMap<>(16);
        result.put(CODE, 3002);
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        try {
            this.request = request;
            status = handleThrowable(result, throwable);
            builder.append(mapper.writeValueAsString(result));
        } catch (Exception e) {
            LOGGER.error("=====ExceptionMapperProvider exception: ", e);
            builder.append(String.format("{\"%s\":%d, \"%s\":\"%s\"}", CODE, 3002, MESSAGE, "exception mapper provider have exception!"));
        }
        return status;
    }

    private HttpStatus handleThrowable(Map<String, Object> result, Throwable throwable) {
        if (throwable instanceof ConstraintViolationException) {
            return handleValidException(result, (ConstraintViolationException) throwable);
        }
        if (throwable instanceof MethodArgumentNotValidException) {
            return handleValidException(result, (MethodArgumentNotValidException) throwable);
        }
        if (throwable instanceof MissingServletRequestParameterException) {
            return handleValidException(result, (MissingServletRequestParameterException) throwable);
        }
        if (throwable instanceof MissingPathVariableException) {
            return handleValidException(result, (MissingPathVariableException) throwable);
        }
        if (throwable instanceof BindException) {
            return handleValidException(result, (BindException) throwable);
        }
        if (throwable != null && throwable.getCause() != null && throwable.getCause() != throwable) {
            return ThrowableUtil.getInstance().setCode(CODE).setMessage(MESSAGE).handleException(result, throwable.getCause());
        }
        return ThrowableUtil.getInstance().setCode(CODE).setMessage(MESSAGE).handleException(result, throwable);
    }

    private HttpStatus handleValidException(Map<String, Object> result, BindException throwable) {
        Set<ConstraintViolation<Object>> violations = validator.validate(throwable.getTarget());
        if (violations != null && !violations.isEmpty()) {
            return handleValidException(result, new ConstraintViolationException(violations));
        }

        result.put(CODE, 104);
        result.put(MESSAGE, PARAMETER_ERROR);
        if (throwable.getAllErrors() != null && !throwable.getAllErrors().isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>();
            throwable.getAllErrors().forEach(objectError -> {
                Map<String, Object> map = new HashMap<>(16);
                map.put(FIELD, throwable.getTarget());
                map.put(MESSAGE, objectError.getDefaultMessage());
                list.add(map);
            });
            result.put(ERRORS, list);
        }
        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus handleValidException(Map<String, Object> result, ConstraintViolationException exception) {
        result.put(CODE, 104);
        result.put(MESSAGE, PARAMETER_ERROR);
        Set<ConstraintViolation<?>> messages = exception.getConstraintViolations();
        if (messages != null && !messages.isEmpty()) {
            Locale locale = LocaleUtil.getLocale(request);
            List<Map<String, String>> list = new ArrayList<>();
            for (ConstraintViolation<?> message : messages) {
                Map<String, String> map = new HashMap<>(16);
                String field = StringUtils.substringAfterLast(message.getPropertyPath().toString(), ".");
                field = (field == null || field.isEmpty()) ? message.getPropertyPath().toString() : field;
                map.put(FIELD, field);
                if (message.getMessageTemplate() != null && message.getMessageTemplate().length() > 0) {
                    MessageInterpolatorContext context = new MessageInterpolatorContext(message.getConstraintDescriptor(), message.getInvalidValue(), message.getRootBeanClass(), new HashMap<>(0));
                    map.put(MESSAGE, interpolate(message.getMessageTemplate(), context, locale));
                } else {
                    map.put(MESSAGE, message.getMessage());
                }
                list.add(map);
            }
            result.put(ERRORS, list);
        }

        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus handleValidException(Map<String, Object> result, MissingPathVariableException exception) {
        result.put(CODE, 104);
        result.put(MESSAGE, PARAMETER_ERROR);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(16);
        map.put(FIELD, exception.getVariableName());
        map.put(MESSAGE, exception.getMessage());
        list.add(map);
        result.put(ERRORS, list);
        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus handleValidException(Map<String, Object> result, MissingServletRequestParameterException exception) {
        result.put(CODE, 104);
        result.put(MESSAGE, PARAMETER_ERROR);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(16);
        map.put(FIELD, exception.getParameterName());
        map.put(MESSAGE, exception.getMessage());
        list.add(map);
        result.put(ERRORS, list);
        return HttpStatus.BAD_REQUEST;
    }

    private HttpStatus handleValidException(Map<String, Object> result, MethodArgumentNotValidException exception) {
        Set<ConstraintViolation<Object>> violations = validator.validate(exception.getBindingResult().getTarget());
        if (violations != null && !violations.isEmpty()) {
            return handleValidException(result, new ConstraintViolationException(violations));
        }

        result.put(CODE, 104);
        result.put(MESSAGE, PARAMETER_ERROR);
        if (exception.getBindingResult() != null && exception.getBindingResult().getAllErrors() != null
                && !exception.getBindingResult().getAllErrors().isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (ObjectError error : exception.getBindingResult().getAllErrors()) {
                Map<String, Object> map = new HashMap<>(16);
                map.put(FIELD, exception.getBindingResult().getTarget());
                map.put(MESSAGE, error.getDefaultMessage());
                list.add(map);
            }
            result.put(ERRORS, list);
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String interpolate(String message, MessageInterpolator.Context context, Locale locale) {
        String interpolatedMessage = message;
        try {
            interpolatedMessage = interpolateMessage(message, context, locale);
        } catch (ValidationException e) {
            LOGGER.warn(e.getMessage());
        }
        return interpolatedMessage;
    }

    /**
     * Runs the message interpolation according to algorithm specified in the Bean Validation specification.
     * <br/>
     * Note:
     * <br/>
     * Look-ups in user bundles is recursive whereas look-ups in default bundle are not!
     *
     * @param message the message to interpolate
     * @param context the context for this interpolation
     * @param locale  the {@code Locale} to use for the resource bundle.
     * @return the interpolated message.
     */
    private String interpolateMessage(String message, MessageInterpolator.Context context, Locale locale)
            throws MessageDescriptorFormatException {
        String resolvedMessage = null;

        // if the message is not already in the cache we have to run step 1-3 of the message resolution
        if (resolvedMessage == null) {
            ResourceBundle userResourceBundle = resourceBundleLocator
                    .getResourceBundle(locale);
            ResourceBundle defaultResourceBundle = resourceBundleLocator
                    .getResourceBundle(locale);

            String userBundleResolvedMessage;
            resolvedMessage = message;
            boolean evaluatedDefaultBundleOnce = false;
            do {
                // search the user bundle recursive (step1)
                userBundleResolvedMessage = interpolateBundleMessage(
                        resolvedMessage, userResourceBundle, locale, true
                );

                // exit condition - we have at least tried to validate against the default bundle and there was no
                // further replacements
                if (evaluatedDefaultBundleOnce
                        && !hasReplacementTakenPlace(userBundleResolvedMessage, resolvedMessage)) {
                    break;
                }

                // search the default bundle non recursive (step2)
                resolvedMessage = interpolateBundleMessage(
                        userBundleResolvedMessage,
                        defaultResourceBundle,
                        locale,
                        false
                );
                evaluatedDefaultBundleOnce = true;
            } while (true);
        }

        // resolve parameter expressions (step 4)
        List<Token> tokens = null;
        if (tokens == null) {
            TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.PARAMETER);
            tokens = tokenCollector.getTokenList();
        }
        resolvedMessage = interpolateExpression(
                new TokenIterator(tokens),
                context,
                locale
        );

        // resolve EL expressions (step 5)
        tokens = null;
        if (tokens == null) {
            TokenCollector tokenCollector = new TokenCollector(resolvedMessage, InterpolationTermType.EL);
            tokens = tokenCollector.getTokenList();
        }
        resolvedMessage = interpolateExpression(
                new TokenIterator(tokens),
                context,
                locale
        );

        // last but not least we have to take care of escaped literals
        resolvedMessage = replaceEscapedLiterals(resolvedMessage);

        return resolvedMessage;
    }

    private String replaceEscapedLiterals(String resolvedMessage) {
        resolvedMessage = resolvedMessage.replace("\\{", "{");
        resolvedMessage = resolvedMessage.replace("\\}", "}");
        resolvedMessage = resolvedMessage.replace("\\\\", "\\");
        resolvedMessage = resolvedMessage.replace("\\$", "$");
        return resolvedMessage;
    }

    private boolean hasReplacementTakenPlace(String origMessage, String newMessage) {
        return !origMessage.equals(newMessage);
    }

    private String interpolateBundleMessage(String message, ResourceBundle bundle, Locale locale, boolean recursive)
            throws MessageDescriptorFormatException {
        TokenCollector tokenCollector = new TokenCollector(message, InterpolationTermType.PARAMETER);
        TokenIterator tokenIterator = new TokenIterator(tokenCollector.getTokenList());
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();
            String resolvedParameterValue = resolveParameter(
                    term, bundle, locale, recursive
            );
            tokenIterator.replaceCurrentInterpolationTerm(resolvedParameterValue);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    private String interpolateExpression(TokenIterator tokenIterator, MessageInterpolator.Context context, Locale locale)
            throws MessageDescriptorFormatException {
        while (tokenIterator.hasMoreInterpolationTerms()) {
            String term = tokenIterator.nextInterpolationTerm();

            InterpolationTerm expression = new InterpolationTerm(term, locale, new ExpressionFactoryImpl());
            String resolvedExpression = expression.interpolate(context);
            tokenIterator.replaceCurrentInterpolationTerm(resolvedExpression);
        }
        return tokenIterator.getInterpolatedMessage();
    }

    private String resolveParameter(String parameterName, ResourceBundle bundle, Locale locale, boolean recursive)
            throws MessageDescriptorFormatException {
        String parameterValue;
        try {
            if (bundle != null) {
                parameterValue = bundle.getString(removeCurlyBraces(parameterName));
                if (recursive) {
                    parameterValue = interpolateBundleMessage(parameterValue, bundle, locale, recursive);
                }
            } else {
                parameterValue = parameterName;
            }
        } catch (MissingResourceException e) {
            // return parameter itself
            parameterValue = parameterName;
        }
        return parameterValue;
    }

    private String removeCurlyBraces(String parameter) {
        return parameter.substring(1, parameter.length() - 1);
    }
}
