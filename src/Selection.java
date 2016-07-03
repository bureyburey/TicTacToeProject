
public interface Selection {
	Individual reproduce(Individual[] pop);

	Individual reproduceBest(Individual[] pop);

	Individual copyElite(Individual[] pop);
	
	double getMutationProb();

	double getCrossoverProb();

}
