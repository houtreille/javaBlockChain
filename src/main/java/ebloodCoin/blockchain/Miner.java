package ebloodCoin.blockchain;

public class Miner extends Wallet {

	public Miner(String minerName, String password) {
		super(minerName, password);
	}
	
	public boolean mineBlock(Block block) throws Exception  {
		return block.mineBlock(3);
	}
}
