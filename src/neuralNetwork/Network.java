package neuralNetwork;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import main.LearningUnit;

public class Network {
	private Neuron[][] net;
	private double ws[][][],
				   errors[][],
				   alpha = 1,
				   n = 4;
	private int input[] = new int[2];
	private Gson gson = new Gson();
	
	public Network() {
		createNetwork(400, 100, 100);
		loadWs("Weights");
		
		fulFill(2);
		bindValues();
		
		/*
		learn();
		saveWs("Weights");
		*/
	}
	
	private void createNetwork(int... sizes) {
		int len = sizes.length;
		net = new Neuron[len][];
		errors = new double[len][];
		for(int i = 0; i < len; i++) {
			net[i] = new Neuron[sizes[i]];
			errors[i] = new double[sizes[i]];
		}
	}
	
	private void fulFill(int wLenStart) {
		Random r = new Random();
		int wLen = wLenStart,
			nLen = net.length;
		for(int i = 0; i < nLen; i++) {
			for(int j = 0; j < net[i].length; j++) {
				double w[] = new double[wLen];
				for(int y = 0; y < wLen; y++) {
					w[y] = ws[i][j][y];
				}
				
				net[i][j] = (i == nLen - 1) ? new OutputNeuron(w) : new Neuron(w);
				net[i][j].bias = ws[i][j][wLen];
			}
			wLen = net[i].length;
		}
	}
	
	private void bindValues() {
		Neuron[] outputLayer = net[net.length - 1];
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				OutputNeuron tmp = (OutputNeuron) outputLayer[(i * 10) + j];
				tmp.setValues(i * 10, (j+1) * 2);
			}
		}
	}
	
	private void wCorrection(double[] answer, double localN) {
		Neuron[] outputLayer = net[net.length - 1];
		for(int x = 0; x < outputLayer.length; x++) {
			double output = outputLayer[x].output,
				   omega = answer[x] - output;
			omega *= .5 * (output * (1 - output));
			errors[net.length - 1][x] = omega;
		}
		
		
		for(int i = net.length - 2; i >= 0; i--) {
			double omega = 0;
			for(int j = 0; j < net[i].length; j++) {
				for(int y = 0; y < errors[i+1].length; y++)
					omega += errors[i+1][y] * net[i+1][y].w[j];
				
				
				omega *= .5 * (net[i][j].output * (1 - net[i][j].output));
				errors[i][j] = omega;
			}
		}
		
		// -- updating ws --
		
		for(int i = 0; i < net.length; i++) {
			int len = net[i][0].w.length;
			for(int j = 0; j < net[i].length; j++) {
				for(int y = 0; y < len; y++) {
					net[i][j].w[y] += localN * errors[i][j] * ((i == 0) ? input[y] : net[i-1][y].output);
				}
				net[i][j].bias += localN * errors[i][j];
			}
		}
	}
	
	
	private void calculate(int p1, int p2) {
		input[0] = p1;
		input[1] = p2;
		double NET;
		for(int x = 0; x < net[0].length; x++) {
			NET = net[0][x].w[0] * p1 + net[0][x].w[1] * p2 + net[0][x].bias;
			net[0][x].output = 1/(1 + Math.pow(Math.E, -1 * alpha * NET));
		}
		
		for(int i = 1; i < net.length; i++) {
			int wLen = net[i][0].w.length;
			double output;
			for(int j = 0; j < net[i].length; j++) {
				NET = 0;
				for(int y = 0; y < wLen; y++)
					NET += net[i][j].w[y] * net[i-1][y].output;
				NET += net[i][j].bias;
				net[i][j].output = 1/(1 + Math.pow(Math.E, -1 * alpha * NET));
			}
		}
	}
	
	public OutputNeuron result(int x, int y) {
		calculate(x, y);
		int pos = net.length - 1;
		OutputNeuron neuron = (OutputNeuron) net[pos][0];
		for(int i = 1; i < net[pos].length; i++) {
			if(net[pos][i].output > neuron.output)
				neuron = (OutputNeuron) net[pos][i];
		}
		return neuron;
	}
	
	public double[][] makeAnswers(List<LearningUnit> units) {
		double[][] answers = new double[units.size()][102];
		for(int i = 0; i < answers.length; i++) {
			LearningUnit unit = units.get(i);
			int winner = unit.angle + (unit.v0/2 - 1);
			for(int j = 0; j < 100; j++)
				answers[i][j] = (double) ((j == winner) ? 1 : 0);
			answers[i][100] = unit.x;
			answers[i][101] = unit.y;
		}
		return answers;
	}
	
	private void loadWs(String fileName) {
		try (FileReader reader = new FileReader("./weights/" + fileName + ".json")){
			ws = gson.fromJson(reader, new TypeToken<double[][][]>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveWs(String fileName) {
		for(int i = 0; i < net.length; i++) {
			int len = net[i][0].w.length;
			for(int j = 0; j < net[i].length; j++) {
				for(int y = 0; y < len; y++)
					ws[i][j][y] = net[i][j].w[y];
				ws[i][j][len] = net[i][j].bias;
			}
		}
		try (FileWriter writer = new FileWriter("./weights/" + fileName + ".json")){
			gson.toJson(ws, writer);
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createWs(int start) {
		Random r = new Random();
		int wLen = start;
		ws = new double[net.length][][];
		for(int i = 0; i < net.length; i++) {
			ws[i] = new double[net[i].length][];
			for(int j = 0; j < net[i].length; j++) {
				ws[i][j] = new double[wLen + 1];
				for(int y = 0; y < wLen + 1; y++)
					ws[i][j][y] = r.nextDouble() - 0.5;
			}
			wLen = net[i].length;
		}
	}
	
	private void learn() {
		List<LearningUnit> units = null;
		try (FileReader reader = new FileReader("./results/result.json")){
			units = gson.fromJson(reader, new TypeToken<List<LearningUnit>>(){}.getType());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
		double[][] answers = makeAnswers(units);
		
		double localN = n;
		for(int j = 0; j < 40; j++) {
			for(int i = 0; i < answers.length; i++) {
				calculate((int) answers[i][100],(int) answers[i][101]);
				wCorrection(answers[i], localN);
			}
			localN /= 2;
		}
	}
}
