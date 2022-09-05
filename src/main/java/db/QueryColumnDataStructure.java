package db;

import org.jetbrains.annotations.Nullable;

import java.sql.JDBCType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record QueryColumnDataStructure(String fieldName, @Nullable JDBCType fieldType, Optional<List<String>> options) {
    private static final String SPACE = " ";
    @Override
    public String toString() {
        return fieldName + SPACE
                + fieldType + SPACE
                + options.stream()
                        .map(x -> String.join(SPACE, x))
                        .collect(Collectors.joining(SPACE));
    }
}