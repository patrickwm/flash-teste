package br.com.patrickwm.flash.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    public static String toDDMMYYYY_HHMM(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        return dtf.format(time);
    }
}
