package ebloodCoin.blockchain.transaction;

import java.security.PublicKey;

public class UTXoAsMiningReward extends UTXo {

	

	public UTXoAsMiningReward (String parentTransactionId, PublicKey receiver, PublicKey sender,double amount) {
		super(parentTransactionId, receiver, sender, amount);
	}
	
	@Override
	public boolean isMiningReward() {
		return true;
	}
	
}
