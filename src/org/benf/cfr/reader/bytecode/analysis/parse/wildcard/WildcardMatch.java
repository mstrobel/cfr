package org.benf.cfr.reader.bytecode.analysis.parse.wildcard;

import org.benf.cfr.reader.bytecode.analysis.parse.Expression;
import org.benf.cfr.reader.bytecode.analysis.parse.LValue;
import org.benf.cfr.reader.bytecode.analysis.parse.StatementContainer;
import org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractNewArray;
import org.benf.cfr.reader.bytecode.analysis.parse.expression.MemberFunctionInvokation;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.*;
import org.benf.cfr.reader.bytecode.analysis.types.discovery.InferredJavaType;
import org.benf.cfr.reader.entities.GenericInfoSource;
import org.benf.cfr.reader.util.ListFactory;
import org.benf.cfr.reader.util.MapFactory;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lee
 * Date: 25/07/2012
 * Time: 17:22
 */
public class WildcardMatch {

    /*
     * This could probably be done with one map....
     */
    private Map<String, LValueWildcard> lValueMap = MapFactory.newMap();
    private Map<String, ExpressionWildcard> expressionMap = MapFactory.newMap();
    private Map<String, NewArrayWildcard> newArrayWildcardMap = MapFactory.newMap();
    private Map<String, MemberFunctionInvokationWildcard> memberFunctionMap = MapFactory.newMap();
    private Map<String, BlockIdentifierWildcard> blockIdentifierWildcardMap = MapFactory.newMap();
    private Map<String, ListWildcard> listMap = MapFactory.newMap();

    private <T> void reset(Collection<? extends Wildcard<T>> coll) {
        for (Wildcard<T> item : coll) {
            item.resetMatch();
        }
    }

    public void reset() {
        reset(lValueMap.values());
        reset(expressionMap.values());
        reset(newArrayWildcardMap.values());
        reset(memberFunctionMap.values());
        reset(blockIdentifierWildcardMap.values());
        reset(listMap.values());
    }

    public LValueWildcard getLValueWildCard(String name) {
        LValueWildcard res = lValueMap.get(name);
        if (res != null) return res;

        res = new LValueWildcard(name);
        lValueMap.put(name, res);
        return res;
    }

    public ExpressionWildcard getExpressionWildCard(String name) {
        ExpressionWildcard res = expressionMap.get(name);
        if (res != null) return res;

        res = new ExpressionWildcard(name);
        expressionMap.put(name, res);
        return res;
    }


    public NewArrayWildcard getNewArrayWildCard(String name) {
        return getNewArrayWildCard(name, 1);
    }

    public NewArrayWildcard getNewArrayWildCard(String name, int numDims) {
        NewArrayWildcard res = newArrayWildcardMap.get(name);
        if (res != null) return res;

        res = new NewArrayWildcard(name, numDims);
        newArrayWildcardMap.put(name, res);
        return res;

    }

    public MemberFunctionInvokationWildcard getMemberFunction(String name, String methodname, Expression object) {
        return getMemberFunction(name, methodname, object, ListFactory.<Expression>newList());
    }

    public MemberFunctionInvokationWildcard getMemberFunction(String name, String methodname, Expression object, Expression... args) {
        return getMemberFunction(name, methodname, object, ListFactory.<Expression>newList(args));
    }

    /* When matching a function invokation, we don't really have all the details to construct a plausible
     * MemberFunctionInvokation expression, so just construct something which will match it!
     */
    public MemberFunctionInvokationWildcard getMemberFunction(String name, String methodname, Expression object, List<Expression> args) {
        MemberFunctionInvokationWildcard res = memberFunctionMap.get(name);
        if (res != null) return res;

        res = new MemberFunctionInvokationWildcard(methodname, object, args);
        memberFunctionMap.put(name, res);
        return res;
    }

    public BlockIdentifierWildcard getBlockIdentifier(String name) {
        BlockIdentifierWildcard res = blockIdentifierWildcardMap.get(name);
        if (res != null) return res;

        res = new BlockIdentifierWildcard();
        blockIdentifierWildcardMap.put(name, res);
        return res;
    }

    public <T> ListWildcard getList(String name) {
        ListWildcard res = listMap.get(name);
        if (res != null) return res;

        res = new ListWildcard();
        listMap.put(name, res);
        return res;
    }

    public boolean match(Object pattern, Object test) {
        return pattern.equals(test);
    }

    public class LValueWildcard implements LValue, Wildcard<LValue> {
        private final String name;
        private transient LValue matchedValue;

        private LValueWildcard(String name) {
            this.name = name;
        }

