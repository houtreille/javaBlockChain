package ebloodCoin.blockchain;

import java.io.Serializable;
import java.util.ArrayList;

public class LedgerList<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<T> list;
	
	public LedgerList() {
		list = new ArrayList<T>();
	}
	
	public T getFirst() {
		return list.get(0);
	}
	
	public T getLast() {
		return list.get(list.size()-1);
	}
	
	public T getElementAt(int i) {
		return list.get(i);
	}
	
	public boolean add(T element) {
		return list.add(element);
	}
	
	public int getSize() {
		return list.size();
	}
}
