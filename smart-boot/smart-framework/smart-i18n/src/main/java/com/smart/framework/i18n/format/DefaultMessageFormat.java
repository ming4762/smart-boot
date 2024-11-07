package com.smart.framework.i18n.format;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认的DefaultMessage
 * @author shizhongming
 * 2021/2/1 11:54 下午
 */
@Slf4j
public class DefaultMessageFormat implements MessageFormat {

    private static final Pattern PATTERN = Pattern.compile("\\{.*?}");

    private Set<String> matchValues(String formValue) {
        final Matcher matcher = PATTERN.matcher(formValue);
        final Set<String> matchValues = new HashSet<>();
        while (matcher.find()) {
            matchValues.add(matcher.group());
        }
        return matchValues;
    }


    @Override
    public String format(String formValue, Map<String, Object> args) {
        if (Objects.isNull(args)) {
            return formValue;
        }
        final Set<String> matchValues = this.matchValues(formValue);
        final String[] result = {formValue};
        matchValues.forEach(matchValue -> {
            String key = matchValue.replace("{", "").replace("}", "");
            if (args.containsKey(key)) {
                result[0] = result[0].replace(matchValue, args.get(key).toString());
            }
        });
        return result[0];
    }

    @Override
    public String format(String formValue, Object[] args) {
        if (Objects.isNull(args)) {
            return formValue;
        }
        final Set<String> matchValues = this.matchValues(formValue);
        final String[] result = {formValue};
        matchValues.forEach(matchValue -> {
            String keyValue = matchValue.replace("{", "").replace("}", "");
            final int key = Integer.parseInt(keyValue);
            if (args.length > key) {
                result[0] = result[0].replace(matchValue, args[key].toString());
            }
        });
        return result[0];
    }
}
