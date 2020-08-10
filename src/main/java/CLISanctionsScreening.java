import java.io.IOException;

public class CLISanctionsScreening {
    public static void main(String[] args) throws IOException {
        InputReader ir = new InputReader();
        SanctionsListScreening sls = new SanctionsListScreening(true,"categorisedSancList.csv");

        String name = ir.CLIInput();
        sls.readSanctionsList();
        sls.verifySanctionsList(name);
    }
}
