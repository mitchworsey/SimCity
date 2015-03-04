package simCity.restaurant5;


public class MarketOrder {
	public enum OrderState {
		Pending, Available, Shipped, Unavailable, Complete, Shipping, PartiallyAvail
	};
	OrderState orderState = OrderState.Pending;
	
		String foodOrder;
		int quantOrder = 0;
		OrderState s;
		int shipped = 0;
		
		
		
		public MarketOrder(String order, int qo) {
			foodOrder = order;
			quantOrder = qo;
			//s = ss;
		}
}
