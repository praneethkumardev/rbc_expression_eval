package com.test.expression;

import java.util.Stack;

public class ExpressionEvaluator {

	enum ExpressionType {
		INFIX, POSTFIX, PREFIX
	}

	public Integer evaluate(String expression) {

		if (expression == null) {
			throw new IllegalArgumentException();
		}

		// Remove extra spaces from left and right ends
		expression = expression.trim();

		// First of all check whether expression is infix, postfix or prefix
		ExpressionType expressionType = expressionType(expression);

		// We can evaluate postfix expression very well. So let's convert to postfix if
		// expression is given in infix or prefix style.
		try {
			String postfixExpression = null;
			if (ExpressionType.INFIX.equals(expressionType)) {
				postfixExpression = infixToPostfix(expression);
			} else if (ExpressionType.PREFIX.equals(expressionType)) {
				postfixExpression = prefixToPostfix(expression);
			}

			System.out.println("Expression in postfix: " + postfixExpression);

			Integer result = evaluatePostfix(postfixExpression);
			return result;
		} catch (Exception ex) {
			throw new IllegalArgumentException();
		}
	}

	private ExpressionType expressionType(String expression) {

		char[] elements = expression.toCharArray();

		for (int count = 0; count < elements.length; count++) {
			if ('(' == elements[count] || ' ' == elements[count]) {
				continue;
			} else if (isOperator(elements[count])) {
				// We found the operator in the beginning
				return ExpressionType.PREFIX;
			} else {
				break;
			}
		}

		for (int count = elements.length - 1; count >= 0; count--) {
			if (')' == elements[count] || ' ' == elements[count]) {
				continue;
			} else if (isOperator(elements[count])) {
				// We found the operator in the beginning
				return ExpressionType.POSTFIX;
			} else {
				break;
			}
		}

		return ExpressionType.INFIX;
	}

	private boolean isOperator(char x) {
		switch (x) {
		case '+':
		case '-':
		case '/':
		case '*':
			return true;
		}
		return false;
	}

	/*
	 * This method will convert an infix expression into postfix expression.
	 */
	private String infixToPostfix(String exp) {

		// initializing empty String for result
		StringBuilder result = new StringBuilder("");

		// initializing empty stack
		Stack<Character> stack = new Stack<>();

		for (int i = 0; i < exp.length(); ++i) {
			char c = exp.charAt(i);

			if (' ' == c) {
				result.append(c);
			}

			// If the scanned character is an
			// operand, add it to output.
			else if (Character.isDigit(c)) {
				// Get the complete operand as space is delimiter
				String operand = getFirstOperand(exp, i);
				i = i + operand.length() - 1;
				result.append(operand);
			}

			// If the scanned character is an '(',
			// push it to the stack.
			else if (c == '(') {
				stack.push(c);
			}

			// If the scanned character is an ')',
			// pop and output from the stack
			// until an '(' is encountered.
			else if (c == ')') {
				while (!stack.isEmpty() && stack.peek() != '(') {
					result.append(stack.pop());
				}

				stack.pop();
			}
			// an operator is encountered
			else {
				while (!stack.isEmpty() && priority(c) <= priority(stack.peek())) {
					result.append(stack.pop());
				}
				stack.push(c);
			}

		}

		// pop all the operators from the stack
		while (!stack.isEmpty()) {
			if (stack.peek() == '(')
				return "Invalid Expression";
			result.append(stack.pop());
		}

		return result.toString();
	}

	/*
	 * Get the operand from the string. Example: 123+122+.. 123 must be returned.
	 * 
	 */
	private String getFirstOperand(String exp, int start) {

		String operand = "";
		for (int i = start; i < exp.length(); ++i) {
			char c = exp.charAt(i);
			if (Character.isDigit(c)) {
				operand += c;
			} else {
				break;
			}
		}

		return operand;
	}

	/**
	 * We need to define the precedence of each operator.
	 */
	private int priority(char ch) {
		switch (ch) {

		case '(':
			return 1;

		case '+':
		case '-':
			return 2;

		case '*':
		case '/':
			return 3;
		}
		return -1;
	}

	// Convert prefix to Postfix expression
	private String prefixToPostfix(String preExpression) {

		Stack<String> stack = new Stack<String>();

		// length of expression
		int length = preExpression.length();

		// reading from right to left
		for (int i = length - 1; i >= 0; i--) {
			// check if symbol is operator
			if (isOperator(preExpression.charAt(i))) {
				// pop two operands from stack
				String op1 = stack.peek();
				stack.pop();
				String op2 = stack.peek();
				stack.pop();

				// concat the operands and operator
				String temp = op1 + op2 + preExpression.charAt(i);

				// Push String temp back to stack
				stack.push(temp);
			}

			// if symbol is an operand
			else {
				// push the operand to the stack
				stack.push(preExpression.charAt(i) + "");
			}
		}

		// stack contains only the Postfix expression
		return stack.peek();
	}

	// Convert prefix to Postfix expression
	// Method to evaluate value of a postfix expression
	private int evaluatePostfix(String exp) {
		// create a stack
		Stack<Integer> stack = new Stack<>();

		// Scan all characters one by one
		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);

			// If the scanned character is an operand (number here),
			// push it to the stack.
			if (Character.isDigit(c)) {
				// Get the complete operand as space is delimiter
				String operand = getFirstOperand(exp, i);
				stack.push(Integer.parseInt(operand));
				i = i + operand.length() - 1;
			} else if (c == ' ') {
				continue;
			}

			// If the scanned character is an operator, pop two
			// elements from stack apply the operator
			else {
				int val1 = stack.pop();
				int val2 = stack.pop();

				switch (c) {
				case '+':
					stack.push(val2 + val1);
					break;

				case '-':
					stack.push(val2 - val1);
					break;

				case '/':
					stack.push(val2 / val1);
					break;

				case '*':
					stack.push(val2 * val1);
					break;
				}
			}
		}

		if (stack.size() > 1) {
			throw new IllegalArgumentException();
		}

		return stack.pop();
	}
}