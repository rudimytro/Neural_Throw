package neuralNetwork;

public class OutputNeuron extends Neuron {
	public int angle,
			   v0;
	
	public OutputNeuron(double w[]) {
		super(w);
	}
	
	public void setValues(int angle, int v0) {
		this.angle = angle;
		this.v0 = v0;
	}
}
