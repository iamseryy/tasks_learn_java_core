package ru.task1.util;
import org.apache.commons.csv.CSVPrinter;
import ru.task1.config.AppConfig;
import ru.task1.model.Note;
import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.logging.Level;
import org.apache.commons.csv.CSVFormat;


public class FIleUtils {
    public static HashSet<Note> readCsv(File file, Charset charset){
        var notes = new HashSet<Note>();
        try (var bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));){
            var records = CSVFormat.EXCEL.withDelimiter(';').parse(bufferedReader);
            records.forEach(record -> {
                var format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                var calendar = Calendar.getInstance();
                try {
                    calendar.setTime(format.parse(record.get(0)));
                } catch (ParseException e) {
                    AppConfig.LOGGER.log(Level.SEVERE, e.toString(), e);
                }
                notes.add(new Note(record.get(1), calendar));
            });

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            AppConfig.LOGGER.log(Level.SEVERE, e.toString(), e);
        }


        return notes;
    }

    public static Boolean writeCsv(File file, Note note, Charset charset){
        var dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try (var fileWriter = new FileWriter(file, charset, file.exists() ? true : false);){
            var csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withDelimiter(';'));
            csvPrinter.printRecord(dateFormat.format(note.date().getTime()), note.note());
            csvPrinter.flush();
        } catch (IOException e) {
            AppConfig.LOGGER.log(Level.SEVERE, e.toString(), e);
            return false;
        }

        return true;
    }

    public InputStream getFileFromResourceAsStream(String fileName) {
        var classLoader = getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

}
