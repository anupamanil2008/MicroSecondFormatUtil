package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CustomDateTimeUtil.
 */
public class CustomDateTimeUtil {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateTimeUtil.class);

    /** The Constant MICROFORMAT. */
    private static final String MICROFORMAT = "SSSSSS";

    /** The input date time. */
    private String inputDateTime;

    /**
     * Parses the date time.
     *
     * @param inputDate the input date
     * @param inputFormat the input format
     * @return the double
     * @throws ParseException the parse exception
     */
    public double parseDateTime(String inputDate, String inputFormat) throws ParseException {

        double timeInMillis;
        setInputDateTime(inputDate);
        String tmpFormat = inputFormat.replaceAll("S", "");
        SimpleDateFormat tmpSDF = new SimpleDateFormat(tmpFormat);
        tmpSDF.setLenient(false);
        long tmpMillis = 0L;
        try {
            tmpMillis = tmpSDF.parse(inputDate).getTime();
        } catch (ParseException e) {
            setInputDateTime(inputDate);
            throw new ParseException("Unable to parse date " + inputDate + " ERROR:" + e.getMessage(), 0);
        }
        String tmpInputDate = tmpSDF.format(new Date(tmpMillis));
        String tmpMillisOnly = StringUtils.difference(tmpInputDate, inputDate);
        double tmpMicrosOnly = 0D;
        try {
            tmpMicrosOnly = Double.parseDouble(tmpMillisOnly) * Math.pow(10, (3 - tmpMillisOnly.length()));
        } catch (NumberFormatException e) {
            setInputDateTime(inputDate);
            throw new ParseException("Unable to parse date " + inputDate + " ERROR:" + e.getMessage(), 0);
        }
        if (inputFormat.contains(MICROFORMAT)) {
            timeInMillis = tmpMillis + tmpMicrosOnly;
        } else {
            timeInMillis = tmpMillis + (long) tmpMicrosOnly;
        }
        return timeInMillis;
    }

    /**
     * Format date time.
     *
     * @param outputDate the output date
     * @param outputFormat the output format
     * @return the string
     * @throws ParseException the parse exception
     */
    public String formatDateTime(double outputDate, String outputFormat) throws ParseException {

        long millis = (long) outputDate;
        String formattedOutputDate = "";
        if (outputFormat.contains(MICROFORMAT)) {
            String tmpFormat = outputFormat.replace(MICROFORMAT, "'" + MICROFORMAT + "'");
            SimpleDateFormat tmpSDF = new SimpleDateFormat(tmpFormat);
            tmpSDF.setLenient(false);
            formattedOutputDate = tmpSDF.format(new Date(millis));
            long tmpMillis = tmpSDF.parse(formattedOutputDate).getTime();
            double tmpMillisOnly = (outputDate - tmpMillis) * 1e3;
            formattedOutputDate = formattedOutputDate.replaceFirst(MICROFORMAT,
                    String.format("%06d", (long) tmpMillisOnly));
        } else {
            SimpleDateFormat outputSDF = new SimpleDateFormat(outputFormat);
            outputSDF.setLenient(false);
            formattedOutputDate = outputSDF.format(new Date(millis));
        }

        return formattedOutputDate;
    }

    /**
     * Gets the input date time.
     *
     * @return the input date time
     */
    public String getInputDateTime() {
        return inputDateTime;
    }

    /**
     * Sets the input date time.
     *
     * @param inputDateTime the new input date time
     */
    public void setInputDateTime(String inputDateTime) {
        this.inputDateTime = inputDateTime;
    }
}
