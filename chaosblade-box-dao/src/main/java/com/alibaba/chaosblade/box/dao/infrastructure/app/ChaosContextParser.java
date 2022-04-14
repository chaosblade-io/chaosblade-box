package com.alibaba.chaosblade.box.dao.infrastructure.app;

import com.alibaba.chaosblade.box.common.app.sdk.ChaosAppContext;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.jexl3.*;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Parser for context expression. Expression MUST be surround with "#{}".
 *
 * $context is inner variable for access context object.
 *
 * Use like:
 * <code>
 *      // get phase
 *     #{$context.phase}
 *
 *     // get phase
 *     #{$context.phase}
 *
 *     // get data value
 *     getContext().addData("name", "Jack Ma");
 *     #{$context.data.name}
 * </code>
 *
 * @author sunju
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChaosContextParser {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final String CONTEXT_EXPRESSION_START = "#{";

    private static final String CONTEXT_EXPRESSION_END = "}";

    private static final String CONTEXT_VARIABLE = "$context";

    private static ChaosContextParser expressionParser;

    public static ChaosContextParser getInstance() {
        LOCK.lock();
        try {
            if (null == expressionParser) {
                expressionParser = new ChaosContextParser();
            }
            return expressionParser;
        } finally {
            if (LOCK.isLocked()&& LOCK.isHeldByCurrentThread()) {
                LOCK.unlock();
            }
        }
    }

    public boolean isExpression(Object value) {
        if (null == value || String.class != value.getClass()) {
            return false;
        }

        String string = String.valueOf(value);

        return !Strings.isNullOrEmpty(string)
            && string.startsWith(CONTEXT_EXPRESSION_START)
            && string.endsWith(CONTEXT_EXPRESSION_END);
    }

    public Object eval(ChaosAppContext appContext, Object value) {
        if (!isExpression(value)) {
            return value;
        }

        if (null == appContext) {
            return value;
        }

        String string = String.valueOf(value);

        int sIndex = string.indexOf('{');
        int eIndex = string.lastIndexOf('}');
        String expression = string.substring(sIndex + 1, eIndex);

        if (!Strings.isNullOrEmpty(expression)) {
            JexlContext context = new MapContext();
            context.set(CONTEXT_VARIABLE, appContext);

            JexlEngine engine = new JexlBuilder().create();

            JexlExpression jexlExpression = engine.createExpression(expression);
            return jexlExpression.evaluate(context);
        }

        return string;
    }

    //public static void main(String[] args) {
    //    ChaosAppContext appContext = new ChaosAppContext();
    //    appContext.setEnvironment(EnvironmentEnum.DAILY);
    //    appContext.setPhase(PhaseType.ATTACK);
    //
    //    appContext.addData("name", "jack");
    //    appContext.addData("text*1", "hello world!");
    //    appContext.addData("list_1", Lists.newArrayList("A", "B", "C"));
    //
    //    String expression = "#{$context.data.list_1[0]}";
    //    //String expression = "#{$context.environment}";
    //    //String expression = "$context.data.user.name";
    //
    //    System.out.println(ChaosContextParser.getInstance().eval(appContext, expression));
    //}

}
