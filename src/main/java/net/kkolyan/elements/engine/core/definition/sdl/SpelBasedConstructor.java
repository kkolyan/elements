package net.kkolyan.elements.engine.core.definition.sdl;

import net.kkolyan.elements.engine.utils.ObjectProvider;
import net.kkolyan.elements.engine.utils.Pool;
import net.kkolyan.elements.engine.utils.SimplePool;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class SpelBasedConstructor implements Constructor {
    private static SpelExpressionParser parser = new SpelExpressionParser();
    private static Map<String,Expression> expressions = new HashMap<>();

    private static Pool<StandardEvaluationContext> evaluationContextPool = new SimplePool<>(new ObjectProvider<StandardEvaluationContext>() {
        @Override
        public StandardEvaluationContext getObject() {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.getPropertyAccessors().add(new ConstructionContextPropertyAccessor());
            return context;
        }
    });

    private Expression expression;
    private String expressionSource;

    public SpelBasedConstructor(String expressionSource) {
        this.expressionSource = expressionSource;
        expression = getExpression(expressionSource);
    }

    private static Expression getExpression(String source) {
        Expression expression = expressions.get(source);
        if (expression == null) {
            try {
                expression = parser.parseExpression(source);
            } catch (ParseException e) {
                throw new IllegalStateException("failed to parse expression '"+source+"'"+e, e);
            }
            expressions.put(source, expression);
        }
        return expression;
    }

    @Override
    public Object create(final ConstructionContext callerContext) {
        StandardEvaluationContext spelContext = evaluationContextPool.borrow();
        spelContext.setRootObject(callerContext);
        try {
            return expression.getValue(spelContext);
        } catch (SpelEvaluationException e) {
            if (e.getMessageCode() == SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE) {
                throw new IllegalStateException("can't resolve property "+e.getInserts()[0]+" of "+callerContext, e);
            }
            throw e;
        } finally {
            evaluationContextPool.release(spelContext);
        }
    }

    @Override
    public String toString() {
        return "SpelBasedConstructor{" +
                expressionSource + '\'' +
                '}';
    }
}
