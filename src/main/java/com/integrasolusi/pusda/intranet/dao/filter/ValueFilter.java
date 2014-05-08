package com.integrasolusi.pusda.intranet.dao.filter;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 8:30:46 PM
 */
public class ValueFilter implements Filter {
    private String property;
    private QueryOperator operator;
    private Object value;
    private String parameterName;

    public ValueFilter() {
    }

    public ValueFilter(String property, QueryOperator operator, Object value, String parameterName) {
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.parameterName = parameterName;
    }

    @Override
    public String getExpression() {
        if (QueryOperator.IS.equals(operator) && (value == null))
            return String.format("%s IS NULL", getProperty());
        else
            return String.format("(%1$s %2$s (:%3$s))", getProperty(), QueryOperator.getExpression(operator), getParameterName());
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public QueryOperator getOperator() {
        return operator;
    }

    public void setOperator(QueryOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
