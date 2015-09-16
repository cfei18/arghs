package cfei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chrisfei on 9/15/15.
 */
public class Arghs {
    private Map<String, List<String>> argsMap;

    private static final String DASH = "-";
    private static final String PRIMITIVE_EXCEPTION_MESSAGE = "Value %s for argument %s undefined for primitive %s";

    public Arghs(String[] args) {
        init(args);
    }

    private void init(String[] args) {
        String currentArg = null;
        List<String> currentArgValue = null;

        for(String arg : args) {
            boolean isKey = isArgKey(arg);
            if(currentArg == null) {
                if(!isKey) {
                    //warn skipping first argument
                    continue;
                }

                currentArg = arg;
                currentArgValue = new ArrayList<>();
            } else {
                if(isKey) {
                    putArg(currentArg, currentArgValue);

                    currentArg = arg;
                    currentArgValue = new ArrayList<>();
                }

                currentArgValue.add(arg);
            }
        }

        if(currentArg != null) {
            putArg(currentArg, currentArgValue);
        }
    }

    private void putArg(String key, List<String> values) {
        if(this.argsMap == null) {
            this.argsMap = new HashMap<>();
        }

        String strippedKey = stripKey(key);

        this.argsMap.put(strippedKey, values);
    }

    private static boolean isArgKey(String str) {
        if(str == null) {
            throw new NullPointerException("Arguments should not be null");
        }

        return str.startsWith(DASH);
    }

    private static String stripKey(String key) {
        return key.replaceFirst("^-+(?!$)", "");
    }

    private static boolean isNullOrEmpty(List<String> values) {
        return values == null || values.isEmpty();
    }

    public List<String> getStrings(String arg) {
        return this.argsMap.get(arg);
    }

    public String getString(String arg) {
        List<String> values = getStrings(arg);
        if(isNullOrEmpty(values)) {
            return null;
        }

        if(values.size() > 1) {
            // warn returning first value of multi-value arg
        }

        return values.get(0);
    }

    private String getStringOrThrowForPrimitive(String arg, Class<?> clazz) {
        String value = getString(arg);
        if(value == null) {
            throw new UndefinedPrimitiveException(String.format(
                    PRIMITIVE_EXCEPTION_MESSAGE,
                    value,
                    arg,
                    clazz.getSimpleName()));
        }

        return value;
    }

    public int getInt(String arg) {
        String value = getStringOrThrowForPrimitive(arg, int.class);
        return Integer.parseInt(value);
    }

    public int getIntOrDefault(String arg, int defaultValue) {
        try {
            return getInt(arg);
        } catch(UndefinedPrimitiveException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String arg) {
        String value = getStringOrThrowForPrimitive(arg, boolean.class);
        return Boolean.parseBoolean(value);
    }

    public boolean getBooleanOrDefault(String arg, boolean defaultValue) {
        try {
            return getBoolean(arg);
        } catch(UndefinedPrimitiveException e) {
            return this.argsMap.containsKey(arg) || defaultValue;
        }
    }

    private static class UndefinedPrimitiveException extends RuntimeException {
        private UndefinedPrimitiveException(String message) {
            super(message);
        }
    }
}
