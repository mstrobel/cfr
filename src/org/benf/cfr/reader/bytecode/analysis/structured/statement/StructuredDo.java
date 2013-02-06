package org.benf.cfr.reader.bytecode.analysis.structured.statement;

import org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement;
import org.benf.cfr.reader.bytecode.analysis.parse.expression.ConditionalExpression;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.BlockIdentifier;
import org.benf.cfr.reader.bytecode.analysis.structured.StructuredStatement;
import org.benf.cfr.reader.bytecode.analysis.structured.StructuredStatementTransformer;
import org.benf.cfr.reader.util.output.Dumper;

import java.util.List;

/**
 * Created:
 * User: lee
 * Date: 15/05/2012
 */
public class StructuredDo extends AbstractStructuredStatement {
    private ConditionalExpression condition;
    private Op04StructuredStatement body;
    private final BlockIdentifier block;

    public StructuredDo(ConditionalExpression condition, Op04StructuredStatement body, BlockIdentifier block) {
        this.condition = condition;
        this.body = body;
        this.block = block;
    }

    @Override
    public void dump(Dumper dumper) {
        if (block.hasForeignReferences()) dumper.print(block.getName() + " : ");
        dumper.print("do ");
        body.dump(dumper);
        dumper.removePendingCarriageReturn();
        dumper.print(" while (" + condition.toString() + ");\n");
    }

    @Override
    public void transformStructuredChildren(StructuredStatementTransformer transformer) {
        body.transform(transformer);
    }

    @Override
    public void linearizeInto(List<StructuredStatement> out) {
        out.add(this);
        body.linearizeStatementsInto(out);
    }

}