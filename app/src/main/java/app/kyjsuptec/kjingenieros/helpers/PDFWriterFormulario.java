package app.kyjsuptec.kjingenieros.helpers;

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
import java.util.StringTokenizer;

import app.kyjsuptec.kjingenieros.controllers.UserManager;
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


    public static String addLinebreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > maxLineLength) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");
            lineLen += word.length();
        }
        return output.toString();
    }

    public static String[] splitIntoLine(String input, int maxCharInLine) {

        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() > maxCharInLine) {
                output.append(word.substring(0, maxCharInLine - lineLen) + "\n");
                word = word.substring(maxCharInLine - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxCharInLine) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(word + " ");

            lineLen += word.length() + 1;
        }
        // output.split();
        // return output.toString();
        return output.toString().split("\n");
    }

    private static final int ALTO_LOGO = 40;
    private static final int ANCHO_LOGO = 170;
    private static final int ALTO_LOGO_ENCABEZADO_INICIO = PaperSize.LETTER_HEIGHT - 123;
    private static final int CUADRICULA_ENCABEZADO_INICIO = PaperSize.LETTER_HEIGHT - 125;
    private static final int ALTO_CUADRICULA_ENCABEZADO_INICIO = 80;
    private static final int POSICION_IZQUIERDA_NOMBRE_FORMULARIO = MARGIN_LEFT + ANCHO_LOGO + 35;
    private static final int POSICION_ALTO_NOMBRE_FORMULARIO = PaperSize.LETTER_HEIGHT - 85;
    private static final int TAMANO_FUENTE_TITULO = 11;
    private static final int TAMANO_FUENTE = 14;


    private static void generatePDF(ArrayList<String> datos, Bitmap pic1,
                                    Bitmap pic2, Context context) throws UnsupportedEncodingException {

        AssetManager mngr = context.getAssets();
        try {
            // LOGO
            Bitmap xoiPNG = BitmapFactory.decodeStream(mngr
                    .open("logo_final_peq.png"));

            mPDFWriter.addImageKeepRatio(MARGIN_LEFT,
                    ALTO_LOGO_ENCABEZADO_INICIO,
                    ANCHO_LOGO,
                    ALTO_LOGO,
                    xoiPNG,
                    Transformation.DEGREES_0_ROTATION);
        } catch (IOException e) {
            Log.e("IO - EX", e.toString());
        }


        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        // Nombre formulario
        String[] form = splitIntoLine(datos.get(0), 38);

        if (form.length > 1) {
            String primero = form[0];
            String segundo = form[1];
            primero = primero.trim();
            segundo = segundo.trim();
            mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO, POSICION_ALTO_NOMBRE_FORMULARIO, TAMANO_FUENTE_TITULO, primero);
            mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO, POSICION_ALTO_NOMBRE_FORMULARIO - 15, TAMANO_FUENTE_TITULO, segundo);
        } else {
            mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO, POSICION_ALTO_NOMBRE_FORMULARIO, TAMANO_FUENTE_TITULO, datos.get(0));
        }


        // CUADRICULA


        mPDFWriter.addRectangle(60,
                CUADRICULA_ENCABEZADO_INICIO,
                PaperSize.LETTER_WIDTH - 120,
                ALTO_CUADRICULA_ENCABEZADO_INICIO);

        mPDFWriter.addLine(MARGIN_LEFT + 190,
                CUADRICULA_ENCABEZADO_INICIO,
                MARGIN_LEFT + 190,
                CUADRICULA_ENCABEZADO_INICIO + 80);

        // FIN CUADRÍCULA

        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        int lineStart = 630;
        int lineJump = TAMANO_FUENTE + 10;

        mPDFWriter.addText(MARGIN_LEFT, lineStart, TAMANO_FUENTE, "De: Interventoría");
        mPDFWriter.addText(MARGIN_LEFT, lineStart - lineJump, TAMANO_FUENTE, "Para: Obra");
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 2), TAMANO_FUENTE, "Asunto: Observaciones obra");
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 3), TAMANO_FUENTE, datos.get(3));

        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 5), TAMANO_FUENTE, "Proyecto: " + UserManager.getProyecto(context));
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 6), TAMANO_FUENTE, "Revisó: " + UserManager.getReviso(context));
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 7), TAMANO_FUENTE, "Aprobó: " + UserManager.getAprobo(context));

        int bodyStart = lineStart - (lineJump * 9);
        int currentLine = bodyStart;

        int jumpIndex = 0;

        ArrayList<String> newDatos = new ArrayList<>(datos.subList(4, datos.size() - 4));

        for (int i = 0; i < newDatos.size(); i++) {
            if ((currentLine - (lineJump * jumpIndex)) < 100) {
                mPDFWriter.newPage();
                mPDFWriter.setFont(StandardFonts.SUBTYPE,
                        StandardFonts.HELVETICA,
                        StandardFonts.WIN_ANSI_ENCODING);
                currentLine = PaperSize.LETTER_HEIGHT - 100;
                jumpIndex = 0;
            }

            String linea = newDatos.get(i);
            String indent = "      ";
            Log.e("Linea", "Size: " + linea.length());
            if (linea.length() > 71) {
                //ArrayList<String> lineas = splitLineas(linea);
                String[] lineas = splitIntoLine(linea, 70);
                newDatos.remove(i);
                for (int k = 0; k < lineas.length; k++) {
                    String elemento = lineas[k];
                    if (k == 0) {
                        if (linea.contains(indent)) {
                            newDatos.add(i + k, indent + elemento);
                        } else {
                            newDatos.add(i + k, elemento);
                        }
                    } else {
                        if (linea.contains(indent)) {
                            newDatos.add(i + k,"    " + indent + elemento);
                        } else {
                            newDatos.add(i + k, "    " + elemento);
                        }
                    }
                }
            }

            mPDFWriter.addText(MARGIN_LEFT, currentLine - (lineJump * jumpIndex), TAMANO_FUENTE, newDatos.get(i));
            jumpIndex++;

        }
        String observaciones = datos.get(datos.size() - 3);
        if (!observaciones.equals("EMPTY")) {
            String[] lineas = splitIntoLine(observaciones, 70);
            for (int i = 0; i < lineas.length; i++) {
                if ((currentLine - (lineJump * jumpIndex)) < 100) {
                    mPDFWriter.newPage();
                    mPDFWriter.setFont(StandardFonts.SUBTYPE,
                            StandardFonts.HELVETICA,
                            StandardFonts.WIN_ANSI_ENCODING);
                    currentLine = PaperSize.LETTER_HEIGHT - 100;
                    jumpIndex = 0;
                }
                mPDFWriter.addText(MARGIN_LEFT, currentLine - (lineJump * jumpIndex), TAMANO_FUENTE, lineas[i]);
                jumpIndex++;
            }

        }

        int pageCount = mPDFWriter.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            mPDFWriter.setCurrentPage(i);
            mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
                    + Integer.toString(pageCount));
        }
        Log.e("Was ist das?", "Es ist: " + datos.get(2));
        if (datos.get(2).equals("9")) {
            mPDFWriter.addLine(70, 120, 350, 120);
        }
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        int sup = PaperSize.LETTER_HEIGHT - 100;

        // Imagenes Evidencia Fotografica
        if (pic1 == null) {
        } else {
            mPDFWriter.newPage();
            mPDFWriter.setFont(StandardFonts.SUBTYPE,
                    StandardFonts.HELVETICA,
                    StandardFonts.WIN_ANSI_ENCODING);

            int[] dimensiones = redimensionarImagen(pic1);

            mPDFWriter
                    .addText(MARGIN_LEFT, sup, 14, "Anexo Fotográfico 1");

            mPDFWriter.addImageKeepRatio(
                    centrar(PaperSize.LETTER_WIDTH, dimensiones[0]),
                    centrar(PaperSize.LETTER_HEIGHT, dimensiones[1]),
                    dimensiones[0], dimensiones[1], pic1,
                    Transformation.DEGREES_0_ROTATION);
        }
        if (pic2 == null) {
        } else {
            mPDFWriter.newPage();
            mPDFWriter.setFont(StandardFonts.SUBTYPE,
                    StandardFonts.HELVETICA,
                    StandardFonts.WIN_ANSI_ENCODING);

            mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                    StandardFonts.WIN_ANSI_ENCODING);
            mPDFWriter
                    .addText(MARGIN_LEFT, sup, 14, "Anexo Fotográfico 2");

            int[] dimensiones = redimensionarImagen(pic2);

            mPDFWriter.addImageKeepRatio(
                    centrar(PaperSize.LETTER_WIDTH, dimensiones[0]),
                    centrar(PaperSize.LETTER_HEIGHT, dimensiones[1]),
                    dimensiones[0], dimensiones[1], pic2,
                    Transformation.DEGREES_0_ROTATION);
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
                    ALTO_LOGO_ENCABEZADO_INICIO,
                    ANCHO_LOGO,
                    ALTO_LOGO,
                    xoiPNG,
                    Transformation.DEGREES_0_ROTATION);
        } catch (IOException e) {
            Log.e("IO - EX", e.toString());
        }

        // ENCABEZADO


        String mMemoExpresCode = "ME - " + UserManager.getProyecto(context) + " - "
                + String.format("%03d", UserManager.getCurrentMemoExpresNumber(context));

        mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO, POSICION_ALTO_NOMBRE_FORMULARIO, TAMANO_FUENTE, mMemoExpresCode);
        mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO, POSICION_ALTO_NOMBRE_FORMULARIO - 20, TAMANO_FUENTE, now());
        mPDFWriter.setFont(StandardFonts.SUBTYPE,
                StandardFonts.HELVETICA_BOLD,
                StandardFonts.WIN_ANSI_ENCODING);
        mPDFWriter.addText(POSICION_IZQUIERDA_NOMBRE_FORMULARIO - 50, POSICION_ALTO_NOMBRE_FORMULARIO - 65, 16, "MEMO EXPRES");

        //mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220, PaperSize.LETTER_HEIGHT - 100, 16, now());
        //mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220, PaperSize.LETTER_HEIGHT - 140, 16, );


        // CUADRICULA


        mPDFWriter.addRectangle(60,
                CUADRICULA_ENCABEZADO_INICIO,
                PaperSize.LETTER_WIDTH - 120,
                ALTO_CUADRICULA_ENCABEZADO_INICIO);

        mPDFWriter.addLine(MARGIN_LEFT + 190,
                CUADRICULA_ENCABEZADO_INICIO,
                MARGIN_LEFT + 190,
                CUADRICULA_ENCABEZADO_INICIO + 80);

        // FIN CUADRÍCULA

        mPDFWriter.setFont(StandardFonts.SUBTYPE,
                StandardFonts.HELVETICA,
                StandardFonts.WIN_ANSI_ENCODING);

        int lineStart = 600;
        int lineJump = TAMANO_FUENTE + 10;

        mPDFWriter.addText(MARGIN_LEFT, lineStart - lineJump, TAMANO_FUENTE, "De: Interventoría");
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 2), TAMANO_FUENTE, "Para: Constructor");
        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 3), TAMANO_FUENTE, "Asunto: Temas de obra");

        Log.e("Datos", datos.get(3));

        mPDFWriter.addText(MARGIN_LEFT, lineStart - (lineJump * 6), TAMANO_FUENTE, "Contenido");
        lineStart = lineStart - (lineJump * 7);
        String observaciones = datos.get(3);

        if (!observaciones.equals("EMPTY")) {
            String[] lineas = splitIntoLine(observaciones, 55);
            for (int i = 0; i < lineas.length; i++) {
                mPDFWriter.addText(MARGIN_LEFT + 15, lineStart - (lineJump * i), TAMANO_FUENTE, lineas[i]);
            }
        }

        // Imagenes Evidencia Fotografica
        if (pic == null) {
        } else {
            mPDFWriter.newPage();
            mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
                    StandardFonts.WIN_ANSI_ENCODING);
            mPDFWriter.addText(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 100, 14,
                    "Anexo Fotográfico 1");

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
