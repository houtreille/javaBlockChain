package ebloodCoin.blockchain;


/*
 * No Setter for security reason
 */

public interface Block {
	public String calculateBlockHash() throws Exception;
	public Boolean mineBlock(int prefixDifficulty) throws Exception;
	public String getHash();
	public String getPreviousHash();
	
	public boolean addTransaction(Object transaction) throws Exception ;
	public Object getData(Object param);
}
