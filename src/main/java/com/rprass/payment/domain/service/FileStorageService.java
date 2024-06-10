package com.rprass.payment.domain.service;

import com.opencsv.CSVReader;
import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.common.exceptions.FileStorageException;
import com.rprass.payment.common.exceptions.MyFileNotFoundException;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.config.FileStorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private AccountService accountService;

    @Autowired
    public FileStorageService(FileStorageConfig storageConfig) {
        Path path = Paths.get(storageConfig.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileStorageLocation = path;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        validateUploadedFile(file);

        String fileName = UUID.randomUUID().toString() + ".csv";
        Path targetLocation = fileStorageLocation.resolve(fileName);

        try {
            copyFile(file, targetLocation);
            processCSVFile(targetLocation);
            return fileName;
        } catch (FileStorageException | MyFileNotFoundException | ParseException e) {
            throw handleStorageException(e);
        } finally {
            deleteTemporaryFile(targetLocation);
        }
    }

    private void validateUploadedFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Empty file uploaded!");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!originalFileName.endsWith(".csv")) {
            throw new FileStorageException("Invalid file format. Only CSV files are allowed.");
        }
    }

    public void copyFile(MultipartFile file, Path targetLocation) throws FileStorageException {
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to copy uploaded file. Please try again!", e);
        }
    }

    private void processCSVFile(Path targetLocation) throws ParseException, MyFileNotFoundException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (CSVReader reader = new CSVReader(new FileReader(targetLocation.toFile()))) {
            List<String[]> rows = reader.readAll();
            for (String[] row : rows) {
                AccountDTO accountDTO = createAccountDTOFromRow(row, dateFormat);
                accountService.create(accountDTO);
            }
        } catch (IOException e) {
            throw new MyFileNotFoundException("Failed to read CSV file.", e);
        }
    }

    private AccountDTO createAccountDTOFromRow(String[] row, SimpleDateFormat dateFormat) throws ParseException {
        return AccountDTO.builder()
                .dueDate(dateFormat.parse(row[0]))
                .paymentDate(row[1].isEmpty() ? null : dateFormat.parse(row[1]))
                .value(new BigDecimal(row[2]))
                .description(row[3])
                .status(Status.valueOf(row[4]))
                .build();
    }

    private FileStorageException handleStorageException(Exception e) {
        String message = "Failed to process uploaded file.";
        if (e instanceof ParseException) {
            message += " Error parsing date: " + e.getMessage();
        } else if (e instanceof MyFileNotFoundException) {
            message += " File not found.";
        } else {
            message += " Unknown error.";
        }
        return new FileStorageException(message, e);
    }

    private void deleteTemporaryFile(Path targetLocation) {
        try {
            Files.deleteIfExists(targetLocation);
        } catch (IOException e) {
            //System.err.println("Failed to delete temporary file: " + targetLocation);
            throw new FileStorageException("Failed to delete temporary file: ", e);
        }
    }

}
