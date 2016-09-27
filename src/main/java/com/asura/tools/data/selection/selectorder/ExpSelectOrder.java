package com.asura.tools.data.selection.selectorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.asura.tools.condition.ClauseParser;
import com.asura.tools.condition.ConditionParseException;
import com.asura.tools.condition.IExpParser;
import com.asura.tools.data.selection.data.DataBlock;
import com.asura.tools.data.selection.data.DataBlocks;
import com.asura.tools.data.selection.expression.BooleanExpressionParser;
import com.asura.tools.data.selection.expression.CharOrderExpressionParser;
import com.asura.tools.data.selection.expression.IExpressionParser;
import com.asura.tools.data.selection.expression.NumberRangeExpressionParser;
import com.asura.tools.data.selection.expression.NumberSequenceExpressionParser;
import com.asura.tools.data.selection.expression.StringRangeExpressionParser;
import com.asura.tools.data.selection.expression.StringSequenceExpressionParser;
import com.asura.tools.data.selection.ordervalue.IOrderValue;
import com.asura.tools.util.StringUtil;

public class ExpSelectOrder implements ISelectOrder, IExpParser<ISelectOrder> {
	private static final long serialVersionUID = -5123723305215251206L;
	private static List<IExpressionParser> parsers = new ArrayList<>();
	private ClauseSelectOrder order;
	private static final String SPLITER_TYPE = ":";
	private String expression;

	static {
		parsers.add(new BooleanExpressionParser());
		parsers.add(new NumberSequenceExpressionParser());
		parsers.add(new NumberRangeExpressionParser());
		parsers.add(new StringSequenceExpressionParser());
		parsers.add(new StringRangeExpressionParser());
		parsers.add(new CharOrderExpressionParser());
	}

	public ExpSelectOrder() {
	}

	public ExpSelectOrder(String expression) {
		this.expression = expression;
	}

	public Set<String> getAllFeatures() {
		if (this.order == null) {
			this.order = ((ClauseSelectOrder) new ClauseParser<ISelectOrder>().parse(this.expression, this, new ClauseSelectOrder()));
		}

		return this.order.getAllFeatures();
	}

	public DataBlocks sort(DataBlock block) {
		if (this.order == null) {
			this.order = ((ClauseSelectOrder) new ClauseParser<ISelectOrder>().parse(this.expression, this, new ClauseSelectOrder()));
		}

		return this.order.sort(block);
	}

	public ExpSelectOrder clone() {
		return new ExpSelectOrder();
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public ISelectOrder parse(String exp) {
		if (!(StringUtil.isNullOrEmpty(exp))) {
			String[] ss = exp.split(SPLITER_TYPE);
			if (ss.length == 2) {
				FeatureSelectOrder featureOrder = new FeatureSelectOrder();
				featureOrder.setFeature(ss[0].trim());

				featureOrder.setValue(getOrderValue(ss[1]));

				return featureOrder;
			}
		}

		throw new ConditionParseException("错误的格式" + exp);
	}

	private IOrderValue getOrderValue(String type) {
		for (IExpressionParser parser : parsers) {
			if (parser.canParse(type)) {
				return parser.parse(type);
			}
		}

		throw new RuntimeException("can not parse type:" + type);
	}
}