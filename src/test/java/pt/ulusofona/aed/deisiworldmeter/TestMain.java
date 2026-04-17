package pt.ulusofona.aed.deisiworldmeter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    @BeforeEach
    public void beforeEach() {
        // ensure static lists are cleared before each test
        Main.pais.clear();
        Main.cidades.clear();
        Main.populacoes.clear();
        Main.inputsInv.clear();
    }

    @AfterEach
    public void afterEach() {
        // no-op
    }

    @Test
    public void testPaisesToString_IdLessThan700() {
        Paises p = new Paises(4, "af", "afg", "Afeganistão", 0);
        assertEquals("Afeganistão | 4 | AF | AFG", p.toString());
    }

    @Test
    public void testPaisesToString_IdGreaterThan700() {
        Paises p = new Paises(710, "za", "zaf", "África do Sul", 5);
        assertEquals("África do Sul | 710 | ZA | ZAF | 5", p.toString());
    }

    @Test
    public void testCidadeToString() {
        Cidade c = new Cidade("br", "São Paulo", "07", 12345678, "-23.55", "-46.63");
        assertEquals("São Paulo | BR | 07 | 12345678 | (-23.55,-46.63)", c.toString());
    }

    @Test
    public void testPopulacaoToString() {
        Populacao p = new Populacao(76, 2020, 100000, 120000, 12.34);
        String s = p.toString();
        assertTrue(s.contains("Populacao{"));
        assertTrue(s.contains("id=76"));
        assertTrue(s.contains("ano=2020"));
    }

    @Test
    public void testParseFilesSuccessAndInputInfo() {
        File folder = new File("test-files");
        assertTrue(folder.exists(), "test-files folder must exist for this test to run");

        boolean ok = Main.parseFiles(folder);
        assertTrue(ok, "parseFiles should return true when files exist (even with invalid lines)");

        ArrayList inputs = Main.getObjects(TipoEntidade.INPUT_INVALIDO);
        assertEquals(3, inputs.size(), "There should be 3 InputsInvalidos entries (paises,cidades,populacao)");

        boolean foundPaises = false;
        boolean foundCidades = false;
        boolean foundPop = false;
        for (Object o : inputs) {
            assertNotNull(o);
            assertTrue(o instanceof InputsInvalidos);
            InputsInvalidos ii = (InputsInvalidos) o;
            if (ii.nome.equals("paises.csv")) foundPaises = true;
            if (ii.nome.equals("cidades.csv")) foundCidades = true;
            if (ii.nome.equals("populacao.csv")) foundPop = true;
        }
        assertTrue(foundPaises && foundCidades && foundPop, "InputsInvalidos must contain entries for all three CSVs");
    }

    @Test
    public void testParseFilesWithBadLinesDetectsInvalids() throws IOException {
        // create a temporary test folder under test-files
        File badFolder = new File("test-files/test-bad");
        if (!badFolder.exists()) badFolder.mkdirs();

        // write a paises.csv with one invalid line (missing fields)
        try (FileWriter fw = new FileWriter(new File(badFolder, "paises.csv"))) {
            fw.write("id,alfa2,alfa3,nome\n");
            fw.write("1,xx,xxx,Country OK\n");
            fw.write("badline_without_commas\n");
        }

        // write cidades.csv with invalid latitude
        try (FileWriter fw = new FileWriter(new File(badFolder, "cidades.csv"))) {
            fw.write("alfa2,nome,regiao,populacao,latitude,longitude\n");
            fw.write("xx,City OK,01,1000,12.3,45.6\n");
            fw.write("xx,City BAD,02,1000,not_a_number,45.6\n");
        }

        // write populacao.csv with invalid id (id not integer)
        try (FileWriter fw = new FileWriter(new File(badFolder, "populacao.csv"))) {
            fw.write("id,ano,popMasc,popFem,densidade\n");
            fw.write("1,2000,100,200,12.0\n");
            fw.write("not_an_int,2001,100,200,12.0\n");
        }

        boolean ok = Main.parseFiles(badFolder);
        assertTrue(ok, "parseFiles returns true when files are present even if lines are invalid");

        ArrayList inputs = Main.getObjects(TipoEntidade.INPUT_INVALIDO);
        assertEquals(3, inputs.size());

        for (Object o : inputs) {
            InputsInvalidos ii = (InputsInvalidos) o;
            // each file above has at least one bad line
            assertTrue(ii.numLinhasMal > 0, "Each test file should report at least one invalid line: " + ii.nome);
        }

        // cleanup created files
        for (File f : badFolder.listFiles()) {
            if (f != null) f.delete();
        }
        badFolder.delete();
    }
}