        @Override
        public int getNumberOfCreators() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void determineLValueEquivalence(Expression assignedTo, StatementContainer statementContainer, LValueAssignmentCollector lValueAssigmentCollector) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SSAIdentifiers collectVariableMutation(SSAIdentifierFactory ssaIdentifierFactory) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LValue replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LValue applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
            return this;
        }

        @Override
        public InferredJavaType getInferredJavaType() {
            return InferredJavaType.IGNORE;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof LValue)) {
                return false;
            }
            if (matchedValue == null) {
                matchedValue = (LValue) o;
                return true;
            }
            return matchedValue.equals(o);
        }

        @Override
        public LValue getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }
    }

    private abstract class AbstractBaseExpressionWildcard implements Expression {

        @Override
        public Expression replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Expression applyExpressionRewriter(ExpressionRewriter expressionRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer, ExpressionRewriterFlags flags) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isSimple() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void collectUsedLValues(LValueUsageCollector lValueUsageCollector) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean canPushDownInto() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Expression pushDown(Expression toPush, Expression parent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public InferredJavaType getInferredJavaType() {
            return InferredJavaType.IGNORE;
        }


        @Override
        public void findGenericTypeInfo(GenericInfoSource genericInfoSource) {
        }

        @Override
        public String toStringWithOuterPrecedence(int outerPrecedence) {
            return toString();
        }
    }

    public class ExpressionWildcard extends AbstractBaseExpressionWildcard implements Wildcard<Expression> {
        private final String name;
        private transient Expression matchedValue;

        public ExpressionWildcard(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Expression)) {
                return false;
            }
            if (matchedValue == null) {
                matchedValue = (Expression) o;
                return true;
            }
            return matchedValue.equals(o);
        }

        @Override
        public Expression getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }

    }

    public class NewArrayWildcard extends AbstractBaseExpressionWildcard implements Wildcard<AbstractNewArray> {
        private final String name;
        private final int numDims;
        private transient AbstractNewArray matchedValue;

        public NewArrayWildcard(String name, int numDims) {
            this.name = name;
            this.numDims = numDims;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AbstractNewArray)) {
                return false;
            }
            if (matchedValue == null) {
                AbstractNewArray abstractNewArray = (AbstractNewArray) o;
                if (numDims != abstractNewArray.getNumDims()) return false;
                matchedValue = abstractNewArray;
                return true;
            }
            return matchedValue.equals(o);
        }

        @Override
        public AbstractNewArray getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }

    }

    public class MemberFunctionInvokationWildcard extends AbstractBaseExpressionWildcard implements Wildcard<Expression> {
        private final String name;
        private final Expression object;
        private final List<Expression> args;
        private transient Expression matchedValue;

        public MemberFunctionInvokationWildcard(String name, Expression object, List<Expression> args) {
            this.name = name;
            this.object = object;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MemberFunctionInvokation)) return false;
            if (matchedValue != null) return matchedValue.equals(o);

            /*
             * See if this is a compatible member function.
             *
             * TODO : since it might fail, we need to rewind any captures!
             */
            MemberFunctionInvokation other = (MemberFunctionInvokation) o;
            if (!name.equals(other.getName())) return false;
            if (!object.equals(other.getObject())) return false;
            List<Expression> otherArgs = other.getArgs();
            if (args.size() != otherArgs.size()) return false;
            for (int x = 0; x < args.size(); ++x) {
                Expression myArg = args.get(x);
                Expression hisArg = otherArgs.get(x);
                if (!myArg.equals(hisArg)) return false;
            }
            matchedValue = (Expression) o;
            return true;
        }

        @Override
        public Expression getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }

    }

    public class BlockIdentifierWildcard extends BlockIdentifier implements Wildcard<BlockIdentifier> {
        private BlockIdentifier matchedValue;

        public BlockIdentifierWildcard() {
            super(0, null);
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null) return false;

            if (matchedValue != null) return matchedValue.equals(o);

            if (!(o instanceof BlockIdentifier)) return false;

            BlockIdentifier other = (BlockIdentifier) o;
            matchedValue = other;
            return true;
        }

        @Override
        public BlockIdentifier getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }

    }

    public class ListWildcard extends AbstractList implements Wildcard<List> {
        private List matchedValue;


        @Override
        public Object get(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null) return false;

            if (matchedValue != null) return matchedValue.equals(o);

            if (!(o instanceof List)) return false;

            List other = (List) o;
            matchedValue = other;
            return true;
        }

        @Override
        public List getMatch() {
            return matchedValue;
        }

        @Override
        public void resetMatch() {
            matchedValue = null;
        }

    }
}
