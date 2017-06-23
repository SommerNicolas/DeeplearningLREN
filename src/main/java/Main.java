import org.apache.commons.io.FileUtils;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Logger log = LoggerFactory.getLogger(Main.class);

    private static final long seed = 12345;
    //private static String path = "src/main/resources/3D/sub";
    private static String pathCube = "generate/cube";
    private static String pathSphere = "generate/sphere";
    private static final Random randGen = new Random(seed);

    private static int height;
    private static int width;
    private static int nbChannel;
    private static int iteration;
    private static int nbLabels;

    private static int niftiSize = 100;
    private static int cubeSize = 10;
    private static String NIFTICubPrefix = "cube";
    private static String NIFTISpherePrefix = "sphere";
    private static int step = 10;

    public static void main(String[] args) {
        generateData();
        ArrayList<String> paths = getPaths();
        DataReader dr = new DataReader();
        for(int i = 0; i <= 1456; i++){
            if(i <= 582){
                dr.load(paths.get(i), new float[]{0, 1}, new int[]{1, 2}, true);
            }
            if(i > 582 && i <= 728){
                dr.load(paths.get(i), new float[]{0, 1}, new int[]{1, 2}, false);
            }
            if(i > 728 && i <= 1410){
                dr.load(paths.get(i), new float[]{1, 0}, new int[]{1, 2}, true);
            }
            else{
                dr.load(paths.get(i), new float[]{1, 0}, new int[]{1, 2}, false);
            }
        }
        int[] metaData = dr.getMetaData(pathCube + "1.nii.gz");
        width = metaData[0];
        height = metaData[1];
        nbChannel = 1;
        iteration = 2;
        nbLabels = 2;

        INDArrayDataSetIterator iteratorTrain = dr.getTrainIterator();
        INDArrayDataSetIterator iteratorTest = dr.getTestiterator();

        /*log.info("***** Main start *****");
        log.info("***** Get info from datas *****");
        DataInput di = new DataInput(path);
        di.printInfo();

        log.info("**** Set up hyper-parameter");
        width =di.getX();
        height = di.getY();
        nbChannel = 1; //di.getZ(); //Surement a modifier getZ() par 1
        iteration = 1;
        nbLabels = 2;

        log.info("***** Get an INDArrayDataSetIterator *****");
        di.createDataSetCube();
        INDArrayDataSetIterator iteratorTrain = di.getIteratorTrain();
        INDArrayDataSetIterator iteratorTest = di.getIteratorTest();*/
        System.out.println("***** Train Iterator Info *****");
        System.out.printf("String of iterator: " + iteratorTrain.toString());
        System.out.println("\nTotal example: " + iteratorTrain.totalExamples());
        System.out.println("Labels: " + iteratorTrain.getLabels());
        System.out.println("***************************");
        System.out.println("***** Test Iterator Info *****");
        System.out.printf("String of iterator: " + iteratorTest.toString());
        System.out.println("\nTotal example: " + iteratorTest.totalExamples());
        System.out.println("Labels: " + iteratorTest.getLabels());
        System.out.println("***************************");


        log.info("***** Get CNN model *****");
        MultiLayerConfiguration conf = getCNNConf();
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        log.info("***** Train model *****");
        training(iteratorTrain, model);

        log.info("***** Eval model *****");
        evalModel(iteratorTest, model);

        log.info("***** Save model *****");
        try{
            FileUtils.write(new File("SaveCNN.json"), model.getLayerWiseConfigurations().toJson());
            FileUtils.write(new File("SaveCNN.yaml"), model.getLayerWiseConfigurations().toYaml());
            DataOutputStream dos = new DataOutputStream(Files.newOutputStream(Paths.get("SaveCNN.bin")));
            Nd4j.write(model.params(), dos);
        }catch(IOException e){
            e.printStackTrace();
        }
        log.info("***** END MAIN *****");

    }

    private static void generateData(){
        DataTestGenerator dtg = new DataTestGenerator();
        try{
            int count = 0;
            for(int offsetZ = 0; offsetZ < niftiSize - cubeSize; offsetZ += step ){
                for(int offsetY = 0; offsetY < niftiSize -  cubeSize; offsetY += step){
                    for(int offsetX = 0; offsetX < niftiSize - cubeSize; offsetX += step){
                        dtg.generateNIFTICube(niftiSize, cubeSize, offsetX, offsetY, offsetZ, NIFTICubPrefix + count++);
                    }
                }
            }
            count = 0;
            for(int offsetZ = 0; offsetZ < niftiSize - cubeSize; offsetZ += step){
                for(int offsetY = 0; offsetY < niftiSize - cubeSize; offsetY += step){
                    for(int offsetX = 0; offsetX < niftiSize - cubeSize; offsetX += step){
                        dtg.generateNIFTISphere(niftiSize, cubeSize, offsetX, offsetY, offsetZ, NIFTISpherePrefix + count++);
                    }
                }
            }
            //exemple
            //dtg.generateNIFTICube(niftiSize, cubeSize, 0, 0, 0, niftiNamePrefix);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getPaths(){
        ArrayList<String> paths = new ArrayList<>();
        for(int i = 0; i <= 728; i++){
            paths.add(pathCube + i + ".nii.gz");
        }
        for(int i = 0; i <= 728; i++){
            paths.add(pathSphere + i + ".nii.gz");
        }
        return paths;
    }

    private static MultiLayerConfiguration getCNNConf(){
        ConvolutionLayer layer0 = new ConvolutionLayer.Builder(5, 5)
                .nIn(1)
                .nOut(20)
                .stride(1, 1)
                .weightInit(WeightInit.XAVIER)
                .name("Convolution layer")
                .activation(Activation.RELU)
                .build();

        SubsamplingLayer layer1 = new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .name("Max pooling layer")
                .build();

        DenseLayer layer2 = new DenseLayer.Builder()
                .activation(Activation.RELU)
                .nOut(180)
                .build();

        OutputLayer layer3 = new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(nbLabels)
                .activation(Activation.SOFTMAX)
                .build();

        ConvolutionLayer layer4 = new ConvolutionLayer.Builder(1, 1)
                .nOut(20)
                .stride(1, 1)
                .padding(2, 2)
                .weightInit(WeightInit.XAVIER)
                .name("Convolution layer")
                .activation(Activation.RELU)
                .build();

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iteration)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(0.001)
                .regularization(true)
                .l2(0.0004)
                .updater(Updater.NESTEROVS)
                .momentum(0.9)
                .list()
                    .layer(0, layer0)
                    .layer(1, layer1)
                    .layer(2, layer4)
                    .layer(3, layer1)
                    .layer(4, layer4)
                    .layer(5, layer1)
                    .layer(6, layer4)
                    .layer(7, layer1)
                    .layer(8, layer2)
                    .layer(9, layer3)
                .pretrain(false)
                .backprop(true)
                .setInputType(InputType.convolutional(1000, 1000, 1))
                .build();

        /*MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(iteration)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(0.001)
                .regularization(true)
                .l2(0.0004)
                .updater(Updater.NESTEROVS)
                .momentum(0.9)
                .list()
                    .layer(0, layer0)
                    .layer(1, layer1)
                    .layer(2, layer2)
                    .layer(3, layer3)
                .pretrain(false)
                .backprop(true)
                .setInputType(InputType.convolutional(1000, 1000, 1))
                .build();*/
        return conf;

    }

    private static void training(INDArrayDataSetIterator iterator, MultiLayerNetwork mlnet) {
        log.info("***** Training Model *****");
        /*while(iterator.hasNext()){
            DataSet ds = iterator.next();
            mlnet.fit(ds);
        }*/
        mlnet.fit(iterator);
    }

    private static void evalModel(INDArrayDataSetIterator iterator, MultiLayerNetwork mlnet){
        log.info("***** Evaluating Model *****");
        iterator.reset();
        Evaluation eval = new Evaluation();
        while(iterator.hasNext()){
            DataSet next = iterator.next();
            INDArray predict = mlnet.output(next.getFeatureMatrix());
            eval.eval(next.getLabels(), predict);
        }
        System.out.println(eval.stats());
    }

    private static INDArray getGeneratedINDArray(int choice){
        DataTestGenerator dtg = new DataTestGenerator();
        if(choice != 1){
            return dtg.generateINDArray();
        }
        return dtg.generateNegativeINDArray();
    }

    public static void testWithDataSet(){
        int nRows = 9;
        int nColumns = 3;
        int nbChannels = 1;

        DataTestGenerator dtg = new DataTestGenerator();
        INDArrayDataSetIterator ds = dtg.generateDataSet(1000);

        NeuralNetwork neuralNetwork = new NeuralNetwork(1, seed, 1, nRows, nColumns, nbChannels, 2);
        MultiLayerNetwork mlnet = neuralNetwork.getNetwork();

        training(ds, mlnet);
        evalModel(ds, mlnet);
    }

    public static void test(){
        //Test la fonction fit avec des INDArray
        int nRows = 9;
        int nColumns = 3;
        int nbChannels = 1;
        int nIn = nRows * nColumns * nbChannels;

        log.info("Generate INDArray");
        INDArray dataPos = getGeneratedINDArray(0);
        INDArray dataNeg = getGeneratedINDArray(1);

        log.info("Setup the Neural Network...");
        NeuralNetwork neuralNetwork = new NeuralNetwork(1, seed, 1, nRows, nColumns, nbChannels, 2);
        MultiLayerNetwork mlnet = neuralNetwork.getNetwork();

        log.info("Train model");
        mlnet.fit(dataPos.reshape(9, 3));
        mlnet.fit(dataNeg.reshape(9, 3));
    }

}