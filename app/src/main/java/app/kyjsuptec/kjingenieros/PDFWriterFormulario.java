package app.kyjsuptec.kjingenieros;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;
import crl.android.pdfwriter.Transformation;

public class PDFWriterFormulario {
    final static int MARGIN_LEFT = 75;
    static PDFWriter mPDFWriter;

    public static void savePDF(ArrayList<String> datos, Bitmap pic1,
                               Bitmap pic2, String fileName, Context context)
            throws UnsupportedEncodingException {

        if (pic1 == null) {
        }
        generatePDF(datos, pic1, pic2, context);
        String pdfcontent = "";
        try {
            outputToFile(fileName, pdfcontent, "iso-8859-1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void savePDF(ArrayList<ArrayList<String>> datos,
                               String fileName, Context context)
            throws UnsupportedEncodingException {
        mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
                PaperSize.LETTER_HEIGHT);
        for (int i = 0; i < datos.size(); i++) {
            Bitmap pic1 = null;
            Bitmap pic2 = null;
            if (i > 0) {
                mPDFWriter.newPage();
            }
            ArrayList<String> formulario = datos.get(i);
            String img1 = formulario.get(formulario.size() - 2);
            String img2 = formulario.get(formulario.size() - 1);
            if (!img1.equals("--1")) {
                pic1 = decodeBase64(img1);
            }
            if (!img2.equals("--2")) {
                pic2 = decodeBase64(img2);
            }

            generatePDF(formulario, pic1, pic2, context);
        }

        String s = mPDFWriter.asString();
        /*
         * String s = Normalizer.normalize(mPDFWriter.asString(),
		 * Normalizer.Form.NFD); s = s.replaceAll("[^\\p{ASCII}]", "");
		 */
        try {
            outputToFile(fileName, s, "ISO-8859-1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void generatePDF(ArrayList<String> datos, Bitmap pic1,
                                    Bitmap pic2, Context context) throws UnsupportedEncodingException {

        AssetManager mngr = context.getAssets();
        try {
            // LOGO
            Bitmap xoiPNG = BitmapFactory.decodeStream(mngr
                    .open("logo_final_peq.png"));

            mPDFWriter.addImageKeepRatio(MARGIN_LEFT,
                    PaperSize.LETTER_HEIGHT - 160, 220, 90, xoiPNG,
                    Transformation.DEGREES_0_ROTATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fecha
        mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
                PaperSize.LETTER_HEIGHT - 100, 16, now());

        // CUADRICULA

        mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
                PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

        mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                PaperSize.LETTER_HEIGHT - 120);

        mPDFWriter.addLine(60, 530, PaperSize.LETTER_WIDTH - 60, 530);
        mPDFWriter.addLine(60, 455, PaperSize.LETTER_WIDTH - 60, 455);

        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA_BOLD,
                StandardFonts.WIN_ANSI_ENCODING);

        mPDFWriter.addText(MARGIN_LEFT, 595, 18, "De: Interventoría");
        mPDFWriter.addText(MARGIN_LEFT, 570, 18, "Para: Obra");
        mPDFWriter.addText(MARGIN_LEFT, 545, 18, "Asunto: Observaciones obra");

        String[] form = datos.get(0).split(" ");
        String primero = "";
        String segundo = "";

        for (int i = 0; i < form.length; i++) {
            if (i < 6) {
                primero += form[i] + " ";
            } else {
                segundo += form[i] + " ";
            }

        }
        primero = primero.trim();
        segundo = segundo.trim();

        if (form.length > 5) {
            mPDFWriter.addText(MARGIN_LEFT, 435, 15, primero);
            mPDFWriter.addText(MARGIN_LEFT, 415, 15, segundo);
        } else {
            mPDFWriter.addText(MARGIN_LEFT, 435, 15, datos.get(0));
        }

        int top = 410;

        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);
        mPDFWriter.addText(MARGIN_LEFT, 515, 15, datos.get(3));
        mPDFWriter.addText(MARGIN_LEFT, 490, 15, datos.get(4));
        mPDFWriter.addText(MARGIN_LEFT, 465, 15, datos.get(5));

        int j = 0;

        for (int i = 6; i < datos.size() - 4; i++) {
            if (i % 18 == 0) {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
                top = PaperSize.LETTER_HEIGHT - 100;
                j = 12 * (i / 18);
                mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                        PaperSize.LETTER_HEIGHT - 120);
            }
            String linea = datos.get(i);
            if (linea.length() > 71) {
                ArrayList<String> lineas = splitLineas(linea);
                datos.remove(i);
                for (int k = 0; k < lineas.size(); k++) {
                    String elemento = lineas.get(k);
                    datos.add(i + k, elemento);
                }
            }
            if (j == 0) {
                mPDFWriter.addText(MARGIN_LEFT, (top - (i - (j + 5)) * 25), 14,
                        datos.get(i));
            } else {

                mPDFWriter.addText(MARGIN_LEFT,
                        (top - (i - (j + (5 * (i / 18)))) * 25), 14,
                        datos.get(i));
            }

        }

        int pageCount = mPDFWriter.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            mPDFWriter.setCurrentPage(i);
            mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
                    + Integer.toString(pageCount));
        }

        if (datos.get(2).equals("9")) {
            mPDFWriter.addLine(70, 120, 350, 120);
        }
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        boolean hasEvidenciaEscrita = false;
        int sup = PaperSize.LETTER_HEIGHT - 100;
        // Imagenes Evidencia Fotografica
        if (pic1 == null) {
        } else {
            String evidencia = datos.get(datos.size() - 3);
            if (!evidencia.equals("EMPTY")) {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
                if (evidencia.contains("\n")) {
                    String[] lines = datos.get(datos.size() - 3).split("\n");
                    for (int i = 0; i < 3; i++) {
                        mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
                                lines[i]);
                    }
                } else {
                    mPDFWriter.addText(MARGIN_LEFT, sup - 25, 14, evidencia);
                }
                hasEvidenciaEscrita = true;
            } else {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
            }

            int[] dimensiones = redimensionarImagen(pic1);

            mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                    PaperSize.LETTER_HEIGHT - 120);
            mPDFWriter
                    .addText(MARGIN_LEFT, sup - 80, 14, "Anexo Fotogr�fico 1");

            mPDFWriter.addImageKeepRatio(
                    centrar(PaperSize.LETTER_WIDTH, dimensiones[0]),
                    centrar(PaperSize.LETTER_HEIGHT, dimensiones[1]),
                    dimensiones[0], dimensiones[1], pic1,
                    Transformation.DEGREES_0_ROTATION);
        }
        if (pic2 == null) {
        } else {
            if (!hasEvidenciaEscrita) {
                String evidencia = datos.get(datos.size() - 3);
                if (!evidencia.equals("EMPTY")) {
                    mPDFWriter.newPage();
                    mPDFWriter.setFont(StandardFonts.SUBTYPE,
                            StandardFonts.HELVETICA,
                            StandardFonts.WIN_ANSI_ENCODING);
                    if (evidencia.contains("\n")) {
                        String[] lines = datos.get(datos.size() - 3)
                                .split("\n");
                        for (int i = 0; i < 3; i++) {
                            mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
                                    lines[i]);
                        }
                    } else {
                        mPDFWriter
                                .addText(MARGIN_LEFT, sup - 25, 14, evidencia);
                    }
                    hasEvidenciaEscrita = true;
                } else {
                    mPDFWriter.newPage();
                    mPDFWriter.setFont(StandardFonts.SUBTYPE,
                            StandardFonts.HELVETICA,
                            StandardFonts.WIN_ANSI_ENCODING);
                }
            } else {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
            }

            mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                    PaperSize.LETTER_HEIGHT - 120);
            mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                    StandardFonts.WIN_ANSI_ENCODING);
            mPDFWriter
                    .addText(MARGIN_LEFT, sup - 80, 14, "Anexo Fotogr�fico 2");

            int[] dimensiones = redimensionarImagen(pic2);

            mPDFWriter.addImageKeepRatio(
                    centrar(PaperSize.LETTER_WIDTH, dimensiones[0]),
                    centrar(PaperSize.LETTER_HEIGHT, dimensiones[1]),
                    dimensiones[0], dimensiones[1], pic2,
                    Transformation.DEGREES_0_ROTATION);
        }

        if (!hasEvidenciaEscrita) {
            String evidencia = datos.get(datos.size() - 3);
            if (!evidencia.equals("EMPTY")) {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
                mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                        PaperSize.LETTER_HEIGHT - 120);
                if (evidencia.contains("\n")) {
                    String[] lines = datos.get(datos.size() - 3).split("\n");
                    for (int i = 0; i < 3; i++) {
                        mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
                                lines[i]);
                    }
                } else {
                    mPDFWriter.addText(MARGIN_LEFT, sup - 25, 14, evidencia);
                }
                hasEvidenciaEscrita = true;
            }
        }

    }

    private static int[] redimensionarImagen(Bitmap fotoBitmapFinal) {

        int[] result = new int[2];
        int height = fotoBitmapFinal.getHeight();
        int width = fotoBitmapFinal.getWidth();
        if (height > width) {
            int widthFinal = (int) Math.floor((width * 400) / height);
            result[0] = widthFinal;
            result[1] = 400;
        } else if (width > height) {
            int heightFinal = (int) Math.floor((height * 400) / width);
            result[0] = 400;
            result[1] = heightFinal;
        } else {
            result[0] = 400;
            result[1] = 400;
        }

        return result;
    }

    public static void savePDF(ArrayList<String> datos, Bitmap pic,
                               String fileName, Context context)
            throws UnsupportedEncodingException {

        String pdfcontent = generatePDF(datos, pic, context);
        try {
            outputToFile(fileName, pdfcontent, "iso-8859-1");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> splitLineas(String text) {
        ArrayList<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index,
                    Math.min(index + 70, text.length())));
            index += 70;
        }
        return strings;
    }

    private static int centrar(int tamañoPapel, int tamañoImagen) {
        int margen = (tamañoPapel - tamañoImagen) / 2;
        return margen;
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return "Fecha: " + sdf.format(cal.getTime());
    }

    private static void outputToFile(String fileName, String pdfContent,
                                     String encoding) throws FileNotFoundException {

        File newFile = new File(Environment.getExternalStorageDirectory() + "/"
                + fileName);

        try {
            newFile.createNewFile();
            try {

                PrintWriter pdfFile = new PrintWriter(newFile, "ISO-8859-1");
                // FileOutputStream pdfFile = new FileOutputStream(newFile);
                // pdfFile.write(pdfContent.getBytes(encoding));
                pdfFile.write(pdfContent);
                pdfFile.close();

            } catch (FileNotFoundException e) {
                Log.e("FNF - EX", e.toString());
            }
        } catch (IOException e) {
            Log.e("IO - EX", e.toString());
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private static String generatePDF(ArrayList<String> datos, Bitmap pic,
                                      Context context) {
        PDFWriter mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
                PaperSize.LETTER_HEIGHT);
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);
        AssetManager mngr = context.getAssets();
        try {
            // LOGO
            Bitmap xoiPNG = BitmapFactory.decodeStream(mngr
                    .open("logo_final_peq.png"));

            mPDFWriter.addImageKeepRatio(MARGIN_LEFT,
                    PaperSize.LETTER_HEIGHT - 160, 220, 90, xoiPNG,
                    Transformation.DEGREES_0_ROTATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fecha
        mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
                PaperSize.LETTER_HEIGHT - 100, 16, now());
        mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
                PaperSize.LETTER_HEIGHT - 120, 16, "MEMO EXPRES");

        // CUADRICULA

        mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
                PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

        mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                PaperSize.LETTER_HEIGHT - 120);

        // CUADRICULA

        mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
                PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

        mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                PaperSize.LETTER_HEIGHT - 120);

        mPDFWriter.addLine(60, 530, PaperSize.LETTER_WIDTH - 60, 530);

        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        mPDFWriter.addText(MARGIN_LEFT, 595, 18, "De: " + datos.get(0));
        mPDFWriter.addText(MARGIN_LEFT, 570, 18, "Para: " + datos.get(1));
        mPDFWriter.addText(MARGIN_LEFT, 545, 18, "Asunto: " + datos.get(2));

        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        int top = 500;
        Pattern p = Pattern.compile(".{1,55}");
        Matcher m = null;

        int i = 0;
        if (datos.get(3).contains("\n")) {
            String[] lines = datos.get(3).split("\n");
            for (int j = 0; j < lines.length; j++) {
                if (lines[j].length() > 55) {
                    m = p.matcher(lines[j]);
                    while (m.find()) { // Find each match in turn; String can't
                        // do this.
                        String name = m.group(0); // Access a submatch group;
                        // String can't
                        // do this.
                        Log.e("REGEX", name);
                        mPDFWriter.addText(MARGIN_LEFT, top - (i * 25), 18,
                                name);
                        i++;
                    }
                } else {
                    mPDFWriter.addText(MARGIN_LEFT, top - (i * 25), 18,
                            lines[j]);
                }
            }
        } else {
            if (datos.get(3).length() > 55) {
                m = p.matcher(datos.get(3));
                while (m.find()) { // Find each match in turn; String can't
                    // do this.
                    String name = m.group(0); // Access a submatch group;
                    // String can't
                    // do this.
                    Log.e("REGEX", name);
                    mPDFWriter.addText(MARGIN_LEFT, top - (i * 25), 18, name);
                    i++;
                }
            } else {
                mPDFWriter.addText(MARGIN_LEFT, 500, 18, datos.get(3));
            }
        }

        // Imagenes Evidencia Fotografica
        if (pic == null) {
        } else {
            mPDFWriter.newPage();
            mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
                    PaperSize.LETTER_HEIGHT - 120);
            mPDFWriter.addText(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 100, 14,
                    "Anexo Fotografico 1");

            int[] dimensiones = redimensionarImagen(pic);

            mPDFWriter.addImageKeepRatio(
                    centrar(PaperSize.LETTER_WIDTH, dimensiones[0]),
                    centrar(PaperSize.LETTER_HEIGHT, dimensiones[1]),
                    dimensiones[0], dimensiones[1], pic,
                    Transformation.DEGREES_0_ROTATION);
        }

        String s = mPDFWriter.asString();

        return s;
    }
}
