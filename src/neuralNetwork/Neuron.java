package neuralNetwork;

public class Neuron {
	public double w[], 
				  bias,
				  output;
	
	public Neuron(double w[]) {
		this.w = w;
	}
	
	public String toString() {
		return output + "";
	}
}
