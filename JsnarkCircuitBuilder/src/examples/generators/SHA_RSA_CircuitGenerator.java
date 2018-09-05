package examples.generators;

import circuit.eval.CircuitEvaluator;
import circuit.structure.CircuitGenerator;
import circuit.structure.Wire;
import examples.gadgets.hash.SHA256Gadget;

import java.math.BigInteger;

public class SHA_RSA_CircuitGenerator extends CircuitGenerator {

    private Wire[] privateInput;
    String inputStr = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijkl";
    static String expectedDigest = "aeacdd7013805404b62e0701cd09aeab2a4994c519d7f1d7cf7a295a5d8201ad";
    //static String expectedDigest = "abacdd7013805404b62e0701cd09aeab2a4994c519d7f1d7cf7a295a5d8201ad";

    public SHA_RSA_CircuitGenerator(String circuitName){
        super(circuitName);
    }

    @Override
    protected void buildCircuit() {
        privateInput = createProverWitnessWireArray(64);
        Wire[] digest = new SHA256Gadget(privateInput, 8, 64, false,
                false).getOutputWires();
        makeOutputArray(digest);
        int beginIndex = 0;
        int endIndex = 8;
        for(int i =0; i<8; i++){
            String ss = expectedDigest.substring(beginIndex, endIndex);
            addEqualityAssertion(digest[i], new BigInteger(ss, 16));
            beginIndex +=8;
            endIndex +=8;
        }

    }

    @Override
    public void generateSampleInput(CircuitEvaluator evaluator) {
        for(int i = 0; i < privateInput.length; i++){
            evaluator.setWireValue(privateInput[i], inputStr.charAt(i));
        }

    }

    public static void main(String[] args) {

        SHA_RSA_CircuitGenerator circuitGenerator = new SHA_RSA_CircuitGenerator("PrivIdEx-1");
        circuitGenerator.generateCircuit();
        circuitGenerator.evalCircuit();
        circuitGenerator.prepFiles();
        circuitGenerator.runLibsnark();

//        CircuitEvaluator evaluator = circuitGenerator.getCircuitEvaluator();
//        String outDigest = "";
//        int beginIndex = 0;
//        int endIndex = 8;
//        for (Wire w : circuitGenerator.getOutWires()) {
//            String ss = expectedDigest.substring(beginIndex, endIndex);
//            System.out.println(ss);
//
//            BigInteger ex = new BigInteger(ss, 16);
//            if(evaluator.getWireValue(w).equals(ex)){
//                System.out.println("true");
//            }
//
//            outDigest = evaluator.getWireValue(w).toString(16);
//            System.out.println(outDigest);
//
//            beginIndex +=8;
//            endIndex +=8;
//        }
//        System.out.println("Finish");
//        System.out.println(outDigest);
    }
}
