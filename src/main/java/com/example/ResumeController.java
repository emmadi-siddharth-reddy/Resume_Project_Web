package com.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class ResumeController {

    @GetMapping("/")
    public ModelAndView showForm() {
        return new ModelAndView("form");
    }

    @PostMapping("/generate")
    public ResponseEntity<InputStreamResource> generatePdf(
            @RequestParam("name") String name,
            @RequestParam("objective") String objective,
            @RequestParam("email") String email,
            @RequestParam("contact") String contact,
            @RequestParam("address") String address,
            @RequestParam("skill1") String skill1,
            @RequestParam("skill2") String skill2,
            @RequestParam("skill3") String skill3,
            @RequestParam("skill4") String skill4,
            @RequestParam("experience") String experience,
            @RequestParam("college") String college,
            @RequestParam("intermediate") String intermediate,
            @RequestParam("schooling") String schooling,
            @RequestParam("languages") String languages,
            @RequestParam("image") MultipartFile image) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Name: " + name));
            document.add(new Paragraph("Objective: " + objective));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph("Contact: " + contact));
            document.add(new Paragraph("Address: " + address));
            document.add(new Paragraph("Skills:"));
            document.add(new Paragraph(skill1));
            document.add(new Paragraph(skill2));
            document.add(new Paragraph(skill3));
            document.add(new Paragraph(skill4));
            document.add(new Paragraph("Experience: " + experience));
            document.add(new Paragraph("Education:"));
            document.add(new Paragraph("College: " + college));
            document.add(new Paragraph("Intermediate: " + intermediate));
            document.add(new Paragraph("Schooling: " + schooling));
            document.add(new Paragraph("Languages Known: " + languages));

            if (image != null && !image.isEmpty()) {
                com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(IOUtils.toByteArray(image.getInputStream()));
                pdfImage.scaleToFit(100, 100);
                document.add(pdfImage);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=resume.pdf");

        return new ResponseEntity<>(new InputStreamResource(in), headers, HttpStatus.OK);
    }
}
