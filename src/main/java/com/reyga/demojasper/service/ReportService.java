package com.reyga.demojasper.service;

import com.reyga.demojasper.dto.response.SampleResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportService {

    public void compileReport(HashMap<String, Object> parameters, String outputFileName, List<SampleResponse> dataList) {
        try {
            log.debug("Generating Document");
            InputStream fileStream = this.getClass().getResourceAsStream("/report/sample_jasper_template.jrxml");
            if (fileStream == null) {
                fileStream = this.getClass().getClassLoader().getResourceAsStream("/report/sample_jasper_template.jrxml");
            }

            JasperReport report = JasperCompileManager.compileReport(fileStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);

            File pdfFile = new File(outputFileName);
            try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, fos);
            } catch (FileNotFoundException e) {
                log.error("FileNotFoundException: {}", e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                log.error("IOException: {}", e.getMessage());
                throw new RuntimeException(e);
            }

            log.debug("PDF generated successfully: {}", outputFileName);
        } catch (JRException jrex) {
            log.error("JRException: {}", jrex.getMessage());
        }
    }

    public byte[] compileReportFile(HashMap<String, Object> parameters, String outputFileName, List<SampleResponse> dataList) {
        try {
            log.debug("Generating Document");
            InputStream fileStream = this.getClass().getResourceAsStream("/report/sample_jasper_template.jrxml");
            if (fileStream == null) {
                fileStream = this.getClass().getClassLoader().getResourceAsStream("/report/sample_jasper_template.jrxml");
            }

            JasperReport report = JasperCompileManager.compileReport(fileStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            byte[] reportContent = JasperExportManager.exportReportToPdf(jasperPrint);

            log.debug("PDF generated successfully: {}", outputFileName);
            return reportContent;
        } catch (JRException jrex) {
            log.error("JRException: {}", jrex.getMessage());
            return null;
        }
    }

    public ByteArrayOutputStream generateReport(InputStream inputStream, Map<String, Object> parameters, JRDataSource jrDataSource) {
        ByteArrayOutputStream outputStream;
        try {
            log.debug("Start Generate ReportService");
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, jrDataSource);
            outputStream = new ByteArrayOutputStream();
            log.debug("Start Config PDF");
            JRPdfExporter exporter = getJrpdfExporter(jasperPrint, outputStream);
            exporter.exportReport();
            log.debug("Report Successfully generated");
            return outputStream;
        } catch (Exception var8) {
            log.error("Exception : {}", var8.getMessage());
            return null;
        }
    }

    @NotNull
    public static JRPdfExporter getJrpdfExporter(JasperPrint jasperPrint, ByteArrayOutputStream outputStream) {
        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("system");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);
        return exporter;
    }
}
