package DataTransfer;

import GroceryStore.Product;

public class Result extends DataTransfer {
	public static final int PRODUCT_NOT_FOUND = 1;
	public static final int MEMBER_NOT_FOUND = 2;
	public static final int OPERATION_COMPLETED = 3;
	public static final int OPERATION_FAILED = 4;
	public static final int SHIPMENT_NOT_FOUND = 5;

	private int resultCode;
	private Product product;

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
