package app.kyjsuptec.kjingenieros.factory;

import java.util.ArrayList;

import app.kyjsuptec.kjingenieros.activities.DatoFormularioFactory;

public class FormularioFactory {

    private ArrayList<DatoFormularioFactory> formulario;
    private ArrayList<String> cumple_nocumple;
    private ArrayList<String> cumple_nocumple_noaplica;
    private ArrayList<String> cumple_noaplica;
    private ArrayList<String> mixer_concretadora;
    private ArrayList<String> torregrua_sistemabombeo_coche;
    private ArrayList<String> aplica_noaplica;
    private ArrayList<String> bomba_noaplica;
    private ArrayList<String> tolva_tuberia_coche_pala;
    private ArrayList<String> agua_aditivo;
    private ArrayList<String> bueno_regular_malo;
    private ArrayList<String> requiere_norequiere;
    private ArrayList<String> si_no;
    private ArrayList<String> I_II_III_IV;
    private ArrayList<String> cemex_holcim_argos_ultracem;
    private ArrayList<String> vibrador;

    public final int TIPO_1 = 1;
    public final int TIPO_2 = 2;
    public final int TIPO_3 = 3;
    public final int TIPO_4 = 4;
    public final int TIPO_5 = 5;
    public final int TITULO = 6;

    public final int ALFANUMERICO = 1;
    public final int ALFABETICO = 2;
    public final int NUMERICO = 3;
    public final int DECIMAL = 4;

    public final boolean ES_SUBTITULO = true;

    public FormularioFactory() {
        inicializarListas();
    }

    private void inicializarListas() {
        cumple_nocumple = new ArrayList<String>();
        cumple_nocumple.add("Cumple");
        cumple_nocumple.add("No cumple");

        cumple_nocumple_noaplica = new ArrayList<String>();
        cumple_nocumple_noaplica.add("Cumple");
        cumple_nocumple_noaplica.add("No cumple");
        cumple_nocumple_noaplica.add("No Aplica");


        cumple_noaplica = new ArrayList<String>();
        cumple_noaplica.add("Cumple");
        cumple_noaplica.add("No Aplica");

        mixer_concretadora = new ArrayList<String>();
        mixer_concretadora.add("Mixer");
        mixer_concretadora.add("Concretadora");

        torregrua_sistemabombeo_coche = new ArrayList<String>();
        torregrua_sistemabombeo_coche.add("Torre Grúa");
        torregrua_sistemabombeo_coche.add("Sistema Bombeo");
        torregrua_sistemabombeo_coche.add("Coche");

        aplica_noaplica = new ArrayList<String>();
        aplica_noaplica.add("Aplica");
        aplica_noaplica.add("No Aplica");

        bomba_noaplica = new ArrayList<String>();
        bomba_noaplica.add("Bomba");
        bomba_noaplica.add("No Aplica");

        tolva_tuberia_coche_pala = new ArrayList<String>();
        tolva_tuberia_coche_pala.add("Tolva");
        tolva_tuberia_coche_pala.add("Tubería");
        tolva_tuberia_coche_pala.add("Coche");
        tolva_tuberia_coche_pala.add("Pala");

        agua_aditivo = new ArrayList<String>();
        agua_aditivo.add("Agua");
        agua_aditivo.add("Aditivo");

        bueno_regular_malo = new ArrayList<String>();
        bueno_regular_malo.add("Bueno");
        bueno_regular_malo.add("Regular");
        bueno_regular_malo.add("Malo");

        requiere_norequiere = new ArrayList<String>();
        requiere_norequiere.add("Requiere");
        requiere_norequiere.add("No Requiere");

        si_no = new ArrayList<String>();
        si_no.add("Si");
        si_no.add("No");

        I_II_III_IV = new ArrayList<String>();
        I_II_III_IV.add("I");
        I_II_III_IV.add("II");
        I_II_III_IV.add("III");
        I_II_III_IV.add("IV");

        cemex_holcim_argos_ultracem = new ArrayList<String>();
        cemex_holcim_argos_ultracem.add("Cemex");
        cemex_holcim_argos_ultracem.add("Holcim");
        cemex_holcim_argos_ultracem.add("Argos");
        cemex_holcim_argos_ultracem.add("Ultracem");

        vibrador = new ArrayList<String>();
        vibrador.add("Vibrador");

    }

    public ArrayList<DatoFormularioFactory> getFormulario(int tipo, int reps) {
        formulario = new ArrayList<DatoFormularioFactory>();
//        formulario.add(new DatoFormularioFactory(TIPO_1, "Proyecto", ALFANUMERICO));
//        formulario.add(new DatoFormularioFactory(TIPO_1, "Revisó", ALFABETICO));
//        formulario.add(new DatoFormularioFactory(TIPO_1, "Aprobó", ALFABETICO));
        switch (tipo) {
            case 1:
                return getf1();
            case 2:
                return getf2();
            case 3:
                return getf3();
            case 4:
                return getf4();
            case 5:
                return getf5();
            case 6:
                return getf6(reps);
            case 7:
                return getf7();
            case 8:
                return getf8();
            case 9:
                return getf9();
            default:
                return null;
        }

    }

