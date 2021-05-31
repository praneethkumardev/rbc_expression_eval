package com.test.expression;

import java.util.Arrays;
import java.util.List;

// Can make this as test cases class.
public class ExpressionTestExecution {

	static List<Character> operators = Arrays.asList('+', '-', '/', '*');

	public static void main(String[] args) {

		test("(51 + 6)", 57, null);
		test("(51 - 6)", 45, null);
		test("(51 * 6)", 306, null);
		test("(51 / 6)", 8, null);

		test("(51 + 2 6)", 8, IllegalArgumentException.class);
	}

	private static void test(String expression, int expected, Class<? extends Exception> expectedExc) {

		try {
			int result = new ExpressionEvaluator().evaluate(expression);
			if (result == expected) {
				System.out.println("Test case passed");
				System.out.println("Expression " + expression + " returned " + result + " as expected " + expected);
			} else {
				System.out.println("Test case failed");
				System.out
						.println("Expression " + expression + " returned " + result + " but expected was " + expected);
			}

			System.out.println();
		} catch (Exception ex) {
			if (expectedExc != null && ex.getClass().equals(expectedExc)) {
				System.out.println("Expected exception for expression " + expression + "\n");
			}
		}
	}
}