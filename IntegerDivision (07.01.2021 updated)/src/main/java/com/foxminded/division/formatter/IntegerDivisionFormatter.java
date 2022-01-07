package com.foxminded.division.formatter;

import com.foxminded.division.model.DivisionResult;
import com.foxminded.division.model.DivisionStep;

public class IntegerDivisionFormatter {
    private static final String SPACE = " ";
    private static final String MINUS = "_";
    private static final String NEWLINE = "\n";
    private static final String DASH = "-";
    private static final String VERTICALLINE = "|";

    public String formatDivisionResult(DivisionResult divisionResult) {
//        if (divisionResult.getDividend() < divisionResult.getDivisor()) {
//         return createDivisionStepWhenDividendLessThenDivisor(divisionResult.getDividend(), divisionResult.getDivisor());
//        }
//        if (divisionResult.getDividend() != 0) {
//            return createDivisionHeader(divisionResult) + createDivisionBody(divisionResult);
//        } else
//            return "_0|" + divisionResult.getDivisor() + NEWLINE + " 0|" + DASH.repeat(calculateNumberLength(divisionResult.getDivisor())) + NEWLINE + " -|0";

        return createDivisionHeader(divisionResult)+createDivisionBody(divisionResult);
    }

    private String createDivisionHeader(DivisionResult divisionResult) {

        int dividend =divisionResult.getDividend();
        int divisor = divisionResult.getDivisor();
        int quotient = divisionResult.getQuotient();

        int firstStepDivisorMultiple = divisionResult.getSteps().get(0).getDivisorMultiple();
        int spaceAmountToEndLine = calculateNumberLength(divisionResult.getDividend()) - calculateNumberLength(firstStepDivisorMultiple);
        int dashAmountBelowDivisorMultiple = calculateNumberLength(firstStepDivisorMultiple);
        int dashAmountInAnswer = Math.max(calculateNumberLength(divisionResult.getQuotient()), calculateNumberLength(divisionResult.getDivisor()));
        int indentAmountBeforeZero = calculateNumberLength(dividend);
        int dashAmountBelowDivisor = calculateNumberLength(divisor);

        if(divisor<0 | quotient<0) dashAmountInAnswer++;


        if(divisionResult.getDividend()<divisionResult.getDivisor()) {

            return MINUS + dividend + VERTICALLINE + divisor
                + NEWLINE + SPACE.repeat(indentAmountBeforeZero)
                + 0 + VERTICALLINE
                + DASH.repeat(dashAmountBelowDivisor) + NEWLINE
                + SPACE.repeat(indentAmountBeforeZero)
                + DASH + VERTICALLINE + 0;
        }
        else {
            return MINUS + divisionResult.getDividend()
                + VERTICALLINE
                + divisionResult.getDivisor()
                + NEWLINE
                + SPACE + firstStepDivisorMultiple
                + SPACE.repeat(spaceAmountToEndLine)
                + VERTICALLINE
                + DASH.repeat(dashAmountInAnswer)
                + NEWLINE + SPACE
                + DASH.repeat(dashAmountBelowDivisorMultiple)
                + SPACE.repeat(spaceAmountToEndLine)
                + VERTICALLINE + divisionResult.getQuotient();

        }
    }

    private String createDivisionBody(DivisionResult divisionResult) {

        StringBuilder result = new StringBuilder();
        int indent = 2;

        int lastStepIndex = divisionResult.getSteps().size() - 1;
        int[] dividendDigits = convertNumberToDigits(divisionResult.getDividend());
        String measuredLine =MINUS + divisionResult.getSteps().get(1).getElementaryDividend();


        for (int i = 1; i < divisionResult.getSteps().size()-1; i++) {

            indent = indent+sizeDifference(divisionResult.getSteps().get(i-1).getDivisorMultiple(),divisionResult.getSteps().get(i-1).getRemainder());

            if(divisionResult.getSteps().get(i-1).getRemainder()==0) {
                indent++;
            }

            DivisionStep step = divisionResult.getSteps().get(i);
            int dividendLength = calculateNumberLength(step.getElementaryDividend());



            int lineLength = measuredLine.length();
            if(divisionResult.getSteps().get(i-1).getRemainder()==0){
                indent=indent+additionalSpacesCalculator(dividendDigits,lineLength-1);
            }

            result.append(formatStep(divisionResult.getSteps().get(i), indent));
            measuredLine = SPACE.repeat(indent-2) + MINUS + divisionResult.getSteps().get(i).getElementaryDividend();

            indent=indent+sizeDifference(divisionResult.getSteps().get(i).getElementaryDividend(),divisionResult.getSteps().get(i).getDivisorMultiple());
        }



        int remainderIntend;
        if(divisionResult.getSteps().size()==2){
            remainderIntend = dividendDigits.length;
        }
        else remainderIntend = measuredLine.length()-calculateNumberLength(divisionResult.getSteps().get(lastStepIndex).getRemainder());
        result.append(NEWLINE + SPACE.repeat(remainderIntend) + divisionResult.getSteps().get(lastStepIndex).getRemainder());

        return result.toString();
    }

    private String formatStep(DivisionStep step, int indent) {
        int dividendLength = calculateNumberLength(step.getElementaryDividend());

        return NEWLINE
            + SPACE.repeat(indent-2)
            + MINUS + step.getElementaryDividend()
            + NEWLINE
            + SPACE.repeat(indent-1+sizeDifference(step.getElementaryDividend(),step.getDivisorMultiple()))
            + step.getDivisorMultiple()
            + NEWLINE
            + SPACE.repeat(indent-1)
            + DASH.repeat(dividendLength);
    }

    private int calculateNumberLength(int number) {
        return String.valueOf(Math.abs(number)).length();
    }

    private int sizeDifference(int i, int j){
        return calculateNumberLength(i)-calculateNumberLength(j);
    }

    public static int[] convertNumberToDigits(int convertible) {
        return Integer.toString(convertible).chars().map(c -> c - '0').toArray();
    }

    public static int additionalSpacesCalculator(int[] dividendDigits,int start){

        int i = start;
        int counter=0;
        while (dividendDigits[i]==0){
            counter++;
            i++;
        }
        return counter;
    }
}