    private ArrayList<DatoFormularioFactory> getf1() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha revisión"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Lote", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Cantidad Total", NUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "VARIABLES A CONTROLAR NTC-121 Numeral 7"));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Tipo", I_II_III_IV));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Marcas Legibles", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Palabra cemento Portland", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Marca", cemex_holcim_argos_ultracem));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Lugar de fabricación", ALFABETICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Masa teórica del bulto (Kg)", DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Masa promedio de bultos (Kg)", DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Almacenamiento (libre de humedad y de agentes contaminantes)", cumple_nocumple));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf2() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "1. Tipo de concreto (PSI)", NUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "2. Preparación"));
        formulario.add(new DatoFormularioFactory(TIPO_3, "N° Mixer", ES_SUBTITULO, ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_3, "N° Tanda de mezclado", ES_SUBTITULO, NUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_2, "3. Hora carga"));
        formulario.add(new DatoFormularioFactory(TIPO_2, "4. Hora descarga"));
        formulario.add(new DatoFormularioFactory(TITULO, "5. Asentamiento (CM)"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Teórico", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Real", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_3, "6. Elemento estrucutral vaciado", ALFANUMERICO));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf3() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha revisión"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "Replanteo Geométrico"));
        //Revisar tipo
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "Sección típica excavación"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Profundidad (m)", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ancho (m)", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TITULO, "Sección Modificada"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Sobre excavación (m)", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Sobre ancho (m)", ES_SUBTITULO, DECIMAL));
        formulario.add(new DatoFormularioFactory(TITULO, "Estrato portante"));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Condición estrato", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Profundidad (m)", DECIMAL));
        formulario.add(new DatoFormularioFactory(TITULO, "Condición de las Excavaciones"));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Limpieza de fondo", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Protección ", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Sistema de drenaje", bomba_noaplica));
        //formulario.add(new DatoFormularioFactory(TIPO_4, "Solado de protección", cumple_nocumple_noaplica));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf4() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha revisión"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "Resistencia del concreto (Mpa)"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "f'c", NUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Medio de mezclado", mixer_concretadora));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Medio de transporte", torregrua_sistemabombeo_coche));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Medio de colocación", tolva_tuberia_coche_pala));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Medio de compactación", vibrador));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Tiempo entre mezcla y colocación (MIN) ", DECIMAL));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Homogeneidad en estado fresco", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(
                TIPO_4,
                "Definición de juntas en construcción y/o dilatación y preparación de las superficies",
                cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Provisiones para el vaciado según el clima", cumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Sistema y procedimiento de curado", agua_aditivo));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf5() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TITULO, "Estado General Obra falsa"));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Formaleta", bueno_regular_malo));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Equipo armado", bueno_regular_malo));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Alineación de la obra falsa", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Plomo de la obra falsa", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Nivel de la obra falsa", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Limpieza e impermeabilidad", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Resistencia y estabilidad ante posibles asentamiento", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Pases o instalaciones técnicas requeridas", cumple_nocumple));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf6(int reps) {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Cantidad", NUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));

        for (int i = 0; i < reps; i++) {
            formulario.add(new DatoFormularioFactory(TITULO, "Grado (Mpa)"));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Barras", NUMERICO));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Mallas", NUMERICO));
            formulario.add(new DatoFormularioFactory(TITULO, "Diámetros y/o especificaciones"));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Barras (#)", NUMERICO));
            formulario.add(new DatoFormularioFactory(TIPO_3, "Mallas", ALFANUMERICO));
            formulario.add(new DatoFormularioFactory(TIPO_1, "N° de Barras", NUMERICO));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Longitud (m)", DECIMAL));
            formulario.add(new DatoFormularioFactory(TITULO, "Ganchos"));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Cantidad", NUMERICO));
            formulario.add(new DatoFormularioFactory(TIPO_1, "Barras (#)", NUMERICO));
        }

        formulario.add(new DatoFormularioFactory(TIPO_4, "Empalmes (Traslapos conexiones)", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Estribos, disposición y cantidad", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(
                TIPO_4,
                "Ubicación de refuerzo para anclaje de muros u otros elementos estructurales",
                cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Limpieza del refuerzo y de la zona de vaciado", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Tolerancia de colocación del refuerzo", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Tolerancia de separación entre barras", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Recubrimiento", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Refuerzo de retracción y temperatura", cumple_nocumple_noaplica));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf7() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Alineación del elemento vaciado", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Plomo del elemento vaciado", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Nivel de elemento vaciado ", cumple_nocumple));
        formulario.add(new DatoFormularioFactory(
                TIPO_4,
                "Ubicación de refuerzo para anclaje de muros u otros elementos estructurales",
                cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Reparación de defectos estructurales", requiere_norequiere));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Reparación de defectos superficiales", requiere_norequiere));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Rechazo de elementos vaciados", si_no));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Aceptación del concreto (Obtención de la resistencia f´c)", cumple_nocumple));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf8() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha revisión"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Muro", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Plano", ALFANUMERICO));

        formulario.add(new DatoFormularioFactory(TITULO, "Condiciones de la ejecución"));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Mezclado mortero de pega", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Mezclado mortero de inyección", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Alineamiento", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Plomo", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Geometría", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Aparejo", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Traba (uniones)", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Juntas de Pega (espesor)", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Ventanas de inspeccion", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Altura de inyección", ES_SUBTITULO, ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Colocación de ductos y tuberías embebidas", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Juntas de control", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Anclaje de dovelas", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Traslapo de dovelas", cumple_nocumple_noaplica, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Revite", cumple_nocumple, ES_SUBTITULO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Aseo", cumple_nocumple, ES_SUBTITULO));

        return formulario;
    }

    private ArrayList<DatoFormularioFactory> getf9() {

        formulario.add(new DatoFormularioFactory(TIPO_5, "Fecha"));
        formulario.add(new DatoFormularioFactory(TIPO_2, "Hora"));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Elemento a revisar", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ubicación", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_1, "Ejes", ALFANUMERICO));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Niveles y ejes", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Plomos", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Formaleta", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Acero de refuerzo", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TIPO_4, "Aseo", cumple_nocumple_noaplica));
        formulario.add(new DatoFormularioFactory(TITULO, "Firma constructor"));
        return formulario;
    }

}
