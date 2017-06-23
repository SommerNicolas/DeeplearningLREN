import niftijio.NiftiVolume;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 01.06.17.
 */
public class DataInput {
    private String dataPath;
    private int nbDim;
    private int x;
    private int y;
    private int z;
    private float[] voxelDim;
    private NiftiVolume volume;
    private INDArrayDataSetIterator iteratorTest;
    private INDArrayDataSetIterator iteratorTrain;

    public INDArrayDataSetIterator getIteratorTest() {
        return iteratorTest;
    }

    public INDArrayDataSetIterator getIteratorTrain() {
        return iteratorTrain;
    }

    public DataInput(String path){
        dataPath = path;
        try{
            volume = NiftiVolume.read(dataPath + "1.nii.gz");
            nbDim = volume.header.dim[4];
            x = volume.header.dim[1];
            y = volume.header.dim[2];
            z = volume.header.dim[3];
            voxelDim = volume.header.pixdim;
        }catch(IOException e){
            System.out.println("Error reading NIFTI file");
            e.printStackTrace();
        }
    }

    public void printInfo(){
        System.out.println("***** Datas Input Info *****");
        System.out.println("Chemin d'acces aux fichiers: " + dataPath);
        System.out.println("Nombre de dimension des fichiers: " + nbDim);
        System.out.println("Shape du fichier: x = " + volume.header.dim[1] + " y = " + volume.header.dim[2] + " z = " + volume.header.dim[2]);
        System.out.println("Datatype: " + volume.header.data_type_string);
        System.out.println("Pixdim: " + voxelDim[0] + " " + voxelDim[1] + " " + voxelDim[2] + " " + voxelDim[3] + " " + voxelDim[4]+ " " + voxelDim[5] + " " + voxelDim[6] + " " + voxelDim[7]);
        System.out.println("****************************");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    private INDArray getData(String path){
        try{
            NiftiVolume volume = NiftiVolume.read(path);
            int nx = volume.header.dim[1];
            int ny = volume.header.dim[2];
            int nz = volume.header.dim[3];
            int dim = volume.header.dim[4];

            int w = 0;
            if(dim == 0){
                dim = 1;
            }
            double[] tab = new double[nx * ny * nz * dim];
            /*for(int d = 0; d < dim; d++){
                for(int k = 0; k < nz; k++){
                    for(int j = 0; j < ny; j++){
                        for(int i = 0; i < nx; i++){
                            tab[w] = volume.data.get(i, j, k, d);
                            w++;
                        }
                    }
                }
            }*/
            //float[] test = new float[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
            //INDArray array = Nd4j.create(test, new int[]{1, 1, 5, 5}, 'c');
            //System.out.println("Array: " + array);
            //INDArray array = Nd4j.create(tab, new int[]{1, 1, 2880, 2048});
            //format 4D-Tensor [minibatch, inputDepth, heigth, width]
            INDArray array = Nd4j.create(tab, new int[]{1, 1, 1000, 1000});
            return array;
            //return Nd4j.create(tab, new int []{1, 1, 160, 36864}, 'c');

        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public INDArrayDataSetIterator getDataSetIterator(){
        INDArray negLabel = Nd4j.rand(1, 2);
        INDArray posLabel = Nd4j.rand(1, 2);

        List<INDArray> labels = new ArrayList<INDArray>();
        List<INDArray> datas = new ArrayList<INDArray>();

        for(int i = 1; i <= 15; i++){
            INDArray arr = getData("src/main/resources/3D/sub" + i + ".nii.gz");
            if(arr != null){
                datas.add(arr);
                if(i%2 == 0){
                    labels.add(negLabel);
                }else{
                    labels.add(posLabel);
                }
            }
        }
        ArrayList<Pair> featureAndLabel = new ArrayList<Pair>();
        for(int i = 0; i < datas.size(); i++){
            featureAndLabel.add(new Pair(datas.get(i), labels.get(i)));
            System.out.println("label du nifti " + i + ": " + labels.get(i));
        }
        System.out.println("Size dataset: " + featureAndLabel.size());
        Iterable featLab = featureAndLabel;
        INDArrayDataSetIterator ds = new INDArrayDataSetIterator(featLab, 1);
        return ds;
    }

    public void createDataSetCube(){
        INDArray cubeLabel = Nd4j.ones(1, 2);
        INDArray notLabel = Nd4j.zeros(1, 2);

        List<INDArray> labelsTrain = new ArrayList<>();
        List<INDArray> featuresTrain = new ArrayList<>();
        List<INDArray> labelsTest = new ArrayList<>();
        List<INDArray> featuresTest = new ArrayList<>();

        //get cube data
        for(int i = 0; i <= 728; i++){
            INDArray array = getData("generate/cube" + i + ".nii.gz");
            if(array != null){
                if(i < 582){
                    featuresTrain.add(array);
                    labelsTrain.add(cubeLabel);
                }else{
                    featuresTest.add(array);
                    labelsTest.add(cubeLabel);
                }

            }
        }

        //get void data
        for(int i = 0; i <= 728; i++){
            INDArray array = getData("generate/sphere" + i + ".nii.gz");
            if(array != null){
                if(i < 582){
                    featuresTrain.add(array);
                    labelsTrain.add(notLabel);
                }else{
                    featuresTest.add(array);
                    labelsTest.add(notLabel);
                }

            }
        }

        ArrayList<Pair> featureAndLabelTrain = new ArrayList<>();
        for(int i = 0; i < featuresTrain.size(); i++){
            featureAndLabelTrain.add(new Pair(featuresTrain.get(i), labelsTrain.get(i)));
        }
        System.out.println("Size dataset train: " + featureAndLabelTrain.size());
        Iterable featLabel = featureAndLabelTrain;
        iteratorTrain = new INDArrayDataSetIterator(featLabel, 1);

        ArrayList<Pair> featureAndLabelTest = new ArrayList<>();
        for(int i = 0; i < featuresTest.size(); i++){
            featureAndLabelTest.add(new Pair(featuresTest.get(i), labelsTest.get(i)));
        }
        System.out.println("Size dataset test: " + featureAndLabelTest.size());
        Iterable featLabel1 = featureAndLabelTest;
        iteratorTest = new INDArrayDataSetIterator(featLabel1, 1);
    }
}
